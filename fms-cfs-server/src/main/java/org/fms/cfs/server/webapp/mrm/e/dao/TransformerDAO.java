/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:11:34
 *    Title:com.riozenc.cim.webapp.dao.UserDAO.java
 **/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class TransformerDAO extends AbstractTransactionDAOSupport implements BaseDAO<TransformerDomain> {

	@Override
	public int insert(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public TransformerDomain findByKey(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<TransformerDomain> findByWhere(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}
	
	public List<TransformerDomain> getTransformerByUser(UserDomain userDomain){
		return getPersistanceManager().find(getNamespace() + ".getTransformerByUser", userDomain);
	}

	public int insertLineTransRela(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insertLineTransRela", t);
	}

	public List<TransformerDomain> findByTranId(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByTranId", t);
	}

	public int updateLineTransRela(TransformerDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".updateLineTransRela", t);
	}

}
