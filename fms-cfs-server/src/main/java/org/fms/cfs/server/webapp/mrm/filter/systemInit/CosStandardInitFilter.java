/**
 * Author : czy
 * Date : 2019年9月20日 下午3:13:32
 * Title : com.riozenc.cfs.webapp.mrm.filter.CosStandardInitFilter.java
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
import org.fms.cfs.common.webapp.domain.CosStandardDomain;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.type.TypeReference;
import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import reactor.core.publisher.Mono;

/**
 * 力率标准初始化
 * 
 * @author czy
 *
 */
public class CosStandardInitFilter implements SystemInitFilter, MongoDAOSupport {
	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		Map<String, String> values = new HashMap<>();
		values.put("status", "1");
		List<CosStandardDomain> cosStandardDomains;
		try {
			cosStandardDomains = titanTemplate.postJson("BILLING-SERVER", "billingServer/CosStandardConfig/select",
					null, values, new TypeReference<List<CosStandardDomain>>() {
					});
			if(cosStandardDomains.size()==0) {
				
				systemInitModel.addExecuteResult("力率标准 数据为空，请检查.");
				return filterChain.filter(exchange);
			}
			List<WriteModel<Document>> updateResult = updateMany(toDocuments(cosStandardDomains),
					new MongoUpdateFilter() {
						@Override
						public Bson filter(Document param) {
							// TODO Auto-generated method stub
							return Filters.eq("id", param.get("id"));
						}
					}, true);
			BulkWriteResult bulkWriteResult = getCollection(systemInitModel.getDate(),
					MongoCollectionConfig.COS_STANDARD_CONFIG.name()).bulkWrite(updateResult);

			systemInitModel.addExecuteResult("力率标准：" + bulkWriteResult.getModifiedCount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			systemInitModel.addExecuteResult(e.getMessage());
		}

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 30;
	}

}
