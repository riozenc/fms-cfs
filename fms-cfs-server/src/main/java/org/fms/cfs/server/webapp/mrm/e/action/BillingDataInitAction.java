/**
 * Author : czy
 * Date : 2019年6月26日 下午4:50:11
 * Title : com.riozenc.cfs.webapp.mrm.e.action.BillingDataInitAction.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.action;

import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.InitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.server.webapp.mrm.handler.FilteringInitHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.riozenc.titanTool.spring.web.http.HttpResult;

import reactor.core.publisher.Mono;

/**
 * 计费数据初始化
 * 
 * @author czy
 *
 */
@ControllerAdvice
@RequestMapping("billingDataInit")
public class BillingDataInitAction {
	private static final Logger logger = LoggerFactory.getLogger(BillingDataInitAction.class);

	@Autowired
	private FilteringInitHandler filteringBillingDateHandler;

	@RequestMapping(params = "method=initializeMany")
	@ResponseBody
	public Mono<?> initializeMany(@RequestBody BillingDataInitModel billingDataInitModel) {
		long time = System.currentTimeMillis();
		InitModelExchange initModelExchange = new InitModelExchange() {
			@Override
			public InitModel getModel() {
				return billingDataInitModel;
			}

			@Override
			public String getExecuteResult() {
				return getModel().getExecuteResult();
			}
		};

		filteringBillingDateHandler.handle(initModelExchange).block();
		System.out.println("执行时间:" + (System.currentTimeMillis() - time));
//		System.out.println(initModelExchange.getExecuteResult());
		return Mono.just(new HttpResult(HttpResult.SUCCESS, initModelExchange.getExecuteResult()));

	}

}
