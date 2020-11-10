/**
 * Author : czy
 * Date : 2019年6月26日 上午9:59:38
 * Title : com.riozenc.cfs.webapp.mrm.filter.InitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter;

import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.springframework.core.Ordered;

import reactor.core.publisher.Mono;

public interface InitFilter extends Ordered {
	Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain);
}
