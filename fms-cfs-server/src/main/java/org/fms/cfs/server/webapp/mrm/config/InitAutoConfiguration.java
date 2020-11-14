/**
 * Author : czy
 * Date : 2019年6月26日 下午2:10:28
 * Title : com.riozenc.cfs.webapp.mrm.config.InitAutoConfiguration.java
 *
**/
package org.fms.cfs.server.webapp.mrm.config;

import java.util.List;

import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.ArchivesInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.BemChangeInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.InitedFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.MeterInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.MeterMpedRelationFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.MeterRelationInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.SettlementMeterRelInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.TransformerInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.TransformerMeterRelationFilter;
import org.fms.cfs.server.webapp.mrm.filter.datainit.WriteFilesInitFilter2;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.CosStandardInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.DeptMonInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.MongoCollectionFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.PriceInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.PriceLadderInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.SystemCommonParamInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.systemInit.WriteSectInitFilter;
import org.fms.cfs.server.webapp.mrm.handler.FilteringInitHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class InitAutoConfiguration {
	@Bean
	public FilteringInitHandler filteringSystemHandler(List<SystemInitFilter> initFilters) {

		return new FilteringInitHandler(initFilters);
	}

	@Bean
	public FilteringInitHandler filteringBillingDateHandler(List<BillingDataInitFilter> initFilters) {
		return new FilteringInitHandler(initFilters);
	}

	@Bean
	public SystemInitFilter deptMonInitFilter() {
		return new DeptMonInitFilter();
	}

	@Bean
	public SystemInitFilter systemCommonParamInitFilter() {
		return new SystemCommonParamInitFilter();
	}

	@Bean
	public SystemInitFilter writeSectInitFilter() {
		return new WriteSectInitFilter();
	}

	@Bean
	public SystemInitFilter priceInitFilter() {
		return new PriceInitFilter();
	}

	@Bean
	public SystemInitFilter priceLadderInitFilter() {
		return new PriceLadderInitFilter();
	}

	@Bean
	public SystemInitFilter cosStandardInitFilter() {
		return new CosStandardInitFilter();
	}

	@Bean
	public SystemInitFilter mongoCollectionFilter() {
		return new MongoCollectionFilter();
	}

	@Bean
	public BillingDataInitFilter archivesInitFilter() {
		return new ArchivesInitFilter();
	}

	@Bean
	public BillingDataInitFilter meterInitFilter() {
		return new MeterInitFilter();
	}

	@Bean
	public BillingDataInitFilter meterRelInitFilter() {
		return new MeterRelationInitFilter();
	}

	@Bean
	public BillingDataInitFilter meterMpedRelationFilter() {
		return new MeterMpedRelationFilter();
	}

	@Bean
	public SettlementMeterRelInitFilter settlementMeterRelInitFilter() {
		return new SettlementMeterRelInitFilter();
	}

	@Bean
	public BillingDataInitFilter bemChangeInitFilter() {
		return new BemChangeInitFilter();
	}

	@Bean
	public BillingDataInitFilter writeFilesInitFilter2() {
		return new WriteFilesInitFilter2();
	}

//	@Bean
//	public BillingDataInitFilter writeFilesInitFilter1() {
//		return new WriteFilesInitFilter1();
//	}

	@Bean
	public BillingDataInitFilter transformerInitFilter() {
		return new TransformerInitFilter();
	}

	@Bean
	public BillingDataInitFilter transformerMeterRelationFilter() {
		return new TransformerMeterRelationFilter();
	}

	public BillingDataInitFilter computedFilter() {
		return new InitedFilter();
	}
}
