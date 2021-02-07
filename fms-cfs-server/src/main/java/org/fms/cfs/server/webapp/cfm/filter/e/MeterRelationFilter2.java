/**
 * Author : czy
 * Date : 2019年4月13日 下午3:01:37
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.MeterRelationFilter.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.MeterDataModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.utils.CalculationUtils;
import org.fms.cfs.common.webapp.domain.MeterRelationGraphDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 套扣关系，处理定量定比
 * 
 * @author czy
 *
 */
public class MeterRelationFilter2 implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(MeterRelationFilter2.class);

	@Override
	public int getOrder() {
		return 20;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理套扣关系...");
		exchange.getModels().values().stream().filter(ECFModel::isComplete).filter(m -> m.isTop()).forEach(m -> {
			List<MeterRelationGraphDomain> meterRelationGraphDomains = m.getBottomMeterRelation();
			meterRelationGraphDomains.stream().sorted(Comparator.comparing(mrg -> {
				return mrg.getMeterRelationType();// 先处理定量关系，再处理定比关系，最后处理总分关系。定量1，定比2，总分3，顺序不能错
			})).forEach(mrg -> {
				computeMeterRelation2(mrg, exchange);
			});

		});
		exchange.getModels().values().stream().filter(e -> e.isComplete()).forEach(e -> {
			e.replaceProtocolPower();
			e.computeChargePower();
		});

		return filterChain.filter(exchange);
	}

	// 根据顶点计算套扣关系
	private void computeMeterRelation2(MeterRelationGraphDomain meterRelationGraphDomain,
			CFModelExchange<ECFModel> exchange) {
		ECFModel pModel = exchange.getModels().get(meterRelationGraphDomain.getpMeterId());
		ECFModel sModel = exchange.getModels().get(meterRelationGraphDomain.getMeterId());

		// 1、父子计量点非空判断
		if (sModel == null) {
			if (pModel != null) {
				pModel.addRemark("子计量点[" + meterRelationGraphDomain.getMeterId() + "]未初始化。");
				pModel.markProcessResult(getOrder(), false);
			}
			return;
		}

		if (pModel == null) {
			if (sModel != null) {
				sModel.addRemark("父计量点[" + meterRelationGraphDomain.getpMeterId() + "]未初始化。");
				sModel.markProcessResult(getOrder(), false);
			}
			return;
		}

		// 过滤掉数据不全的计量点（包括关系值及抄表数据）
		if (sModel.getMeterData().isEmpty() ) {
			sModel.addRemark("计量点套扣关系值为空，可能该计量点未进行抄表初始化，请检查。");
			sModel.markProcessResult(getOrder(), false);
			return;
		}

		
		// 2、进行拆分。情况有3*3种：
		// （1）3:定量、定比、总分
		// （2）3:相同类型电能表、分时-普通、普通-分时

		// 先是否定量定比--定量定比存在特殊处理
		// （1）特殊处理.如果是定量(1)定比(2)关系，需要补充子表分时抄表单。 3=总分
		// （2）分配给子表时，直接累计到子表的抄见电量上
		boolean isProtocol = false;
		pModel.getMeterData().forEach(m -> {
			m.setProtocol(true);// 重置 父表该属性
		});
		// 关系(定量、定比、总分)
		BigDecimal protocolPower = null;// 协议（定量定比）
//		BigDecimal deductionPower = null;// 套减（总分）
		switch (meterRelationGraphDomain.getMeterRelationType()) {
		// 定量
		case 1:
			isProtocol = true;
			protocolPower = meterRelationGraphDomain.getMeterRelationValue();

			break;
		// 定比
		case 2:
			isProtocol = true;

			MeterDataModel pMeterData = pModel.getMeterData().stream().filter(m -> m.isP1R0()).findFirst().get();

			// 扣掉定量分摊的电量,判断一下协议电量是正还是负
			// 正的说明电量是被分配的，不需要扣掉，该特殊情况用于先扣电量再扣定比
			protocolPower = CalculationUtils.divide(pMeterData.getActiveEnergy().add(pMeterData.getProtocolPower())
					.multiply(meterRelationGraphDomain.getMeterRelationValue()), BigDecimal.valueOf(100), 0);

			break;
		// 总分
		case 3:
			protocolPower = sModel.getActiveEnergy();
			sModel.getMeterData().forEach(m -> {
				m.setProtocol(false);// 子表自己用电，不是协议电量，子表总电量不需要处理套扣电量
			});

			break;
		}

		if (protocolPower != null && pModel.getActiveEnergy().compareTo(protocolPower) < 0) {
			pModel.markProcessResult(getOrder(), false);
			pModel.addRemark("电度电量小于协议电量.");
		}

//		if (deductionPower != null && pModel.getActiveEnergy().compareTo(deductionPower) < 0) {
//			pModel.markProcessResult(getOrder(), false);
//			pModel.addRemark("父表电量小于子表电量.");
//		}

		// 3、电量分摊到细项数据
		// TODO 存在BUG
		chooseProtocolHandler(pModel.getTsType(), sModel.getTsType(), meterRelationGraphDomain).handle(pModel, sModel,
				protocolPower, isProtocol);

		// 4、处理特殊情况：如果是定量(1)定比(2)关系，需要补充子表分时抄表单。 3=总分
		if (isProtocol && meterRelationGraphDomain.isSplitTs()) {
			// 父表必定只有一个总
			String shareRate = meterRelationGraphDomain.getShareRate();
			String[] rate = shareRate == null ? new String[] { "1", "1", "1", "1" } : shareRate.split(":");
			BigDecimal denominator = Arrays.asList(rate).stream().map(BigDecimal::new).reduce(BigDecimal.ZERO,
					BigDecimal::add);

			pModel.getMeterData().stream().filter(m -> !"0".equals(m.getTimeSeg())).forEach(m -> {
				m.addProtocolPower(pModel.getProtocolPower().abs().multiply(new BigDecimal(rate[Integer.parseInt(m.getTimeSeg())]))
						.divide(denominator));
			});
		}

		// 5、如果还有下级，则循环
		if (meterRelationGraphDomain.getRel() != null) {
			meterRelationGraphDomain.getRel().stream()
					.sorted(Comparator.comparing(MeterRelationGraphDomain::getNumConnections))
					.forEach(r -> computeMeterRelation2(r, exchange));
		}
	}

	interface ProtocolHandler {
		public void handle(ECFModel pModel, ECFModel sModel, BigDecimal protocolPower, boolean isProtocol);

	}

	/**
	 * @param meterRelationGraphDomain
	 * @return
	 */
	private ProtocolHandler chooseProtocolHandler(Byte pMeterRateFlag, Byte meterRateFlag,
			MeterRelationGraphDomain meterRelationGraphDomain) {
		switch (pMeterRateFlag - meterRateFlag) {
		case 0:// 相同类型电能表
			return new ProtocolHandler() {

				@Override
				public void handle(ECFModel pModel, ECFModel sModel, BigDecimal protocolPower, boolean isProtocol) {
					meterRelationGraphDomain.setSplitTs(false);
					BigDecimal totalPower = pModel.getMeterData().stream().filter(m -> m.isP1R0())
							.map(MeterDataModel::getActiveEnergy).reduce(BigDecimal.ZERO, BigDecimal::add);

					pModel.getMeterData().stream().filter(pMeterData -> pMeterData.isP1())
							.forEach(pMeterData -> {
								MeterDataModel sMeterData = sModel.getMeterData().stream()
										.filter(s -> s.getKey() == pMeterData.getKey()).findFirst().orElse(null);

								if (sMeterData == null) {
									throw new NullPointerException("子计量点ID=" + sModel.getMeterId() + "对应数据不存在.");
								}

								if ("0".equals(pMeterData.getTimeSeg())) {// 总
									protocolAssignment(pMeterData, sMeterData, protocolPower, isProtocol);
								} else {
									protocolAssignment(pMeterData, sMeterData,
											protocolPower
											.multiply(pMeterData.getActiveEnergy().divide(totalPower, 4,RoundingMode.HALF_UP)),
											isProtocol);
								}

							});

				}

			};

		case 1:// TODO 分时-普通
			return new ProtocolHandler() {

				@Override
				public void handle(ECFModel pModel, ECFModel sModel, BigDecimal protocolPower, boolean isProtocl) {
					meterRelationGraphDomain.setSplitTs(false);

					MeterDataModel sMeterDataModel = sModel.getMeterData().stream().findFirst().get();
//					protocolHandler(pMeterData, sMeterData, protocolPower);

					// 总表电量分摊
					MeterDataModel[] dataModels = getProtocolOrder(pModel.getMeterData().stream().filter(m -> m.isP1())
							.filter(m -> !"0".equals(m.getTimeSeg())).collect(Collectors.toList()));

					BigDecimal pending = protocolPower;

					for (MeterDataModel temp : dataModels) {

						if (temp.getActiveEnergy().compareTo(pending) >= 0) {
							temp.addProtocolPower(pending.negate());
							sMeterDataModel.addProtocolPower(protocolPower);
							break;// 够扣，结束
						} else {
							temp.addProtocolPower(temp.getActiveEnergy().negate());
							sMeterDataModel
									.addProtocolPower(temp.getActiveEnergy().add(sMeterDataModel.getProtocolPower()));
							pending = protocolPower.subtract(temp.getActiveEnergy());
						}

					}

				}

			};

		case -1:// 普通-分时

			return new ProtocolHandler() {

				@Override
				public void handle(ECFModel pModel, ECFModel sModel, BigDecimal protocolPower, boolean isProtocl) {
					meterRelationGraphDomain.setSplitTs(true);
					pModel.getMeterData().stream().filter(m -> m.isP1R0()).forEach(pMeterData -> {
						MeterDataModel sMeterData = sModel.getMeterData().stream()
								.filter(s -> s.getKey() == pMeterData.getKey()).findFirst().orElse(null);
						protocolAssignment(pMeterData, sMeterData, protocolPower, isProtocl);
					});

				}

			};

		default:
			return null;
		}

	}

	private void protocolAssignment(MeterDataModel pMeterData, MeterDataModel sMeterData, BigDecimal protocolPower,
			boolean isProtocol) {
		if (pMeterData.getActiveEnergy().compareTo(protocolPower) >= 0) {
			if (isProtocol) {
				pMeterData.addProtocolPower(protocolPower.negate());
				sMeterData.addProtocolPower(protocolPower);
			} else {
				pMeterData.addProtocolPower(protocolPower.negate());
			}
		} else {
			if (isProtocol) {
				pMeterData.addProtocolPower(pMeterData.getActiveEnergy().negate());
				sMeterData.addProtocolPower(pMeterData.getActiveEnergy());
			} else {
				pMeterData.addProtocolPower(pMeterData.getActiveEnergy().negate());
			}
		}
	}

	// 协议扣减顺序。桂东是先分给平
	public MeterDataModel[] getProtocolOrder(Collection<MeterDataModel> dataModels) {
		return insertionSort(dataModels.toArray(new MeterDataModel[dataModels.size()]));
	}

	// 选择排序？
	private MeterDataModel[] insertionSort(MeterDataModel[] array) {
		if (array.length == 0)
			return array;
		MeterDataModel current;
		for (int i = 0; i < array.length - 1; i++) {
			current = array[i + 1];
			int preIndex = i;
			while (preIndex >= 0 && current.getKey() < array[preIndex].getKey()) {
				array[preIndex + 1] = array[preIndex];
				preIndex--;
			}
			array[preIndex + 1] = current;
		}

		for (int i = 0; i < array.length; i++) {

			if (array[i].getKey() == ECFModel.P) {// 平段第一个
				current = array[0];
				array[0] = array[i];
				array[i] = current;
			}
		}

		return array;
	}

}
