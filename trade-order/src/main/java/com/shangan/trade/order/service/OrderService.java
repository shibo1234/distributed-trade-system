package com.shangan.trade.order.service;

import com.shangan.trade.order.db.model.Order;

public interface OrderService {
    Order createOrder(long userId, long goodsId);

    /**
     * 订单查询
     * @param orderId
     * @return
     */
    Order queryOrder(long orderId);

    /**
     * 支付订单
     * @param orderId
     */
    void payOrder(long orderId);
}
