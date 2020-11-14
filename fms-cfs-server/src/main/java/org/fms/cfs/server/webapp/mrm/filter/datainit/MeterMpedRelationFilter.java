/**
 * Author : chizf
 * Date : 2020年11月14日 下午3:25:11
 * Title : org.fms.cfs.server.webapp.mrm.filter.datainit.MeterMpedRelationFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.MeterMpedRelDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 计量点与计费点关系（原计量点与资产关系）
 * 
 * @author czy
 *
 */
public class MeterMpedRelationFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {

		// TODO Auto-generated method stub

		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		if (billingDataInitModel.getMeterMpedRelDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("计量点与计费点关系数据为0,请检查计量点与计费点关系数据.");
			return filterChain.filter(exchange);
		}

		List<WriteModel<Document>> meterMongoList = updateMany(toDocuments(
				billingDataInitModel.getMeterMpedRelDomains(), new ToDocumentCallBack<MeterMpedRelDomain>() {
					@Override
					public MeterMpedRelDomain call(MeterMpedRelDomain t) {
						t.set_id(t.getId());
						return t;
					}
				}), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
						return Filters.eq("_id", param.get("_id"));
					}
				}, true);

		BulkWriteResult meterWriteResult = getCollection(
				getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.METER_MPED_REL.name()))
						.bulkWrite(meterMongoList);

		billingDataInitModel
				.addExecuteResult("计量点与计费点关系：" + (meterMongoList.size() - meterWriteResult.getModifiedCount()));
		return filterChain.filter(exchange);

	}

	@Override
	public int getOrder() {
		return 22;
	}

}
