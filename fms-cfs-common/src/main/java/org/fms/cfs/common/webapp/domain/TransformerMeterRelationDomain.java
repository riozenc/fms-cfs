package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fms.cfs.common.utils.MongoUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TransformerMeterRelationDomain implements MybatisEntity {

	private String _id;
	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long transformerId;// 变压器ID TRANSFORMER_ID bigint FALSE FALSE FALSE
	private Long meterId;// 计量点ID METER_ID bigint FALSE FALSE FALSE
	private Byte msType;// 计量方式 MS_TYPE smallint(2) 2 FALSE FALSE FALSE
	private String transformerGroupNo;// 变压器组号 TRANSFORMER_GROUP_HP smallint FALSE FALSE FALSE
	private Integer transLostType;// 分摊方式
	private BigDecimal transLostNum;// 分摊量
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate;// 创建时间 CREATE_DATE datetime FALSE FALSE FALSE
	private String remark;// 备注 REMARK varchar(256) 256 FALSE FALSE FALSE
	private Byte status;// 状态 STATUS smallint FALSE FALSE FALSE

	private Byte isPubType; // 公用变标志
	
	private Integer loadChangeSign;
	private String meterNo;
	private String meterName;
	private String userName;
	
	private String transformerNo;
	private String deskName;
	

	public Integer getLoadChangeSign() {
		return loadChangeSign;
	}

	public void setLoadChangeSign(Integer loadChangeSign) {
		this.loadChangeSign = loadChangeSign;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getMeterName() {
		return meterName;
	}

	public void setMeterName(String meterName) {
		this.meterName = meterName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getTransformerNo() {
		return transformerNo;
	}

	public void setTransformerNo(String transformerNo) {
		this.transformerNo = transformerNo;
	}

	public String getDeskName() {
		return deskName;
	}

	public void setDeskName(String deskName) {
		this.deskName = deskName;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public String get_id() {
		return _id;
	}

	public void createObjectId() {
		this._id = MongoUtils.createObjectId(this.getMeterId(), this.getTransformerId());
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransformerId() {
		return transformerId;
	}

	public void setTransformerId(Long transformerId) {
		this.transformerId = transformerId;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public Byte getMsType() {
		return msType;
	}

	public void setMsType(Byte msType) {
		this.msType = msType;
	}

	public String getTransformerGroupNo() {
		return transformerGroupNo;
	}

	public void setTransformerGroupNo(String transformerGroupNo) {
		this.transformerGroupNo = transformerGroupNo;
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

	public Integer getTransLostType() {
		return transLostType;
	}

	public void setTransLostType(Integer transLostType) {
		this.transLostType = transLostType;
	}

	public BigDecimal getTransLostNum() {
		return transLostNum;
	}

	public void setTransLostNum(BigDecimal transLostNum) {
		this.transLostNum = transLostNum;
	}

	public Byte getIsPubType() {
		return isPubType;
	}

	public void setIsPubType(Byte isPubType) {
		this.isPubType = isPubType;
	}

}
