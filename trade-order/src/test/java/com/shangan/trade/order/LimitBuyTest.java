package com.shangan.trade.order;

import com.shangan.trade.order.service.LimitBuyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class LimitBuyTest {
    @Autowired
    private LimitBuyService limitBuyService;

    @Test
    public void addLimitMemberTest() {
        try {
            limitBuyService.addLimitMember(4L, 123456L);
        } catch (Exception e){
            e.printStackTrace();
            fail("错误：" + e.getMessage() );
        }
    }

    @Test
    public void isInLimitMemberTest() {
        try {
            limitBuyService.isInLimitMember(4L, 123456L);
        } catch (Exception e){
            e.printStackTrace();
            fail("错误：" + e.getMessage() );
        }
    }

    @Test
    public void removeLimitMemberTest() {
        try {
            limitBuyService.removeLimitMember(4L, 1234L);
        } catch (Exception e){
            e.printStackTrace();
            fail("错误：" + e.getMessage() );
        }
    }
}
