package com.shangan.tradewebportal;

import com.shangan.tradelightningdeal.utils.RedisWorker;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.jupiter.api.Assertions.fail;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RedisTest {
    @Autowired
    public RedisWorker redisWorker;

    @Test
    public void setValue() {
        try {
            redisWorker.setValue("hello", "world");
        } catch (Exception e) {
            e.printStackTrace();
            fail("错误: " + e.getMessage());
        }
    }

    @Test
    public void getValue() {
        try {
            String result = redisWorker.getValueByKey("hello");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("错误：" + e.getMessage());
        }
    }

    @Test
    public void setStockTest() {
        try {
            redisWorker.setValue("stock:5", 10L);
        } catch (Exception e) {
            e.printStackTrace();
            fail("错误：" + e.getMessage() );
        }
    }

    @Test
    public void stockCheckTest() {
        try {
            String result = redisWorker.getValueByKey("stock:5");
            System.out.println(result);
        } catch (Exception e) {
            e.printStackTrace();
            fail("错误：" + e.getMessage());
        }
    }
//    @Test
//    public void setJmeterStockTest() {
//        redisWorker.setValue("stock: 5", 10L);
//    }
}
