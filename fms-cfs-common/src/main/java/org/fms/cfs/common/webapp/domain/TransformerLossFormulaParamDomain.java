/**
 * Author : czy
 * Date : 2019年7月30日 上午8:46:07
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.TransformerLossFormulaParamDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 变损参数表（公式法）
 * 
 * @author czy
 *
 */
public class TransformerLossFormulaParamDomain {

	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Byte transformerType;// 变压器型号 TRANSFORMER_TYPE smallint FALSE FALSE FALSE
	private BigDecimal capacity;// 容量 CAPACITY decimal(8,2) 8 2 FALSE FALSE FALSE
	private BigDecimal emptyLose;// 空载损耗 EMPTY_LOSE decimal(8,4) 8 4 FALSE FALSE FALSE

	private BigDecimal shortCircuitLoss1;// 短路损耗,Y-△接法
	private BigDecimal shortCircuitLoss2;// 短路损耗，Yyn0接法
	private BigDecimal impedance;// 阻抗

	private BigDecimal emptyCurrent;// 空载电流 EMPTY_CURRENT decimal(8,2) 8 2 FALSE FALSE FALSE

	private Long operator;// 操作人 OPERATOR bigint FALSE FALSE FALSE
	private Date datetime;// 操作时间 OPERATION_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getTransformerType() {
		return transformerType;
	}

	public void setTransformerType(Byte transformerType) {
		this.transformerType = transformerType;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public BigDecimal getEmptyLose() {
		return emptyLose;
	}

	public void setEmptyLose(BigDecimal emptyLose) {
		this.emptyLose = emptyLose;
	}

	public BigDecimal getShortCircuitLoss1() {
		return shortCircuitLoss1;
	}

	public void setShortCircuitLoss1(BigDecimal shortCircuitLoss1) {
		this.shortCircuitLoss1 = shortCircuitLoss1;
	}

	public BigDecimal getShortCircuitLoss2() {
		return shortCircuitLoss2;
	}

	public void setShortCircuitLoss2(BigDecimal shortCircuitLoss2) {
		this.shortCircuitLoss2 = shortCircuitLoss2;
	}

	public BigDecimal getImpedance() {
		return impedance;
	}

	public void setImpedance(BigDecimal impedance) {
		this.impedance = impedance;
	}

	public BigDecimal getEmptyCurrent() {
		return emptyCurrent;
	}

	public void setEmptyCurrent(BigDecimal emptyCurrent) {
		this.emptyCurrent = emptyCurrent;
	}

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Date getDatetime() {
		return datetime;
	}

	public void setDatetime(Date datetime) {
		this.datetime = datetime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
