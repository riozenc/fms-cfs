/**
 * Author : czy
 * Date : 2019年7月3日 上午10:45:10
 * Title : com.riozenc.cfs.webapp.mrm.filter.MeterRelInitFilter.java
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
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.common.webapp.domain.MeterRelationDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 计量点关系
 * 
 * @author czy
 *
 */
public class MeterRelationInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	// TODO 根据pMeterId进行删除，再进行insert？？？

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		if (billingDataInitModel.getMeterRelationDomains() == null
				|| billingDataInitModel.getMeterRelationDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("电计量点关系数据为0,请检查电计量点档案.");
			return filterChain.filter(exchange);
		}

		Map<Long, MeterDomain> meterMap = billingDataInitModel.getMeterDomains().stream()
				.collect(Collectors.toMap(MeterDomain::getId, m -> m, (k, v) -> k));

		// 删除
		deleteMany(getCollectionName(billingDataInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_REL.name()),
				new MongoDeleteFilter() {
					@Override
					public Bson filter() {
						return Filters.in("pMeterId", meterMap.keySet());
					}
				});

		List<WriteModel<Document>> meterRelMongoList = updateMany(toDocuments(
				billingDataInitModel.getMeterRelationDomains(), new ToDocumentCallBack<MeterRelationDomain>() {
					@Override
					public MeterRelationDomain call(MeterRelationDomain t) {
						if (meterMap.get(t.getpMeterId()) != null)
							t.setpMeterRateFlag(meterMap.get(t.getpMeterId()).getTsType());
						if (meterMap.get(t.getMeterId()) != null)
							t.setMeterRateFlag(meterMap.get(t.getMeterId()).getTsType());
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

		BulkWriteResult meterRelResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.ELECTRIC_METER_REL.name()).bulkWrite(meterRelMongoList);

		billingDataInitModel.addExecuteResult("计量点关系："
				+ (meterRelMongoList.size() - meterRelResult.getModifiedCount() + meterRelResult.getInsertedCount()));
		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 21;
	}

}
