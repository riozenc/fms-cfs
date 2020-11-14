/**
 * Author : chizf
 * Date : 2020年11月14日 下午4:45:48
 * Title : org.fms.cfs.common.webapp.service.ISDevIrService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import org.fms.cfs.common.webapp.domain.SDevIrDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface ISDevIrService extends BaseService<SDevIrDomain> {
	public long initialize(String date, SDevIrDomain meterReplaceDomain);

	public long initializeByMeter(String date, String meterIds);

	public long initializeByUser(String date, String userIds);

	public long initializeByWriteSect(String date, String writeSectIds);
}