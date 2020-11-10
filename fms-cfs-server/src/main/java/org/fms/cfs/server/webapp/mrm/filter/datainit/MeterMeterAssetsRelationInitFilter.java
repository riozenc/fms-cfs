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
import org.fms.cfs.common.webapp.domain.MeterMeterAssetsRelDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 计量点与电能表关系
 * 
 * @author czy
 *
 */
public class MeterMeterAssetsRelationInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub

		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		if (billingDataInitModel.getMeterMeterAssetsRelDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("计量点与电能表关系数据为0,请检查计量点与电能表关系.");
			return filterChain.filter(exchange);
		}

		//？感觉没用
//		List<WriteModel<Document>> updateWriteFilesWriteSnByMeterAssets = updateMany2(
//				billingDataInitModel.getMeterMeterAssetsRelDomains().stream().filter(mma -> mma.getFunctionCode() == 1)
//						.collect(Collectors.toList()),
//				new MongoUpdateFilter2<MeterMeterAssetsRelDomain>() {
//
//					@Override
//					public Bson filter(MeterMeterAssetsRelDomain t) {
//						return Filters.eq("meterAssetsId", t.getMeterAssetsId());
//					}
//
//					@Override
//					public Document setUpdate(MeterMeterAssetsRelDomain t) {
//						return new Document().append("writeSn", t.getWriteSn());
//					}
//
//				}, false);

		List<WriteModel<Document>> meterMongoList = updateMany(
				toDocuments(billingDataInitModel.getMeterMeterAssetsRelDomains(),
						new ToDocumentCallBack<MeterMeterAssetsRelDomain>() {
							@Override
							public MeterMeterAssetsRelDomain call(MeterMeterAssetsRelDomain t) {
								t.set_id(t.getId());
								return t;
							}
						}),
				new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
						return Filters.eq("_id", param.get("_id"));
					}
				}, true);

		BulkWriteResult meterWriteResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.METER_METER_ASSETS_REL.name()).bulkWrite(meterMongoList);

//		if (updateWriteFilesWriteSnByMeterAssets.size() != 0) {
//			BulkWriteResult meterWriteResult1 = getCollection(billingDataInitModel.getDate(),
//					MongoCollectionConfig.WRITE_FILES.name()).bulkWrite(updateWriteFilesWriteSnByMeterAssets);
//		}

		billingDataInitModel
				.addExecuteResult("计量点与电能表关系：" + (meterMongoList.size() - meterWriteResult.getModifiedCount()));
		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 22;
	}

}
