/**
 * Author : czy
 * Date : 2019年4月22日 下午5:07:09
 * Title : com.riozenc.cfs.webapp.mrm.e.domain.WriteFilesDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.Date;

import org.fms.cfs.common.utils.MongoUtils;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.riozenc.titanTool.mybatis.MybatisEntity;

@JsonIgnoreProperties(ignoreUnknown = true)
public class WriteFilesDomain implements MybatisEntity {

	private String _id;
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private Long meterId;// 计费点ID
	private Long mpedId; // 计量点ID
	private Integer mon;// 月份
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date initDate;// 初始化时间 INIT_DATE datetime FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date writeDate;// 抄表日期 WRITE_DATE datetime FALSE FALSE FALSE
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private Date lastWriteDate;// 上次抄表日期 LAST_WRITE_DATE datetime FALSE FALSE FALSE
	private Byte sn;// 本月次数 SN smallint FALSE FALSE FALSE
	private Byte phaseSeq;// 相序
	private Byte functionCode;// 功能代码
	private Byte powerDirection;// 功率方向POWER_DIRECTION;
	private Byte writeFlag;// 抄表标志 WRITE_FLAG smallint FALSE FALSE FALSE
	private Byte writeMethod;// 抄表方式 WRITE_METHOD smallint FALSE FALSE FALSE
	private Byte timeSeg;// 时段 TIME_SEG smallint FALSE FALSE FALSE
	private BigDecimal startNum;// 起码 START_NUM decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal endNum;// 止码 END_NUM decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal diffNum;// 度差 DIFF_NUM decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal writePower;// 抄见电量 WRITE_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal chgPower = BigDecimal.ZERO;// 换表电量 CHG_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private BigDecimal addPower;// 增减电量 ADD_POWER decimal(12,2) 12 2 FALSE FALSE FALSE
	private String inputMan;// 录入人 INPUT_MAN varchar(16) 16 FALSE FALSE FALSE
	private Byte meterOrder;// 序号

	private BigDecimal factorNum;// 倍率

	private Long businessPlaceCode;// 营业区域
	private String userNo;
	private String meterNo;//
	private Long writeSectionId;// 所属抄表区段
	private Long tgId; // 所属台区 TG_ID bigint
	private Long lineId; // 所属线路 LINE_ID bigint
	private Long subsId; // 所属厂站 SUBS_ID bigint
	private Long userId; // 所属用户 USER_ID bigint
	private String userName;
	private String address;// 用电地址

	public String get_id() {
		return _id;
	}

//	public void createObjectId() {
//		this._id = MongoUtils.createObjectId(this.getMeterId(), this.getSn(), this.getTimeSeg(),
//				this.getPowerDirection(), this.getFunctionCode(), this.getMeterAssetsId());
//	}

	public void createObjectId() {
		this._id = MongoUtils.createObjectId(this.getMeterId(), this.getPhaseSeq(), this.getSn(), this.getTimeSeg(),
				this.getPowerDirection(), this.getFunctionCode());
	}

	/**
	 * 是否有功
	 * 
	 * @return
	 */
	public boolean isActive() {
		return getPowerDirection() == 1;
	}

	/**
	 * 是否无功
	 * 
	 * @return
	 */
	public boolean isReactive() {
		return getPowerDirection() == 2;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Integer getMon() {
		return mon;
	}

	public void setMon(Integer mon) {
		this.mon = mon;
	}

	public Date getInitDate() {
		return initDate;
	}

	public void setInitDate(Date initDate) {
		this.initDate = initDate;
	}

	public Date getWriteDate() {
		return writeDate;
	}

	public void setWriteDate(Date writeDate) {
		this.writeDate = writeDate;
	}

	public Date getLastWriteDate() {
		return lastWriteDate;
	}

	public void setLastWriteDate(Date lastWriteDate) {
		this.lastWriteDate = lastWriteDate;
	}

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

	public Byte getWriteFlag() {
		return writeFlag;
	}

	public void setWriteFlag(Byte writeFlag) {
		this.writeFlag = writeFlag;
	}

	public Byte getWriteMethod() {
		return writeMethod;
	}

	public void setWriteMethod(Byte writeMethod) {
		this.writeMethod = writeMethod;
	}

	public Byte getTimeSeg() {
		return timeSeg;
	}

	public void setTimeSeg(Byte timeSeg) {
		this.timeSeg = timeSeg;
	}

	public BigDecimal getStartNum() {
		return startNum;
	}

	public void setStartNum(BigDecimal startNum) {
		this.startNum = startNum;
	}

	public BigDecimal getEndNum() {
		return endNum;
	}

	public void setEndNum(BigDecimal endNum) {
		this.endNum = endNum;
	}

	public BigDecimal getDiffNum() {
		return diffNum;
	}

	public void setDiffNum(BigDecimal diffNum) {
		this.diffNum = diffNum;
	}

	public BigDecimal getWritePower() {
		return writePower;
	}

	public void setWritePower(BigDecimal writePower) {
		this.writePower = writePower;
	}

	public BigDecimal getChgPower() {
		return chgPower;
	}

	public void setChgPower(BigDecimal chgPower) {
		this.chgPower = chgPower;
	}

	public void addChgPower(BigDecimal chgPower) {
		this.chgPower = this.chgPower.add(chgPower);
	}

	public BigDecimal getAddPower() {
		return addPower;
	}

	public void setAddPower(BigDecimal addPower) {
		this.addPower = addPower;
	}

	public String getInputMan() {
		return inputMan;
	}

	public void setInputMan(String inputMan) {
		this.inputMan = inputMan;
	}

	public Long getTgId() {
		return tgId;
	}

	public void setTgId(Long tgId) {
		this.tgId = tgId;
	}

	public Long getLineId() {
		return lineId;
	}

	public void setLineId(Long lineId) {
		this.lineId = lineId;
	}

	public Long getSubsId() {
		return subsId;
	}

	public void setSubsId(Long subsId) {
		this.subsId = subsId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserNo() {
		return userNo;
	}

	public void setUserNo(String userNo) {
		this.userNo = userNo;
	}

	public Long getWriteSectionId() {
		return writeSectionId;
	}

	public void setWriteSectionId(Long writeSectionId) {
		this.writeSectionId = writeSectionId;
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

	public String getMeterNo() {
		return meterNo;
	}

	public void setMeterNo(String meterNo) {
		this.meterNo = meterNo;
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

	public Byte getMeterOrder() {
		return meterOrder;
	}

	public void setMeterOrder(Byte meterOrder) {
		this.meterOrder = meterOrder;
	}

	public Long getBusinessPlaceCode() {
		return businessPlaceCode;
	}

	public void setBusinessPlaceCode(Long businessPlaceCode) {
		this.businessPlaceCode = businessPlaceCode;
	}

	public Long getMeterId() {
		return meterId;
	}

	public void setMeterId(Long meterId) {
		this.meterId = meterId;
	}

	public BigDecimal getFactorNum() {
		return factorNum;
	}

	public void setFactorNum(BigDecimal factorNum) {
		this.factorNum = factorNum;
	}

	public Long getMpedId() {
		return mpedId;
	}

	public void setMpedId(Long mpedId) {
		this.mpedId = mpedId;
	}

}
