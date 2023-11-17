package com.shangan.tradelightningdeal;


import com.shangan.trade.order.service.RiskBlackListService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


@RunWith(SpringRunner.class)
@SpringBootTest
public class RiskBlackListServiceTest {
    @Autowired
    public RiskBlackListService riskBlackListService;


//    先add，再判断，最后移除
    @Test
    public void addRiskBlackListMemberTest() {
        riskBlackListService.addRiskBlackListMember(123456L);
    }

    @Test
    public void isInRiskBlackListMemberTest() {
        riskBlackListService.isInRiskBlackListMember(123456L);
    }

    @Test
    public void removeRiskBlackListMemberTest() {
        riskBlackListService.removeRiskBlackListMember(12345L);
    }
}
