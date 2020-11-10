/**
 * Author : czy
 * Date : 2019年6月23日 上午9:30:19
 * Title : com.riozenc.cfs.webapp.mrm.e.service.IPriceExecutionService.java
 *
**/
package org.fms.cfs.common.webapp.service;

import java.util.List;

import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;

import com.riozenc.titanTool.spring.webapp.service.BaseService;

public interface IPriceExecutionService extends BaseService<PriceExecutionDomain> {
	public List<PriceExecutionDomain> init(String date);
}
