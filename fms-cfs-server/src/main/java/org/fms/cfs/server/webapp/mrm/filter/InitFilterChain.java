/**
 * Author : czy
 * Date : 2019年6月26日 上午10:00:18
 * Title : com.riozenc.cfs.webapp.mrm.filter.InitFilterChain.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter;

import org.fms.cfs.common.model.exchange.InitModelExchange;

import reactor.core.publisher.Mono;

public interface InitFilterChain {
	Mono<Void> filter(InitModelExchange exchange);
}
