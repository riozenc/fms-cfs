/**
 * Author : czy
 * Date : 2019年4月13日 下午3:10:20
 * Title : com.riozenc.cfs.webapp.mrm.handler.LadderHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.LadderModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.utils.MonUtils;
import org.fms.cfs.common.webapp.domain.MeterMoneyDomain;
import org.fms.cfs.common.webapp.domain.PriceLadderRelaDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.client.model.Filters;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 阶梯电费
 * 
 * @author czy
 *
 */
public class LadderFilter2 implements EcfFilter, MongoDAOSupport {
	private static final PriceLadderRelaDomain ZERO_DOMAIN = new PriceLadderRelaDomain();
	static {
		ZERO_DOMAIN.setLadderSn(0);
		ZERO_DOMAIN.setLadderValue(BigDecimal.ZERO);
		ZERO_DOMAIN.setPrice(BigDecimal.ZERO);
	}
	private Log logger = LogFactory.getLog(LadderFilter2.class);

	@Override
	public int getOrder() {
		return 80;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理阶梯电费...");

		String mon = exchange.getDate().toString();

		List<PriceLadderRelaDomain> priceLadderRelaDomains = findMany(
				getCollection(mon, MongoCollectionConfig.PRICE_LADDER_RELA.name()), new MongoFindFilter() {

					@Override
					public Bson filter() {
						// TODO Auto-generated method stub
						return new Document();
					}
				}, PriceLadderRelaDomain.class);

		Map<Long, List<PriceLadderRelaDomain>> priceLadderRelaMap = priceLadderRelaDomains.stream()
				.collect(Collectors.groupingBy(PriceLadderRelaDomain::getPriceExecutionId));

		List<ECFModel> ladderECFModel = exchange.getModels().values().parallelStream()
				.filter(e -> priceLadderRelaMap.containsKey(e.getPriceType())).collect(Collectors.toList());

		logger.info("满足阶梯的计量点数量:" + ladderECFModel.size());

		// 判断是否新一年的1月份
		if (exchange.getDate() % 100 != 1) {
			ladderHandle(exchange, ladderECFModel, priceLadderRelaMap);
		}

		return filterChain.filter(exchange);
	}

	private int getLadder(BigDecimal ladderTotalPower, PriceLadderRelaDomain[] priceLadderRelaDomains,
			BigDecimal ladderNum) {

		// 小于等于 1阶梯阀值
		if (ladderTotalPower.compareTo(priceLadderRelaDomains[1].getLadderValue(ladderNum)) < 1) {
			return priceLadderRelaDomains[1].getLadderSn();
		}

		// 大于1阶梯阀值，判断是否小于等于2阶梯
		if (ladderTotalPower.compareTo(priceLadderRelaDomains[2].getLadderValue(ladderNum)) < 1) {
			return priceLadderRelaDomains[2].getLadderSn();
		}
		// 大于2阶梯阀值，进行3阶梯判断
		if (ladderTotalPower.compareTo(priceLadderRelaDomains[3].getLadderValue(ladderNum)) < 1) {
			return priceLadderRelaDomains[3].getLadderSn();
		} else {
			// 都不满足，认为是4阶梯
			return priceLadderRelaDomains[4].getLadderSn();
		}
	}

	private void ladderHandle(CFModelExchange<ECFModel> exchange, List<ECFModel> ladderECFModel,
			Map<Long, List<PriceLadderRelaDomain>> priceLadderRelaMap) {

		List<MeterMoneyDomain> lastMeterMoneyList = findMany(
				getCollection(MonUtils.getLastMon(exchange.getDate().toString()),
						MongoCollectionConfig.ELECTRIC_METER_MONEY.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.and(
								Filters.in("meterId",
										ladderECFModel.stream().map(ECFModel::getMeterId).collect(Collectors.toList())),
								Filters.eq("sn", exchange.getSn()));
					}
				}, MeterMoneyDomain.class);

