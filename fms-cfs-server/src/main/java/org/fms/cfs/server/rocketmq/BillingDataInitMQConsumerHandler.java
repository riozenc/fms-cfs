/**
 * Author : chizf
 * Date : 2020年4月9日 下午3:30:03
 * Title : com.riozenc.cfs.rocketmq.BillingDataInitMQConsumerHandler.java
 *
**/
package org.fms.cfs.server.rocketmq;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.fms.cfs.common.model.BillingDataInitModel;
import org.fms.cfs.server.webapp.mrm.e.action.BillingDataInitAction;
import org.mbs.client.consumer.ConsumerClient;
import org.mbs.client.consumer.ConsumerClient.ConsumerConfig;
import org.springframework.beans.factory.annotation.Autowired;

import com.riozenc.titanTool.common.json.utils.GsonUtils;

public class BillingDataInitMQConsumerHandler {

	private Log logger = LogFactory.getLog(BillingDataInitMQConsumerHandler.class);

	private static final String TOPIC_STRING = "billingDataInit";
	@Autowired
	private BillingDataInitAction billingDataInitAction;

	public BillingDataInitMQConsumerHandler() throws MQClientException {
		System.out.println("BillingDataInitMQConsumerHandler   启动...");
		start();
	}

	public void start() throws MQClientException {

		ConsumerConfig consumerConfig = new ConsumerConfig();

		consumerConfig.setNamesrvAddr("172.21.29.55:9876");
		consumerConfig.setConsumerGroup("titan");
		consumerConfig.setTopic(TOPIC_STRING);
		consumerConfig.setSubExpression("*");

		ConsumerClient consumerClient = ConsumerClient.getInstance(consumerConfig);

		consumerClient.registerMessageListener(new MessageListenerConcurrently() {

			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

				try {
					for (MessageExt msg : msgs) {

						String json = new String(msg.getBody());
						logger.info("接收[" + json + "]消息..");

						billingDataInitAction.initializeMany(GsonUtils.readValue(json, BillingDataInitModel.class));
					}
				} catch (Exception e) {
					return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
				}

				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});

		consumerClient.start();

	}

}
