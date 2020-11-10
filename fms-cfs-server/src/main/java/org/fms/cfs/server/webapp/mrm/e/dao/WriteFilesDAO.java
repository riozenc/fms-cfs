/**
 * Author : czy
 * Date : 2019年4月22日 下午5:13:08
 * Title : com.riozenc.cfs.webapp.mrm.e.dao.WriteFilesDAO.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.WriteFilesDomain;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.common.json.utils.JSONUtil;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class WriteFilesDAO extends AbstractTransactionDAOSupport implements MongoDAOSupport, BaseDAO<WriteFilesDomain> {

	@Override
	public int insert(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public WriteFilesDomain findByKey(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<WriteFilesDomain> findByWhere(WriteFilesDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public List<WriteFilesDomain> findByWhere(String date, WriteFilesDomain writeFilesDomain) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		MongoCollection<Document> collection = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name()));

		FindIterable<WriteFilesDomain> findIterable = collection
				.find(Document.parse(JSONUtil.toJsonString(writeFilesDomain, true)), WriteFilesDomain.class);

		MongoCursor<WriteFilesDomain> mongoCursor = findIterable.iterator();

		List<WriteFilesDomain> result = new ArrayList<>();
		while (mongoCursor.hasNext()) {
			WriteFilesDomain domain = mongoCursor.next();
			result.add(domain);
		}

		return result;
	}

	public long initialize(String date, List<WriteFilesDomain> writeFilesDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name())).drop();
		mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name()))
				.insertMany(toDocuments(writeFilesDomains));// 参数批量入库
		return mongoTemplate.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name()))
				.countDocuments();

	}

	public long initializeMany(String date, List<WriteFilesDomain> writeFilesDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		List<WriteModel<Document>> requests = updateMany(toDocuments(writeFilesDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				// TODO Auto-generated method stub
				return Filters.eq("id", param.get("id"));
			}
		}, true);
		BulkWriteResult bulkWriteResult = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name())).bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

	/**
	 * 抄表
	 * 
	 * @param date
	 * @param documents
	 * @return
	 */
	public long meterReading(String date, List<WriteFilesDomain> writeFilesDomains) {
		MongoTemplate mongoTemplate = getMongoTemplate();
		MongoCollection<Document> collection = mongoTemplate
				.getCollection(getCollectionName(date, MongoCollectionConfig.WRITE_FILES.name()));
		List<WriteModel<Document>> requests = updateMany(toDocuments(writeFilesDomains), new MongoUpdateFilter() {
			@Override
			public Bson filter(Document param) {
				// TODO Auto-generated method stub
				return Filters.eq("meterId", param.get("meterId"));
			}
		}, false);
		BulkWriteResult bulkWriteResult = collection.bulkWrite(requests);
		return bulkWriteResult.getModifiedCount();
	}

}
