/**
 * Author : czy
 * Date : 2019年6月26日 下午7:11:06
 * Title : com.riozenc.cfs.webapp.mrm.filter.BemChangeInitFilter.java
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
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterReplaceDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 业务变更
 * 
 * @author czy
 *
 */
public class BemChangeInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		deleteMany(
				getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()),
				new MongoDeleteFilter() {

					@Override
					public Bson filter() {
						return Filters.in("meterId", billingDataInitModel.getMeterDomains().stream()
								.map(MeterDomain::getId).collect(Collectors.toList()));
					}
				});

		// 换表记录

		List<WriteModel<Document>> updateResult = updateMany(toDocuments(billingDataInitModel.getMeterReplaceDomains(),
				new ToDocumentCallBack<MeterReplaceDomain>() {

					@Override
					public MeterReplaceDomain call(MeterReplaceDomain t) {
						t.createObjectId();
						return t;
					}

				}), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
						// TODO Auto-generated method stub
						return Filters.eq("_id", param.get("_id"));
					}
				}, true);

		if (updateResult.isEmpty()) {
			billingDataInitModel.addExecuteResult("换表记录：0");
			return filterChain.filter(exchange);
		}

		BulkWriteResult bulkWriteResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.ELECTRIC_METER_REPLACE.name()).bulkWrite(updateResult);
		billingDataInitModel.addExecuteResult("换表记录：" + (updateResult.size() - bulkWriteResult.getModifiedCount()));
		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 30;
	}

}
