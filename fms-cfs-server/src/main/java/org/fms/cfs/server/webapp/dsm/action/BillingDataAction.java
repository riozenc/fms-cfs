/**
 * Author : czy
 * Date : 2019年11月2日 上午11:07:07
 * Title : com.riozenc.cfs.webapp.dsm.action.BillingDataAction.java
 *
**/
package org.fms.cfs.server.webapp.dsm.action;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.fms.cfs.common.annotation.ServerCallback;
import org.fms.cfs.common.webapp.domain.ArrearageDomain;
import org.fms.cfs.common.webapp.domain.BillingDataBusinessDomain;
import org.fms.cfs.common.webapp.service.IElectricBillService;
import org.fms.cfs.server.web.http.callback.ServerCallbackHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.riozenc.titanTool.spring.web.http.HttpResult;

import reactor.core.publisher.Mono;

@ControllerAdvice
@RequestMapping("billingData")
public class BillingDataAction {

	@Autowired
	@Qualifier("electricBillServiceImpl")
	private IElectricBillService electricBillService;

	@Autowired
	private ServerCallbackHandler serverCallbackHandler;

	/**
	 * 发行
	 * 
	 * @param electricMonthlyBillDomain
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=issue")
	@ResponseBody
	@ServerCallback
	public Mono<?> issue(@RequestBody BillingDataBusinessDomain billingDataBusinessDomain) throws Exception {
//	public Mono<?> issue(@RequestBody String json) throws Exception {

		// 将mongo数据同步至mysql数据库

		// 抄表数据
		// 电费数据
		// 欠费数据
		// 余额
//		BillingDataBusinessDomain billingDataBusinessDomain = GsonUtils.readValue(json, BillingDataBusinessDomain.class);
		
		long time = System.currentTimeMillis();
		System.out.println("发行开始:"+LocalDateTime.now());
		Map<String, List<?>> result = electricBillService.issue(billingDataBusinessDomain);

		HttpResult httpResult = serverCallbackHandler.callback("BILLING-SERVER",
				billingDataBusinessDomain.getCallback(), result);

		// 根据返回结果更新状态

		if (httpResult.getStatusCode() == HttpResult.SUCCESS)
			electricBillService.issueComplete(billingDataBusinessDomain,
					(List<ArrearageDomain>) result.get("arrearageDomains"));
		
		System.out.println("发行时间:"+(System.currentTimeMillis()-time));
		return Mono.just(httpResult);
	}
}
