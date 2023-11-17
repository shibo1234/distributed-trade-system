package com.shangan.tradelightningdeal.db.mappers;

import com.shangan.tradelightningdeal.db.model.SeckillActivity;

import java.util.List;

public interface SeckillActivityMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SeckillActivity record);

    int insertSelective(SeckillActivity record);

    SeckillActivity selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(SeckillActivity record);

    int updateByPrimaryKey(SeckillActivity record);

    List<SeckillActivity> queryActivitysByStatus(int status);

    int updateAvailableStockByPrimaryKey(long id);

    int revertStock(long id);

    int deductStock(long id);

    int lockStock(long id);
}