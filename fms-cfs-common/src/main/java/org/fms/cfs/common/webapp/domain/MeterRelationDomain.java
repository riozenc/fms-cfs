/**
 *    Auth:riozenc
 *    Date:2019年3月14日 下午6:07:30
 *    Title:com.riozenc.cim.webapp.domain.MeterRelationDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fms.cfs.common.utils.MongoUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class MeterRelationDomain implements MybatisEntity {

	private String _id;
	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long meterId;// 计量点ID METER_ID bigint FALSE FALSE FALSE
	private Long pMeterId;// 关系计量点ID P_METER_ID bigint FALSE FALSE FALSE
	private Byte meterRateFlag;// 计量点表计类型 METER_RATE_FLAG smallint FALSE FALSE FALSE
	private Byte pMeterRateFlag;// 关系计量点表计类型 P_METER_RATE_FLAG smallint FALSE FALSE FALSE
	private String shareRate;// 分时分摊方式 SHARE_RATE char(10) 10 FALSE FALSE FALSE
	private Byte meterRelationType;// 计量点关系 METER_RELATION_TYPE smallint FALSE FALSE FALSE
	private BigDecimal meterRelationValue;// 关系值 METER_RELATION_VALUE decimal(5,2) 5 2 FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	public String get_id() {
		return _id;
	}

	public void createObjectId() {
		this._id = MongoUtils.createObjectId(this.getMeterId(),this.getpMeterId());
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

	public Long getpMeterId() {
		return pMeterId;
	}

	public void setpMeterId(Long pMeterId) {
		this.pMeterId = pMeterId;
	}

	public Byte getMeterRelationType() {
		return meterRelationType;
	}

	public void setMeterRelationType(Byte meterRelationType) {
		this.meterRelationType = meterRelationType;
	}

	public BigDecimal getMeterRelationValue() {
		return meterRelationValue;
	}

	public void setMeterRelationValue(BigDecimal meterRelationValue) {
		this.meterRelationValue = meterRelationValue;
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

	public Byte getMeterRateFlag() {
		return meterRateFlag;
	}

	public void setMeterRateFlag(Byte meterRateFlag) {
		this.meterRateFlag = meterRateFlag;
	}

	public Byte getpMeterRateFlag() {
		return pMeterRateFlag;
	}

	public void setpMeterRateFlag(Byte pMeterRateFlag) {
		this.pMeterRateFlag = pMeterRateFlag;
	}

	public String getShareRate() {
		return shareRate;
	}

	public void setShareRate(String shareRate) {
		this.shareRate = shareRate;
	}

}
