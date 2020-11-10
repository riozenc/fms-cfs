/**
 *    Auth:riozenc
 *    Date:2019年3月8日 下午3:38:03
 *    Title:com.riozenc.cim.webapp.service.ICustomerService.java
 **/
package org.fms.cfs.common.webapp.service;

import org.fms.cfs.common.webapp.domain.ElectricMeterDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IMeterService extends BaseService<ElectricMeterDomain> {

	public long initialize(String date, ElectricMeterDomain electricMeterDomain);

	public long initializeByMeter(String date, String meterIds);

	public long initializeByUser(String date, String userIds);

	public long initializeByWriteSect(String date, String writeSectIds);

}
