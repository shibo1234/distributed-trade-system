package com.shangan.trade.order.mq;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

//test message sending
@Slf4j
@Component
public class OrderMessageSender {
    @Autowired
    private AmqpTemplate amqpTemplate;

    public void sendPayStatusCheckDelayMessage(String message) {
        log.info("发送订单创建完成，支付状态确认消息:{}", message);
        amqpTemplate.convertAndSend("order-event-exchange", "order.create", message);
    }

    /**
     * 发送创建订单消息
     * @param message
     */
    public void sendCreateOrderMessage(String message) {
        log.info("发送创建订单消息:{}", message);
        amqpTemplate.convertAndSend("order-event-exchange", "to.create.order", message);
    }

    /**
     * 发送秒杀订单支付成功消息
     * @param message
     */
    public void sendSeckillPaySucessMessage(String message) {
        log.info("发送秒杀订单支付成功消息:{}", message);
        amqpTemplate.convertAndSend("order-event-exchange", "seckill.order.pay.success", message);
    }
}
