/**
 * Author : czy
 * Date : 2019年10月25日 下午7:28:50
 * Title : com.riozenc.cfs.webapp.mrm.filter.MeterMeterAssetsRelationInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 计量点与互感器关系
 * 
 * @author czy
 *
 */
public class MeterInductorAssetsRelationInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub

		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		if (billingDataInitModel.getMeterInductorAssetsRelDomains() == null
				|| billingDataInitModel.getMeterInductorAssetsRelDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("计量点与互感器关系数据为0,请检查计量点与互感器关系.");
			return filterChain.filter(exchange);
		}

		List<WriteModel<Document>> meterMongoList = updateMany(
				toDocuments(billingDataInitModel.getMeterInductorAssetsRelDomains()), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
						// TODO Auto-generated method stub
						return Filters.eq("id", param.get("id"));
					}
				}, true);

		BulkWriteResult meterWriteResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.METER_INDUCTOR_ASSETS_REL.name()).bulkWrite(meterMongoList);

		billingDataInitModel
				.addExecuteResult("计量点与互感器关系：" + (meterMongoList.size() - meterWriteResult.getModifiedCount()));
		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 23;
	}

}
