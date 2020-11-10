/**
 * Author : czy
 * Date : 2019年6月26日 下午2:19:52
 * Title : com.riozenc.cfs.webapp.mrm.filter.PriceInitFilter.java
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
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import reactor.core.publisher.Mono;

public class PriceInitFilter implements SystemInitFilter, MongoDAOSupport {
	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 20;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		// 电价
		try {

			Map<String, String> values = new HashMap<>();
			values.put("pageSize", "-1");
			List<PriceExecutionDomain> priceExecutionDomains = titanTemplate.postJson("BILLING-SERVER",
					"billingServer/priceExecution?method=getPriceExecutionInfo", null, values,
					new TypeReference<List<PriceExecutionDomain>>() {
					});

			systemInitModel.setPriceExecutionDomains(priceExecutionDomains);

			if (priceExecutionDomains.size() == 0) {
				systemInitModel.addExecuteResult("执行电价未定义，数量为0，请检查.");
				return filterChain.filter(exchange);
			}

			deleteMany(getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.PRICE_EXECUTION.name()),
					new MongoDeleteFilter() {
						@Override
						public Bson filter() {
							return new Document();
						}
					});

			List<WriteModel<Document>> insertResult = insertMany(toDocuments(priceExecutionDomains));

			BulkWriteResult bulkWriteResult = getCollection(systemInitModel.getDate(),
					MongoCollectionConfig.PRICE_EXECUTION.name()).bulkWrite(insertResult);

			systemInitModel.addExecuteResult("电价：" + bulkWriteResult.getModifiedCount());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			systemInitModel.addExecuteResult(e.getMessage());
		}

		return filterChain.filter(exchange);
	}

}
