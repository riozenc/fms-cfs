/**
 * Author : czy
 * Date : 2019年11月1日 下午4:37:27
 * Title : com.riozenc.cfs.webapp.cfm.filter.e.ComputedFilter.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.MeterDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

public class ComputedFilter implements EcfFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {

//		List<MeterDomain> meterDomains = exchange.getModels().values().stream().filter(e -> e.getRemark().isEmpty())
		List<MeterDomain> meterDomains = exchange.getModels().values().stream().map(e -> {

			MeterDomain meterDomain = new MeterDomain();
			meterDomain.setId(e.getMeterId());
			meterDomain.setSn(e.getSn());

			if (e.isComplete()) {
				meterDomain.setStatus(MeterDomain.COMPUTED);
			} else {
				meterDomain.setStatus(MeterDomain.UN_COMPUTED);
			}

			return meterDomain;
		}).collect(Collectors.toList());

		List<WriteModel<Document>> writeModels = updateMany(toDocuments(meterDomains), new MongoUpdateFilter() {

			@Override
			public Bson filter(Document param) {
				return Filters.and(Filters.eq("id", param.get("id")), Filters.eq("sn", param.get("sn")));
			}
		}, false);

		if (writeModels.size() == 0) {
			exchange.writeComputeLog("合计处理阶段数据量=0");
			return filterChain.filter(exchange);
		}

		BulkWriteResult bulkWriteResult = getCollection(exchange.getDate().toString(),
				MongoCollectionConfig.ELECTRIC_METER.name()).bulkWrite(writeModels);

		exchange.writeComputeLog(
				"计量点匹配" + bulkWriteResult.getMatchedCount() + "个,计量点状态更新" + bulkWriteResult.getModifiedCount());

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return LOWEST_PRECEDENCE;
	}

}
