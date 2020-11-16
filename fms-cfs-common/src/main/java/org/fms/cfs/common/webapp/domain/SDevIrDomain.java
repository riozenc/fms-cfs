/**
 *    Auth:riozenc
 *    Date:2019年3月14日 下午6:07:30
 *    Title:com.riozenc.cim.webapp.domain.MeterRelationDomain.java
 **/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 * 设备装拆记录表 S_DEV_IR
 */

public class SDevIrDomain implements MybatisEntity {

	private Long _id;
	public Long id; // ID ID bigint
	public Long mpedId; // 测量点标识
	public Byte equipTypeCode; // 设备类别
	public Long equipId; // 设备标识
	private Byte typeCode; // 装拆类别
	@JsonFormat(pattern = "yyyy-MM-dd", timezone = "GMT+8")
	private Date irDate; // 装拆日期
	private String empNo; // 装拆人员
	private Long deptId; // 装拆部门
	private Long businessPlaceCode; // 装拆单位
	private String reason;// 装拆原因
	private String remark; // 备注
	private String meterAssetNo;// 表计资产编号
	private String tvAssetNo; // TV资产编号
	private String taAssetNo; // TA资产编号
	private String modelCode; // 型号
	private String manufacturer; // 制造单位
	private String taRatio; // TA变比
	private BigDecimal taValue; // TA值
	private String tvRatio; // TV变比
	private BigDecimal tvValue; // TV值
	private BigDecimal tFactor; // 倍率
	private Byte iDirection; // 装表方向
	private Byte functionCode; // 功能代码
	private String meterNo; // 表号
	private Long coef; // 采集系数
	private BigDecimal p1r0; // 正向有功总 P1R0 decimal(14,4)
	private BigDecimal p2r0; // 反向有功总 P2R0 decimal(14,4)
	private BigDecimal p3r0; // 正向无功总 P3R0 decimal(14,4)
	private BigDecimal p4r0; // 反向无功总 P4R0 decimal(14,4)
	private BigDecimal p5r0; // 一象限无功总 P5R0 decimal(14,4)
	private BigDecimal p6r0; // 二象限无功总 P6R0 decimal(14,4)
	private BigDecimal p7r0; // 三象限无功总 P7R0 decimal(14,4)
	private BigDecimal p8r0; // 四象限无功总 P8R0 decimal(14,4)
	private BigDecimal p1r1; // 正向有功尖 P1R1 decimal(14,4)
	private BigDecimal p2r1; // 反向有功尖 P2R1 decimal(14,4)
	private BigDecimal p3r1; // 正向无功尖 P3R1 decimal(14,4)
	private BigDecimal p4r1; // 反向无功尖 P4R1 decimal(14,4)
	private BigDecimal p5r1; // 一象限无功尖 P5R1 decimal(14,4)
	private BigDecimal p6r1; // 二象限无功尖 P6R1 decimal(14,4)
	private BigDecimal p7r1; // 三象限无功尖 P7R1 decimal(14,4)
	private BigDecimal p8r1; // 四象限无功尖 P8R1 decimal(14,4)
	private BigDecimal p1r2; // 正向有功峰 P1R2 decimal(14,4)
	private BigDecimal p2r2; // 反向有功峰 P2R2 decimal(14,4)
	private BigDecimal p3r2; // 正向无功峰 P3R2 decimal(14,4)
	private BigDecimal p4r2; // 反向无功峰 P4R2 decimal(14,4)
	private BigDecimal p5r2; // 一象限无功峰 P5R2 decimal(14,4)
	private BigDecimal p6r2; // 二象限无功峰 P6R2 decimal(14,4)
	private BigDecimal p7r2; // 三象限无功峰 P7R2 decimal(14,4)
	private BigDecimal p8r2; // 四象限无功峰 P8R2 decimal(14,4)
	private BigDecimal p1r3; // 正向有功平 P1R3 decimal(14,4)
	private BigDecimal p2r3; // 反向有功平 P2R3 decimal(14,4)
	private BigDecimal p3r3; // 正向无功平 P3R3 decimal(14,4)
	private BigDecimal p4r3; // 反向无功平 P4R3 decimal(14,4)
	private BigDecimal p5r3; // 一象限无功平 P5R3 decimal(14,4)
	private BigDecimal p6r3; // 二象限无功平 P6R3 decimal(14,4)
	private BigDecimal p7r3; // 三象限无功平 P7R3 decimal(14,4)
	private BigDecimal p8r3; // 四象限无功平 P8R3 decimal(14,4)
	private BigDecimal p1r4; // 正向有功谷 P1R4 decimal(14,4)
	private BigDecimal p2r4; // 反向有功谷 P2R4 decimal(14,4)
	private BigDecimal p3r4; // 正向无功谷 P3R4 decimal(14,4)
	private BigDecimal p4r4; // 反向无功谷 P4R4 decimal(14,4)
	private BigDecimal p5r4; // 一象限无功谷 P5R4 decimal(14,4)
	private BigDecimal p6r4; // 二象限无功谷 P6R4 decimal(14,4)
	private BigDecimal p7r4; // 三象限无功谷 P7R4 decimal(14,4)
	private BigDecimal p8r4; // 四象限无功谷 P8R4 decimal(14,4)
	private BigDecimal p1r5; // 正向有功脊谷 P1R5 decimal(14,4)
	private BigDecimal p2r5; // 反向有功脊谷 P2R5 decimal(14,4)
	private BigDecimal p3r5; // 正向无功脊谷 P3R5 decimal(14,4)
	private BigDecimal p4r5; // 反向无功脊谷 P4R5 decimal(14,4)
	private BigDecimal p5r5; // 一象限无功脊谷 P5R5 decimal(14,4)
	private BigDecimal p6r5; // 二象限无功脊谷 P6R5 decimal(14,4)
	private BigDecimal p7r5; // 三象限无功脊谷 P7R5 decimal(14,4)
	private BigDecimal p8r5; // 四象限无功脊谷 P8R5 decimal(14,4)
	private BigDecimal remcap; // 剩余电量
	private Long creatorId; // 创建者
	private Date createDate; // 创建时间
	private Long lastModifierId; // 最后修改者
	private String lastModifyTime; // 最后修改时间
	private Integer currentMon; // 当前月
	private Integer calcMon; // 计算月

