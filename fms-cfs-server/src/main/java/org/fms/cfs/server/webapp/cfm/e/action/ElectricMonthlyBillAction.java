/**
 * Author : czy
 * Date : 2019年4月13日 下午3:15:51
 * Title : com.riozenc.cfs.webapp.cfm.action.ElectricMonthlyBillAction.java
 *
**/
package org.fms.cfs.server.webapp.cfm.e.action;

import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.model.exchange.ECFModelExchange;
import org.fms.cfs.common.webapp.domain.ElectricMonthlyBillDomain;
import org.fms.cfs.server.webapp.cfm.handler.FilteringCFHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.riozenc.titanTool.spring.web.http.HttpResult;

import reactor.core.publisher.Mono;

/**
 * 电费月账单
 * 
 * @author czy
 *
 */
@ControllerAdvice
@RequestMapping("electricMonthlyBill")
public class ElectricMonthlyBillAction {

	@Autowired
	private FilteringCFHandler filteringECFHandler;
	@Autowired
	private FilteringCFHandler f1;
	
	@Autowired
	private FilteringCFHandler f2;

	@RequestMapping(params = "method=compute")
	@ResponseBody
	public Mono<?> compute(@RequestBody ElectricMonthlyBillDomain electricMonthlyBillDomain) {

		long time = System.currentTimeMillis();

		AtomicLong count = new AtomicLong();

		Map<Long, ECFModel> ecfModels = electricMonthlyBillDomain.getMeterDomains().parallelStream().map(m -> {
			ECFModel ecfModel = new ECFModel(m, electricMonthlyBillDomain.getDate().toString(),
					electricMonthlyBillDomain.getSn());
			return ecfModel;
		}).collect(Collectors.toMap(ECFModel::getMeterId, e -> e));

		CFModelExchange<ECFModel> exchange = new ECFModelExchange(electricMonthlyBillDomain.getDate(),
				electricMonthlyBillDomain.getSn(), ecfModels, electricMonthlyBillDomain.getCallback(),
				electricMonthlyBillDomain.getUserId());

		filteringECFHandler.handle(exchange).block();

		System.out.println(System.currentTimeMillis() - time);

		return Mono.just(new HttpResult(HttpResult.SUCCESS, count));
	}

	/**
	 * 鹤岗局内算法,所有计量点的有功电量和无功电量都汇总到一起(减少力调电费)
	 * 
	 * @param electricMonthlyBillDomain
	 * @return
	 */
	@RequestMapping(params = "method=computeInside")
	@ResponseBody
	public Mono<?> computeInside(@RequestBody ElectricMonthlyBillDomain electricMonthlyBillDomain) {

		AtomicLong count = new AtomicLong();

		Map<Long, ECFModel> ecfModels = electricMonthlyBillDomain.getMeterDomains().parallelStream().map(m -> {
			ECFModel ecfModel = new ECFModel(m, electricMonthlyBillDomain.getDate().toString(),
					electricMonthlyBillDomain.getSn());
			return ecfModel;
		}).collect(Collectors.toMap(ECFModel::getMeterId, e -> e));

		CFModelExchange<ECFModel> exchange = new ECFModelExchange(electricMonthlyBillDomain.getDate(),
				electricMonthlyBillDomain.getSn(), ecfModels, electricMonthlyBillDomain.getCallback(),
				electricMonthlyBillDomain.getUserId());

		f2.handle(exchange).block();

		return Mono.just(new HttpResult(HttpResult.SUCCESS, count));
	}
	
	
	/**
	 * 华润水费计算
	 * 
	 * @param electricMonthlyBillDomain
	 * @return
	 */
	@RequestMapping(params = "method=computeWater")
	@ResponseBody
	public Mono<?> computeWater(){
		
		return null;
	}
	
	/**
	 * 华润蒸汽费计算
	 * 
	 * @param electricMonthlyBillDomain
	 * @return
	 */
	@RequestMapping(params = "method=computeSteam")
	@ResponseBody
	public Mono<?> computeSteam(){
		return null;
	}
}
