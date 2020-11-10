/**
 * Author : czy
 * Date : 2019年6月23日 上午9:31:05
 * Title : com.riozenc.cfs.webapp.mrm.e.service.impl.PriceExecutionServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.io.IOException;
import java.util.List;

import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.common.webapp.service.IPriceExecutionService;
import org.fms.cfs.server.webapp.mrm.e.dao.PriceExecutionDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.annotation.TransactionService;
import com.riozenc.titanTool.common.json.utils.JSONUtil;

@TransactionService
public class PriceExecutionServiceImpl implements IPriceExecutionService {

	@TransactionDAO
	private PriceExecutionDAO priceExecutionDAO;

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public int insert(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return priceExecutionDAO.insert(t);
	}

	@Override
	public int delete(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return priceExecutionDAO.delete(t);
	}

	@Override
	public int update(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return priceExecutionDAO.update(t);
	}

	@Override
	public PriceExecutionDomain findByKey(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return priceExecutionDAO.findByKey(t);
	}

	@Override
	public List<PriceExecutionDomain> findByWhere(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return priceExecutionDAO.findByWhere(t);
	}

	@Override
	public List<PriceExecutionDomain> init(String date) {
		// TODO Auto-generated method stub

		String result = restTemplate.postForObject("http://BILLING-SERVER/priceExecution?method=getPriceExecutionInfo",
				null, String.class);

		List<PriceExecutionDomain> priceExecutionDomains;
		try {
			priceExecutionDomains = JSONUtil.readValue(result, new TypeReference<List<PriceExecutionDomain>>() {
			});
			return priceExecutionDomains;
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
