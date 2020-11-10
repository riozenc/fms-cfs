/**
 * Author : czy
 * Date : 2019年4月13日 上午11:26:20
 * Title : com.riozenc.cfs.webapp.mrm.service.IMeterRelationService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import org.fms.cfs.common.webapp.domain.MeterRelationDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IMeterRelationService extends BaseService<MeterRelationDomain> {
	
	public long initialize(String date);

	public long initializeByMeter(String date, String meterIds);

	public long initializeByUser(String date, String userIds);

	public long initializeByWriteSect(String date, String writeSectIds);
}
