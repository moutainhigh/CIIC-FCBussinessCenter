package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api;


import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("fc-business-center-tax-command-service")
@RequestMapping("/info")
public interface TestProxy {

    @RequestMapping("")
    JsonResult test();

    /*@PostMapping("")
    JsonResult saveBasicProduct(@RequestBody Map<String, Object> param);

    @PutMapping("edit")
    JsonResult editBasicProduct(@RequestBody Map<String, Object> param);

    @GetMapping("/forbidden/{basicProductId}/{active}")
    JsonResult forbiddenBasicProduct(@PathVariable("basicProductId") String basicProductId, @PathVariable("active") String active);*/
}