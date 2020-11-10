/**
 * Author : czy
 * Date : 2019年6月28日 下午3:19:18
 * Title : com.riozenc.cfs.webapp.cfm.domain.ElectricMonthlyBillDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.util.List;

public class ElectricMonthlyBillDomain {

	private Integer date;
	private Byte sn;
	private List<MeterDomain> meterDomains;
	private List<WriteFilesDomain> writeFilesDomains;
	private String callback;

	private String userId;

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

	public List<MeterDomain> getMeterDomains() {
		return meterDomains;
	}

	public void setMeterDomains(List<MeterDomain> meterDomains) {
		this.meterDomains = meterDomains;
	}

	public List<WriteFilesDomain> getWriteFilesDomains() {
		return writeFilesDomains;
	}

	public void setWriteFilesDomains(List<WriteFilesDomain> writeFilesDomains) {
		this.writeFilesDomains = writeFilesDomains;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

}
