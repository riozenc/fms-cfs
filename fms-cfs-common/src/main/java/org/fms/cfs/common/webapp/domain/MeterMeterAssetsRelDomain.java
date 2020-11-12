package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 * 计量点与表资产中间表
 */
public class MeterMeterAssetsRelDomain implements MybatisEntity {

	private Long _id;
	private Long id;
	// 计量点id
	private Long meterId;
	// 资产id
	private Long meterAssetsId;
	// 相线
	private Byte phaseSeq;
	// 功能代码
	private Byte functionCode;
	// 功率方向
	private Byte powerDirection;
	// 表序号
	private Integer meterOrder;
	// 分时标识
	private Byte tsFlag;
	// 创建时间
	private Date createDate;

	// 倍率
	private BigDecimal factorNum;
	 //倍率
    private BigDecimal multiplyingPower;
	// 状态
	private Byte status;
	// 抄表序号
	private Long writeSn;
	  private Integer meterSn;

	    private String meterAssetsNo;
	    
	    private String userNo;


	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Long getMeterAssetsId() {
		return meterAssetsId;
	}

	public void setMeterAssetsId(Long meterAssetsId) {
		this.meterAssetsId = meterAssetsId;
	}

	public Byte getPhaseSeq() {
		return phaseSeq;
	}

	public void setPhaseSeq(Byte phaseSeq) {
		this.phaseSeq = phaseSeq;
	}

	public Byte getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Byte functionCode) {
		this.functionCode = functionCode;
	}

	public Byte getPowerDirection() {
		return powerDirection;
	}

	public void setPowerDirection(Byte powerDirection) {
		this.powerDirection = powerDirection;
	}

	public Integer getMeterOrder() {
		return meterOrder;
	}

	public void setMeterOrder(Integer meterOrder) {
		this.meterOrder = meterOrder;
	}

	public Byte getTsFlag() {
		return tsFlag;
	}

	public void setTsFlag(Byte tsFlag) {
		this.tsFlag = tsFlag;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Byte getStatus() {
		return status;
	}

	public void setStatus(Byte status) {
		this.status = status;
	}

	public BigDecimal getFactorNum() {
		return factorNum;
	}

	public void setFactorNum(BigDecimal factorNum) {
		this.factorNum = factorNum;
	}

	public Long getWriteSn() {
		return writeSn;
	}

	public void setWriteSn(Long writeSn) {
		this.writeSn = writeSn;
	}

	public BigDecimal getMultiplyingPower() {
		return multiplyingPower;
	}

	public void setMultiplyingPower(BigDecimal multiplyingPower) {
		this.multiplyingPower = multiplyingPower;
	}

	public Integer getMeterSn() {
		return meterSn;
	}

	public void setMeterSn(Integer meterSn) {
		this.meterSn = meterSn;
	}

	public String getMeterAssetsNo() {
		return meterAssetsNo;
	}

	public void setMeterAssetsNo(String meterAssetsNo) {
		this.meterAssetsNo = meterAssetsNo;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}
	
}
