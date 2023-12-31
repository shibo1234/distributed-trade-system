package com.shangan.trade.order.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.model.Order;
import com.shangan.trade.order.mq.OrderMessageSender;
import com.shangan.trade.order.service.OrderService;
import com.shangan.trade.order.service.RiskBlackListService;
import com.shangan.trade.order.utils.SnowflakeIdWorker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;


@Service
@Slf4j
public class OrderServiceImpl implements OrderService {
    @Autowired
    private OrderDao orderDao;

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private OrderMessageSender orderMessageSender;

    @Autowired
    private RiskBlackListService riskBlackListService;

    /**
     * datacenterId;  数据中心
     * machineId;     机器标识
     * 在分布式环境中可以从机器配置上读取
     * 单机开发环境中先写死
     */
    private SnowflakeIdWorker snowFlake = new SnowflakeIdWorker(6, 8);

    /**
     * 创建订单和库存锁定在一个事务中，要么同时成功，要么同时失败
     * 使用 @Transactional(rollbackFor = Exception.class)
     *
     * @param userId
     * @param goodsId
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public Order createOrder(long userId, long goodsId) {
        //判断 用户 是否在黑名单中
        boolean inRiskBlackListMember = riskBlackListService.isInRiskBlackListMember(userId);
        if (inRiskBlackListMember) {
            log.error("user is in risk black list can not buy userId={}", userId);
            throw new RuntimeException("用户在黑名单中");
        }
        Order order = new Order();
        //普通商品购买默认无活动
        order.setId(snowFlake.nextId());
        order.setActivityId(0L);
        order.setActivityType(0);
        order.setGoodsId(goodsId);
        order.setUserId(userId);
        /*
         * 状态:0,没有可用库存订单创建失败;1,已创建，等待付款;2 已支付,等待发货;99 订单关闭，超时未付款
         */
        order.setStatus(1);
        order.setCreateTime(new Date());

        //查询
        Goods goods = goodsService.queryGoodsById(goodsId);
        if (goods == null) {
            log.error("goods is null goodsId={},userId={}", goodsId, userId);
            throw new RuntimeException("商品不存在");
        }
        //查询库存
        if (goods.getAvailableStock() <= 0) {
            log.error("goods stock not enough goodsId={},userId={}", goodsId, userId);
            throw new RuntimeException("商品库存库存不足");
        }

        //lock库存
        boolean lockResult = goodsService.lockStock(goodsId);
        if (!lockResult) {
            log.error("order lock stock error order={}", JSON.toJSONString(order));
            throw new RuntimeException("订单锁定库存失败");
        }
        order.setPayPrice(goods.getPrice());

        //创建订单，发送创建订单消息
        orderMessageSender.sendCreateOrderMessage(JSON.toJSONString(order));
        return order;
    }

//    @Transactional(rollbackFor = Exception.class)
//    @Override
//    public Order createOrder(long userId, long goodsId) {
//        Order order = new Order();
//        //普通商品购买默认无活动
//        order.setId(snowFlake.nextId());
//        order.setActivityId(0L);
//        order.setActivityType(0);
//        order.setGoodsId(goodsId);
//        order.setUserId(userId);
//        /*
//         * 状态:0,没有可用库存订单创建失败;
//         * 1,已创建，等待付款;
//         * 2 已支付,等待发货;
//         * 99 订单关闭，超时未付款
//         */
//        order.setStatus(1);
//        order.setCreateTime(new Date());
//        //Goods goods = goodsDao.queryGoodsById(goodsId);
//        //查询
//        Goods goods = goodsService.queryGoodsById(goodsId);
//        if (goods == null) {
//            log.error("goods is null goodsId={},userId={}", goodsId, userId);
//            throw new RuntimeException("no goods exist");
//        }
//        //查询库存
//        if(goods.getAvailableStock() <= 0) {
//            log.error("goods stock not enough goodsId={}, userId={}", goodsId, userId);
//            throw new RuntimeException("not enough inventory");
//        }
//        // lock 库存
//        boolean lockResult = goodsService.lockStock(goodsId);
//        if(!lockResult) {
//            log.error("order lock stock error order ={}", JSON.toJSONString(order));
//            throw new RuntimeException("lock failed");
//        }

        //create order

//        order.setPayPrice(goods.getPrice());
////        boolean res = orderDao.insertOrder(order);
////
////        if (!res) {
////            log.error("order insert error order={}", JSON.toJSONString(order));
////            throw  new RuntimeException("failed creating order");
////        }
//        //发送订单支付状态检查消息
//        orderMessageSender.sendPayStatusCheckDelayMessage(JSON.toJSONString(order));
//        return order;
//    }

    @Override
    public Order queryOrder(long orderId) {
        return orderDao.queryOrderById(orderId);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void payOrder(long orderId) {
        log.info("支付订单  订单号：{}", orderId);
        Order order = orderDao.queryOrderById(orderId);
        /*
         * 订单支付校验
         * 1.判断订单号对应订单是否存在
         * 2.判断查询到的订单状态是否为未支付
         */
        if (order == null) {
            log.error("orderId={} 对应订单不存在", orderId);
            throw new RuntimeException("对应订单不存在");
        }
        if (order.getStatus() != 1) {
            log.error("orderId={}  订单状态无法支付：", orderId);
            throw new RuntimeException("订单状态无法支付");
        }
        //Mock 模拟调用支付平台
        log.info("调用第三方支付平台付款.......");

        order.setPayTime(new Date());
        /*
         * 0:没有可用库存，无效订单
         * 1:已创建等待付款
         * 2:支付完成
         */
        order.setStatus(2);
        boolean updateResult = orderDao.updateOrder(order);
        if(!updateResult) {
            log.error("orderId={} 更新失败", orderId);
            throw new RuntimeException("订单支付状态更新失败");
        }

        //库存扣减
        if (order.getActivityType() == 0) {
            //普通商品处理
            goodsService.deductStock(order.getGoodsId());
        } else if (order.getActivityType() == 1) {
            //秒杀活动处理,发送支付成功消息
            orderMessageSender.sendSeckillPaySucessMessage(JSON.toJSONString(order));
        }
    }

//        boolean deductResult = goodsService.deductStock(order.getGoodsId());
//        if(!deductResult) {
//            log.error("orderId={} 删减失败", orderId);
//            throw new RuntimeException("delete failed");
//        }
        //orderDao.updateOrder(order);
//    }
}