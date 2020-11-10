/**
 * Author : czy
 * Date : 2019年10月16日 下午2:29:56
 * Title : com.riozenc.cfs.webapp.mrm.filter.PriceLadderInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.systemInit;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.SystemInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.PriceLadderRelaDomain;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;
import com.riozenc.titanTool.spring.web.http.HttpResultPagination;

import reactor.core.publisher.Mono;

public class PriceLadderInitFilter implements SystemInitFilter, MongoDAOSupport {
	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public int getOrder() {
		return 21;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();
		Map<String, String> values = new HashMap<>();
		values.put("pageSize", "-1");

		try {
			HttpResultPagination<PriceLadderRelaDomain> httpResultPagination = titanTemplate.postJson("BILLING-SERVER",
					"billingServer/priceLadderRela/get", new HttpHeaders(), values,
					new TypeReference<HttpResultPagination<PriceLadderRelaDomain>>() {
					});

			if (httpResultPagination.getList().size() == 0) {
				systemInitModel.addExecuteResult("执行电价未定义，数量为0，请检查.");
				return filterChain.filter(exchange);
			}
			List<WriteModel<Document>> updateResult = updateMany(toDocuments(httpResultPagination.getList()),
					new MongoUpdateFilter() {
						@Override
						public Bson filter(Document param) {
							// TODO Auto-generated method stub
							return Filters.eq("id", param.get("id"));
						}
					}, true);
			BulkWriteResult bulkWriteResult = getCollection(systemInitModel.getDate(),
					MongoCollectionConfig.PRICE_LADDER_RELA.name()).bulkWrite(updateResult);

			systemInitModel.addExecuteResult("电价阶梯：" + bulkWriteResult.getModifiedCount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			systemInitModel.addExecuteResult(e.getMessage());
		}

		return filterChain.filter(exchange);
	}

}
