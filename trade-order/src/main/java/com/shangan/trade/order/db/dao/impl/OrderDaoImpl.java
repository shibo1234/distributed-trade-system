package com.shangan.trade.order.db.dao.impl;

import com.shangan.trade.order.db.dao.OrderDao;
import com.shangan.trade.order.db.mappers.OrderMapper;
import com.shangan.trade.order.db.model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public boolean insertOrder(Order order) {
        int result = orderMapper.insert(order);
        if (result > 0) {
            return true;
        }
        return false;
    }

    @Override
    public boolean deleteOrder(long id) {
        int result = orderMapper.deleteByPrimaryKey(id);
        return result > 0;
    }

    @Override
    public Order queryOrderById(long id) {
        return orderMapper.selectByPrimaryKey(id);
    }

    @Override
    public boolean updateOrder(Order order) {
        int result = orderMapper.updateByPrimaryKey(order);
        return result > 0;
    }
}
