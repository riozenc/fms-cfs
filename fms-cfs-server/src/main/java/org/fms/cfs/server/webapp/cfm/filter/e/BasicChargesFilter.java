/**
 * Author : czy
 * Date : 2019年4月13日 下午3:06:32
 * Title : com.riozenc.cfs.webapp.mrm.handler.BasicChargesHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.PriceExecutionDomain;
import org.fms.cfs.common.webapp.domain.TransformerDomain;
import org.fms.cfs.common.webapp.domain.TransformerMeterRelationDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.mongodb.client.model.Filters;
import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 基本电费
 * 
 * @author czy
 *
 */
public class BasicChargesFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(BasicChargesFilter.class);

	@Override
	public int getOrder() {
		return 60;
	}

//	1、按最大需量计算：2019年算法修改为 最大需量*单价
//	2、按容量计算：基本电费=运行容量*基本电费单价*运行天数

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理基本电费...");

		// 过滤掉不计算基本电费的计量点
		exchange.getModels().values().parallelStream()
				.filter(e -> e.getBasicMoneyFlag() != null && e.getBasicMoneyFlag() != 0).forEach(e -> {

					// 判断是否最后一次算费，如果是，则继续
					if (e.isLastTime()) {
						// 容量/需量值获取

//						e.calculateBasicMoney(runDays, MonUtils.getMonthDays(exchange.getDate()));
						boolean flag = e.calculateBasicMoney(1, 1);
						if (!flag) {
							e.markProcessResult(getOrder(), flag);
							e.addRemark("基本电费计算问题,具体原因未知");
						}

					}

				});

		return filterChain.filter(exchange);
	}

	/**
	 * //TODO 获取变压器的运行天数
	 * 
	 * @return
	 */
	private int getTransformerRunDays(ECFModel ecfModel) {
		List<Long> transformerIds = findMany(
				getCollectionName(ecfModel.getMon(), MongoCollectionConfig.TRANSFORMER_METER_REL.name()),
				new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.eq("meterId", ecfModel.getMeterId());
					}
				}, TransformerMeterRelationDomain.class).stream().map(TransformerMeterRelationDomain::getTransformerId)
						.collect(Collectors.toList());

		List<TransformerDomain> transformerDomains = findMany(
				getCollection(ecfModel.getMon(), MongoCollectionConfig.TRANSFORMER_INFO.name()), new MongoFindFilter() {
					@Override
					public Bson filter() {
						return Filters.in("id", transformerIds);
					}
				}, TransformerDomain.class);

		// 通过业扩查询变压器运行天数

		return 1;
	}

	private BigDecimal getBasicPrice(String date, Integer id) {

		// priceClass=0 代表基本电费 elecType=5 代表大工业
		// SELECT b.* FROM PRICE_TYPE_INFO a RIGHT JOIN PRICE_EXECUTION_INFO b ON a.ID =
		// b.PRICE_TYPE_ID WHERE a.PRICE_CLASS = 0 AND a.ELEC_TYPE = 5

		List<PriceExecutionDomain> priceExecutionDomains = findMany(
				getCollectionName(date, MongoCollectionConfig.PRICE_EXECUTION.name()), new MongoFindFilter() {

					@Override
					public Bson filter() {
						return Filters.eq("id", id);
					}

				}, PriceExecutionDomain.class);

		return priceExecutionDomains.get(0).getPrice();
	}
}
