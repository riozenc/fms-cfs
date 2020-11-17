/**
 * Author : czy
 * Date : 2019年11月22日 上午11:19:27
 * Title : com.riozenc.cfs.webapp.cfm.model.TransformerCalculateModel.java
 *
**/
package org.fms.cfs.common.model;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;

import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossFormulaParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossTableParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;

import com.riozenc.titanTool.common.json.annotation.IgnorRead;

/**
 * 变压器计算模型
 * 
 * @author czy
 *
 */
public class TransformerCalculateModel {
	private int day = 30;

	private Long key;
	private BigDecimal capacity = BigDecimal.ZERO;// 可能使用计量点上的计费容量
//	private BigDecimal meterCapacity = BigDecimal.ZERO;// 计量点容量 //2020.1.9 废弃，基本电费力调电费用计量点自己的容量合
	private BigDecimal activeEnergy = BigDecimal.ZERO;// 有功电量
	private BigDecimal reactiveEnergy = BigDecimal.ZERO;// 无功电量
	private BigDecimal activeTransformerLoss = BigDecimal.ZERO;;//
	private BigDecimal reactiveTransformerLoss = BigDecimal.ZERO;;//

	private HashSet<TransformerLossAllocation> transformerLossAllocations = new HashSet<TransformerCalculateModel.TransformerLossAllocation>();

	@IgnorRead
	private List<TransformerDomain> transformerDomains;
	@IgnorRead
	private List<TransformerMeterRelationDomain> transformerMeterRelationDomains;

	private Byte transformerLossType;// 变损计算方式
	private Byte transformerModelType;// 变压器型号

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

