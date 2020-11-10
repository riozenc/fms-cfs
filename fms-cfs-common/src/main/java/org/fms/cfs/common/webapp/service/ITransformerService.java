/**
 *    Auth:riozenc
 *    Date:2019年3月12日 下午7:15:22
 *    Title:com.riozenc.cim.webapp.service.ISettlementService.java
 **/
package org.fms.cfs.common.webapp.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface ITransformerService extends BaseService<TransformerDomain>{

	public List<TransformerDomain> getTransformerByUser(UserDomain userDomain);
}
