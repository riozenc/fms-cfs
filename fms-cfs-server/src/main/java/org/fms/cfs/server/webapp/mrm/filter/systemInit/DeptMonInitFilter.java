/**
 * Author : czy
 * Date : 2019年6月26日 下午2:27:26
 * Title : com.riozenc.cfs.webapp.mrm.filter.DeptMonInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.systemInit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.fms.cfs.common.model.SystemInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.DeptMonDomain;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;

import com.fasterxml.jackson.core.type.TypeReference;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResult;

import reactor.core.publisher.Mono;

public class DeptMonInitFilter implements SystemInitFilter {
	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return HIGHEST_PRECEDENCE;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub

		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		// 营业区域电费月份
		LinkedMultiValueMap<String, Object> deptMonParams = new LinkedMultiValueMap<>();
		deptMonParams.addAll("deptIds", systemInitModel.getDeptId());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		try {
			HttpResult<List<DeptMonDomain>> nextMonResult = titanTemplate.post("BILLING-SERVER",
					"billingServer/deptMon?method=nextMon", headers, deptMonParams,
					new TypeReference<HttpResult<List<DeptMonDomain>>>() {
					});

			if (nextMonResult.getStatusCode() == HttpResult.SUCCESS) {

				List<DeptMonDomain> deptMonDomains = nextMonResult.getResultData();

				systemInitModel.setDeptMonDomains(deptMonDomains);
				HttpHeaders httpHeaders = new HttpHeaders();
				httpHeaders.setContentType(MediaType.APPLICATION_JSON);
				Map<String, String> params = new HashMap<>();

				params.put("type", "CURRENT_MON");
				params.put("paramKey", "0");
				params.put("paramValue", deptMonDomains.get(0).getMon());
				
				systemInitModel.setDate(deptMonDomains.get(0).getMon());

				// 更新参数表
				HttpResult httpResult = titanTemplate.postJson("TITAN-CONFIG", "titan-config/sysCommConfig/update",
						httpHeaders, params, new TypeReference<HttpResult>() {
						});

				if (httpResult.getStatusCode() == HttpResult.SUCCESS) {
					systemInitModel.addExecuteResult("系统当前月份更新成功" + systemInitModel.getDate() + ".");
				} else {
					systemInitModel.addExecuteResult("系统当前月份更新失败(" + httpResult.getMessage() + "),检查并人工处理.");
				}
				return filterChain.filter(exchange);
			} else {
				throw new RuntimeException(nextMonResult.getMessage());
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage());
		}

	}

}
