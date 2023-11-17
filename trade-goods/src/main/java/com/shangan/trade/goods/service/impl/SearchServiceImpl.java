package com.shangan.trade.goods.service.impl;

import com.alibaba.fastjson.JSON;
import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.SearchService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
@Slf4j
public class SearchServiceImpl implements SearchService {

    @Autowired
    private RestHighLevelClient client;

    @Override
    public void addGoodsToES(Goods goods) {
        try {
           //Elasticsearch 接受 JSON 格式的文档数据以进行索引操作
            String data = JSON.toJSONString(goods);
            // 创建一个 IndexRequest 对象
            IndexRequest request = new IndexRequest("goods");
            // 设置文档的源数据为 JSON 格式
            request.source(data, XContentType.JSON);

            IndexResponse response = client.index(request, RequestOptions.DEFAULT);

            log.info("addGoods to Es successful. goods: {}, result{}" , data, response);
        } catch (Exception exception){
            log.error("SearchService addGoods error", exception);
        }
    }

    @Override
    public List<Goods> searchGoodsList(String keyword, int from, int size) {
        try {
            SearchRequest searchRequest = new SearchRequest("goods");
            SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

            // 创建布尔查询
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            // 添加通配符查询（模糊查询）
            QueryBuilder wildcardQuery = QueryBuilders.wildcardQuery("title", "*" + keyword + "*");
            boolQuery.should(wildcardQuery);

            // 添加多字段匹配查询
            QueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(keyword, "title", "description", "keywords");
            boolQuery.should(multiMatchQuery);

            // 将布尔查询添加到查询构建器
            searchSourceBuilder.query(boolQuery);

            // 设置查询的起始位置和数量
            searchSourceBuilder.from(from);
            searchSourceBuilder.size(size);

            // 排序
            searchSourceBuilder.sort("saleNum", SortOrder.DESC);

            // 将查询构建器添加到搜索请求
            searchRequest.source(searchSourceBuilder);

            // 获取查询结果
            SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
            System.out.println(JSON.toJSON(searchResponse));

            SearchHits searchHits = searchResponse.getHits();
            long totalNum = searchHits.getTotalHits().value;
            log.info("search 总记录数： {}", totalNum);

            List<Goods> goodsList = new ArrayList<>();

            // 获取命中的hits数据,搜索结果数据
            SearchHit[] hits = searchHits.getHits();
            for (SearchHit searchHit : hits) {
                // 获取json字符串格式的数据
                String sourceAsString = searchHit.getSourceAsString();
                Goods goods = JSON.parseObject(sourceAsString, Goods.class);
                goodsList.add(goods);
            }

            log.info("search result {}", JSON.toJSONString(goodsList));
            return goodsList;

        } catch(Exception exception) {
            log.error("SearchGoodList error", exception);
            return null;
        }
    }
}
