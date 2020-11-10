/**
 * Author : chizf
 * Date : 2020年5月25日 下午5:44:50
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.PersistenceFilter.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.MeterDataModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.MeterMoneyDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 算费数据入库
 * 
 * @author czy
 *
 */
public class PersistenceFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(PersistenceFilter.class);

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE - 100;// 优先级不是最后
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {

		logger.info("处理合计...");
		Map<Long, ECFModel> ecfModelMap = exchange.getModels();

		// 更新年阶梯数据
		List<MeterMoneyDomain> domains = ecfModelMap.values().parallelStream().map(e -> {
			MeterMoneyDomain meterMoneyDomain = model2MeterMoney(e);
			e.setMeterMoneyDomain(meterMoneyDomain);
			return e.getMeterMoneyDomain();
		}).collect(Collectors.toList());

		List<WriteModel<Document>> writeModels = updateMany(toDocuments(domains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				return Filters.and(Filters.eq("meterId", param.get("meterId")), Filters.eq("mon", param.get("mon")),
						Filters.eq("sn", param.get("sn")));
			}
		}, true);

		logger.info("合计数据数量=" + writeModels.size());
		if (writeModels.size() == 0) {
			exchange.writeComputeLog("电费合计阶段，数据为0，请检查..");
			return filterChain.filter(exchange);
		}

		BulkWriteResult bulkWriteResult = getCollection(exchange.getDate().toString(),
				MongoCollectionConfig.ELECTRIC_METER_MONEY.name()).bulkWrite(writeModels);

		logger.info("处理合计完毕(" + bulkWriteResult.getInsertedCount() + ")...");
		return filterChain.filter(exchange);

	}

	private MeterMoneyDomain model2MeterMoney(ECFModel ecfModel) {
		MeterMoneyDomain meterMoneyDomain = new MeterMoneyDomain();

		meterMoneyDomain.setWriteSectId(ecfModel.getWriteSectId());
		meterMoneyDomain.setMeterId(ecfModel.getMeterId());
		meterMoneyDomain.setMon(Integer.parseInt(ecfModel.getMon()));
		meterMoneyDomain.setSn(ecfModel.getSn());
		meterMoneyDomain.setMsType(ecfModel.getMsType());
		meterMoneyDomain.setPriceTypeId(ecfModel.getPriceType());
		meterMoneyDomain.setActiveWritePower(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.P1R0)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 总
		meterMoneyDomain.setActiveWritePower1(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.F)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 峰
		meterMoneyDomain.setActiveWritePower2(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.P)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 平
		meterMoneyDomain.setActiveWritePower3(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.G)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 谷
		meterMoneyDomain.setActiveWritePower4(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.J)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 尖

		meterMoneyDomain.setReactiveWritePower(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.P3R0)
				.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 正无
		meterMoneyDomain
				.setReverseReactiveWritePower(ecfModel.getMeterData().stream().filter(m -> m.getKey() == ECFModel.P4R0)
						.map(MeterDataModel::getReadPower).findFirst().orElse(BigDecimal.ZERO));// 反无

		// 换表电量 = 正向有功尖峰平谷 换表电量相加
		meterMoneyDomain.setChgPower(ecfModel.getMeterData().stream().filter(m -> m.getPowerDirection() == 1)
				.filter(m -> m.getFunctionCode() == 1)
//				.filter(m -> m.getTimeSeg() != 0)
				.map(MeterDataModel::getChangePower).reduce(BigDecimal.ZERO, BigDecimal::add));

		// 增减电量 = 正向有功尖峰平谷增减电量相加
		meterMoneyDomain.setAddPower(ecfModel.getMeterData().stream().filter(m -> m.getPowerDirection() == 1)
				.filter(m -> m.getFunctionCode() == 1)
//				.filter(m -> m.getTimeSeg() != 0)
				.map(MeterDataModel::getAddPower).reduce(BigDecimal.ZERO, BigDecimal::add));

		// 有功套减电量 = 正向有功 尖峰平谷 （套减电量+协议电量） 相加
		meterMoneyDomain.setActiveDeductionPower(ecfModel.getMeterData().stream()
//				.filter(m -> m.getPowerDirection() == 1).filter(m -> m.getFunctionCode() == 1).filter(m -> m.getTimeSeg() != 0)
				.filter(m -> m.isP1R0()).map(m -> {
					return m.getDeductionPower().add(m.getProtocolPower());
				}).reduce(BigDecimal.ZERO, BigDecimal::add));

		// 无功套减电量 = 无功表 （正无反无 套减电量+协议电量） 相加
		meterMoneyDomain
				.setReactiveDeductionPower(ecfModel.getMeterData().stream().filter(m -> m.getFunctionCode() == 2)
//				.filter(m -> m.getTimeSeg() != 0)
						.map(m -> {
							return m.getDeductionPower().add(m.getProtocolPower());
						}).reduce(BigDecimal.ZERO, BigDecimal::add));

		// 线损
		meterMoneyDomain.setActiveLineLossPower(ecfModel.getLineLoss());
		// 有功变损
		meterMoneyDomain.setActiveTransformerLossPower(ecfModel.getActiveTransformerLoss());
		// 无功变损
		meterMoneyDomain.setReactiveTransformerLossPower(ecfModel.getReactiveTransformerLoss());
		// 有功总电量
		meterMoneyDomain.setTotalPower(ecfModel.getActiveChargePower());
		// 无功总电量
		meterMoneyDomain.setqTotalPower(ecfModel.getReactiveChargePower());
		// 调整率
		meterMoneyDomain.setCos(ecfModel.getCos());
		// 功率因数
		meterMoneyDomain.setCosRate(ecfModel.getCosRate());
		// 变压器 计费容量
		meterMoneyDomain.setCalCapacity(ecfModel.getCapacity());
		// TODO 分摊容量
//		meterMoneyDomain.setShareCapacity(shareCapacity);
		// 电度电费
		meterMoneyDomain.setVolumeCharge(ecfModel.getVolumeCharge());
		// 基本电费
		meterMoneyDomain.setBasicMoney(ecfModel.getBasicCharge());
		// 力调电费
		meterMoneyDomain.setPowerRateMoney(ecfModel.getPowerRateCharge());
		// 附加费
		meterMoneyDomain.setSurcharges(ecfModel.getSurcharge());
		// 附加费明细
		meterMoneyDomain.setSurchargeDetail(ecfModel.getSurchargeDetail());
		meterMoneyDomain.setSurchargePrices(ecfModel.getSurchargePrices());

		// 总退补电费
		meterMoneyDomain.setRefundMoney(ecfModel.getRefundMoney());
		// 总电费
		meterMoneyDomain.setTotalAmount(ecfModel.getTotalAmount());
		// 应收电费
		meterMoneyDomain.setAmount(ecfModel.getAmount());

		meterMoneyDomain.setCreateDate(new Date());
		meterMoneyDomain.setComputeLog(ecfModel.getRemark());

		meterMoneyDomain.setTransformerCalculateInfo(ecfModel.getTransformerCalculateModel());
		meterMoneyDomain.setMeterDataInfo(ecfModel.getMeterData());
		// TODO 阶梯总电量有问题
		meterMoneyDomain.setLadderTotalPower(ecfModel.getLadderTotalPower());
		meterMoneyDomain.setLadderSn(ecfModel.getLadderSn());

		meterMoneyDomain
				.setLadderDataModels(ecfModel.getLadderDataModels().isEmpty() ? null : ecfModel.getLadderDataModels());

		return meterMoneyDomain;
	}

}
