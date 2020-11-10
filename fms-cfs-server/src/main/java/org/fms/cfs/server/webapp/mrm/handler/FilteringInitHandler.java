/**
 * Author : czy
 * Date : 2019年6月26日 上午9:58:53
 * Title : com.riozenc.cfs.webapp.mrm.handler.FilteringInitHandler.java
 *
**/
package org.fms.cfs.server.webapp.mrm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.server.webapp.mrm.filter.InitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import reactor.core.publisher.Mono;

public class FilteringInitHandler {
	protected static final Log logger = LogFactory.getLog(FilteringInitHandler.class);
	private final List<InitFilter> initFilters;

	public FilteringInitHandler(List<? extends InitFilter> initFilters) {

		List<InitFilter> asd = initFilters.stream().map(filter -> {
			InitFilterAdapter calculationFeeFilterAdapter = new InitFilterAdapter(filter);
			return calculationFeeFilterAdapter;
		}).collect(Collectors.toList());

		this.initFilters = asd;
	}

	public Mono<Void> handle(InitModelExchange exchange) {
		List<InitFilter> combined = new ArrayList<>(this.initFilters);
		// TODO: needed or cached?
		AnnotationAwareOrderComparator.sort(combined);

		if (logger.isDebugEnabled()) {
			logger.debug("Sorted gatewayFilterFactories: " + combined);
		}

		return new DefaultInitFilterChain(combined).filter(exchange);
	}

	private static class DefaultInitFilterChain implements InitFilterChain {

		private final int index;
		private final List<InitFilter> filters;

		public DefaultInitFilterChain(List<InitFilter> filters) {
			this.filters = filters;
			this.index = 0;
		}

		private DefaultInitFilterChain(DefaultInitFilterChain parent, int index) {
			this.filters = parent.getFilters();
			this.index = index;
		}

		public List<InitFilter> getFilters() {
			return filters;
		}

		@Override
		public Mono<Void> filter(InitModelExchange exchange) {
			return Mono.defer(() -> {
				if (this.index < filters.size()) {
					InitFilter filter = filters.get(this.index);
					DefaultInitFilterChain chain = new DefaultInitFilterChain(this, this.index + 1);
					return filter.filter(exchange, chain);
				} else {
					return Mono.empty(); // complete
				}
			});
		}

	}

	private static class InitFilterAdapter implements InitFilter {

		private final InitFilter delegate;

		public InitFilterAdapter(InitFilter delegate) {
			this.delegate = delegate;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("GatewayFilterAdapter{");
			sb.append("delegate=").append(delegate);
			sb.append('}');
			return sb.toString();
		}

		@Override
		public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
			long time = System.currentTimeMillis();
			try {

				return this.delegate.filter(exchange, filterChain);
			} finally {
				logger.info(this.delegate.getClass() + "[" + this.delegate.getOrder() + "]" + "执行时间"
						+ (System.currentTimeMillis() - time));
			}
		}

		@Override
		public int getOrder() {
			// TODO Auto-generated method stub
			return 0;
		}
	}
}
