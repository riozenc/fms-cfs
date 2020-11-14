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
 * 变压器 TRANSFORMER_INFO
 * 
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class TransformerDomain extends Page implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long transformerAssetsId; // 变压器资产ID
	private String transformerNo; // 变压器编号
	private String deskName; // 变压器名称
	private BigDecimal capacity = BigDecimal.ZERO; // 变压器容量
	private String transformerGroupNo; // 变压器组号
	private Byte isPubType; // 公用变标志
	private Byte transformerLossType; // 变损计算方法
	private Long businessPlaceCode; // 供电所
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date runDate; // 投运日期
	private Integer standTime; // 标准时间
	private Byte transformerModelType;// 变压器型号
	private Byte voltType; // 供电电压
	private String chargeMan; // 责任人
	private Byte connectionGroup;// 连接组别
	private Integer produceTeam; // 生产班次
	private BigDecimal correctionFactor;
	private Byte setFormat; // 安装形式
	private String areaNo; // 片区号
	private String supplyRoom; // 配电房编号
	private String villageNo; // 村别
	private String transChange; // 变台改造情况
	private String inputNodeCode; // 接入线路对应结束节点编号
	private BigDecimal capcitorCapacity; // 补偿电容器总容量
	private Integer cosStdCode; // 考核功率因数
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date createDate; // 创建日期
	private String remark; // 备注
	private Byte status; // 状态
	private Long operator;// 操作人ID ID bigint TRUE FALSE TRUE
	private Long lineId;// 线路ID ID bigint TRUE FALSE TRUE
  
	private String address; // 地理位置
	private Long consId; // 所属用户
	private Long tgId; // 所属台区
	private Long stationId; // 所属厂站
	private Byte runStatusCode; // 状态
	private Integer weight; // 显示顺序
	private Long creatorId; // 创建者
	private Long lastModifierId; // 最后修改者
	private String lastModifierTime; // 最后修改时间

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getTransformerAssetsId() {
		return transformerAssetsId;
	}

	public void setTransformerAssetsId(Long transformerAssetsId) {
		this.transformerAssetsId = transformerAssetsId;
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

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public String getTransformerGroupNo() {
		return transformerGroupNo;
	}

	public void setTransformerGroupNo(String transformerGroupNo) {
		this.transformerGroupNo = transformerGroupNo;
	}

	public Byte getIsPubType() {
		return isPubType;
	}

	public void setIsPubType(Byte isPubType) {
		this.isPubType = isPubType;
	}

	public Byte getTransformerLossType() {
		return transformerLossType;
	}

	public void setTransformerLossType(Byte transformerLossType) {
		this.transformerLossType = transformerLossType;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}

	public Date getRunDate() {
		return runDate;
	}

	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}

	public Integer getStandTime() {
		return standTime;
	}

	public void setStandTime(Integer standTime) {
		this.standTime = standTime;
	}

	public Byte getVoltType() {
		return voltType;
	}

	public void setVoltType(Byte voltType) {
		this.voltType = voltType;
	}

	public String getChargeMan() {
		return chargeMan;
	}

	public void setChargeMan(String chargeMan) {
		this.chargeMan = chargeMan;
	}

	public Integer getProduceTeam() {
		return produceTeam;
	}

	public void setProduceTeam(Integer produceTeam) {
		this.produceTeam = produceTeam;
	}

	public Byte getSetFormat() {
		return setFormat;
	}

	public void setSetFormat(Byte setFormat) {
		this.setFormat = setFormat;
	}

	public String getAreaNo() {
		return areaNo;
	}

	public void setAreaNo(String areaNo) {
		this.areaNo = areaNo;
	}

	public String getSupplyRoom() {
		return supplyRoom;
	}

	public void setSupplyRoom(String supplyRoom) {
		this.supplyRoom = supplyRoom;
	}

	public String getVillageNo() {
		return villageNo;
	}

	public void setVillageNo(String villageNo) {
		this.villageNo = villageNo;
	}

	public String getTransChange() {
		return transChange;
	}

	public void setTransChange(String transChange) {
		this.transChange = transChange;
	}

	public String getInputNodeCode() {
		return inputNodeCode;
	}

	public void setInputNodeCode(String inputNodeCode) {
		this.inputNodeCode = inputNodeCode;
	}

	public BigDecimal getCapcitorCapacity() {
		return capcitorCapacity;
	}

	public void setCapcitorCapacity(BigDecimal capcitorCapacity) {
		this.capcitorCapacity = capcitorCapacity;
	}

	public Integer getCosStdCode() {
		return cosStdCode;
	}

	public void setCosStdCode(Integer cosStdCode) {
		this.cosStdCode = cosStdCode;
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

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Byte getTransformerModelType() {
		return transformerModelType;
	}

	public void setTransformerModelType(Byte transformerModelType) {
		this.transformerModelType = transformerModelType;
	}

	public BigDecimal getCorrectionFactor() {
		return correctionFactor;
	}

	public void setCorrectionFactor(BigDecimal correctionFactor) {
		this.correctionFactor = correctionFactor;
	}

	public Byte getConnectionGroup() {
		return connectionGroup;
	}

	public void setConnectionGroup(Byte connectionGroup) {
		this.connectionGroup = connectionGroup;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Long getConsId() {
		return consId;
	}

	public void setConsId(Long consId) {
		this.consId = consId;
	}

	public Long getTgId() {
		return tgId;
	}

	public void setTgId(Long tgId) {
		this.tgId = tgId;
	}

	public Long getStationId() {
		return stationId;
	}

	public void setStationId(Long stationId) {
		this.stationId = stationId;
	}

	public Byte getRunStatusCode() {
		return runStatusCode;
	}

	public void setRunStatusCode(Byte runStatusCode) {
		this.runStatusCode = runStatusCode;
	}

	public Integer getWeight() {
		return weight;
	}

	public void setWeight(Integer weight) {
		this.weight = weight;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getLastModifierId() {
		return lastModifierId;
	}

	public void setLastModifierId(Long lastModifierId) {
		this.lastModifierId = lastModifierId;
	}

	public String getLastModifierTime() {
		return lastModifierTime;
	}

	public void setLastModifierTime(String lastModifierTime) {
		this.lastModifierTime = lastModifierTime;
	}
    
}
