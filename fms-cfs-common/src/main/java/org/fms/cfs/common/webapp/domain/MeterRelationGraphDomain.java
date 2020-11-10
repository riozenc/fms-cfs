/**
 * Author : czy
 * Date : 2019年10月28日 下午3:36:12
 * Title : com.riozenc.cfs.webapp.cfm.domain.MeterRelationGraphDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.math.BigDecimal;
import java.util.List;

/**
 * 计量点关系图表
 * 
 * @author czy
 *
 */
public class MeterRelationGraphDomain {

	private Long id;//
	private Long meterId;//
	private Long pMeterId;//
	private Byte meterRateFlag;// 计量点表计类型 METER_RATE_FLAG smallint FALSE FALSE FALSE
	private Byte pMeterRateFlag;// 关系计量点表计类型 P_METER_RATE_FLAG smallint FALSE FALSE FALSE
	private String shareRate;// 分摊比例（普通拆分时）
	private Byte meterRelationType;// 计量点关系 METER_RELATION_TYPE smallint FALSE FALSE FALSE
	private BigDecimal meterRelationValue=BigDecimal.ZERO;// 关系值 METER_RELATION_VALUE decimal(5,2) 5 2 FALSE FALSE FALSE
	private Long numConnections;// 深度
	private List<MeterRelationGraphDomain> rel;

	private boolean isSplitTs = false;// 是否普通拆分时

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

	public List<MeterRelationGraphDomain> getRel() {
		return rel;
	}

	public void setRel(List<MeterRelationGraphDomain> rel) {
		this.rel = rel;
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

	public Long getNumConnections() {
		return numConnections;
	}

	public void setNumConnections(Long numConnections) {
		this.numConnections = numConnections;
	}

	public String getShareRate() {
		return shareRate;
	}

	public void setShareRate(String shareRate) {
		this.shareRate = shareRate;
	}

	public boolean isSplitTs() {
		return isSplitTs;
	}

	public void setSplitTs(boolean isSplitTs) {
		this.isSplitTs = isSplitTs;
	}

}
