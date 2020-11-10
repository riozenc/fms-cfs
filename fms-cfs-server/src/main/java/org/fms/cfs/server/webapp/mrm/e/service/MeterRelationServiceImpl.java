/**
 * Author : czy
 * Date : 2019年4月13日 上午11:26:53
 * Title : com.riozenc.cfs.webapp.mrm.service.impl.MeterRelServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.MeterRelationDomain;
import org.fms.cfs.common.webapp.service.IMeterRelationService;
import org.fms.cfs.server.webapp.mrm.e.dao.MeterRelationDAO;
import org.springframework.util.CollectionUtils;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class MeterRelationServiceImpl implements IMeterRelationService {

	@TransactionDAO("cim")
	private MeterRelationDAO meterRelDAO;

	@Override
	public int insert(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return meterRelDAO.insert(t);
	}

	@Override
	public int delete(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return meterRelDAO.delete(t);
	}

	@Override
	public int update(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return meterRelDAO.update(t);
	}

	@Override
	public MeterRelationDomain findByKey(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return meterRelDAO.findByKey(t);
	}

	@Override
	public List<MeterRelationDomain> findByWhere(MeterRelationDomain t) {
		// TODO Auto-generated method stub
		return meterRelDAO.findByWhere(t);
	}

	@Override
	public long initialize(String date) {
		// TODO Auto-generated method stub
		List<MeterRelationDomain> meterRelDomains = findByWhere(new MeterRelationDomain());
		if (CollectionUtils.isEmpty(meterRelDomains))
			return 0;
		
		return meterRelDAO.initialize(date, meterRelDomains);
	}

	@Override
	public long initializeByMeter(String date, String meterIds) {
		// TODO Auto-generated method stub
		List<MeterRelationDomain> meterRelDomains = meterRelDAO.findByMeter(meterIds);
		if (CollectionUtils.isEmpty(meterRelDomains))
			return 0;
		
		return meterRelDAO.initializeMany(date, meterRelDomains);
	}

	@Override
	public long initializeByUser(String date, String userIds) {
		// TODO Auto-generated method stub
		List<MeterRelationDomain> meterRelDomains = meterRelDAO.findByUser(userIds);
		if (CollectionUtils.isEmpty(meterRelDomains))
			return 0;
		return meterRelDAO.initializeMany(date, meterRelDomains);
	}

	@Override
	public long initializeByWriteSect(String date, String writeSectIds) {
		// TODO Auto-generated method stub
		List<MeterRelationDomain> meterRelDomains = meterRelDAO.findByWriteSect(writeSectIds);
		if (CollectionUtils.isEmpty(meterRelDomains))
			return 0;
		return meterRelDAO.initializeMany(date, meterRelDomains);
	}

}
