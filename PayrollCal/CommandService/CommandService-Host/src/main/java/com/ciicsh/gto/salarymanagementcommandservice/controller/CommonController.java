package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.basicdataservice.api.CityServiceProxy;
import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.basicdataservice.api.ProvinceServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CityDTO;
import com.ciicsh.gto.basicdataservice.api.dto.CountryDTO;
import com.ciicsh.gto.basicdataservice.api.dto.ProvinceDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by NeoJiang on 2018/5/18.
 */
@RestController
@RequestMapping("/api/common")
public class CommonController {

    @Autowired
    private CountryServiceProxy countryServiceProxy;

    @Autowired
    private ProvinceServiceProxy provinceServiceProxy;

    @Autowired
    private CityServiceProxy cityServiceProxy;

    @GetMapping(value = "/country")
    public JsonResult getCountryList() {
        try {
            List<CountryDTO> countryDTOList = countryServiceProxy.listAll();
            return JsonResult.success(countryDTOList);
        } catch (Exception e) {
            return JsonResult.faultMessage("获取国家列表失败");
        }
    }

    @GetMapping(value = "/country/{countryCode}/province")
    public JsonResult getProvinceList(@PathVariable(value = "countryCode") String countryCode) {
        try {
            List<ProvinceDTO> provinceDTOList = provinceServiceProxy.listByCountryCode(countryCode);
            return JsonResult.success(provinceDTOList);
        } catch (Exception e) {
            return JsonResult.faultMessage("获取省份列表失败");
        }
    }

    @GetMapping(value = "/province/{provinceCode}/city")
    public JsonResult getCityList(@PathVariable(value = "provinceCode") String provinceCode) {
        try {
            List<CityDTO> cityDTOList = cityServiceProxy.listByProvinceCode(provinceCode);
            return JsonResult.success(cityDTOList);
        } catch (Exception e) {
            return JsonResult.faultMessage("获取城市列表失败");
        }
    }
}

