/**
 * Author : czy
 * Date : 2019年6月26日 下午3:00:34
 * Title : com.riozenc.cfs.webapp.mrm.filter.ArchivesInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.util.List;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import reactor.core.publisher.Mono;

public class ArchivesInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	// --客户
	// 用户
	// --结算户

	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		// TODO Auto-generated method stub
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		StringBuilder executeResult = new StringBuilder();
		if (billingDataInitModel.getUserDomains().size() == 0) {
			billingDataInitModel.addExecuteResult("用户数据为0,请检查用户档案.");
			return filterChain.filter(exchange);
		}

		List<UserDomain> userList = billingDataInitModel.getUserDomains().stream().filter(u -> u.getStatus() == 1)
				.collect(Collectors.toList());

		billingDataInitModel.setUserDomains(userList);

		// 取mongo数据，判断次数
		List<WriteModel<Document>> userMongoList = updateMany(
				toDocuments(billingDataInitModel.getUserDomains(), new ToDocumentCallBack<UserDomain>() {
					@Override
					public UserDomain call(UserDomain userDomain) {
						userDomain.setSn(billingDataInitModel.getSn());
						userDomain.createObjectId();
						return userDomain;
					}
				}), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
//						return Filters.and(Filters.eq("id", param.get("id")), Filters.eq("sn", param.get("sn")));
						return Filters.eq("_id", param.get("_id"));
					}
				}, true);

		BulkWriteResult userResult = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.USER_INFO.name()).bulkWrite(userMongoList);

		executeResult.append("用电户：")
				.append(userMongoList.size() - userResult.getModifiedCount() + userResult.getInsertedCount());

		billingDataInitModel.addExecuteResult(executeResult.toString());

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		// TODO Auto-generated method stub
		return 10;
	}

}
