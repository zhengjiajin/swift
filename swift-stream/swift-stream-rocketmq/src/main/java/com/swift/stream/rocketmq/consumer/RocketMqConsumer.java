/*
 * @(#)AliMqConsumer.java   1.0  2018年7月10日
 * 
 * Copyright (c)	2014-2020. All Rights Reserved.	GuangZhou hhmk Technology Company LTD.
 */
package com.swift.stream.rocketmq.consumer;

import java.util.Properties;

import org.apache.rocketmq.client.AccessChannel;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.rebalance.AllocateMessageQueueAveragely;
import org.apache.rocketmq.common.protocol.heartbeat.MessageModel;

import com.aliyun.openservices.ons.api.Consumer;
import com.aliyun.openservices.ons.api.ONSFactory;
import com.aliyun.openservices.ons.api.PropertyKeyConst;
import com.aliyun.openservices.ons.api.PropertyValueConst;
import com.swift.exception.extend.SystemException;
import com.swift.stream.rocketmq.consumer.core.RocketMqMessageHandler;
import com.swift.util.type.TypeUtil;

/**
 * @author zhengjiajin
 * @version 1.0 2018年7月10日
 */
public class RocketMqConsumer extends RocketMqMessageHandler{

    // 开源客户端
    private DefaultMQPushConsumer consumerApache;
    // 阿里客户端
    private Consumer consumerAliyun;

    public void start() {
        if (super.getMqconfig() == null) return;
        if (!super.getMqconfig().isStart()) return;
        if (TypeUtil.isNull(super.getMqconfig().getGroupId())) return;
        if (TypeUtil.isNull(super.getListener())) return;
        try {
            if (super.getMqconfig().isAliyun()) {
                crateAliyunConsumer();
            } else {
                crateApacheConsumer();
            }
        } catch (Exception e) {
            throw new SystemException("启动MQ客户端异常", e);
        }
    }

    private void crateApacheConsumer() throws Exception {
        consumerApache = new DefaultMQPushConsumer(super.getMqconfig().getGroupId(), super.getMqconfig().createRpcHook(),
            new AllocateMessageQueueAveragely());
        consumerApache.setNamesrvAddr(super.getMqconfig().getONSAddr());
        consumerApache.setAccessChannel(AccessChannel.CLOUD);
        consumerApache.setConsumeMessageBatchMaxSize(1);
        // 是否广播模式
        if (super.getMessageModel().equalsIgnoreCase("BROADCASTING")) {
            consumerApache.setMessageModel(MessageModel.BROADCASTING);
        }
        for (String topic : super.getSubscribeTag().keySet()) {
            consumerApache.subscribe(super.getMqconfig().remoteTopic(topic),
                ExpressionUtil.subExpression(super.getSubscribeTag().get(topic)));
        }
        consumerApache.registerMessageListener(this);
        consumerApache.start();

    }

    private void crateAliyunConsumer() throws Exception {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.GROUP_ID, super.getMqconfig().getGroupId());
        properties.put(PropertyKeyConst.AccessKey, super.getMqconfig().getAccessKey());
        properties.put(PropertyKeyConst.SecretKey, super.getMqconfig().getSecretKey());
        properties.put(PropertyKeyConst.NAMESRV_ADDR, super.getMqconfig().getONSAddr());
        // 是否广播模式
        if (super.getMessageModel().equalsIgnoreCase("BROADCASTING")) {
            properties.put(PropertyKeyConst.MessageModel, PropertyValueConst.BROADCASTING);
        }
        consumerAliyun = ONSFactory.createConsumer(properties);

        for (String topic : super.getSubscribeTag().keySet()) {
            consumerAliyun.subscribe(super.getMqconfig().remoteTopic(topic),
                ExpressionUtil.subExpression(super.getSubscribeTag().get(topic)),
                this);
        }
        consumerAliyun.start();
    }

    public void close() {
        if (consumerApache != null) consumerApache.shutdown();
        if (consumerAliyun != null) consumerAliyun.shutdown();
    }

}
