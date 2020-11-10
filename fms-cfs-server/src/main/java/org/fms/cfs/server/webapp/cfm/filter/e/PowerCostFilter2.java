/**
 * Author : czy
 * Date : 2019年6月13日 下午7:39:08
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.PowerCostFilter.java
 **/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.FixedParametersConfig;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.utils.CalculationUtils;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 电度电费、附加费？？？
 *
 * @author czy
 */
public class PowerCostFilter2 implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(PowerCostFilter2.class);

	@Override
	public int getOrder() {
		return 50;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理电度电费、附加费...");
		Map<Long, ECFModel> ecfModelMap = exchange.getModels();

		Map<Long, List<PriceExecutionDomain>> priceMap = findMany(
				getCollectionName(exchange.getDate().toString(), MongoCollectionConfig.PRICE_EXECUTION.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				}, PriceExecutionDomain.class).stream()
						.collect(Collectors.groupingBy(PriceExecutionDomain::getPriceTypeId));

		ecfModelMap.values().parallelStream().forEach(e -> {
			e.computeChargePower();// 计算 计费电量
			List<PriceExecutionDomain> priceList = priceMap.get(e.getPriceType());// 获取电价list
			if (priceList == null) {
				e.markProcessResult(getOrder(), false);
				e.addRemark(e.getPriceType() + "电价类型对应的电价不存在,请检查电价信息是否初始化.");
				return;
			}

			Map<Integer, BigDecimal> powerMap = new HashMap<>();

			e.getMeterData().stream().filter(m -> m.isP1()).forEach(m -> {
				powerMap.put(m.getTimeSeg().intValue(), m.getChargePower());
			});

			// 奇怪的政策：谷电量大于总电量60%的部分，按平的电价收费
			if (e.isTs()) {
				BigDecimal tempPower = powerMap.get(FixedParametersConfig.TIME_SEG_0).multiply(BigDecimal.valueOf(0.6))
						.setScale(BigDecimal.ROUND_HALF_UP, 2);
				if (powerMap.get(FixedParametersConfig.TIME_SEG_3).compareTo(tempPower) >= 0) {
					powerMap.put(FixedParametersConfig.TIME_SEG_2, powerMap.get(FixedParametersConfig.TIME_SEG_2)
							.add(powerMap.get(FixedParametersConfig.TIME_SEG_3).subtract(tempPower)));
					powerMap.put(FixedParametersConfig.TIME_SEG_3, tempPower);
				}
			}

			powerMap.forEach((timeSeg, power) -> {

				// 附加费
				priceList.stream().filter(p -> p.getPriceItemId() != 1).filter(p -> {
					if (e.isTs()) {// 分时只计算“分项”
						return p.getTimeSeg() != 0;
					} else {// 非分时 只计算“总”
						return p.getTimeSeg() == 0;
					}
				}).filter(p -> p.getTimeSeg().intValue() == timeSeg).forEach(p -> {

					BigDecimal surcharge = CalculationUtils.multiply(power, p.getPrice(), 2);
					// 根据时段赋值附加费
					e.addSurcharge(surcharge);
					e.addSurchargeDetail(p.getKey(), surcharge);
				});

				// 获取总电价
				PriceExecutionDomain price = priceList.stream().filter(p -> p.getPriceItemId() == 1)
						.filter(p -> p.getTimeSeg().intValue() == timeSeg).findFirst().orElse(null);
				if (price == null) {
					StringBuilder stringBuilder = new StringBuilder();
					stringBuilder.append(e.getMeterId()).append(" 电价类型:").append(e.getPriceType()).append("#电价项目:1#时段:")
							.append(timeSeg).append(" 电价不存在，请检查电价参数.");
					e.addRemark(stringBuilder.toString());
					e.markProcessResult(getOrder(), false);
					return;
				}

				e.getMeterData().stream().filter(m -> m.getTimeSeg().intValue() == timeSeg).forEach(m -> {
//					m.computeCharge(price.getPrice(), BigDecimal.ZERO);
					m.setCharge(CalculationUtils.multiply(power, price.getPrice(), 2));
				});

			});

			// 计算电度电费
			e.computeVolumeCharge();

		});

		return filterChain.filter(exchange);
	}

}
