/**
 * Author : czy
 * Date : 2019年8月24日 下午1:44:48
 * Title : com.riozenc.cfs.webapp.mrm.filter.TransformerMeterRelationFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 变压器与计量点关系
 * 
 * @author czy
 *
 */
public class TransformerMeterRelationFilter implements BillingDataInitFilter, MongoDAOSupport {
	@Override
	public int getOrder() {
		return 51;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		Map<Long, TransformerDomain> transformerMap = billingDataInitModel.getTransformerDomains().stream()
				.collect(Collectors.toMap(TransformerDomain::getId, v -> v, (k, v) -> {

					return v;
				}));

		Map<Long, TransformerMeterRelationDomain> transformerMeterRelationMap = billingDataInitModel
				.getTransformerMeterRelationDomains().stream()
				.collect(Collectors.toMap(TransformerMeterRelationDomain::getId, v -> v, (k, v) -> {
					return v;
				}));

		long delteCount = deleteMany(
				getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoDeleteFilter() {
					@Override
					public Bson filter() {
						return Filters.in("meterId", billingDataInitModel.getTransformerMeterRelationDomains().stream()
								.map(TransformerMeterRelationDomain::getMeterId).collect(Collectors.toList()));
					}
				});

		List<WriteModel<Document>> writeModels = insertMany(toDocuments(transformerMeterRelationMap.values(),
				new ToDocumentCallBack<TransformerMeterRelationDomain>() {
					@Override
					public TransformerMeterRelationDomain call(TransformerMeterRelationDomain t) {
						t.setIsPubType(transformerMap.get(t.getTransformerId()).getIsPubType());
						t.createObjectId();
						return t;
					}
				}));

		if (writeModels == null || writeModels.size() == 0) {
			billingDataInitModel.addExecuteResult("变压器与计量点关系：0");
		} else {
			BulkWriteResult bulkWriteResult = getCollection(billingDataInitModel.getDate(),
					MongoCollectionConfig.TRANSFORMER_METER_REL.name()).bulkWrite(writeModels);
			billingDataInitModel.addExecuteResult(
					"变压器与计量点关系：" + delteCount + "->" + (writeModels.size() - bulkWriteResult.getModifiedCount()));
		}

		return filterChain.filter(exchange);
	}

}
