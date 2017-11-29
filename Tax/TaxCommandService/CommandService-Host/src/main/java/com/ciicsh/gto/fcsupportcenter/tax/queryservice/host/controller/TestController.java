package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.commandservice.api.TestProxy;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TestService;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;

@RestController
public class TestController implements TestProxy {

    @Autowired
    private TestService testService;

    @Override
    public JsonResult test(@RequestBody Map<String, Object> param) {
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

}
