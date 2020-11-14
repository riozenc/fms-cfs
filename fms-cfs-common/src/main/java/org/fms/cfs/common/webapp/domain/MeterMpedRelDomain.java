package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.riozenc.titanTool.mybatis.MybatisEntity;

public class MeterMpedRelDomain implements MybatisEntity {
	private Long _id;
	private Long id; // ID ID bigint
	private Long meterId;// 计费点ID METER_ID bigint
	private Long mpedId;// 计量点ID MPED_ID bigint
	private Byte phaseSeq;// 相序 PHASE_SEQ smallint
	private Byte functionCode;// 功能代码 FUNCTION_CODE smallint
	private Byte powerDirection;// 功率方向 POWER_DIRECTION smallint
	private Byte tsFlag;// 分时标识 TS_FLAG smallint
	private Date createDate;// 创建时间 CREATE_DATE datetime
	private String status;// 状态 STATUS smallint
	private BigDecimal factorNum;// 综合倍率 FACTOR_NUM decimal
	private Long writeSn;// 抄表序号 WRITE_SN bigint
	private String meterName;// METER_NAME
	private String name;// NAME

	public Long get_id() {
		return _id;
	}

	public void set_id(Long _id) {
		this._id = _id;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public Long getMpedId() {
		return mpedId;
	}

	public void setMpedId(Long mpedId) {
		this.mpedId = mpedId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
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

	public Byte getTsFlag() {
		return tsFlag;
	}

	public void setTsFlag(Byte tsFlag) {
		this.tsFlag = tsFlag;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
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

}
