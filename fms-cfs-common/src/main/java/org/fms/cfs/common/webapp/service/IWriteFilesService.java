/**
 * Author : czy
 * Date : 2019年4月22日 下午5:18:58
 * Title : com.riozenc.cfs.webapp.mrm.e.service.IWriteFilesService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.ElectricMeterDomain;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IWriteFilesService extends BaseService<WriteFilesDomain> {
	public long initialize(String date);

	public long initializeByMeter(String date, String meterIds);

	public long initializeByUser(String date, String userIds);

	public long initializeByWriteSect(String date, String writeSectIds);

	public long meterReading(String date, ElectricMeterDomain electricMeterDomain);

	public long meterReadingMany(String date, String meterIds);

	public long check(String date);

	public long checkMany(String date, String meterIds);

	public long computeMany(String date, String meterIds);

	public long manualMeterReading(String date, List<WriteFilesDomain> writeFilesDomains);
}
