/**
 * Author : czy
 * Date : 2019年4月13日 下午3:26:00
 * Title : com.riozenc.cfs.webapp.cfm.handler.CalculationFeeHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter;

import org.fms.cfs.common.model.CFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.springframework.core.Ordered;

import reactor.core.publisher.Mono;

/**
 * 借用spring的Ordered进行filter排序（AnnotationAwareOrderComparator）
 * 
 * @author czy
 *
 */
public interface CalculationFeeFilter<T extends CFModel> extends Ordered {

	Mono<Void> filter(CFModelExchange<T> exchange, CFFilterChain filterChain);

}
