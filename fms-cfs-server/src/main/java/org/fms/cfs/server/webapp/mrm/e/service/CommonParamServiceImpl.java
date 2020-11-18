package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.bson.Document;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.common.webapp.service.ICommonParamService;
import org.fms.cfs.server.webapp.mrm.e.dao.CommonParamDAO;

import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.JSONUtil;

@TransactionService
public class CommonParamServiceImpl implements ICommonParamService {
	@TransactionDAO("cim")
	private CommonParamDAO commonParamDAO;

	@Override
	public int insert(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return commonParamDAO.insert(t);
	}

	@Override
	public int delete(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return commonParamDAO.delete(t);
	}

	@Override
	public int update(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return commonParamDAO.update(t);
	}

	@Override
	public CommonParamDomain findByKey(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return commonParamDAO.findByKey(t);
	}

	@Override
	public List<CommonParamDomain> findByWhere(CommonParamDomain t) {
		// TODO Auto-generated method stub
		return commonParamDAO.findByWhere(t);
	}

	@Override
	public List<CommonParamDomain> getAllType(CommonParamDomain domain) {

		return commonParamDAO.getAllType(domain);
	}

	@Override
	public List<CommonParamDomain> getAllTypeForList(String t) {

		return commonParamDAO.getAllTypeForList(t);
	}

	@Override
	public long paramsInit(String date, CommonParamDomain commonParamDomain) {
		// TODO Auto-generated method stub
		List<CommonParamDomain> commonParamDomains = findByWhere(commonParamDomain);
		List<Document> documents = Collections.synchronizedList(new ArrayList<>());

		commonParamDomains.parallelStream().forEach(p -> {
			documents.add(Document.parse(JSONUtil.toJsonString(p)));
		});

		return commonParamDAO.paramsInit(date, documents);
	}

	@Override
	public int nextMon(String date) {
		// TODO Auto-generated method stub
		CommonParamDomain commonParamDomain = new CommonParamDomain();
		commonParamDomain.setType("CURRENT_MON");
		commonParamDomain.setParamKey("0");
		commonParamDomain.setParamValue(date);
		return commonParamDAO.nextMon(commonParamDomain);
	}

}