		Map<Long, MeterMoneyDomain> lastMeterMoneyMap = lastMeterMoneyList.stream()
				.collect(Collectors.toMap(MeterMoneyDomain::getMeterId, k -> k, (k, v) -> k));

		// 找出计算阶梯的电价
		ladderECFModel.forEach(e -> {
			MeterMoneyDomain lastMeterMoney = lastMeterMoneyMap.get(e.getMeterId());

			BigDecimal lastLadderTotalPower = null;

			if (lastMeterMoney == null) {
				// 没有记录，则是新用电户，阶梯值为0
				lastLadderTotalPower = BigDecimal.ZERO;
			} else {
				lastLadderTotalPower = lastMeterMoney.getLadderTotalPower();
			}
			BigDecimal ladderTotalPower = e.addLastLadderTotalPower(lastLadderTotalPower);

			List<PriceLadderRelaDomain> list = priceLadderRelaMap.get(e.getPriceType());
			PriceLadderRelaDomain[] arrays = new PriceLadderRelaDomain[5];
			arrays[0] = ZERO_DOMAIN;

			for (PriceLadderRelaDomain p : list) {
//				p.setLadderValue(p.getLadderValue().multiply(e.getLadderNum()));
				arrays[p.getLadderSn()] = p;
			}

			int ladderSn = getLadder(ladderTotalPower, arrays, e.getLadderNum());
			int ladderSnLast = getLadder(lastLadderTotalPower, arrays, e.getLadderNum());
			e.setLadderSn(ladderSn);// 赋值所处阶梯

			// 不管是否跨阶梯，根据当前阶梯电价（第一次计算电度用的是第一阶梯）重算一次
			e.getMeterData().forEach(m -> {
				m.setLadder(ladderSnLast);// 赋值当前所属阶梯
				m.computeCharge(arrays[ladderSnLast].getPrice(), BigDecimal.ZERO);// 用 当前阶梯电价重新计算抄见电量的电费
			});

			for (int i = ladderSnLast; i < ladderSn + 1; i++) {

				BigDecimal lowerLimit = BigDecimal.ZERO;

				if (i == ladderSnLast) {
					// 不跨阶梯 跟历史电量对比
					lowerLimit = lastLadderTotalPower;

				} else if (i > ladderSnLast) {
					// 跨阶梯 跟上个阀值比
					lowerLimit = arrays[i - 1].getLadderValue(e.getLadderNum());
				} else {
					e.addRemark("计量点 : " + e.getMeterNo() + " 阶梯电量出现问题 : 抄见电量=" + e.getReadPower() + " 历史阶梯电量="
							+ lastLadderTotalPower);
					e.markProcessResult(getOrder(), false);
				}

				// 3120
				// 4400
				// 4500

				// 3660 + 594
				e.addLadderData(createLadderModel(i,
						getLadderPower(ladderTotalPower, lowerLimit, arrays[i].getLadderValue(e.getLadderNum())),
						arrays[i].getPrice()));
			}

			e.computeVolumeCharge();// 重新计算电度电费
		});

	}

	private LadderModel createLadderModel(int ladderSn, BigDecimal chargePower, BigDecimal price) {
		LadderModel ladderModel = new LadderModel(ladderSn, chargePower, price);
		return ladderModel;
	}

	private static BigDecimal getLadderPower(BigDecimal ladderTotalPower, BigDecimal lowerLimit,
			BigDecimal upperLimit) {

		if (ladderTotalPower.compareTo(lowerLimit) < 0) {
			return BigDecimal.ZERO;
		}

		if (BigDecimal.valueOf(-1).equals(upperLimit)) {
			return ladderTotalPower.subtract(lowerLimit);
		}

		// 是否跨越下个阶梯，如果是，则阶梯电量就是两个阶梯阀值的差值
		if (ladderTotalPower.compareTo(upperLimit) > 0) {
			return upperLimit.subtract(lowerLimit);
		}

		return ladderTotalPower.subtract(lowerLimit);
	}

}
