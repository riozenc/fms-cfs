/**
 * Author : czy
 * Date : 2019年6月23日 上午9:28:00
 * Title : com.riozenc.cfs.webapp.mrm.e.dao.PriceExecutionDAO.java
 *
**/
package org.fms.cfs.server.webapp.mrm.e.dao;

import java.util.List;

import org.bson.Document;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;

import com.mongodb.bulk.BulkWriteResult;
import com.mongodb.client.model.WriteModel;
import com.riozenc.titanTool.annotation.TransactionDAO;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.AbstractTransactionDAOSupport;
import com.riozenc.titanTool.spring.webapp.dao.BaseDAO;

@TransactionDAO
public class PriceExecutionDAO extends AbstractTransactionDAOSupport
		implements BaseDAO<PriceExecutionDomain>, MongoDAOSupport {

	@Override
	public int insert(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().insert(getNamespace() + ".insert", t);
	}

	@Override
	public int delete(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().delete(getNamespace() + ".delete", t);
	}

	@Override
	public int update(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().update(getNamespace() + ".update", t);
	}

	@Override
	public PriceExecutionDomain findByKey(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().load(getNamespace() + ".findByKey", t);
	}

	@Override
	public List<PriceExecutionDomain> findByWhere(PriceExecutionDomain t) {
		// TODO Auto-generated method stub
		return getPersistanceManager().find(getNamespace() + ".findByWhere", t);
	}

	public int init(String date) {

		List<PriceExecutionDomain> executionDomains = findByWhere(new PriceExecutionDomain());
		if (!executionDomains.isEmpty()) {
//			insertMany(getCollection(date, MongoCollectionConfig.PRICE_EXECUTION.name()),
//					toDocuments(executionDomains));

			List<WriteModel<Document>> writeModels = insertMany(toDocuments(executionDomains));
			BulkWriteResult bulkWriteResult = getCollection(date, MongoCollectionConfig.PRICE_EXECUTION.name())
					.bulkWrite(writeModels);
			return bulkWriteResult.getInsertedCount();
		}

		return 0;
	}

}
