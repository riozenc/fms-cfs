package org.fms.cfs.common.webapp.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.CommonParamDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface ICommonParamService extends BaseService<CommonParamDomain> {

	public List<CommonParamDomain> getAllType(CommonParamDomain commonParamDomain);

	public List<CommonParamDomain> getAllTypeForList(String t);

	public long paramsInit(String date, CommonParamDomain commonParamDomain);
	
	public int nextMon(String date);
}
