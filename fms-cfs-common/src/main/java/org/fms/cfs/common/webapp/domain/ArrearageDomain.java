/**
 * Author : czy
 * Date : 2019年11月2日 上午11:33:36
 * Title : com.riozenc.cfs.webapp.dsm.domain.ArrearageDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fms.cfs.common.utils.MongoUtils;


/**
 * ARREARAGE_INFO
 * 
 * @author czy
 *
 */
public class ArrearageDomain {

	private String _id;
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private String arrearageNo;// 应收凭证号 ARREARAGE_NO varchar(16) 16 FALSE FALSE FALSE
	private Long meterId;// 计量点ID METER_ID bigint FALSE FALSE FALSE
	private Integer mon;// 电费月份 MON int FALSE FALSE FALSE
	private Byte sn;// 算费次数 SN smallint FALSE FALSE FALSE
	private Long meterMoneyId;// 明细ID METER_MONEY_ID bigint FALSE FALSE FALSE
	private Date endDate;// 收费截止日期 END_DATE date FALSE FALSE FALSE
	private BigDecimal receivable;// 应收电费 RECEIVABLE decimal(14,4) 14 4 FALSE FALSE FALSE
	private Byte isSettle;// 是否结清 IS_SETTLE smallint FALSE FALSE FALSE
	private Date createDate;// 创建日期 CREATE_DATE datetime FALSE FALSE FALSE
	private String operator;// 操作员 OPERATOR varchar(64) 64 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE
	private Long writeSectId;// 抄表区段ID
	private Long writorId;// 抄表员ID
	private Long businessPlaceCode;// 营业区域ID
	private BigDecimal totalPower;

	public ArrearageDomain(MeterMoneyDomain meterMoneyDomain, MeterDomain meterDomain) {

		this.meterId = meterMoneyDomain.getMeterId();
		this.mon = meterMoneyDomain.getMon();
		this.sn = meterMoneyDomain.getSn();
		this.meterMoneyId = meterMoneyDomain.getId();
		this.receivable = meterMoneyDomain.getAmount();
		this.isSettle = 0;
		this.createDate = new Date();
		this.totalPower = meterMoneyDomain.getTotalPower();
		this.setOperator(null);
		this.setStatus((byte) 1);

		this.writeSectId = meterMoneyDomain.getWriteSectId();
		this.writorId = meterDomain.getWritorId();
		this.businessPlaceCode = meterDomain.getBusinessPlaceCode();

//		DateUtil.getDatePlusDays(date, day)

//		MonUtils.getDateByMon(MonUtils.getNextMon(mon.toString()), meterMoneyDomain.get);
		// TODO 截至日期
		this.endDate = null;// 固定值

		createObjectId();
	}

	public String get_id() {
		return _id;
	}

	public void createObjectId() {
		this._id = MongoUtils.createObjectId(this.getMeterId(), this.getMon(), this.getSn());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getArrearageNo() {
		return arrearageNo;
	}

	public void setArrearageNo(String arrearageNo) {
		this.arrearageNo = arrearageNo;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Integer getMon() {
		return mon;
	}

	public void setMon(Integer mon) {
		this.mon = mon;
	}

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

	public Long getMeterMoneyId() {
		return meterMoneyId;
	}

	public void setMeterMoneyId(Long meterMoneyId) {
		this.meterMoneyId = meterMoneyId;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public BigDecimal getReceivable() {
		return receivable;
	}

	public void setReceivable(BigDecimal receivable) {
		this.receivable = receivable;
	}

	public Byte getIsSettle() {
		return isSettle;
	}

	public void setIsSettle(Byte isSettle) {
		this.isSettle = isSettle;
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

	public Long getWriteSectId() {
		return writeSectId;
	}

	public Long getWritorId() {
		return writorId;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public BigDecimal getTotalPower() {
		return totalPower;
	}

}
