/**
 * Author : czy
 * Date : 2019年11月1日 下午4:17:09
 * Title : com.riozenc.cfs.webapp.mrm.filter.InitedFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

public class InitedFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {

		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();
		// 完成抄表初始化，修改计量点的状态

		List<WriteModel<Document>> writeModels = updateMany(
				toDocuments(billingDataInitModel.getMeterDomains(), new ToDocumentCallBack<MeterDomain>() {

					@Override
					public MeterDomain call(MeterDomain t) {
						t.setStatus(MeterDomain.INITED);
						return t;
					}

				}), new MongoUpdateFilter() {

					@Override
					public Bson filter(Document param) {
						// TODO Auto-generated method stub
						return Filters.and(Filters.eq("id", param.get("id")), Filters.eq("sn", param.get("sn")));
					}
				}, false);

		BulkWriteResult bulkWriteResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.ELECTRIC_METER.name()).bulkWrite(writeModels);

		billingDataInitModel.addExecuteResult("计量点状态修改数量:" + bulkWriteResult.getMatchedCount());

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
