/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:16:23
 *    Title:com.riozenc.cim.webapp.service.impl.SettlementServiceImpl.java
 **/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.common.webapp.service.ITransformerService;
import org.fms.cfs.server.webapp.mrm.e.dao.TransformerDAO;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class TransformerServiceImpl implements ITransformerService {
	
	@TransactionDAO("cim")
	private TransformerDAO transformerDAO;

	@Override
	public int insert(TransformerDomain t) {
		
		int tc = transformerDAO.insert(t);
		if(tc<=0) {
			return 0;
		}else if(t.getLineId()==null) {
			return 1;
		}
		
		return transformerDAO.insertLineTransRela(t);
	}

	@Override
	public int delete(TransformerDomain t) {
		// TODO Auto-generated method stub
		return transformerDAO.delete(t);
	}

	@Override
	public int update(TransformerDomain t) {
		int tc = transformerDAO.update(t);
		
		List<TransformerDomain> tt = transformerDAO.findByTranId(t);
		int rc = 0;
		if(tt.size()==0) {
			rc = transformerDAO.insertLineTransRela(t);
		}

		return transformerDAO.updateLineTransRela(t);
	}

	@Override
	public TransformerDomain findByKey(TransformerDomain t) {
		// TODO Auto-generated method stub
		return transformerDAO.findByKey(t);
	}

	@Override
	public List<TransformerDomain> findByWhere(TransformerDomain t) {
		// TODO Auto-generated method stub
		return transformerDAO.findByWhere(t);
	}

	@Override
	public List<TransformerDomain> getTransformerByUser(UserDomain userDomain) {
		// TODO Auto-generated method stub
		return transformerDAO.getTransformerByUser(userDomain);
	}

}
