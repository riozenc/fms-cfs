/**
 * Author : czy
 * Date : 2019年6月24日 下午2:44:10
 * Title : com.riozenc.cfs.webapp.mrm.e.service.impl.ArchivesServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fms.cfs.common.webapp.service.IArchivesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;

import com.riozenc.titanTool.annotation.TransactionService;

@TransactionService
public class ArchivesServiceImpl implements IArchivesService {

	@Autowired
	private RestTemplate restTemplate;

	@Override
	public Callable<?> archivesInit(String date) {
		// TODO Auto-generated method stub

//		customer?method=getCustomer  获取客户信息
//				user?method=getUserInfo 获取用电户信息

		Map<String, String> requestBody = new HashMap<>();
		requestBody.put("status", "1");
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.setContentType(MediaType.APPLICATION_JSON);

		HttpEntity<Map<String, String>> request = new HttpEntity<Map<String, String>>(requestBody, httpHeaders);
		String customJson = restTemplate.postForObject("http://CIM-SERVER/cimServer/customer?method=getCustomer",
				request, String.class);

		String userJson = restTemplate.postForObject("http://CIM-SERVER/cimServer/user?method=getUserInfo", request,
				String.class);

		return new Callable<String>() {

			public String call() {
				return userJson;
			}
		};
	}

}
