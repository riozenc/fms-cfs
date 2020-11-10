/**
 * Author : czy
 * Date : 2019年6月14日 上午9:31:44
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.SumMoneyFilter.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 最后合计费用
 * 
 * @author czy
 *
 */
public class SumMoneyFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(SumMoneyFilter.class);

	@Override
	public int getOrder() {

		return 100;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理合计...");
		Map<Long, ECFModel> ecfModelMap = exchange.getModels();

		// 计算电费
		ecfModelMap.values().parallelStream().forEach(e -> {
			e.computeAmount();
		});

		return filterChain.filter(exchange);
	}

}
