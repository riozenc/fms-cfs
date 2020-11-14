/**
 * Author : czy
 * Date : 2019年7月30日 上午8:46:07
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.TransformerLossTableParamDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;

/**
 * 变损参数表（查表法）
 * 
 * @author czy
 *
 */
public class TransformerLossTableParamDomain {

	private Long id;// ID ID bigint TRUE FALSE TRUE
	private BigDecimal loadRatio;// 负荷比
	private BigDecimal capacity;// 容量 CAPACITY decimal(8,2) 8 2 FALSE FALSE FALSE
	private BigDecimal activeTransformerLoss1;
	private BigDecimal activeTransformerLoss2;
	private BigDecimal activeTransformerLoss3;

	private BigDecimal reactiveTransformerLoss1;// 无功变损
	private BigDecimal reactiveTransformerLoss2;// 无功变损
	private BigDecimal reactiveTransformerLoss3;// 无功变损

	private Byte status;//

	private Byte transformerType;// 变压器型号
	private Byte voltLevelType; // 电压等级
	private BigDecimal powerLowerLimit;// 电量下限
	private BigDecimal powerUpperLimit;// 电量上限
	private BigDecimal activeTransformerLoss;// 有功变损
	private BigDecimal reactiveTransformerLoss;// 无功变损
	
	
	public Byte getTransformerType() {
		return transformerType;
	}

	public void setTransformerType(Byte transformerType) {
		this.transformerType = transformerType;
	}

	public Byte getVoltLevelType() {
		return voltLevelType;
	}

	public void setVoltLevelType(Byte voltLevelType) {
		this.voltLevelType = voltLevelType;
	}

	public BigDecimal getPowerLowerLimit() {
		return powerLowerLimit;
	}

	public void setPowerLowerLimit(BigDecimal powerLowerLimit) {
		this.powerLowerLimit = powerLowerLimit;
	}

	public BigDecimal getPowerUpperLimit() {
		return powerUpperLimit;
	}

	public void setPowerUpperLimit(BigDecimal powerUpperLimit) {
		this.powerUpperLimit = powerUpperLimit;
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

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getLoadRatio() {
		return loadRatio;
	}

	public void setLoadRatio(BigDecimal loadRatio) {
		this.loadRatio = loadRatio;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getActiveTransformerLoss1() {
		return activeTransformerLoss1;
	}

	public void setActiveTransformerLoss1(BigDecimal activeTransformerLoss1) {
		this.activeTransformerLoss1 = activeTransformerLoss1;
	}

	public BigDecimal getActiveTransformerLoss2() {
		return activeTransformerLoss2;
	}

	public void setActiveTransformerLoss2(BigDecimal activeTransformerLoss2) {
		this.activeTransformerLoss2 = activeTransformerLoss2;
	}

	public BigDecimal getActiveTransformerLoss3() {
		return activeTransformerLoss3;
	}

	public void setActiveTransformerLoss3(BigDecimal activeTransformerLoss3) {
		this.activeTransformerLoss3 = activeTransformerLoss3;
	}

	public BigDecimal getReactiveTransformerLoss1() {
		return reactiveTransformerLoss1;
	}

	public void setReactiveTransformerLoss1(BigDecimal reactiveTransformerLoss1) {
		this.reactiveTransformerLoss1 = reactiveTransformerLoss1;
	}

	public BigDecimal getReactiveTransformerLoss2() {
		return reactiveTransformerLoss2;
	}

	public void setReactiveTransformerLoss2(BigDecimal reactiveTransformerLoss2) {
		this.reactiveTransformerLoss2 = reactiveTransformerLoss2;
	}

	public BigDecimal getReactiveTransformerLoss3() {
		return reactiveTransformerLoss3;
	}

	public void setReactiveTransformerLoss3(BigDecimal reactiveTransformerLoss3) {
		this.reactiveTransformerLoss3 = reactiveTransformerLoss3;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
