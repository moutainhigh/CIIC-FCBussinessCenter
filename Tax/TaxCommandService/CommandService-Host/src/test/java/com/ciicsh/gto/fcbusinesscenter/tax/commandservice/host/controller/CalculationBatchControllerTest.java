package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.kie.api.cdi.KReleaseId;
import org.kie.api.cdi.KSession;
import org.kie.api.runtime.KieSession;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration( { "classpath:drools-spring-config.xml" })
public class CalculationBatchControllerTest {

    @KSession("defaultKieSession")
    @KReleaseId(groupId = "com.dools.web.test", artifactId = "droos_web_class", version = "1.6.8-SNAPSHOT")
    private KieSession kSession;

    @Test
    public void  runRules() {
        while (true) {
            try {
                //CalculationContext c  = new CalculationContext();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
