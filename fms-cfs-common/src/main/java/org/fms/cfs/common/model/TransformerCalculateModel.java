/**
 * Author : czy
 * Date : 2019年11月22日 上午11:19:27
 * Title : com.riozenc.cfs.webapp.cfm.model.TransformerCalculateModel.java
 *
**/
package org.fms.cfs.common.model;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerLoadParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossFormulaParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossTableParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;

import com.riozenc.titanTool.common.reflect.ReflectUtil;

/**
 * 变压器计算模型
 * 
 * @author czy
 *
 */
public class TransformerCalculateModel {

	private static final BigDecimal HOURS_24_DECIMAL = BigDecimal.valueOf(24);
	private static final BigDecimal DAY_30 = BigDecimal.valueOf(30);

	private int day = 30;// 变压器运行天数

	private Long key;
	private BigDecimal capacity = BigDecimal.ZERO;// 可能使用计量点上的计费容量
	private BigDecimal weightingCapacity = BigDecimal.ZERO;// 加权容量
	private BigDecimal activeEnergy = BigDecimal.ZERO;// 有功电量
	private BigDecimal reactiveEnergy = BigDecimal.ZERO;// 无功电量
	private BigDecimal activeTransformerLoss = BigDecimal.ZERO;;//
	private BigDecimal reactiveTransformerLoss = BigDecimal.ZERO;;//
	private List<TransformerDomain> transformerDomains;
	private List<TransformerMeterRelationDomain> transformerMeterRelationDomains;

	private Byte transformerLossType;// 变损计算方式
	private Byte transformerModelType;// 变压器型号

	private boolean isIncludedReactiveMeter = false;
	private boolean isCalculated = false;

	public TransformerCalculateModel(TransformerDomain transformerDomain,
			List<TransformerMeterRelationDomain> transformerMeterRelationDomains) {
		this.transformerDomains = Arrays.asList(transformerDomain);
		this.transformerLossType = transformerDomain.getTransformerLossType();
		this.transformerModelType = transformerDomain.getTransformerModelType();
		this.key = transformerDomain.getId();
		this.transformerMeterRelationDomains = transformerMeterRelationDomains;
		this.capacity = this.capacity.add(transformerDomain.getCapacity());
	}

	public TransformerCalculateModel(List<TransformerDomain> transformerDomains,
			List<TransformerMeterRelationDomain> transformerMeterRelationDomains, Long groupNo) {
		this.transformerDomains = transformerDomains;
		this.transformerLossType = transformerDomains.get(0).getTransformerLossType();
		this.transformerModelType = transformerDomains.get(0).getTransformerModelType();
		this.key = groupNo;
		this.transformerMeterRelationDomains = transformerMeterRelationDomains;
		transformerDomains.forEach(transformerDomain -> {
			this.capacity = this.capacity.add(transformerDomain.getCapacity());
		});
	}

	public Long getKey() {
		return key;
	}

	public void setDay() {
		// TODO 设置变压器运行天数
	}

	private BigDecimal getDay() {
		return BigDecimal.valueOf(day);
	}

	public Byte getTransformerLossType() {
		return transformerLossType;
	}

	public Byte getTransformerModelType() {
		return transformerModelType;
	}

	public BigDecimal getActiveEnergy() {
		return activeEnergy;
	}

	public BigDecimal getReactiveEnergy() {
		return reactiveEnergy;
	}

	public List<TransformerDomain> getTransformerDomains() {
		return transformerDomains;
	}

	public boolean isIncludedReactiveMeter() {
		return isIncludedReactiveMeter;
	}

