package com.shangan.trade.goods.service;

import com.shangan.trade.goods.db.model.Goods;

import java.util.List;

public interface SearchService {
    void addGoodsToES(Goods goods);

    //keyword search
    List<Goods> searchGoodsList(String keyword, int from, int size);
}
