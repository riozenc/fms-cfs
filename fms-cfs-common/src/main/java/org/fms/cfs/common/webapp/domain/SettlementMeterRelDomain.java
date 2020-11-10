/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午6:15:27
 *    Title:com.riozenc.cim.webapp.domain.SettlementDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 *
 * 结算户与计量点关系表SETTLEMENT_METER_REL
 *
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class SettlementMeterRelDomain extends ManagerParamEntity implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long settlementId; // 结算户ID SETTLEMENT_ID bigint
	private Long meterId; // 计量点ID METER_ID bigint
	private String meterName; // 计量点名称
	private String meterNo; // 计量点编号
	private Byte deductionOrder; // 扣减顺序 DEDUCTION_ORDER int
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE
	private Byte mpType; // 计量点类型 1电表 2水表 3燃气表 4住户

	public Long getSettlementId() {
		return settlementId;
	}

	public void setSettlementId(Long settlementId) {
		this.settlementId = settlementId;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public Byte getDeductionOrder() {
		return deductionOrder;
	}

	public void setDeductionOrder(Byte deductionOrder) {
		this.deductionOrder = deductionOrder;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public Byte getMpType() {
		return mpType;
	}

	public void setMpType(Byte mpType) {
		this.mpType = mpType;
	}

}
