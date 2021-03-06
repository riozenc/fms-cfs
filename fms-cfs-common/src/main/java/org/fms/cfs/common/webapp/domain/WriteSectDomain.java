/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:38:56
 *    Title:com.riozenc.cim.webapp.domain.WriteSectDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;
import com.riozenc.titanTool.mybatis.pagination.Page;

/**
 * 抄表区段 WRITE_SECTION_INFO
 * 
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class WriteSectDomain extends Page implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID bigint TRUE FALSE TRUE
	private String writeSectNo;// 抄表区段 WRITE_SECT_NO varchar(8) 8 FALSE FALSE FALSE
	private String writeSectName;// 抄表区段名称 WRITE_SECT_NAME varchar(32) 32 FALSE FALSE FALSE
	private Long writorId;// 抄表员ID WRITOR_ID bigint FALSE FALSE FALSE
	private Long calculatorId;// 计算人ID CALCULATOR_ID bigint FALSE FALSE FALSE
	private Long businessPlaceCode;// 营业区域 BUSINESS_PLACE_CODE varchar(8) 8 FALSE FALSE FALSE
	private String deptCode;// 抄表班组 DEPT_CODE varchar(8) 8 FALSE FALSE FALSE
	private Byte writeType;// 抄表方式 WRITE_TYPE smallint FALSE FALSE FALSE
	private Byte shouldWriteDays;// 应抄日期 SHOULD_WRITE_DAY smallint FALSE FALSE FALSE
	private Byte standardWriteDays;// 抄表考核周期 STANDARD_WRITE_DAY smallint FALSE FALSE FALSE
	private Byte shouldCalDays;// 应计算日期 SHOULD_CAL_DAY smallint FALSE FALSE FALSE
	private Long sectUserType;// 区段用户类型 SECT_USER_TYPE varchar(8) 8 FALSE FALSE FALSE
	private Integer punishDays;// 应算违约金天数 PUNISH_DAY decimal(5) 5 FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	private String mon;// 电费月份

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getWriteSectNo() {
		return writeSectNo;
	}

	public void setWriteSectNo(String writeSectNo) {
		this.writeSectNo = writeSectNo;
	}

	public String getWriteSectName() {
		return writeSectName;
	}

	public void setWriteSectName(String writeSectName) {
		this.writeSectName = writeSectName;
	}

	public Long getWritorId() {
		return writorId;
	}

	public void setWritorId(Long writorId) {
		this.writorId = writorId;
	}

	public Long getCalculatorId() {
		return calculatorId;
	}

	public void setCalculatorId(Long calculatorId) {
		this.calculatorId = calculatorId;
	}

	public String getDeptCode() {
		return deptCode;
	}

	public void setDeptCode(String deptCode) {
		this.deptCode = deptCode;
	}

	public Byte getWriteType() {
		return writeType;
	}

	public void setWriteType(Byte writeType) {
		this.writeType = writeType;
	}

	public Byte getShouldWriteDays() {
		return shouldWriteDays;
	}

	public void setShouldWriteDays(Byte shouldWriteDays) {
		this.shouldWriteDays = shouldWriteDays;
	}

	public Byte getStandardWriteDays() {
		return standardWriteDays;
	}

	public void setStandardWriteDays(Byte standardWriteDays) {
		this.standardWriteDays = standardWriteDays;
	}

	public Byte getShouldCalDays() {
		return shouldCalDays;
	}

	public void setShouldCalDays(Byte shouldCalDays) {
		this.shouldCalDays = shouldCalDays;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}

	public Long getSectUserType() {
		return sectUserType;
	}

	public void setSectUserType(Long sectUserType) {
		this.sectUserType = sectUserType;
	}

	public Integer getPunishDays() {
		return punishDays;
	}

	public void setPunishDays(Integer punishDays) {
		this.punishDays = punishDays;
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

	public String getMon() {
		return mon;
	}

	public void setMon(String mon) {
		this.mon = mon;
	}

}
