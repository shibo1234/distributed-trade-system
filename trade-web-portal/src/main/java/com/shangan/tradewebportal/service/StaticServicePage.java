package com.shangan.tradewebportal.service;


import com.shangan.trade.goods.db.model.Goods;
import com.shangan.trade.goods.service.GoodsService;
import com.shangan.tradelightningdeal.db.model.SeckillActivity;
import com.shangan.tradelightningdeal.service.SeckillActivityService;
import com.shangan.tradewebportal.util.CommonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.io.File;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class StaticServicePage {
    @Autowired
    private SeckillActivityService seckillActivityService;

    @Autowired
    private GoodsService goodsService;

    @Autowired
    private TemplateEngine templateEngine;

    public void createStaticPage(long seckillActivityId) {
        PrintWriter writer = null;
        try {


            //查询秒杀详情页所需要数据
            SeckillActivity seckillActivity = seckillActivityService.querySeckillActivityById(seckillActivityId);
            Goods goodsInfo = goodsService.queryGoodsById(seckillActivity.getGoodsId());
            String seckillPrice = CommonUtils.changeF2Y(seckillActivity.getSeckillPrice());
            String oldPrice = CommonUtils.changeF2Y(seckillActivity.getOldPrice());



            //存放页面数据
            Map<String, Object> resultMap = new HashMap<>();
            resultMap.put("seckillActivity", seckillActivity);
            resultMap.put("seckillPrice", seckillPrice);
            resultMap.put("oldPrice", oldPrice);
            resultMap.put("goods", goodsInfo);

            //创建thymeleaf上下文对象
            Context context = new Context();


            //放入数据
            context.setVariables(resultMap);

            //创建HTML页面
            File file = new File("src/main/resources/templates/" + "seckill_item_" + seckillActivityId + ".html");
            writer = new PrintWriter(file);
            //执行模版引擎
            templateEngine.process("seckill_item", context, writer);
        } catch (Exception e) {
            log.error("创建静态化页面 error seckillActivityId={} ", seckillActivityId, e);
        } finally {


            //关闭输出
            if (writer != null) {
                writer.close();
            }
        }
    }
}
