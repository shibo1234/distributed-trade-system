package com.shangan.tradelightningdeal.db.dao;

import com.shangan.tradelightningdeal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityDao {
    boolean insertSeckillActivity(SeckillActivity seckillActivity);
    SeckillActivity querySeckillActivityById(long id);

    List<SeckillActivity> queryActivitysByStatus(int status);
    boolean updateAvailableStockByPrimaryKey(long id);

    boolean lockStock(long id);

    boolean deductStock(long id);

    boolean revertStock(long id);
}
