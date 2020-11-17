/**
 * Author : czy
 * Date : 2019年4月13日 下午3:02:36
 * Title : com.riozenc.cfs.webapp.mrm.handler.TransformerLossHandler.java
 **/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
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
import org.fms.cfs.common.model.TransformerCalculateModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.TransformerLoadParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossFormulaParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossTableParamDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 变压器损耗
 *
 * @author czy
 *
 */
public class TransformerLossFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(TransformerLossFilter.class);

	@Override
	public int getOrder() {
		return 30;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理变损...");
		Map<Long, ECFModel> ecfModelMap = exchange.getModels();

		// 获取变压器损耗计算参数-公式
		Map<String, TransformerLossFormulaParamDomain> formulaMap = getTransformerLossFormulaParamDomains(
				exchange.getDate().toString());

		// 获取变压器损耗计算参数-查表
		Map<String, List<TransformerLossTableParamDomain>> tableMap = getTransformerLossTableParamDomains(
				exchange.getDate().toString());

		// msType = 2 代表 高供低计
		ecfModelMap.values().stream().filter(m -> m.isComplete())
				.filter(e -> e.getTransformerMeterRelationDomain() != null)
//				.filter(e -> e.getTransformerMeterRelationDomain().getMsType() == FixedParametersConfig.MS_MODE_2)//高供低计
				.filter(e -> e.getMeterType() != FixedParametersConfig.METER_TYPE_2)// 考核表
				.collect(Collectors.groupingBy(ECFModel::getTransformerCalculateModel))
				.forEach((transformerCalculateModel, list) -> {
					try {
						// 汇总关联的计量点的电量
						list.stream().filter(ecfModel -> ecfModel.getTransformerLossType() != null)
								.forEach(ecfModel -> {
									transformerCalculateModel.addActiveEnergy(ecfModel.getActiveChargePower());
									transformerCalculateModel.addReactiveEnergy(ecfModel.getReactiveChargePower());
//							transformerCalculateModel.addActiveEnergy(ecfModel.getActiveEnergy());
//							transformerCalculateModel.addReactiveEnergy(ecfModel.getReactiveEnergy());
								});

						// 计算损耗
						try {
							switch (transformerCalculateModel.getTransformerLossType()) {
							case 1:// 不计算
								transformerCalculateModel.calculateByEmpty();
								break;
							case 2:// 查表

								transformerCalculateModel
										.calculateByTable(getNearTableParam(tableMap, transformerCalculateModel));
								break;
							case 3:// 公式
								transformerCalculateModel
										.calculateByFormula(formulaMap.get(transformerCalculateModel.getFormulaKey()));
								break;
							default:
								transformerCalculateModel.calculateByEmpty();
								break;
							}
						} catch (Exception exception) {
							list.forEach(ecfModel -> {
								ecfModel.markProcessResult(getOrder(), false);
								ecfModel.addRemark(
										transformerCalculateModel.getTableKey() + " 未找到该类型匹配的变损参数-查表，请检查档案或参数表.");
							});
						}

						BigDecimal[] subActiveTransformerLoss = { BigDecimal.ZERO };
						BigDecimal[] subReactiveTransformerLoss = { BigDecimal.ZERO };
						List<Boolean> flag = new LinkedList<>();

						List<ECFModel> newList = list.stream()
								.filter(e -> e.getMsType() == FixedParametersConfig.MS_MODE_2)
								.filter(e -> e.getTransformerLossType() != FixedParametersConfig.TRANS_SHARE_FLAG_0)
								.collect(Collectors.toList());// 剔除不计算的

						newList.forEach(ecfModel -> {
							if (ecfModel.getActiveChargePower().compareTo(BigDecimal.ZERO) == 0) {
								flag.add(false);
							} else {
								flag.add(true);
							}
						});

						if (!flag.contains(true)) {
							newList.stream().forEach(ecfModel -> {

								ecfModel.setActiveTransformerLoss(transformerCalculateModel.lossAllocation(
										transformerCalculateModel.getActiveTransformerLoss(), BigDecimal.ONE,
										BigDecimal.valueOf(newList.size())));

								ecfModel.setReactiveTransformerLoss(transformerCalculateModel.lossAllocation(
										transformerCalculateModel.getReactiveTransformerLoss(), BigDecimal.ONE,
										BigDecimal.valueOf(newList.size())));

								transformerCalculateModel.addTransformerLossAllocation(ecfModel.getMeterId(),
										ecfModel.getMeterNo(), ecfModel.getActiveChargePower(),
										ecfModel.getTransformerLossType(), ecfModel.getActiveTransformerLoss(),
										ecfModel.getReactiveTransformerLoss());

							});
						} else {
							BigDecimal activeTotalApportionment = newList.stream().map(ECFModel::getActiveChargePower)
									.reduce(BigDecimal.ZERO, BigDecimal::add);// 分摊总量

							BigDecimal reactiveTotalApportionment = newList.stream()
									.map(ECFModel::getReactiveChargePower).reduce(BigDecimal.ZERO, BigDecimal::add);// 分摊总量

							newList.stream().forEach(ecfModel -> {

								switch (ecfModel.getTransformerLossType()) {
								case FixedParametersConfig.TRANS_SHARE_FLAG_0://
									ecfModel.setActiveTransformerLoss(BigDecimal.ZERO);

									ecfModel.setReactiveTransformerLoss(BigDecimal.ZERO);

									break;
								case FixedParametersConfig.TRANS_SHARE_FLAG_1:// 按合同容量分摊

									ecfModel.setActiveTransformerLoss(transformerCalculateModel.lossAllocation(
											transformerCalculateModel.getActiveTransformerLoss(),
											ecfModel.getCapacity(), transformerCalculateModel.getCapacity()));

									ecfModel.setReactiveTransformerLoss(transformerCalculateModel.lossAllocation(
											transformerCalculateModel.getReactiveTransformerLoss(),
											ecfModel.getCapacity(), transformerCalculateModel.getCapacity()));

									break;
								case FixedParametersConfig.TRANS_SHARE_FLAG_2:// 按用电量分摊

									ecfModel.setActiveTransformerLoss(transformerCalculateModel.lossAllocation(
											transformerCalculateModel.getActiveTransformerLoss(),
											ecfModel.getActiveChargePower(), activeTotalApportionment));

									ecfModel.setReactiveTransformerLoss(transformerCalculateModel.lossAllocation(
											transformerCalculateModel.getReactiveTransformerLoss(),
											ecfModel.getReactiveChargePower(), reactiveTotalApportionment));

									break;
								case FixedParametersConfig.TRANS_SHARE_FLAG_3:// 固定变损

									if (ecfModel.getTransformerMeterRelationDomain().getTransLostNum() != null) {
										ecfModel.setActiveTransformerLoss(
												ecfModel.getTransformerMeterRelationDomain().getTransLostNum());
										ecfModel.setReactiveTransformerLoss(
												ecfModel.getTransformerMeterRelationDomain().getTransLostNum());
									}

									break;
								case FixedParametersConfig.TRANS_SHARE_FLAG_4:// 参与分摊但不计算变损

									ecfModel.setActiveTransformerLoss(BigDecimal.ZERO);

									ecfModel.setReactiveTransformerLoss(BigDecimal.ZERO);

									break;
								default:
									break;
								}

								subActiveTransformerLoss[0] = subActiveTransformerLoss[0]
										.add(ecfModel.getActiveTransformerLoss());
								subReactiveTransformerLoss[0] = subReactiveTransformerLoss[0]
										.add(ecfModel.getReactiveTransformerLoss());

								transformerCalculateModel.addTransformerLossAllocation(ecfModel.getMeterId(),
										ecfModel.getMeterNo(), ecfModel.getActiveChargePower(),
										ecfModel.getTransformerLossType(), ecfModel.getActiveTransformerLoss(),
										ecfModel.getReactiveTransformerLoss());

							});

						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				});

		return filterChain.filter(exchange);
	}

	/**
	 * 获取变损计算参数-公式
	 *
	 * @param date
	 * @return
	 */
	private Map<String, TransformerLossFormulaParamDomain> getTransformerLossFormulaParamDomains(String date) {
		List<TransformerLossFormulaParamDomain> transformerLossFormulaParamDomains = findMany(
				getCollection(date, MongoCollectionConfig.TRANSFORMER_LOSS_FORMULA_PARAM_INFO.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				}, TransformerLossFormulaParamDomain.class);

		// 变损参数Map
		Map<String, TransformerLossFormulaParamDomain> tpMap = transformerLossFormulaParamDomains.parallelStream()
				.collect(Collectors.toMap(t -> {
					return t.getTransformerType() + "#" + t.getCapacity();
				}, t -> t));
		return tpMap;
	}

	/**
	 * 获取变损计算参数-表
	 *
	 * @param date
	 * @return
	 */
	private Map<String, List<TransformerLossTableParamDomain>> getTransformerLossTableParamDomains(String date) {
		List<TransformerLossTableParamDomain> transformerLossTableParamDomains = findMany(
				getCollection(date, MongoCollectionConfig.TRANSFORMER_LOSS_TABLE_PARAM_INFO.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				}, TransformerLossTableParamDomain.class);

		// 变损参数Map
		Map<String, List<TransformerLossTableParamDomain>> tableMap = transformerLossTableParamDomains.parallelStream()
				.collect(Collectors.groupingBy(t -> {
					return t.getTransformerType() + "#" + t.getVoltLevelType();
				}));

		return tableMap;
	}

	private List<TransformerLossTableParamDomain> getNearTableParam(
			Map<String, List<TransformerLossTableParamDomain>> tableMap,
			TransformerCalculateModel transformerCalculateModel) {

		List<TransformerLossTableParamDomain> transformerLossTableParamDomains = tableMap
				.get(transformerCalculateModel.getTableKey());

		if (transformerLossTableParamDomains == null) {
			// 匹配不上变压器型号以及电压等级
			throw new RuntimeException(transformerCalculateModel.getTableKey() + "在参数表中没有匹配项.");
		}

		Map<BigDecimal, List<TransformerLossTableParamDomain>> tpMap = transformerLossTableParamDomains.stream()
				.collect(Collectors.groupingBy(TransformerLossTableParamDomain::getCapacity));

		List<BigDecimal> capacitys = new LinkedList<BigDecimal>(tpMap.keySet());
		// 不取整 值会变为30.0
		if (tpMap.keySet().contains(transformerCalculateModel.getCapacity().setScale(0, BigDecimal.ROUND_DOWN))) {
			return tpMap.get(transformerCalculateModel.getCapacity().setScale(0, BigDecimal.ROUND_DOWN));
		} else {
			capacitys.add(transformerCalculateModel.getCapacity());
			Collections.sort(capacitys);
			int index = capacitys.indexOf(transformerCalculateModel.getCapacity());
			int dealIndex = (index == (capacitys.size() - 1) ? index : index + 1);
			return tpMap.get(capacitys.get(dealIndex));
		}
	}

}
