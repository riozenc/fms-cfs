/**
 * Author : czy
 * Date : 2019年6月26日 下午2:12:37
 * Title : com.riozenc.cfs.webapp.mrm.filter.WriteSectInitFilter.java
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
import org.fms.cfs.common.webapp.domain.WriteSectDomain;
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

import reactor.core.publisher.Mono;

/**
 * 抄表区段初始化
 * 
 * @author czy
 *
 */
public class WriteSectInitFilter implements SystemInitFilter, MongoDAOSupport {
	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub

		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		Map<String, String> writeSectParams = new HashMap<>();
		writeSectParams.put("status", "1");
		writeSectParams.put("pageSize", "-1");
		// 抄表区段
		try {

			List<WriteSectDomain> writeSectDomains = titanTemplate.postJson("CIM-SERVER",
					"cimServer/writeSect?method=getAllWriteSect", new HttpHeaders(), writeSectParams,
					new TypeReference<List<WriteSectDomain>>() {
					});

			systemInitModel.setWriteSectDomains(writeSectDomains);
			if (writeSectDomains.size() == 0) {
				systemInitModel.addExecuteResult("抄表区段数量为0，请检查.");
				return filterChain.filter(exchange);
			}

			List<WriteModel<Document>> updateResult = updateMany(
					toDocuments(writeSectDomains, new ToDocumentCallBack<WriteSectDomain>() {

						@Override
						public WriteSectDomain call(WriteSectDomain t) {
							// TODO Auto-generated method stub
							t.setMon(systemInitModel.getDate());
							return t;
						}

					}), new MongoUpdateFilter() {

						@Override
						public Bson filter(Document param) {
							// TODO Auto-generated method stub
							return Filters.eq("id", param.get("id"));
						}

					}, true);
			BulkWriteResult bulkWriteResult = getCollection(systemInitModel.getDate(),
					MongoCollectionConfig.WRITE_SECT.name()).bulkWrite(updateResult);

			systemInitModel.addExecuteResult("抄表区段：" + bulkWriteResult.getModifiedCount());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();

			systemInitModel.addExecuteResult(e.getMessage());

			return Mono.error(e);
		}

		return filterChain.filter(exchange);
	}

}
