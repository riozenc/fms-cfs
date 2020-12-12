/**
 * Author : czy
 * Date : 2019年6月26日 下午7:12:39
 * Title : com.riozenc.cfs.webapp.mrm.filter.TransformerInitFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.datainit;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.common.webapp.domain.CommonParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossFormulaParamDomain;
import org.fms.cfs.common.webapp.domain.TransformerLossTableParamDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.fms.cfs.server.webapp.mrm.filter.BillingDataInitFilter;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport.MongoUpdateFilter;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport.ToDocumentCallBack;
import com.riozenc.titanTool.spring.web.client.TitanTemplate;

import reactor.core.publisher.Mono;

/**
 * 变压器档案
 * 
 * @author czy
 *
 */
public class TransformerInitFilter implements BillingDataInitFilter, MongoDAOSupport {

	@Autowired
	private TitanTemplate titanTemplate;

	@Override
	public int getOrder() {
		return 50;
	}

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		BillingDataInitModel billingDataInitModel = (BillingDataInitModel) exchange.getModel();

		Map<String, CommonParamDomain> commonParamMap = findMany(
				getCollection(billingDataInitModel.getDate(), MongoCollectionConfig.SYSTEM_COMMON.name()),
				new MongoFindFilter() {

					@Override
					public Bson filter() {
						return Filters.eq("type", "RATED_CAPACITY");
					}
				}, CommonParamDomain.class).stream().collect(Collectors.toMap(CommonParamDomain::getParamKey, k -> k));

		List<WriteModel<Document>> transformerMongoList = updateMany(
				toDocuments(billingDataInitModel.getTransformerDomains(), new ToDocumentCallBack<TransformerDomain>() {
					@Override
					public TransformerDomain call(TransformerDomain transformerDomain) {

						transformerDomain.setCapacity(new BigDecimal(
								commonParamMap.get(transformerDomain.getRatedCapacity()).getParamValue()));

						return transformerDomain;
					}
				}), new MongoUpdateFilter() {
					@Override
					public Bson filter(Document param) {
						return Filters.eq("id", param.get("id"));
					}
				}, true);

		if (transformerMongoList.size() == 0) {
			billingDataInitModel.addExecuteResult("变压器数据为0,请检查参数档案.");
			return filterChain.filter(exchange);
		}

		BulkWriteResult bulkWriteResult1 = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.TRANSFORMER_INFO.name()).bulkWrite(transformerMongoList);

		// 公式
		List<TransformerLossFormulaParamDomain> transformerLossFormulaParamDomains = billingDataInitModel
				.getTransformerLossFormulaParamDomains();
		// 查表
		List<TransformerLossTableParamDomain> transformerLossTableParamDomains = billingDataInitModel
				.getTransformerLossTableParamDomains();

		deleteMany(getCollectionName(billingDataInitModel.getDate(),
				MongoCollectionConfig.TRANSFORMER_LOSS_FORMULA_PARAM_INFO.name()), new MongoDeleteFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				});

		deleteMany(getCollectionName(billingDataInitModel.getDate(),
				MongoCollectionConfig.TRANSFORMER_LOSS_TABLE_PARAM_INFO.name()), new MongoDeleteFilter() {
					@Override
					public Bson filter() {
						return new Document();
					}
				});

		List<WriteModel<Document>> insertTLFPI = insertMany(toDocuments(transformerLossFormulaParamDomains));
		List<WriteModel<Document>> insertTLTPI = insertMany(toDocuments(transformerLossTableParamDomains));

		BulkWriteResult resultTLFPI = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.TRANSFORMER_LOSS_FORMULA_PARAM_INFO.name()).bulkWrite(insertTLFPI);
		BulkWriteResult resultTLTPI = getCollection(billingDataInitModel.getDate(),
				MongoCollectionConfig.TRANSFORMER_LOSS_TABLE_PARAM_INFO.name()).bulkWrite(insertTLTPI);

		return filterChain.filter(exchange);
	}

}
