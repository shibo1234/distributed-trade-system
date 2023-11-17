package com.shangan.trade.order.mq;
import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Slf4j
public class OrderPayCheckReceiver {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsService goodsService;
//    @Autowired
//    private OrderMessageSender orderMessageSender;
    /**
     * 消息处理
     *
     * @param message
     */

    @RabbitListener(queues = "order.pay.status.check.queue")
    public void process(String message) {
        try {
            log.info("创建订单消息处理，接收到信息内容：{}", message);
            Order order = JSON.parseObject(message, Order.class);
            // 0 no available inventory, failing creating order
            // 1 create, waiting for pay
            // 2 paid, waiting for delivering
            // 99 order closed, not paid

            if (order.getActivityType() != 0) return;

            //1.查询订单信息
            Order orderInfo = orderDao.queryOrderById(order.getId());
            //状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
            if (orderInfo.getStatus() == 1) {
                //2.判断是否完成支付
                log.info("订单{}超时支付，关闭订单", orderInfo.getId());
                orderInfo.setStatus(99);
                //3.更新订单状态为关闭
                orderDao.updateOrder(orderInfo);
                //4.将锁定的库存回补
                goodsService.revertStock(orderInfo.getGoodsId());
            }
        } catch (Exception e) {
            log.error("错误" + e.getMessage());
        }

        //生成订单
//        boolean insertResult = orderDao.insertOrder(order);
//        if(!insertResult) {
//            log.error("order insert error = {}", JSON.toJSONString(order));
//            throw  new RuntimeException("create order failed");
//        }

        //发送订单支付状态检查消息
        //orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));
//        Order orderInfo = orderDao.queryOrderById(order.getId());
//        if(orderInfo.getStatus() == 1) {
//            log.info("order{} not paid on time, order closed", order.getId());
//            orderInfo.setStatus(99);
//            orderDao.updateOrder(orderInfo);
//            //补充
//            goodsService.revertStock(orderInfo.getGoodsId());
//        }
    }
}
