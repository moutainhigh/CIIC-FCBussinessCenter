package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NeoJiang on 2018/2/7.
 * @author NeoJiang
 */
@RestController
public class ManagementController {

    @Autowired
    private ManagementProxy managementProxy;

    class MgrData{
        public String Code;
        public String Name;

        public MgrData(String code, String name){
            this.Code = code;
            this.Name = name;
        }
    }

    @GetMapping("/getMgrList")
    public JsonResult getMgrList(@RequestParam String query) {

        GetManagementRequestDTO param = new GetManagementRequestDTO();

        param.setQueryWords(query);

        com.ciicsh.gto.salecenter.apiservice.api.dto.core.JsonResult<List<DetailResponseDTO>> managementResult
                = managementProxy.getManagement(param);

        if (managementResult == null) {
            return JsonResult.faultMessage("获取管理方失败");
        }

        List<DetailResponseDTO> managementList = managementResult.getObject();

        if (managementList == null) {
            return JsonResult.faultMessage("获取管理方失败");
        }

        List<MgrData> data = managementList.stream()
                .map(i -> new MgrData(i.getManagementId(), i.getTitle()))
                .collect(Collectors.toList());

        return JsonResult.success(data);
    }
}
