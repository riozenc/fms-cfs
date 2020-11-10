/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午5:40:37
 *    Title:com.riozenc.cim.webapp.domain.UserDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fms.cfs.common.utils.MongoUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;
import com.riozenc.titanTool.mybatis.pagination.Page;

/**
 * 用电户 USER_INFO
 * 
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserDomain extends Page implements MybatisEntity {

	private String _id;// mongoId
	private Long id;// ID ID bigint TRUE FALSE TRUE

	private Long customerId;// 对应 CUSTOMER_INFO中的ID 客户ID
	private Long tgId;// 台区ID
	private String userNo;// 客户编号 USER_NO varchar(16) 16 FALSE FALSE FALSE
	private String userName;// 客户名称 USER_NAME varchar(64) 64 FALSE FALSE FALSE
	private String address;// 用电地址 ADDRESS varchar(64) 64 FALSE FALSE FALSE
	private Long businessPlaceCode;// 营业区域 BUSINESS_PLACE_CODE varchar(8) 8 FALSE FALSE FALSE
	private Long writeSectId;// 抄表区段 WRITE_SECT_NO varchar(8) 8 FALSE FALSE FALSE
	private BigDecimal allCapacity;// 合同容量 ALL_CAPACITY decimal(10,2) 10 2 FALSE FALSE FALSE
	private Byte tradeType;// 行业类别 TRADE_TYPE smallint FALSE FALSE FALSE
	private Integer elecType;// 用电类别 ELEC_TYPE_TYPE smallint FALSE FALSE FALSE
	private Byte voltLevelType;// 电压等级 VOLT_LEVEL_TYPE smallint FALSE FALSE FALSE
	private Byte msModeType;// 计量方式 MS_MODE_TYPE smallint FALSE FALSE FALSE
	private Byte loadType;// 负荷类别 LOAD_TYPE smallint FALSE FALSE FALSE
	private Byte isHighType;// 高可靠性标志 IS_HIGH_TYPE smallint FALSE FALSE FALSE
	private Byte tempType;// 临时用电标志 TEMP_TYPE smallint FALSE FALSE FALSE
	private Byte creditRankType;// 信誉等级 CREDIT_RANK_TYPE smallint FALSE FALSE FALSE
	private Byte writeType;// 抄表周期 WRITE_TYPE smallint FALSE FALSE FALSE
	private Byte userType;// 分类标识 USER_TYPE smallint FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建日期 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 客户状态 STATUS smallint FALSE FALSE FALSE

	private Byte sn;// 算费次数

	public String get_id() {
		return _id;
	}

	public void createObjectId() {
		this._id = MongoUtils.createObjectId(this.getId(), this.getSn());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Long customerId) {
		this.customerId = customerId;
	}

	public Long getTgId() {
		return tgId;
	}

	public void setTgId(Long tgId) {
		this.tgId = tgId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}

	public Long getWriteSectId() {
		return writeSectId;
	}

	public void setWriteSectId(Long writeSectId) {
		this.writeSectId = writeSectId;
	}

	public BigDecimal getAllCapacity() {
		return allCapacity;
	}

	public void setAllCapacity(BigDecimal allCapacity) {
		this.allCapacity = allCapacity;
	}

	public Byte getTradeType() {
		return tradeType;
	}

	public void setTradeType(Byte tradeType) {
		this.tradeType = tradeType;
	}

	public Integer getElecType() {
		return elecType;
	}

	public void setElecType(Integer elecType) {
		this.elecType = elecType;
	}

	public Byte getVoltLevelType() {
		return voltLevelType;
	}

	public void setVoltLevelType(Byte voltLevelType) {
		this.voltLevelType = voltLevelType;
	}

	public Byte getMsModeType() {
		return msModeType;
	}

	public void setMsModeType(Byte msModeType) {
		this.msModeType = msModeType;
	}

	public Byte getLoadType() {
		return loadType;
	}

	public void setLoadType(Byte loadType) {
		this.loadType = loadType;
	}

	public Byte getIsHighType() {
		return isHighType;
	}

	public void setIsHighType(Byte isHighType) {
		this.isHighType = isHighType;
	}

	public Byte getTempType() {
		return tempType;
	}

	public void setTempType(Byte tempType) {
		this.tempType = tempType;
	}

	public Byte getCreditRankType() {
		return creditRankType;
	}

	public void setCreditRankType(Byte creditRankType) {
		this.creditRankType = creditRankType;
	}

	public Byte getWriteType() {
		return writeType;
	}

	public void setWriteType(Byte writeType) {
		this.writeType = writeType;
	}

	public Byte getUserType() {
		return userType;
	}

	public void setUserType(Byte userType) {
		this.userType = userType;
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

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

}
