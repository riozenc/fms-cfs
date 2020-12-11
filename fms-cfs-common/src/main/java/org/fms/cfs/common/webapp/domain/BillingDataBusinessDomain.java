/**
 * Author : czy
 * Date : 2019年11月2日 上午11:17:30
 * Title : com.riozenc.cfs.webapp.dsm.domain.BillingDataBusinessDomain.java
 *
**/
package org.fms.cfs.common.webapp.domain;

import java.util.List;

public class BillingDataBusinessDomain {

	private Integer date;
	private List<MeterDomain> meterDomains;
	private List<PreChargeDomain> preChargeDomains;
	private Byte sn;
	private String callback;

	public Integer getDate() {
		return date;
	}

	public void setDate(Integer date) {
		this.date = date;
	}

	public List<MeterDomain> getMeterDomains() {
		return meterDomains;
	}

	public void setMeterDomains(List<MeterDomain> meterDomains) {
		this.meterDomains = meterDomains;
	}

	public Byte getSn() {
		return sn;
	}

	public void setSn(Byte sn) {
		this.sn = sn;
	}

	public String getCallback() {
		return callback;
	}

	public void setCallback(String callback) {
		this.callback = callback;
	}

	public List<PreChargeDomain> getPreChargeDomains() {
		return preChargeDomains;
	}

	public void setPreChargeDomains(List<PreChargeDomain> preChargeDomains) {
		this.preChargeDomains = preChargeDomains;
	}

}
