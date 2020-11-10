/**
 * Author : chizf
 * Date : 2020年8月12日 下午6:37:07
 * Title : com.riozenc.cfs.webapp.mrm.filter.SettlementMeterRelInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.SettlementMeterRelDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 结算户与计量点关系
 * 
 * @author czy
 *
 */
public class SettlementMeterRelInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {

		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		List<SettlementMeterRelDomain> settlementMeterRelDomains = billingDataInitModel.getSettlementMeterRelDomains();

		if (settlementMeterRelDomains.isEmpty()) {
			billingDataInitModel.addExecuteResult("结算户与计量点关系数量为0，请检查.");
			return filterChain.filter(exchange);
		}

		deleteMany(getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.SETTLEMENT_METER_REL.name()),
				new MongoDeleteFilter() {

					@Override
					public Bson filter() {
						return Filters.in("settlementId", settlementMeterRelDomains.stream()
								.map(SettlementMeterRelDomain::getSettlementId).collect(Collectors.toList()));
					}
				});

		List<WriteModel<Document>> insertModels = insertMany(toDocuments(settlementMeterRelDomains));

		BulkWriteResult writeResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.SETTLEMENT_METER_REL.name()).bulkWrite(insertModels);

		System.out.println(writeResult.getInsertedCount());

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 11;
	}

}
