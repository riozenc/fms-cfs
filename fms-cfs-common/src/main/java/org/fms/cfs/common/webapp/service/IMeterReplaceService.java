/**
 * Author : czy
 * Date : 2019年4月22日 下午4:06:51
 * Title : com.riozenc.cfs.webapp.mrm.e.service.IMeterReplaceService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IMeterReplaceService extends BaseService<MeterReplaceDomain> {
	public long initialize(String date, MeterReplaceDomain meterReplaceDomain);

	public long initializeByMeter(String date, String meterIds);

	public long initializeByUser(String date, String userIds);

	public long initializeByWriteSect(String date, String writeSectIds);
}
