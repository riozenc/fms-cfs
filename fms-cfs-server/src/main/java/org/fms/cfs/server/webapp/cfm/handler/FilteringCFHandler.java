/**
 * Author : czy
 * Date : 2019年6月12日 下午4:54:16
 * Title : com.riozenc.cfs.webapp.cfm.handler.FilteringCFHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.fms.cfs.common.model.CFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.CalculationFeeFilter;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import reactor.core.publisher.Mono;

public class FilteringCFHandler {
	protected static final Log logger = LogFactory.getLog(FilteringCFHandler.class);
	
	private final List<CalculationFeeFilter<? extends CFModel>> calculationFeeFilters;

	public FilteringCFHandler(List<? extends CalculationFeeFilter<? extends CFModel>> calculationFeeFilters) {
		this.calculationFeeFilters = loadFilters(calculationFeeFilters);
	}

	private static List<CalculationFeeFilter<? extends CFModel>> loadFilters(
			List<? extends CalculationFeeFilter<? extends CFModel>> filters) {
		return filters.stream().map(filter -> {
			CalculationFeeFilterAdapter calculationFeeFilterAdapter = new CalculationFeeFilterAdapter(filter);
			return calculationFeeFilterAdapter;
		}).collect(Collectors.toList());
	}

	public Mono<Void> handle(CFModelExchange<? extends CFModel> exchange) {
		List<CalculationFeeFilter<? extends CFModel>> combined = new ArrayList<>(this.calculationFeeFilters);
		// TODO: needed or cached?
		AnnotationAwareOrderComparator.sort(combined);

		if (logger.isDebugEnabled()) {
			logger.debug("Sorted gatewayFilterFactories: " + combined);
		}

		return new DefaultCFFilterChain(combined).filter(exchange);
	}

	private static class DefaultCFFilterChain implements CFFilterChain {

		private final int index;
		private final List<CalculationFeeFilter<? extends CFModel>> filters;

		public DefaultCFFilterChain(List<CalculationFeeFilter<? extends CFModel>> filters) {
			this.filters = filters;
			this.index = 0;
		}

		private DefaultCFFilterChain(DefaultCFFilterChain parent, int index) {
			this.filters = parent.getFilters();
			this.index = index;
		}

		public List<CalculationFeeFilter<? extends CFModel>> getFilters() {
			return filters;
		}

		@SuppressWarnings("unchecked")
		@Override
		public Mono<Void> filter(CFModelExchange exchange) {
			return Mono.defer(() -> {
				if (this.index < filters.size()) {
					CalculationFeeFilter filter = filters.get(this.index);
					DefaultCFFilterChain chain = new DefaultCFFilterChain(this, this.index + 1);
					return filter.filter(exchange, chain);
				} else {
					return Mono.empty(); // complete
				}
			});
		}
	}

	private static class CalculationFeeFilterAdapter implements CalculationFeeFilter<CFModel> {

		private final CalculationFeeFilter<? extends CFModel> delegate;

		public CalculationFeeFilterAdapter(CalculationFeeFilter<? extends CFModel> filter) {
			this.delegate = filter;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder("GatewayFilterAdapter{");
			sb.append("delegate=").append(delegate);
			sb.append('}');
			return sb.toString();
		}

		@Override
		public Mono<Void> filter(CFModelExchange exchange, CFFilterChain filterChain) {
			long time =System.currentTimeMillis();
			try {
				return this.delegate.filter(exchange, filterChain);
			} finally {
				System.out.println(this.delegate + "[" + this.delegate.getOrder() + "]" + "执行时间"
						+ (System.currentTimeMillis() - time));
			}
		}

		@Override
		public int getOrder() {
			return 0;
		}
	}
}
