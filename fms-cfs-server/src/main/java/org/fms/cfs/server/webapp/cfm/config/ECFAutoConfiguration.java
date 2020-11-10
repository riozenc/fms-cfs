/**
 * Author : czy
 * Date : 2019年6月12日 下午4:56:48
 * Title : com.riozenc.cfs.webapp.cfm.config.ECFAutoConfiguration.java
 *
**/
package org.fms.cfs.server.webapp.cfm.config;

import java.util.List;

import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.BasicChargesFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.ComputedFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.IntegrityCheckFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.LadderFilter2;
import org.fms.cfs.server.webapp.cfm.filter.e.MeterRelationFilter2;
import org.fms.cfs.server.webapp.cfm.filter.e.PersistenceFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.PowerCostFilter2;
import org.fms.cfs.server.webapp.cfm.filter.e.PowerRateFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.SumMoneyFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.TransformerLossFilter;
import org.fms.cfs.server.webapp.cfm.filter.e.WritePowerFilter;
import org.fms.cfs.server.webapp.cfm.handler.FilteringCFHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ECFAutoConfiguration {

	@Bean
	public FilteringCFHandler filteringECFHandler(List<EcfFilter> calculationFeeFilters) {
		ECFModel.FILTER_NUMBER = calculationFeeFilters.size();// filter的数量
		return new FilteringCFHandler(calculationFeeFilters);
	}

	@Bean
	public IntegrityCheckFilter integrityCheckFilter() {
		return new IntegrityCheckFilter();
	}

	@Bean
	public WritePowerFilter writePowerFilter() {
		return new WritePowerFilter();
	}

	@Bean
	public WritePowerFilter writePowerFilter2() {
		return new WritePowerFilter();
	}

	@Bean
	public MeterRelationFilter2 meterRelFilter() {
		return new MeterRelationFilter2();
	}

	@Bean
	public TransformerLossFilter transformerLossFilter() {
		return new TransformerLossFilter();
	}

	/**
	 * 鹤岗无线损分摊
	 * 
	 * @return
	 */
//	@Bean
//	public LineLossFilter lineLossFilter() {
//		return new LineLossFilter();
//	}

	@Bean
	public PowerCostFilter2 powerCostFilter() {
		return new PowerCostFilter2();
	}

	@Bean
	public BasicChargesFilter basicChargesFilter() {
		return new BasicChargesFilter();
	}

	// 70
	@Bean
	public PowerRateFilter powerRateFilter() {
		return new PowerRateFilter();
	}

	// 80
	@Bean
	public LadderFilter2 ladderFilter() {
		return new LadderFilter2();
	}

	// 100
	@Bean
	public SumMoneyFilter sumMoneyFilter() {
		return new SumMoneyFilter();
	}

	// MAX-100
	@Bean
	public PersistenceFilter persistenceFilter() {
		return new PersistenceFilter();
	}

	// MAX
	@Bean
	public ComputedFilter computedFilter() {
		return new ComputedFilter();
	}

}
