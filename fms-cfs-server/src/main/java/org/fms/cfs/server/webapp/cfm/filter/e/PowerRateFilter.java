/**
 * Author : czy
 * Date : 2019年4月13日 下午3:09:33
 * Title : com.riozenc.cfs.webapp.mrm.handler.PowerRateHandler.java
 *
**/
package org.fms.cfs.server.webapp.cfm.filter.e;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.fms.cfs.common.config.MongoCollectionConfig;
import org.fms.cfs.common.model.ECFModel;
import org.fms.cfs.common.model.exchange.CFModelExchange;
import org.fms.cfs.common.webapp.domain.CosStandardDomain;
import org.fms.cfs.server.webapp.cfm.filter.CFFilterChain;
import org.fms.cfs.server.webapp.cfm.filter.EcfFilter;

import com.riozenc.titanTool.mongo.dao.MongoDAOSupport;

import reactor.core.publisher.Mono;

/**
 * 力率调整电费-7 TODO 共力率关系 有功电量=父表+子表
 * 
 * @author czy
 *
 */
public class PowerRateFilter implements EcfFilter, MongoDAOSupport {
	private Log logger = LogFactory.getLog(PowerRateFilter.class);

	private BigDecimal hundred = BigDecimal.valueOf(100);

	@Override
	public int getOrder() {
		return 70;
	}

	@Override
	public Mono<Void> filter(CFModelExchange<ECFModel> exchange, CFFilterChain filterChain) {
		logger.info("处理力调电费...");
		// 根据电量 计算 功率因数，根据功率因数查力调系数 ，根据力调系数找力调比例
		// 力调电费= 总电费（电度电费+基本电费+附加费）*力调比例

//		1、计算用户功率因数=有功电量/sqrt（有功电量*有功电量+无功电量*无功电量）
//		2、查表获取力率调整系数
//		3、计算力调电费，计算公式：力调电费 = (电度电费+基本电费)*力率调整系数
//		4、共力率关系：存在套扣关系的用户计算功率因数，将子表扣减的电量加回父表中统一计算功率因数，子表父表共用一个功率因数

		Map<Long, ECFModel> ecfModelMap = exchange.getModels();

		// 获取功率因数参数
		List<CosStandardDomain> cosStandardDomains = findMany(
				getCollection(exchange.getDate().toString(), MongoCollectionConfig.COS_STANDARD_CONFIG.name()),
				new MongoFindFilter() {

					@Override
					public Bson filter() {
						return new Document();
					}
				}, CosStandardDomain.class);

		Map<String, CosStandardDomain> cosMap = cosStandardDomains.stream().collect(Collectors.toMap(c -> {
			return c.getCosType() + "#" + c.getCos().intValue();
		}, v -> v));

		// 过滤掉不执行力调的计量点
		ecfModelMap.values().parallelStream().filter(e -> e.getCosType() != null && e.getCosType() != 4).forEach(e -> {

			// sqrt（有功电量*有功电量+无功电量*无功电量）

			BigDecimal division = BigDecimal.valueOf(
					Math.sqrt(e.getActiveChargePower().pow(2).add(e.getReactiveChargePower().pow(2)).doubleValue()));

			// 除数不等于0
			if (division.compareTo(BigDecimal.ZERO) != 0) {

				// 计算用户功率因数=有功电量/sqrt
				BigDecimal g = e.getActiveChargePower().divide(division, 2, RoundingMode.HALF_UP).multiply(hundred);
				// 查表获取力率调整系数

				CosStandardDomain cosStandardDomain = cosMap.get(e.getCosType() + "#" + g.intValue());
				if (cosStandardDomain == null) {
					e.computePowerRateMoney(BigDecimal.ZERO, g);
					e.addRemark("计量点ID:" + e.getMeterId() + " " + e.getCosType() + "#" + g.intValue() + "不存在，默认为不收力率。");
					e.markProcessResult(getOrder(), false);
					return;
				}

				e.computePowerRateMoney(cosStandardDomain.getCosStd().divide(hundred), g);
			} else {
				e.addRemark("力调电费计算： 有功电量+无功电量 = 0,无法计算力调电费");
				e.markProcessResult(getOrder(), false);
			}

		});

		return filterChain.filter(exchange);
	}

}
