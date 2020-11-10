/**
 *    Auth:riozenc
 *    Date:2019年3月8日 下午3:22:58
 *    Title:com.riozenc.cim.webapp.domain.CustomerDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;
import com.riozenc.titanTool.mybatis.pagination.Page;

/**
 * 计量点 METER_INFO
 * 
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElectricMeterDomain extends Page implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private String meterNo; // 计量点号
	private String meterName; // 计量点名称
	private Long meterAssetsId; // 电表资产ID
	private String setAddress; // 安装地点
	private Long ctAssetsId; // CT资产号
	private Long ptAssetsId; // PT资产号
	private String ctValue; // CT变比
	private String ptValue; // PT变比
	private Byte priceType; // 电价
	private Integer needIndex; // 需量定值
	private Byte voltLevelType; // 计量点电压
	private Byte meterType; // 计量点类别
	private Byte meterClassType; // 计量点分类
	private Byte msType; // 计量方式
	private Byte elecTypeCode; // 用电类别
	private Byte baseMoneyFlag; // 基本电费计算方法
	private Byte cosType; // 力率标准
	private Byte tradeType; // 行业用电分类
	private Byte tsType; // 分时计费标准
	private Byte transLostType; // 变损分摊方式
	private BigDecimal transLostNum; // 变损率or变损值
	private BigDecimal qTransLostNum; // 无功变损率or值
	private Byte lineLostType; // 线损计算方法
	private BigDecimal lineLostNum; // 线损率or线损值
	private BigDecimal qLineLostNum; // 无功线损率or值
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate; // 创建时间
	private String remark; // 备注
	private Byte status; // 状态

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Long getMeterAssetsId() {
		return meterAssetsId;
	}

	public void setMeterAssetsId(Long meterAssetsId) {
		this.meterAssetsId = meterAssetsId;
	}

	public String getSetAddress() {
		return setAddress;
	}

	public void setSetAddress(String setAddress) {
		this.setAddress = setAddress;
	}

	public Long getCtAssetsId() {
		return ctAssetsId;
	}

	public void setCtAssetsId(Long ctAssetsId) {
		this.ctAssetsId = ctAssetsId;
	}

	public Long getPtAssetsId() {
		return ptAssetsId;
	}

	public void setPtAssetsId(Long ptAssetsId) {
		this.ptAssetsId = ptAssetsId;
	}

	public String getCtValue() {
		return ctValue;
	}

	public void setCtValue(String ctValue) {
		this.ctValue = ctValue;
	}

	public String getPtValue() {
		return ptValue;
	}

	public void setPtValue(String ptValue) {
		this.ptValue = ptValue;
	}

	public Byte getPriceType() {
		return priceType;
	}

	public void setPriceType(Byte priceType) {
		this.priceType = priceType;
	}

	public Integer getNeedIndex() {
		return needIndex;
	}

	public void setNeedIndex(Integer needIndex) {
		this.needIndex = needIndex;
	}

	public Byte getVoltLevelType() {
		return voltLevelType;
	}

	public void setVoltLevelType(Byte voltLevelType) {
		this.voltLevelType = voltLevelType;
	}

	public Byte getMeterType() {
		return meterType;
	}

	public void setMeterType(Byte meterType) {
		this.meterType = meterType;
	}

	public Byte getMeterClassType() {
		return meterClassType;
	}

	public void setMeterClassType(Byte meterClassType) {
		this.meterClassType = meterClassType;
	}

	public Byte getMsType() {
		return msType;
	}

	public void setMsType(Byte msType) {
		this.msType = msType;
	}

	public Byte getElecTypeCode() {
		return elecTypeCode;
	}

	public void setElecTypeCode(Byte elecTypeCode) {
		this.elecTypeCode = elecTypeCode;
	}

	public Byte getBaseMoneyFlag() {
		return baseMoneyFlag;
	}

	public void setBaseMoneyFlag(Byte baseMoneyFlag) {
		this.baseMoneyFlag = baseMoneyFlag;
	}

	public Byte getCosType() {
		return cosType;
	}

	public void setCosType(Byte cosType) {
		this.cosType = cosType;
	}

	public Byte getTradeType() {
		return tradeType;
	}

	public void setTradeType(Byte tradeType) {
		this.tradeType = tradeType;
	}

	public Byte getTsType() {
		return tsType;
	}

	public void setTsType(Byte tsType) {
		this.tsType = tsType;
	}

	public Byte getTransLostType() {
		return transLostType;
	}

	public void setTransLostType(Byte transLostType) {
		this.transLostType = transLostType;
	}

	public BigDecimal getTransLostNum() {
		return transLostNum;
	}

	public void setTransLostNum(BigDecimal transLostNum) {
		this.transLostNum = transLostNum;
	}

	public BigDecimal getqTransLostNum() {
		return qTransLostNum;
	}

	public void setqTransLostNum(BigDecimal qTransLostNum) {
		this.qTransLostNum = qTransLostNum;
	}

	public Byte getLineLostType() {
		return lineLostType;
	}

	public void setLineLostType(Byte lineLostType) {
		this.lineLostType = lineLostType;
	}

	public BigDecimal getLineLostNum() {
		return lineLostNum;
	}

	public void setLineLostNum(BigDecimal lineLostNum) {
		this.lineLostNum = lineLostNum;
	}

	public BigDecimal getqLineLostNum() {
		return qLineLostNum;
	}

	public void setqLineLostNum(BigDecimal qLineLostNum) {
		this.qLineLostNum = qLineLostNum;
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

}
