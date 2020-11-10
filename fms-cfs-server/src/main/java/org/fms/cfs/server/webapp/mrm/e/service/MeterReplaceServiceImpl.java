/**
 * Author : czy
 * Date : 2019年4月22日 下午4:08:35
 * Title : com.riozenc.cfs.webapp.mrm.e.service.impl.MeterReplaceServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;
import org.fms.cfs.common.webapp.service.IMeterReplaceService;
import org.fms.cfs.server.webapp.mrm.e.dao.MeterReplaceDAO;
import org.springframework.util.CollectionUtils;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class MeterReplaceServiceImpl implements IMeterReplaceService {

	@TransactionDAO("cim")
	private MeterReplaceDAO meterReplaceDAO;

	@Override
	public int insert(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return meterReplaceDAO.insert(t);
	}

	@Override
	public int delete(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return meterReplaceDAO.delete(t);
	}

	@Override
	public int update(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return meterReplaceDAO.update(t);
	}

	@Override
	public MeterReplaceDomain findByKey(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return meterReplaceDAO.findByKey(t);
	}

	@Override
	public List<MeterReplaceDomain> findByWhere(MeterReplaceDomain t) {
		// TODO Auto-generated method stub
		return meterReplaceDAO.findByWhere(t);
	}

	@Override
	public long initialize(String date, MeterReplaceDomain meterReplaceDomain) {
		// TODO Auto-generated method stub
		List<MeterReplaceDomain> meterReplaceDomains = findByWhere(meterReplaceDomain);
		if (CollectionUtils.isEmpty(meterReplaceDomains))
			return 0;
		return meterReplaceDAO.initialize(date, meterReplaceDomains);
	}

	@Override
	public long initializeByMeter(String date, String meterIds) {
		// TODO Auto-generated method stub
		List<MeterReplaceDomain> meterReplaceDomains = meterReplaceDAO.findByMeter(meterIds);
		if (CollectionUtils.isEmpty(meterReplaceDomains))
			return 0;
		return meterReplaceDAO.initializeMany(date, meterReplaceDomains);
	}

	@Override
	public long initializeByUser(String date, String userIds) {
		// TODO Auto-generated method stub
		List<MeterReplaceDomain> meterReplaceDomains = meterReplaceDAO.findByUser(userIds);
		if (CollectionUtils.isEmpty(meterReplaceDomains))
			return 0;
		return meterReplaceDAO.initializeMany(date, meterReplaceDomains);
	}

	@Override
	public long initializeByWriteSect(String date, String writeSectIds) {
		// TODO Auto-generated method stub
		List<MeterReplaceDomain> meterReplaceDomains = meterReplaceDAO.findByWriteSect(writeSectIds);
		if (CollectionUtils.isEmpty(meterReplaceDomains))
			return 0;
		return meterReplaceDAO.initializeMany(date, meterReplaceDomains);
	}

}
