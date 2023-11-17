package com.shangan.tradelightningdeal;

import com.shangan.tradelightningdeal.db.dao.SeckillActivityDao;
import com.shangan.tradelightningdeal.db.model.SeckillActivity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

@RunWith(SpringRunner.class)
@SpringBootTest
public class LightningDealTest {
    @Autowired
    private SeckillActivityDao seckillActivityDao;

    @Test
    public void  insertGoodTest() {
        SeckillActivity seckillActivity = new SeckillActivity();
        seckillActivity.setActivityName("秒杀test");
        seckillActivity.setGoodsId(111L);


        seckillActivity.setCreateTime(new Date());
        seckillActivity.setEndTime(new Date());
        seckillActivity.setAvailableStock(10);

        seckillActivity.setActivityStatus(1);

        seckillActivity.setLockStock(0);
        seckillActivity.setSeckillPrice(111);
        seckillActivity.setOldPrice(1111);
        seckillActivity.setCreateTime(new Date());
        seckillActivityDao.insertSeckillActivity(seckillActivity);
    }
}