	public void setIncludedReactiveMeter(boolean isIncludedReactiveMeter) {
		this.isIncludedReactiveMeter = isIncludedReactiveMeter;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public boolean isCalculated() {
		return isCalculated;
	}

	public BigDecimal getActiveTransformerLoss() {
		return activeTransformerLoss;
	}

	public BigDecimal getReactiveTransformerLoss() {
		return reactiveTransformerLoss;
	}

	public BigDecimal addActiveEnergy(BigDecimal activeEnergy) {
		this.activeEnergy = this.activeEnergy.add(activeEnergy);
		return this.activeEnergy;
	}

	public BigDecimal addReactiveEnergy(BigDecimal reactiveEnergy) {
		this.reactiveEnergy = this.reactiveEnergy.add(reactiveEnergy);
		return this.reactiveEnergy;
	}

	public String getTableKey() {

		return getTransformerModelType() + "#" + transformerDomains.stream().map(TransformerDomain::getVoltType)
				.max(Comparator.comparing(i -> i)).get();
	}

	public String getFormulaKey() {
		return getTransformerModelType() + "#" + getCapacity();
	}

	public BigDecimal[] calculateByEmpty() {
		this.isCalculated = true;
		return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
	}

	public BigDecimal[] calculateByTable(List<TransformerLossTableParamDomain> transformerLossTableParamDomains,
			TransformerLoadParamDomain loadDomain) {
		BigDecimal loadRatio = BigDecimal.ZERO;// 负荷比
		if (transformerLossTableParamDomains == null)
			return null;

		if (BigDecimal.ZERO.compareTo(activeEnergy) == 0) {// 有功电量等于0的情况
			BigDecimal loss = transformerLossTableParamDomains.stream()
					.sorted(Comparator.comparing(TransformerLossTableParamDomain::getId))
					.filter(t -> BigDecimal.ZERO.compareTo(t.getLoadRatio()) == 0).findFirst().get()
					.getActiveTransformerLoss1();

			this.activeTransformerLoss = loss.multiply(getDay()).divide(DAY_30).setScale(0, BigDecimal.ROUND_HALF_UP);

			this.reactiveTransformerLoss = this.activeTransformerLoss;
			return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
		} else {

			//生产班次
			int produceTeam = transformerDomains.stream().filter(t -> t.getProduceTeam() != null).findFirst().get()
					.getProduceTeam();

			//班次负荷
			BigDecimal bcfh = (BigDecimal) ReflectUtil.getFieldValue(loadDomain, "load" + produceTeam);

			// 最大负荷电量zdfh = 班次负荷量 * 变压器使用天数/30
			BigDecimal zdfh = bcfh.multiply(getDay()).divide(DAY_30).setScale(0, BigDecimal.ROUND_HALF_UP);

			if (BigDecimal.ZERO.compareTo(zdfh) != 0) {
				loadRatio = activeEnergy.divide(zdfh, 5, BigDecimal.ROUND_HALF_UP).add(BigDecimal.valueOf(0.05));
				if (loadRatio.compareTo(BigDecimal.ONE) > 0) {
					loadRatio = BigDecimal.valueOf(1.1);
				} else {
					loadRatio = loadRatio.setScale(1, BigDecimal.ROUND_HALF_UP);

//					loadRatio = loadRatio.setScale(1, BigDecimal.ROUND_DOWN);
				}
			}

			final BigDecimal newloadRatio = loadRatio;
			TransformerLossTableParamDomain transformerLossTableParamDomain = transformerLossTableParamDomains.stream()
					.filter(t -> t.getLoadRatio().compareTo(newloadRatio) == 0)
					.filter(t -> t.getCapacity().compareTo(capacity) == 0)
					.sorted(Comparator.comparing(TransformerLossTableParamDomain::getId)).findFirst().get();

			if (transformerLossTableParamDomain == null) {
				return null;
			} else {
				if (loadRatio.compareTo(BigDecimal.ONE) <= 0) {// 负荷比Fhb<=1
					this.activeTransformerLoss = ((BigDecimal) ReflectUtil
							.getFieldValue(transformerLossTableParamDomain, "activeTransformerLoss" + produceTeam))
									.multiply(getDay()).divide(DAY_30, 0, BigDecimal.ROUND_HALF_UP);

					this.reactiveTransformerLoss = ((BigDecimal) ReflectUtil
							.getFieldValue(transformerLossTableParamDomain, "reactiveTransformerLoss" + produceTeam))
									.multiply(getDay()).divide(DAY_30, 0, BigDecimal.ROUND_HALF_UP);

				} else {// 负荷比Fhb>1
					this.activeTransformerLoss = ((BigDecimal) ReflectUtil
							.getFieldValue(transformerLossTableParamDomain, "activeTransformerLoss" + produceTeam))
									.multiply(activeEnergy)
									.divide(BigDecimal.valueOf(1000), 0, BigDecimal.ROUND_HALF_UP);

					this.reactiveTransformerLoss = ((BigDecimal) ReflectUtil
							.getFieldValue(transformerLossTableParamDomain, "reactiveTransformerLoss" + produceTeam))
									.multiply(activeEnergy).divide(BigDecimal.valueOf(1000)).multiply(getDay())
									.divide(DAY_30, 0, BigDecimal.ROUND_HALF_UP);

					if (this.reactiveTransformerLoss.compareTo(BigDecimal.ZERO) == 0) {
						TransformerLossTableParamDomain temp = transformerLossTableParamDomains.stream()
								.filter(t -> t.getLoadRatio().compareTo(BigDecimal.ONE) == 0)
								.filter(t -> t.getCapacity().compareTo(capacity) == 0).findFirst().get();
						this.reactiveTransformerLoss = (BigDecimal) ReflectUtil.getFieldValue(temp,
								"reactiveTransformerLoss" + produceTeam);
					}
				}
			}

		}
		this.isCalculated = true;
		return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
	}

	public BigDecimal[] calculateByFormula(TransformerLossFormulaParamDomain transformerLossFormulaParamDomain) {

		if (0 == this.day) {
			return new BigDecimal[] { BigDecimal.ZERO, BigDecimal.ZERO };
		}

		BigDecimal calculatedCapacity = this.weightingCapacity.divide(BigDecimal.valueOf(30)).setScale(0,
				BigDecimal.ROUND_HALF_UP);

		// 有功不变损失 yg_bbss =空载损耗sh_kz * 运行时间（小时）
		BigDecimal ygbbss = transformerLossFormulaParamDomain.getEmptyLose().multiply(BigDecimal.valueOf(this.day))
				.multiply(BigDecimal.valueOf(24));

		// 修正系数
		BigDecimal xzxs = transformerDomains.stream().filter(t -> t.getProduceTeam() != null).findFirst().get()
				.getCorrectionFactor();
		Byte connectionGroup = transformerDomains.stream().filter(t -> t.getProduceTeam() != null).findFirst().get()
				.getConnectionGroup();

		// 有功损失常数yg_sscs =:(修正系数xzxs * 短路损耗sh_dl* 1000000)/(运行天数yx_days*24*变压器容量*变压器容量)
		BigDecimal ygsscs = (xzxs
				.multiply(connectionGroup == 1 ? transformerLossFormulaParamDomain.getShortCircuitLoss1()
						: transformerLossFormulaParamDomain.getShortCircuitLoss2())
				.multiply(BigDecimal.valueOf(1000000)))
						.divide(BigDecimal.valueOf(this.day).multiply(
								BigDecimal.valueOf(24).multiply(calculatedCapacity).multiply(calculatedCapacity)), 1,
								BigDecimal.ROUND_HALF_UP);

		if (isIncludedReactiveMeter()) {// 存着无功表，
			// 有功变损 (有功不变损失yg_bbss + 有功损失常数yg_sscs * ( 有功电量yg_syl_k * 有功电量yg_syl_k +
			// 无功电量wg_syl_k * 无功电量wg_syl_k )
			this.activeTransformerLoss = ygbbss.add(ygsscs.multiply(
					(activeEnergy.pow(2).add(reactiveEnergy.pow(2))).divide(BigDecimal.valueOf(1000).pow(2))));

			// 无功不变损失wg_bbss = 空载电流kzdl * 变压器容量byq_rl * 运行时间（小时）*0.01
			BigDecimal wgbbss = transformerLossFormulaParamDomain.getEmptyCurrent().multiply(calculatedCapacity)
					.multiply(BigDecimal.valueOf(this.day)).multiply(BigDecimal.valueOf(24))
					.divide(BigDecimal.valueOf(100), 1, BigDecimal.ROUND_HALF_UP);
			// 无功损失常数wg_sscs = (修正系数xzxs * 阻抗电压zkdy * 10000)/(运行天数 * 24 * 变压器容量)
			BigDecimal wgsscs = xzxs.multiply(transformerLossFormulaParamDomain.getImpedance())
					.multiply(BigDecimal.valueOf(10000))
					.divide((BigDecimal.valueOf(this.day)
							.multiply(BigDecimal.valueOf(24).multiply(calculatedCapacity))), 1,
							BigDecimal.ROUND_HALF_UP);

			// 无功变损wg_bs = ( 无功不变损失wg_bbss + 功损失常数wg_sscs *( 有功电量yg_syl_k * 有功电量yg_syl_k +
			// 无功电量wg_syl_k *功电量wg_syl_k )
			this.reactiveTransformerLoss = wgbbss.add(wgsscs
					.multiply(activeEnergy.pow(2).add(reactiveEnergy.pow(2)).divide(BigDecimal.valueOf(1000).pow(2))));

		} else {// 不存在无功表
			// （无无功）有功变损yg_bs = (有功不变损失yg_bbss+有功损失常数yg_sscs * (有功电量yg_syl_k * 1.0/功率因数)
			// *（有功电量*1.0/功率因数）
			BigDecimal k = BigDecimal.valueOf(0.8);// 维护到SYSTEM_COMMON中
			this.activeTransformerLoss = ygbbss.add(ygsscs.multiply(
					(activeEnergy.divide(BigDecimal.valueOf(1000)).multiply(BigDecimal.ONE).divide(k)).pow(2)));
			this.reactiveTransformerLoss = BigDecimal.ZERO;
		}

		this.isCalculated = true;
		return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
	}

	/**
	 * 变损分摊
	 */
	public BigDecimal lossAllocation(BigDecimal power, BigDecimal divisor, BigDecimal dividend) {
		if (BigDecimal.ZERO.compareTo(dividend) == 0) {
			return BigDecimal.ZERO;
		}

		BigDecimal proportion = divisor.divide(dividend, 4, BigDecimal.ROUND_HALF_UP);

		return power.multiply(proportion).setScale(0, BigDecimal.ROUND_HALF_UP);
	}
}