	public int getDay() {
		return day;
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

	public List<TransformerMeterRelationDomain> getTransformerMeterRelationDomains() {
		return transformerMeterRelationDomains;
	}

	public void addTransformerLossAllocation(Long meterId, String meterNo, BigDecimal power,
			Integer transformerLossType, BigDecimal activeTransformerLoss, BigDecimal reactiveTransformerLoss) {

		this.transformerLossAllocations.add(new TransformerLossAllocation(meterId, meterNo, power, transformerLossType,
				activeTransformerLoss, reactiveTransformerLoss));
	}

	public String getTableKey() {

		return getTransformerModelType() + "#" + transformerDomains.stream().map(TransformerDomain::getVoltType)
				.max(Comparator.comparing(i -> i)).get();
	}

	public String getFormulaKey() {
		return getTransformerModelType() + "#" + getCapacity().intValue();
	}

	public BigDecimal[] calculateByEmpty() {
		this.isCalculated = true;
		return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
	}

	public BigDecimal[] calculateByTable(List<TransformerLossTableParamDomain> transformerLossTableParamDomains) {

		if (transformerLossTableParamDomains == null)
			return null;
		for (TransformerLossTableParamDomain tltpd : transformerLossTableParamDomains) {
			if (activeEnergy.compareTo(tltpd.getPowerLowerLimit()) != -1) { // >= 下限
				if (activeEnergy.compareTo(tltpd.getPowerUpperLimit()) != 1) { // <=上限
					this.isCalculated = true;
					this.activeTransformerLoss = tltpd.getActiveTransformerLoss();
					this.reactiveTransformerLoss = tltpd.getReactiveTransformerLoss();
					return new BigDecimal[] { this.activeTransformerLoss, this.reactiveTransformerLoss };
				}
			}
		}
		return null;
	}

	public BigDecimal[] calculateByFormula(TransformerLossFormulaParamDomain transformerLossFormulaParamDomain) {
//		有功损耗=有功空载损耗×24×变压器运行天数+修正系数K值×（有功抄见电量^2＋无功抄见电量^2）×有功负载损耗/(额定容量2×24×变压器运行天数)
//		变压器属性 生产班次PRODUCE_TEAM

		// 查询变压器的变成信息（流程）

		BigDecimal ak = BigDecimal.valueOf(
				transformerDomains.stream().filter(t -> t.getProduceTeam() != null).findFirst().get().getProduceTeam())
				.divide(BigDecimal.valueOf(10));

		BigDecimal ar1 = transformerLossFormulaParamDomain.getEmptyLose().multiply(BigDecimal.valueOf(24))
				.multiply(BigDecimal.valueOf(day));
		BigDecimal ar2 = ak.multiply((activeEnergy.add(reactiveEnergy)).multiply(transformerLossFormulaParamDomain
				.getLoadLoss().divide((capacity.multiply(BigDecimal.valueOf(24)).multiply(BigDecimal.valueOf(day))))));

		this.activeTransformerLoss = ar1.add(ar2);

		// =================无功==================

//		无功损耗=无功空载损耗×24×变压器运行天数+修正系数K值×（有功抄见电量2＋无功抄见电量2）×无功负载损耗/(额定容量2×24×变压器运行天数)
//		 无功损耗（没有无功表）=无功空载损耗×24×变压器运行天数+修正系数K值×（有功抄见电量2）×无功负载损耗/(功率因数×24×变压器运行天数)

//无功空载损耗=容量*空载电流/100
//无功负载损耗=容量*短路电压/100

		BigDecimal rp1 = capacity.multiply(transformerLossFormulaParamDomain.getEmptyCurrent())
				.divide(BigDecimal.valueOf(100));
		BigDecimal rp2 = capacity.multiply(transformerLossFormulaParamDomain.getLoadVoltage())
				.divide(BigDecimal.valueOf(100));

		BigDecimal rk = BigDecimal.valueOf(
				transformerDomains.stream().filter(t -> t.getProduceTeam() != null).findFirst().get().getProduceTeam())
				.divide(BigDecimal.valueOf(10));
		BigDecimal rr1 = rp1.multiply(BigDecimal.valueOf(24)).multiply(BigDecimal.valueOf(day));
		BigDecimal rr2 = rk.multiply((activeEnergy.add(reactiveEnergy)).multiply(rp2)
				.divide((capacity.multiply(BigDecimal.valueOf(24)).multiply(BigDecimal.valueOf(24)))));

		this.reactiveTransformerLoss = rr1.add(rr2);

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

		BigDecimal proportion = divisor.divide(dividend, 6, RoundingMode.HALF_UP);

		return power.multiply(proportion).setScale(0, BigDecimal.ROUND_HALF_UP);
	}

	class TransformerLossAllocation {
		private Long meterId;
		private String meterNo;
		private BigDecimal power;
		private Integer transformerLossType;
		private BigDecimal activeTransformerLoss;
		private BigDecimal reactiveTransformerLoss;

		public TransformerLossAllocation(Long meterId, String meterNo, BigDecimal power, Integer transformerLossType,
				BigDecimal activeTransformerLoss, BigDecimal reactiveTransformerLoss) {
			this.meterId = meterId;
			this.meterNo = meterNo;
			this.transformerLossType = transformerLossType;
			this.power = power;
			this.activeTransformerLoss = activeTransformerLoss;
			this.reactiveTransformerLoss = reactiveTransformerLoss;
		}

		public Long getMeterId() {
			return meterId;
		}

		public void setMeterId(Long meterId) {
			this.meterId = meterId;
		}

		public String getMeterNo() {
			return meterNo;
		}

		public void setMeterNo(String meterNo) {
			this.meterNo = meterNo;
		}

		public BigDecimal getActiveTransformerLoss() {
			return activeTransformerLoss;
		}

		public void setActiveTransformerLoss(BigDecimal activeTransformerLoss) {
			this.activeTransformerLoss = activeTransformerLoss;
		}

		public BigDecimal getReactiveTransformerLoss() {
			return reactiveTransformerLoss;
		}

		public void setReactiveTransformerLoss(BigDecimal reactiveTransformerLoss) {
			this.reactiveTransformerLoss = reactiveTransformerLoss;
		}

		public BigDecimal getPower() {
			return power;
		}

		public void setPower(BigDecimal power) {
			this.power = power;
		}

		public Integer getTransformerLossType() {
			return transformerLossType;
		}

		public void setTransformerLossType(Integer transformerLossType) {
			this.transformerLossType = transformerLossType;
		}

		@Override
		public int hashCode() {
			return this.meterId.intValue();
		}

		@Override
		public boolean equals(Object obj) {
			return hashCode() == (obj.hashCode());
		}

	}
}
