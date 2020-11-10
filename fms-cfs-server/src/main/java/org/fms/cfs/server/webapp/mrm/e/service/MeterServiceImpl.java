/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:16:23
 *    Title:com.riozenc.cim.webapp.service.impl.SettlementServiceImpl.java
 **/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.ElectricMeterDomain;
import org.fms.cfs.common.webapp.service.IMeterService;
import org.fms.cfs.server.webapp.mrm.e.dao.MeterDAO;
import org.springframework.util.CollectionUtils;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class MeterServiceImpl implements IMeterService {

	@TransactionDAO("cim")
	private MeterDAO meterDAO;

	@Override
	public int insert(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return meterDAO.insert(t);
	}

	@Override
	public int delete(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return meterDAO.delete(t);
	}

	@Override
	public int update(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return meterDAO.update(t);
	}

	@Override
	public ElectricMeterDomain findByKey(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return meterDAO.findByKey(t);
	}

	@Override
	public List<ElectricMeterDomain> findByWhere(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return meterDAO.findByWhere(t);
	}

	@Override
	public long initialize(String date, ElectricMeterDomain electricMeterDomain) {
		// TODO Auto-generated method stub

		List<ElectricMeterDomain> meterDomains = findByWhere(electricMeterDomain);
		if (CollectionUtils.isEmpty(meterDomains))
			return 0;

		return meterDAO.initialize(date, meterDomains);
	}

	@Override
	public long initializeByMeter(String date, String meterIds) {
		// TODO Auto-generated method stub
		List<ElectricMeterDomain> meterDomains = meterDAO.findByMeter(meterIds);
		if (CollectionUtils.isEmpty(meterDomains))
			return 0;

		return meterDAO.initializeMany(date, meterDomains);
	}

	@Override
	public long initializeByUser(String date, String userIds) {
		// TODO Auto-generated method stub

		List<ElectricMeterDomain> meterDomains = meterDAO.findByUser(userIds);
		if (CollectionUtils.isEmpty(meterDomains))
			return 0;

		return meterDAO.initializeMany(date, meterDomains);
	}

	@Override
	public long initializeByWriteSect(String date, String writeSectIds) {
		// TODO Auto-generated method stub
		List<ElectricMeterDomain> meterDomains = meterDAO.findByWriteSect(writeSectIds);
		if (CollectionUtils.isEmpty(meterDomains))
			return 0;

		return meterDAO.initializeMany(date, meterDomains);
	}

}
