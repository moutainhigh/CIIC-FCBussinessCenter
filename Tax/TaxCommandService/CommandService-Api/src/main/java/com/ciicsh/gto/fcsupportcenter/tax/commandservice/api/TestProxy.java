package com.ciicsh.gto.fcsupportcenter.tax.commandservice.api;


import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@FeignClient("fc-support-center-command-service")
@RequestMapping("/testProxyTax")
public interface TestProxy {

    @PostMapping("")
    JsonResult test(@RequestBody Map<String, Object> param);

    /*@PostMapping("")
    JsonResult saveBasicProduct(@RequestBody Map<String, Object> param);

    @PutMapping("edit")
    JsonResult editBasicProduct(@RequestBody Map<String, Object> param);

    @GetMapping("/forbidden/{basicProductId}/{active}")
    JsonResult forbiddenBasicProduct(@PathVariable("basicProductId") String basicProductId, @PathVariable("active") String active);*/
}