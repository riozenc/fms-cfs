/**
 *    Auth:riozenc
 *    Date:2019年3月8日 下午3:28:46
 *    Title:com.riozenc.cim.webapp.dao.CustomerDAO.java
 **/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.ElectricMeterDomain;
import org.fms.cfs.common.webapp.domain.UserDomain;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.PaginationSupport;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.properties.Global;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO()
public class MeterDAO extends AbstractTransactionDAOSupport implements MongoDAOSupport, BaseDAO<ElectricMeterDomain> {

	@Override
	public int insert(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public ElectricMeterDomain findByKey(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	@PaginationSupport
	public List<ElectricMeterDomain> findByWhere(ElectricMeterDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public List<ElectricMeterDomain> findByMeter(String meterIds) {
		return getPersistanceManager().find(getNamespace() + ".findByMeter", meterIds);
	}

	public List<ElectricMeterDomain> findByUser(String userIds) {
		return getPersistanceManager().find(getNamespace() + ".findByUser", userIds);
	}

	public List<ElectricMeterDomain> findByWriteSect(String writeSectIds) {
		return getPersistanceManager().find(getNamespace() + ".findByWriteSect", writeSectIds);
	}

	public List<ElectricMeterDomain> getMeterByUser(UserDomain userDomain) {
		return getPersistanceManager().find(getNamespace() + ".getMeterByUser", userDomain);
	}

	/**
	 * 初始化
	 * 
	 * @param date
	 * @param documents
	 * @return
	 */
	public long initialize(String date, List<ElectricMeterDomain> electricMeterDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate(Global.getConfig("mongo.databaseName"));
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER.name())).drop();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER.name()))
				.insertMany(toDocuments(electricMeterDomains));// 参数批量入库
		return mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER.name()))
				.countDocuments();
	}

	public long initializeMany(String date, List<ElectricMeterDomain> electricMeterDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate(Global.getConfig("mongo.databaseName"));

		List<WriteModel<Document>> requests = updateMany(toDocuments(electricMeterDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				// TODO Auto-generated method stub
				return Filters.eq("id", param.get("id"));
			}
		}, true);

		BulkWriteResult bulkWriteResult = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.ELECTRIC_METER.name()))
				.bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

}
