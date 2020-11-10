/**
 * Author : czy
 * Date : 2019年6月25日 上午9:33:06
 * Title : com.riozenc.cfs.webapp.mrm.e.service.impl.SystemInitServiceImpl.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;

import org.fms.cfs.common.webapp.domain.DeptMonDomain;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.common.webapp.domain.WriteSectDomain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;

@Service
public class SystemInitServiceImpl {

	@Autowired
	private TitanTemplate titanTemplate;

	public Callable<HttpResult> systemInit(List<String> deptIds) throws Exception {

		// 判断date 与当前系统时间，不能大于

		Map<String, String> writeSectParams = new HashMap<>();
		writeSectParams.put("status", "1");
		// 抄表区段
		List<WriteSectDomain> writeSectDomains = titanTemplate.postJson("CIM-SERVER",
				"cimServer/writeSect?method=getWriteSect", new HttpHeaders(), writeSectParams,
				new TypeReference<List<WriteSectDomain>>() {
				});

		// 电价
		List<PriceExecutionDomain> priceExecutionDomains = titanTemplate.post("BILLING-SERVER",
				"billingServer/priceExecution?method=getPriceExecutionInfo", null, null,
				new TypeReference<List<PriceExecutionDomain>>() {
				});

		// 营业区域电费月份
		LinkedMultiValueMap<String, Object> deptMonParams = new LinkedMultiValueMap<>();
		deptMonParams.addAll("deptIds", deptIds);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		List<DeptMonDomain> deptMonDomains = titanTemplate.post("BILLING-SERVER",
				"billingServer/deptMon?method=nextMon", headers, deptMonParams,
				new TypeReference<List<DeptMonDomain>>() {
				});
		// 翻月

		// 电费力率调整表

		return new Callable<HttpResult>() {

			@Override
			public HttpResult call() throws Exception {
				// TODO Auto-generated method stub

				StringBuilder stringBuilder = new StringBuilder();
				stringBuilder.append("抄表区段：").append(writeSectDomains.size());
				stringBuilder.append("电价：").append(priceExecutionDomains.size());
				stringBuilder.append("营业区域电费月份：").append(deptMonDomains.size());
				HttpResult httpResult = new HttpResult(HttpResult.SUCCESS, stringBuilder.toString());
				return httpResult;
			}

		};
	}
}
