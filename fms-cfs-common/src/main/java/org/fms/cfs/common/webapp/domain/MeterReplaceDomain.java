/**
 * Author : czy
 * Date : 2019年4月22日 下午3:18:13
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.MeterReplaceDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.riozenc.titanTool.annotation.TablePrimaryKey;
import com.riozenc.titanTool.mybatis.MybatisEntity;

/**
 * 换表记录
 * 
 * @author czy
 *
 */
public class MeterReplaceDomain implements MybatisEntity {
	private Long _id;
	@TablePrimaryKey
	public Long id; // ID ID bigint
	public Long meterId; // 计量点ID METER_ID bigint
	public Long meterAssetsId; // 电表资产ID METER_ASSETS_ID bigint
	public Long ctAssetsId; // CT资产ID CT_ASSETS_ID bigint
	public Long ptAssetsId; // PT资产ID PT_ASSETS_ID bigint
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone="GMT+8")
	private Date replaceDate; // 装拆时间 REPLACE_DATE datetime
	private String reason; // 装拆原因 REASON varchar(256)
	private Byte equipmentType; // 设备类型 EQUIPMENT_TYPE smallint
	private Byte operateType; // 操作类型 OPERATE_TYPE smallint
	private BigDecimal p1r0; // 正向有功总 P1R0 decimal(12,2)
	private BigDecimal p2r0; // 反向有功总 P2R0 decimal(12,2)
	private BigDecimal p3r0; // 正向无功总 P3R0 decimal(12,2)
	private BigDecimal p4r0; // 反向无功总 P4R0 decimal(12,2)
	private BigDecimal p5r0; // 一象限无功总 P5R0 decimal(12,2)
	private BigDecimal p6r0; // 二象限无功总 P6R0 decimal(12,2)
	private BigDecimal p7r0; // 三象限无功总 P7R0 decimal(12,2)
	private BigDecimal p8r0; // 四象限无功总 P8R0 decimal(12,2)
	private BigDecimal p1r1; // 正向有功尖 P1R1 decimal(12,2)
	private BigDecimal p2r1; // 反向有功尖 P2R1 decimal(12,2)
	private BigDecimal p3r1; // 正向无功尖 P3R1 decimal(12,2)
	private BigDecimal p4r1; // 反向无功尖 P4R1 decimal(12,2)
	private BigDecimal p5r1; // 一象限无功尖 P5R1 decimal(12,2)
	private BigDecimal p6r1; // 二象限无功尖 P6R1 decimal(12,2)
	private BigDecimal p7r1; // 三象限无功尖 P7R1 decimal(12,2)
	private BigDecimal p8r1; // 四象限无功尖 P8R1 decimal(12,2)
	private BigDecimal p1r2; // 正向有功峰 P1R2 decimal(12,2)
	private BigDecimal p2r2; // 反向有功峰 P2R2 decimal(12,2)
	private BigDecimal p3r2; // 正向无功峰 P3R2 decimal(12,2)
	private BigDecimal p4r2; // 反向无功峰 P4R2 decimal(12,2)
	private BigDecimal p5r2; // 一象限无功峰 P5R2 decimal(12,2)
	private BigDecimal p6r2; // 二象限无功峰 P6R2 decimal(12,2)
	private BigDecimal p7r2; // 三象限无功峰 P7R2 decimal(12,2)
	private BigDecimal p8r2; // 四象限无功峰 P8R2 decimal(12,2)
	private BigDecimal p1r3; // 正向有功平 P1R3 decimal(12,2)
	private BigDecimal p2r3; // 反向有功平 P2R3 decimal(12,2)
	private BigDecimal p3r3; // 正向无功平 P3R3 decimal(12,2)
	private BigDecimal p4r3; // 反向无功平 P4R3 decimal(12,2)
	private BigDecimal p5r3; // 一象限无功平 P5R3 decimal(12,2)
	private BigDecimal p6r3; // 二象限无功平 P6R3 decimal(12,2)
	private BigDecimal p7r3; // 三象限无功平 P7R3 decimal(12,2)
	private BigDecimal p8r3; // 四象限无功平 P8R3 decimal(12,2)
	private BigDecimal p1r4; // 正向有功谷 P1R4 decimal(12,2)
	private BigDecimal p2r4; // 反向有功谷 P2R4 decimal(12,2)
	private BigDecimal p3r4; // 正向无功谷 P3R4 decimal(12,2)
	private BigDecimal p4r4; // 反向无功谷 P4R4 decimal(12,2)
	private BigDecimal p5r4; // 一象限无功谷 P5R4 decimal(12,2)
	private BigDecimal p6r4; // 二象限无功谷 P6R4 decimal(12,2)
	private BigDecimal p7r4; // 三象限无功谷 P7R4 decimal(12,2)
	private BigDecimal p8r4; // 四象限无功谷 P8R4 decimal(12,2)
	private BigDecimal p1r5; // 正向有功脊谷 P1R5 decimal(12,2)
	private BigDecimal p2r5; // 反向有功脊谷 P2R5 decimal(12,2)
	private BigDecimal p3r5; // 正向无功脊谷 P3R5 decimal(12,2)
	private BigDecimal p4r5; // 反向无功脊谷 P4R5 decimal(12,2)
	private BigDecimal p5r5; // 一象限无功脊谷 P5R5 decimal(12,2)
	private BigDecimal p6r5; // 二象限无功脊谷 P6R5 decimal(12,2)
	private BigDecimal p7r5; // 三象限无功脊谷 P7R5 decimal(12,2)
	private BigDecimal p8r5; // 四象限无功脊谷 P8R5 decimal(12,2)
	private Long operator; // 操作人 OPERATOR varchar(64)
	private Date createDate; // 操作时间 CREATE_DATE datetime
	private Byte status; // 状态 STATUS smallint

	/*---------------------------------------------------------------------------------*/
	private String meterAssetsNo; // METER_ASSETS_NO 电能表资产号
	private String ctAssetsNo; // ct资产号
	private Integer ratedCtCode; // CT变比
	private String ptAssetsNo; // pt资产号
	private Integer ratedPtCode; // PT变比
	private Integer currentMon; // PT变比
	private Integer calcMon; // PT变比
	private Byte phaseSeq; // 相序
	private Byte functionCode; // 功能代码
	private Byte powerDirection; // 功率方向
	private Byte tsFlag; // 分时标志
	private Byte meterOrder; // 表序号
	private Byte inductorOrder; // 互感器序号

	private String meterNo;
	private String assetNo;

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

	public Date getReplaceDate() {
		return replaceDate;
	}

	public void setReplaceDate(Date replaceDate) {
		this.replaceDate = replaceDate;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Byte getEquipmentType() {
		return equipmentType;
	}

	public void setEquipmentType(Byte equipmentType) {
		this.equipmentType = equipmentType;
	}

	public Byte getOperateType() {
		return operateType;
	}

	public void setOperateType(Byte operateType) {
		this.operateType = operateType;
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

	public Long getOperator() {
		return operator;
	}

	public void setOperator(Long operator) {
		this.operator = operator;
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

	public String getMeterAssetsNo() {
		return meterAssetsNo;
	}

	public void setMeterAssetsNo(String meterAssetsNo) {
		this.meterAssetsNo = meterAssetsNo;
	}

	public String getCtAssetsNo() {
		return ctAssetsNo;
	}

	public void setCtAssetsNo(String ctAssetsNo) {
		this.ctAssetsNo = ctAssetsNo;
	}

	public String getPtAssetsNo() {
		return ptAssetsNo;
	}

	public void setPtAssetsNo(String ptAssetsNo) {
		this.ptAssetsNo = ptAssetsNo;
	}

	public Integer getRatedCtCode() {
		return ratedCtCode;
	}

	public void setRatedCtCode(Integer ratedCtCode) {
		this.ratedCtCode = ratedCtCode;
	}

	public Integer getRatedPtCode() {
		return ratedPtCode;
	}

	public void setRatedPtCode(Integer ratedPtCode) {
		this.ratedPtCode = ratedPtCode;
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

	public Byte getMeterOrder() {
		return meterOrder;
	}

	public void setMeterOrder(Byte meterOrder) {
		this.meterOrder = meterOrder;
	}

	public Byte getInductorOrder() {
		return inductorOrder;
	}

	public void setInductorOrder(Byte inductorOrder) {
		this.inductorOrder = inductorOrder;
	}

	public Integer getCalcMon() {
		return calcMon;
	}

	public void setCalcMon(Integer calcMon) {
		this.calcMon = calcMon;
	}

	public Integer getCurrentMon() {
		return currentMon;
	}

	public void setCurrentMon(Integer currentMon) {
		this.currentMon = currentMon;
	}

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
	}

	public String getAssetNo() {
		return assetNo;
	}

	public void setAssetNo(String assetNo) {
		this.assetNo = assetNo;
	}

}