	private String meterAssetId;// 表计资产标识
	private String tvAssetId; // TV资产标识
	private String taAssetId; // TA资产标识

	public Long get_id() {
		return _id;
	}

	public void createObjectId() {
		this._id = this.getId();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getMpedId() {
		return mpedId;
	}

	public void setMpedId(Long mpedId) {
		this.mpedId = mpedId;
	}

	public Byte getEquipTypeCode() {
		return equipTypeCode;
	}

	public void setEquipTypeCode(Byte equipTypeCode) {
		this.equipTypeCode = equipTypeCode;
	}

	public Long getEquipId() {
		return equipId;
	}

	public void setEquipId(Long equipId) {
		this.equipId = equipId;
	}

	public Byte getTypeCode() {
		return typeCode;
	}

	public void setTypeCode(Byte typeCode) {
		this.typeCode = typeCode;
	}

	public Date getIrDate() {
		return irDate;
	}

	public void setIrDate(Date irDate) {
		this.irDate = irDate;
	}

	public String getEmpNo() {
		return empNo;
	}

	public void setEmpNo(String empNo) {
		this.empNo = empNo;
	}

	public Long getDeptId() {
		return deptId;
	}

	public void setDeptId(Long deptId) {
		this.deptId = deptId;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getMeterAssetNo() {
		return meterAssetNo;
	}

	public void setMeterAssetNo(String meterAssetNo) {
		this.meterAssetNo = meterAssetNo;
	}

	public String getTvAssetNo() {
		return tvAssetNo;
	}

	public void setTvAssetNo(String tvAssetNo) {
		this.tvAssetNo = tvAssetNo;
	}

	public String getTaAssetNo() {
		return taAssetNo;
	}

	public void setTaAssetNo(String taAssetNo) {
		this.taAssetNo = taAssetNo;
	}

	public String getModelCode() {
		return modelCode;
	}

	public void setModelCode(String modelCode) {
		this.modelCode = modelCode;
	}

	public String getManufacturer() {
		return manufacturer;
	}

	public void setManufacturer(String manufacturer) {
		this.manufacturer = manufacturer;
	}

	public String getTaRatio() {
		return taRatio;
	}

	public void setTaRatio(String taRatio) {
		this.taRatio = taRatio;
	}

	public BigDecimal getTaValue() {
		return taValue;
	}

	public void setTaValue(BigDecimal taValue) {
		this.taValue = taValue;
	}

	public String getTvRatio() {
		return tvRatio;
	}

	public void setTvRatio(String tvRatio) {
		this.tvRatio = tvRatio;
	}

	public BigDecimal getTvValue() {
		return tvValue;
	}

	public void setTvValue(BigDecimal tvValue) {
		this.tvValue = tvValue;
	}

	public BigDecimal gettFactor() {
		return tFactor;
	}

	public void settFactor(BigDecimal tFactor) {
		this.tFactor = tFactor;
	}

	public Byte getiDirection() {
		return iDirection;
	}

	public void setiDirection(Byte iDirection) {
		this.iDirection = iDirection;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public Long getCoef() {
		return coef;
	}

	public void setCoef(Long coef) {
		this.coef = coef;
	}

	public BigDecimal getP1r0() {
		return p1r0;
	}

	public void setP1r0(BigDecimal p1r0) {
		this.p1r0 = p1r0;
	}

	public BigDecimal getP2r0() {
		return p2r0;
	}

	public void setP2r0(BigDecimal p2r0) {
		this.p2r0 = p2r0;
	}

	public BigDecimal getP3r0() {
		return p3r0;
	}

	public void setP3r0(BigDecimal p3r0) {
		this.p3r0 = p3r0;
	}

	public BigDecimal getP4r0() {
		return p4r0;
	}

	public void setP4r0(BigDecimal p4r0) {
		this.p4r0 = p4r0;
	}

	public BigDecimal getP5r0() {
		return p5r0;
	}

	public void setP5r0(BigDecimal p5r0) {
		this.p5r0 = p5r0;
	}

	public BigDecimal getP6r0() {
		return p6r0;
	}

	public void setP6r0(BigDecimal p6r0) {
		this.p6r0 = p6r0;
	}

	public BigDecimal getP7r0() {
		return p7r0;
	}

	public void setP7r0(BigDecimal p7r0) {
		this.p7r0 = p7r0;
	}

	public BigDecimal getP8r0() {
		return p8r0;
	}

	public void setP8r0(BigDecimal p8r0) {
		this.p8r0 = p8r0;
	}

	public BigDecimal getP1r1() {
		return p1r1;
	}

	public void setP1r1(BigDecimal p1r1) {
		this.p1r1 = p1r1;
	}

	public BigDecimal getP2r1() {
		return p2r1;
	}

	public void setP2r1(BigDecimal p2r1) {
		this.p2r1 = p2r1;
	}

	public BigDecimal getP3r1() {
		return p3r1;
	}

	public void setP3r1(BigDecimal p3r1) {
		this.p3r1 = p3r1;
	}

	public BigDecimal getP4r1() {
		return p4r1;
	}

	public void setP4r1(BigDecimal p4r1) {
		this.p4r1 = p4r1;
	}

	public BigDecimal getP5r1() {
		return p5r1;
	}

	public void setP5r1(BigDecimal p5r1) {
		this.p5r1 = p5r1;
	}

	public BigDecimal getP6r1() {
		return p6r1;
	}

	public void setP6r1(BigDecimal p6r1) {
		this.p6r1 = p6r1;
	}

	public BigDecimal getP7r1() {
		return p7r1;
	}

	public void setP7r1(BigDecimal p7r1) {
		this.p7r1 = p7r1;
	}

	public BigDecimal getP8r1() {
		return p8r1;
	}

	public void setP8r1(BigDecimal p8r1) {
		this.p8r1 = p8r1;
	}

	public BigDecimal getP1r2() {
		return p1r2;
	}

	public void setP1r2(BigDecimal p1r2) {
		this.p1r2 = p1r2;
	}

	public BigDecimal getP2r2() {
		return p2r2;
	}

	public void setP2r2(BigDecimal p2r2) {
		this.p2r2 = p2r2;
	}

	public BigDecimal getP3r2() {
		return p3r2;
	}

	public void setP3r2(BigDecimal p3r2) {
		this.p3r2 = p3r2;
	}

	public BigDecimal getP4r2() {
		return p4r2;
	}

	public void setP4r2(BigDecimal p4r2) {
		this.p4r2 = p4r2;
	}

	public BigDecimal getP5r2() {
		return p5r2;
	}

	public void setP5r2(BigDecimal p5r2) {
		this.p5r2 = p5r2;
	}

	public BigDecimal getP6r2() {
		return p6r2;
	}

	public void setP6r2(BigDecimal p6r2) {
		this.p6r2 = p6r2;
	}

	public BigDecimal getP7r2() {
		return p7r2;
	}

	public void setP7r2(BigDecimal p7r2) {
		this.p7r2 = p7r2;
	}

	public BigDecimal getP8r2() {
		return p8r2;
	}

	public void setP8r2(BigDecimal p8r2) {
		this.p8r2 = p8r2;
	}

	public BigDecimal getP1r3() {
		return p1r3;
	}

	public void setP1r3(BigDecimal p1r3) {
		this.p1r3 = p1r3;
	}

	public BigDecimal getP2r3() {
		return p2r3;
	}

	public void setP2r3(BigDecimal p2r3) {
		this.p2r3 = p2r3;
	}

	public BigDecimal getP3r3() {
		return p3r3;
	}

	public void setP3r3(BigDecimal p3r3) {
		this.p3r3 = p3r3;
	}

	public BigDecimal getP4r3() {
		return p4r3;
	}

	public void setP4r3(BigDecimal p4r3) {
		this.p4r3 = p4r3;
	}

	public BigDecimal getP5r3() {
		return p5r3;
	}

	public void setP5r3(BigDecimal p5r3) {
		this.p5r3 = p5r3;
	}

	public BigDecimal getP6r3() {
		return p6r3;
	}

	public void setP6r3(BigDecimal p6r3) {
		this.p6r3 = p6r3;
	}

	public BigDecimal getP7r3() {
		return p7r3;
	}

	public void setP7r3(BigDecimal p7r3) {
		this.p7r3 = p7r3;
	}

	public BigDecimal getP8r3() {
		return p8r3;
	}

	public void setP8r3(BigDecimal p8r3) {
		this.p8r3 = p8r3;
	}

	public BigDecimal getP1r4() {
		return p1r4;
	}

	public void setP1r4(BigDecimal p1r4) {
		this.p1r4 = p1r4;
	}

	public BigDecimal getP2r4() {
		return p2r4;
	}

	public void setP2r4(BigDecimal p2r4) {
		this.p2r4 = p2r4;
	}

	public BigDecimal getP3r4() {
		return p3r4;
	}

	public void setP3r4(BigDecimal p3r4) {
		this.p3r4 = p3r4;
	}

	public BigDecimal getP4r4() {
		return p4r4;
	}

	public void setP4r4(BigDecimal p4r4) {
		this.p4r4 = p4r4;
	}

	public BigDecimal getP5r4() {
		return p5r4;
	}

	public void setP5r4(BigDecimal p5r4) {
		this.p5r4 = p5r4;
	}

	public BigDecimal getP6r4() {
		return p6r4;
	}

	public void setP6r4(BigDecimal p6r4) {
		this.p6r4 = p6r4;
	}

	public BigDecimal getP7r4() {
		return p7r4;
	}

	public void setP7r4(BigDecimal p7r4) {
		this.p7r4 = p7r4;
	}

	public BigDecimal getP8r4() {
		return p8r4;
	}

	public void setP8r4(BigDecimal p8r4) {
		this.p8r4 = p8r4;
	}

	public BigDecimal getP1r5() {
		return p1r5;
	}

	public void setP1r5(BigDecimal p1r5) {
		this.p1r5 = p1r5;
	}

	public BigDecimal getP2r5() {
		return p2r5;
	}

	public void setP2r5(BigDecimal p2r5) {
		this.p2r5 = p2r5;
	}

	public BigDecimal getP3r5() {
		return p3r5;
	}

	public void setP3r5(BigDecimal p3r5) {
		this.p3r5 = p3r5;
	}

	public BigDecimal getP4r5() {
		return p4r5;
	}

	public void setP4r5(BigDecimal p4r5) {
		this.p4r5 = p4r5;
	}

	public BigDecimal getP5r5() {
		return p5r5;
	}

	public void setP5r5(BigDecimal p5r5) {
		this.p5r5 = p5r5;
	}

	public BigDecimal getP6r5() {
		return p6r5;
	}

	public void setP6r5(BigDecimal p6r5) {
		this.p6r5 = p6r5;
	}

	public BigDecimal getP7r5() {
		return p7r5;
	}

	public void setP7r5(BigDecimal p7r5) {
		this.p7r5 = p7r5;
	}

	public BigDecimal getP8r5() {
		return p8r5;
	}

	public void setP8r5(BigDecimal p8r5) {
		this.p8r5 = p8r5;
	}

	public BigDecimal getRemcap() {
		return remcap;
	}

	public void setRemcap(BigDecimal remcap) {
		this.remcap = remcap;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Long getLastModifierId() {
		return lastModifierId;
	}

	public void setLastModifierId(Long lastModifierId) {
		this.lastModifierId = lastModifierId;
	}

	public String getLastModifyTime() {
		return lastModifyTime;
	}

	public void setLastModifyTime(String lastModifyTime) {
		this.lastModifyTime = lastModifyTime;
	}

	public Integer getCurrentMon() {
		return currentMon;
	}

	public void setCurrentMon(Integer currentMon) {
		this.currentMon = currentMon;
	}

	public Integer getCalcMon() {
		return calcMon;
	}

	public void setCalcMon(Integer calcMon) {
		this.calcMon = calcMon;
	}

	public String getMeterAssetId() {
		return meterAssetId;
	}

	public void setMeterAssetId(String meterAssetId) {
		this.meterAssetId = meterAssetId;
	}

	public String getTvAssetId() {
		return tvAssetId;
	}

	public void setTvAssetId(String tvAssetId) {
		this.tvAssetId = tvAssetId;
	}

	public String getTaAssetId() {
		return taAssetId;
	}

	public void setTaAssetId(String taAssetId) {
		this.taAssetId = taAssetId;
	}

	public Byte getFunctionCode() {
		return functionCode;
	}

	public void setFunctionCode(Byte functionCode) {
		this.functionCode = functionCode;
	}

}
