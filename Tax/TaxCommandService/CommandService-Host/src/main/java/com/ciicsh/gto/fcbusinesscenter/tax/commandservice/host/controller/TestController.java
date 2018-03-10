package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.EmployeeDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TestProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TestService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.message.TaxKafkaSender;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController extends BaseController implements TestProxy{

    private static final Logger logger = LoggerFactory.getLogger(TestController.class);

    @Autowired
    private TestService testService;

    @Autowired
    private KafkaTemplate template;

    @Override
    public JsonResult test() {
        JsonResult jr = new JsonResult();
        /*Object productObj = param.get("product");
        Object dependedRelation = param.get("dependedRelation");
        Object excludedRelation = param.get("excludedRelation");
        try {
            ProductPO product = JSONConverter.convertToEntity(productObj, ProductPO.class);
            List<String> depends = JSONConverter.convertToEntityArr(dependedRelation, String.class);
            List<String> excludes = JSONConverter.convertToEntityArr(excludedRelation, String.class);
            String productId = productCommandService.save(product, depends, excludes);
            jr.setErrorcode("200");
            jr.setData(productId);
        } catch (Exception e) {
            jr.setErrorcode("500");
        }*/

        return jr;
    }

    @Autowired
    private TaxKafkaSender taxKafkaSender;

//    @RequestMapping("taxTest")
//    @ResponseBody
//    public void taxTest(EmployeeDTO employeeDTO){
//        taxKafkaSender.taxOutputTest(employeeDTO);
//    }

    @RequestMapping("/kafkaTest")
    @ResponseBody
    public String kafkaTest(String topic, String key, String data){
        template.send(topic, key, data);
        return "success";
    }

    @KafkaListener(id = "t1", topics = "t1")
    public void listenT1(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("listenT1 {} - {} : {}", cr.topic(), cr.key(), cr.value());
    }

    @KafkaListener(id = "t2", topics = "t2")
    public void listenT2(ConsumerRecord<?, ?> cr) throws Exception {
        logger.info("listenT2 {} - {} : {}", cr.topic(), cr.key(), cr.value());
    }
}
