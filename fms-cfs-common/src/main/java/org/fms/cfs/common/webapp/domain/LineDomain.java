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
 * 线路	LINE_INFO
 * 
 * @author riozenc
 *
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineDomain extends Page implements MybatisEntity {

	@TablePrimaryKey
	private Long id;// ID ID bigint TRUE FALSE TRUE
	private String lineCode; //线路编号
	private String lineName; //线路名称
	private Byte voltType; //电压等级
	private Byte lineType; //线路类型
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date runDate; //投运日期
	private String switchNo; //开关号
	private String lineModel; //线路型号
	private BigDecimal lineLenght; //线路长度
	private String ratingCurnt; //额定电流
	private String ratingVol; //额定电压
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	private Date createDate; //创建时间
	private String remark; //备注
	private Byte status; //状态
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getLineCode() {
		return lineCode;
	}
	public void setLineCode(String lineCode) {
		this.lineCode = lineCode;
	}
	public String getLineName() {
		return lineName;
	}
	public void setLineName(String lineName) {
		this.lineName = lineName;
	}
	public Byte getVoltType() {
		return voltType;
	}
	public void setVoltType(Byte voltType) {
		this.voltType = voltType;
	}
	public Byte getLineType() {
		return lineType;
	}
	public void setLineType(Byte lineType) {
		this.lineType = lineType;
	}
	public Date getRunDate() {
		return runDate;
	}
	public void setRunDate(Date runDate) {
		this.runDate = runDate;
	}
	public String getSwitchNo() {
		return switchNo;
	}
	public void setSwitchNo(String switchNo) {
		this.switchNo = switchNo;
	}
	public String getLineModel() {
		return lineModel;
	}
	public void setLineModel(String lineModel) {
		this.lineModel = lineModel;
	}
	public BigDecimal getLineLenght() {
		return lineLenght;
	}
	public void setLineLenght(BigDecimal lineLenght) {
		this.lineLenght = lineLenght;
	}
	public String getRatingCurnt() {
		return ratingCurnt;
	}
	public void setRatingCurnt(String ratingCurnt) {
		this.ratingCurnt = ratingCurnt;
	}
	public String getRatingVol() {
		return ratingVol;
	}
	public void setRatingVol(String ratingVol) {
		this.ratingVol = ratingVol;
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
