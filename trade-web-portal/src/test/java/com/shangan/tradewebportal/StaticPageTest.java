package com.shangan.tradewebportal;

import com.shangan.tradewebportal.service.StaticServicePage;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;



@SpringBootTest
public class StaticPageTest {

    @Autowired
    private StaticServicePage staticServicePage;

    @Test
    public void createStaticPageTest() {
        try {
            staticServicePage.createStaticPage(3);
        } catch (Exception e) {
            fail("创建失败" + e.getMessage());
        }
    }
}
