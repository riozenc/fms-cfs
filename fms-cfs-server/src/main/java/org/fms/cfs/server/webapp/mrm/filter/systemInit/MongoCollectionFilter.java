/**
 * Author : czy
 * Date : 2019年12月28日 下午1:10:27
 * Title : com.riozenc.cfs.webapp.mrm.filter.MongoCollectionFilter.java
 *
**/
package org.fms.cfs.server.webapp.mrm.filter.systemInit;

import org.bson.Document;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.SystemInitModel;
import org.fms.cfs.common.model.exchange.InitModelExchange;
import org.fms.cfs.server.webapp.mrm.filter.InitFilterChain;
import org.fms.cfs.server.webapp.mrm.filter.SystemInitFilter;

import com.mongodb.client.MongoCollection;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

public class MongoCollectionFilter implements SystemInitFilter, MongoDAOSupport {

	@Override
	public Mono<Void> filter(InitModelExchange exchange, InitFilterChain filterChain) {
		SystemInitModel systemInitModel = (SystemInitModel) exchange.getModel();

		if (systemInitModel.getDate() == null) {
			throw new RuntimeException("电费月份为空,无法创建集合.	");
		}

		// 用户表
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.USER_INFO.name()))) {
			MongoCollection<Document> meter = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.USER_INFO.name()));
			meter.createIndex(new Document("id", 1));
		}

		// 计量点
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER.name()))) {
			MongoCollection<Document> meter = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER.name()));
			meter.createIndex(new Document("id", 1));
			meter.createIndex(new Document("id", 1).append("sn", 1));
		}

		// 计量点电费明细
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_MONEY.name()))) {
			MongoCollection<Document> meterMoney = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_MONEY.name()));
			meterMoney.createIndex(new Document("meterId", 1).append("mon", 1).append("sn", 1));
		}

		// 结算户与计量点关系表
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.SETTLEMENT_METER_REL.name()))) {
			MongoCollection<Document> settlementMeterRel = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.SETTLEMENT_METER_REL.name()));
			settlementMeterRel.createIndex(new Document("meterId", 1));
		}

		// 运行变压器
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.TRANSFORMER_INFO.name()))) {
			MongoCollection<Document> transformer = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.TRANSFORMER_INFO.name()));
			transformer.createIndex(new Document("id", 1));
		}

		// 变压器与计量点关系
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()))) {
			MongoCollection<Document> transformerMeterRel = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()));
			transformerMeterRel.createIndex(new Document("meterId", 1));
		}

		// 计量点关系
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_REL.name()))) {
			MongoCollection<Document> transformerMeterRel = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.ELECTRIC_METER_REL.name()));
			transformerMeterRel.createIndex(new Document("meterId", 1).append("pMeterId", 1));
		}

		// 抄表记录
		if (!getMongoTemplate().collectionExists(
				getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.WRITE_FILES.name()))) {
			MongoCollection<Document> transformerMeterRel = getMongoTemplate().createCollection(
					getCollectionName(systemInitModel.getDate(), MongoCollectionConfig.WRITE_FILES.name()));
			transformerMeterRel.createIndex(new Document("meterId", 1));
		}

		return filterChain.filter(exchange);
	}

	@Override
	public int getOrder() {
		return 0;
	}

}
