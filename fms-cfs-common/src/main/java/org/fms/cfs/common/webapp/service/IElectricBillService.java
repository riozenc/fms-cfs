/**
 * Author : czy
 * Date : 2019年11月1日 下午5:29:36
 * Title : com.riozenc.cfs.webapp.cfm.service.IElectricBillService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import java.util.List;
import java.util.Map;

import org.fms.cfs.common.webapp.domain.ArrearageDomain;
import org.fms.cfs.common.webapp.domain.BillingDataBusinessDomain;



public interface IElectricBillService {
	public Map<String, List<?>> issue(BillingDataBusinessDomain billingDataBusinessDomain);
	
	
	public int issueComplete(BillingDataBusinessDomain billingDataBusinessDomain,List<ArrearageDomain> arrearageDomains);

}
