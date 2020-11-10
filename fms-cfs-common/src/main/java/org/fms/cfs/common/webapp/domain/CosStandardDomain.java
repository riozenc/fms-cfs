/**
 * Author : czy
 * Date : 2019年9月20日 下午2:10:21
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.CosStandardDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

/**
 * 力调标准
 * 
 * @author czy
 *
 */
public class CosStandardDomain {

	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Byte cosType;// 力调标准 COS_TYPE smallint FALSE FALSE FALSE
	private BigDecimal cos;// 功率因数 COS decimal(5,2) 5 2 FALSE FALSE FALSE
	private BigDecimal cosStd;// 调整系数 COS_STD decimal(5,2) 5 2 FALSE FALSE FALSE
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String operator;// 操作人 OPERATOR varchar(32) 32 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Byte getCosType() {
		return cosType;
	}

	public void setCosType(Byte cosType) {
		this.cosType = cosType;
	}

	public BigDecimal getCos() {
		return cos;
	}

	public void setCos(BigDecimal cos) {
		this.cos = cos;
	}

	public BigDecimal getCosStd() {
		return cosStd;
	}

	public void setCosStd(BigDecimal cosStd) {
		this.cosStd = cosStd;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

}
