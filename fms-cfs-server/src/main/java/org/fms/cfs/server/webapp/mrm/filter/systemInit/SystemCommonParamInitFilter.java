/**
 * Author : czy
 * Date : 2019年8月22日 上午10:30:04
 * Title : com.riozenc.cfs.webapp.mrm.filter.SystemCommonParamInitFilter.java
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
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import reactor.core.publisher.Mono;

/**
 * 系统参数初始化
 * 
 * @author czy
 *
 */
public class SystemCommonParamInitFilter implements SystemInitFilter, MongoDAOSupport {

	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		Map<String, String> params = new HashMap<>();
		params.put("status", "1");
		params.put("pageSize", "-1");
		try {

			List<CommonParamDomain> commonParamDomains = titanTemplate.postJsonToList("TITAN-CONFIG",
					"titan-config/sysCommConfig/findByWhere", new HttpHeaders(), params, CommonParamDomain.class);

			if (commonParamDomains.size() == 0) {
				systemInitModel.addExecuteResult("系统参数数量为0，请检查.");
				return filterChain.filter(exchange);
			}

			List<WriteModel<Document>> updateResult = updateMany(toDocuments(commonParamDomains),
					new MongoUpdateFilter() {

						@Override
						public Bson filter(Document param) {
							// TODO Auto-generated method stub
							return Filters.eq("id", param.get("id"));
						}

					}, true);
			BulkWriteResult bulkWriteResult = getCollection(systemInitModel.getDate(),
					MongoCollectionConfig.SYSTEM_COMMON.name()).bulkWrite(updateResult);

			systemInitModel.addExecuteResult("系统参数：" + bulkWriteResult.getModifiedCount());

			return filterChain.filter(exchange);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return Mono.empty();
		}

	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 1;
	}

}
