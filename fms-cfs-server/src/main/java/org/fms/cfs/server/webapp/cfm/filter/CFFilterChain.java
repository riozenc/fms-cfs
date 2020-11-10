/**
 * Author : czy
 * Date : 2019年6月12日 下午6:44:03
 * Title : com.riozenc.cfs.webapp.cfm.filter.CFFilterChain.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter;

import org.fms.cfs.common.model.CFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;

import reactor.core.publisher.Mono;

public interface CFFilterChain {
	Mono<Void> filter(CFModelExchange<? extends CFModel> exchange);
}
