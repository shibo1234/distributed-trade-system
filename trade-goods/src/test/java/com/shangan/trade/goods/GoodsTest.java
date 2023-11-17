package com.shangan.trade.goods;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.dao.GoodsDao;
import com.shangan.trade.goods.db.mappers.GoodsMapper;
import com.shangan.trade.goods.db.model.Goods;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GoodsTest {

    @Autowired
    private GoodsDao goodsDao;

    @Autowired
    private GoodsMapper goodsMapper;
    //先检查id是否存在于database
    //如果存在，就删掉，然后再加进去


    @Before
    public void initTest() {
        if (goodsMapper.selectByPrimaryKey(2L) != null) {
            goodsMapper.deleteByPrimaryKey(2L);
        }
        if (goodsMapper.selectByPrimaryKey(1L) != null) {
            goodsMapper.deleteByPrimaryKey(1L);
        }
        Goods goods = new Goods();
        goods.setTitle("iphone 14 pro max");
        goods.setId(1L);
        goods.setBrand("苹果 Apple");
        goods.setCategory("手机");
        goods.setNumber("NO123456");
        goods.setImage("test");
        goods.setDescription("iphone 14 pro max is very good");
        goods.setKeywords("苹果 手机 apple");
        goods.setSaleNum(0);
        goods.setLockStock(10000);
        goods.setPrice(999999);
        goods.setStatus(1);
        goods.setAvailableStock(10);
        goodsMapper.insert(goods);
    }

    @Test
    public void insertGoodsTest() {
        System.out.println("Hello");
        Goods goods = new Goods();
        goods.setTitle("iphone 14 pro max");
        goods.setBrand("苹果 Apple");
        goods.setCategory("手机");
        goods.setNumber("NO123456");
        goods.setImage("test");
        goods.setDescription("iphone 14 pro max is very good");
        goods.setKeywords("苹果 手机 apple");
        goods.setSaleNum(0);
        goods.setLockStock(10000);
        goods.setPrice(999999);
        goods.setStatus(1);
        goods.setAvailableStock(10);
        boolean insertResult = goodsDao.insertGoods(goods);
        System.out.println(insertResult);
    }

    @Test
    public void deleteGoodsTest() {
        assertNotNull(goodsDao.queryGoodsById(1));
        assertTrue(goodsDao.deleteGoods(1));
        assertNull(goodsDao.queryGoodsById(1));
    }

    @Test
    public void queryGoodsTest() {
        Goods goods = goodsDao.queryGoodsById(1);
        assertEquals(1, goods.getId());
        assertEquals("iphone 14 pro max", goods.getTitle());
        // ...
    }

    @Test
    public void queryGoodsTestNotExist() {
        Goods goods = goodsDao.queryGoodsById(2);
        assertNull(goods);
    }

    @Test
    public void updateGoods() {
        Goods goods = goodsDao.queryGoodsById(16);
        goods.setTitle(goods.getTitle() + " update");
        goodsDao.updateGoods(goods);
    }
}
