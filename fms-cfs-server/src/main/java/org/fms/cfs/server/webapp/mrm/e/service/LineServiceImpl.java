/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:16:23
 *    Title:com.riozenc.cim.webapp.service.impl.SettlementServiceImpl.java
 **/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.Date;
import java.util.List;

import org.fms.cfs.common.webapp.domain.LineDomain;
import org.fms.cfs.common.webapp.service.ILineService;
import org.fms.cfs.server.webapp.mrm.e.dao.LineDAO;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class LineServiceImpl implements ILineService {

	@TransactionDAO("cim")
	private LineDAO lineDAO;

	Date now = new Date();

	@Override
	public int insert(LineDomain t) {
		// TODO Auto-generated method stub
		return lineDAO.insert(t);
	}

	@Override
	public int delete(LineDomain t) {
		// TODO Auto-generated method stub
		return lineDAO.delete(t);
	}

	@Override
	public int update(LineDomain t) {
		// TODO Auto-generated method stub
		return lineDAO.update(t);
	}

	@Override
	public LineDomain findByKey(LineDomain t) {
		// TODO Auto-generated method stub
		return lineDAO.findByKey(t);
	}

	@Override
	public List<LineDomain> findByWhere(LineDomain t) {
		// TODO Auto-generated method stub
		return lineDAO.findByWhere(t);
	}

}
