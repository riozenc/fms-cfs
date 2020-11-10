/**
 * Author : czy
 * Date : 2019年4月13日 上午9:44:20
 * Title : com.riozenc.cfs.webapp.mrm.action.SystemInitAction.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.action;

import org.fms.cfs.common.model.InitModel;
import org.fms.cfs.common.model.SystemInitModel;
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
 * 系统初始化 -系统初始化
 * 
 * @author czy
 *
 */
@ControllerAdvice
@RequestMapping("electricSystemInit")
public class ElectricSystemInitAction {

	private static final Logger logger = LoggerFactory.getLogger(ElectricSystemInitAction.class);

	@Autowired
	private FilteringInitHandler filteringSystemHandler;

	@RequestMapping(params = "method=initializeMany")
	@ResponseBody
	public Mono<?> initializeMany(@RequestBody SystemInitModel systemInitModel) {

		InitModelExchange initModelExchange = new InitModelExchange() {
			@Override
			public InitModel getModel() {
				// TODO Auto-generated method stub
				return systemInitModel;
			}

			@Override
			public String getExecuteResult() {
				// TODO Auto-generated method stub
				return getModel().getExecuteResult();
			}
		};

		try {
			filteringSystemHandler.handle(initModelExchange).block();
			return Mono.just(new HttpResult<String>(HttpResult.SUCCESS, initModelExchange.getExecuteResult(), null));
		} catch (Exception e) {
			e.printStackTrace();
			return Mono.just(new HttpResult<String>(HttpResult.ERROR, e.getMessage(), null));
		}

	}

}
