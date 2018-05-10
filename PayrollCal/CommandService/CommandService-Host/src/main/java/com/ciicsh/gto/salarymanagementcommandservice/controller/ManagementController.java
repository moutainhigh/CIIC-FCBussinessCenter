package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.messageBus.KafkaSender;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NeoJiang on 2018/2/7.
 * @author NeoJiang
 */
@RestController
@RequestMapping(value = "/api")
public class ManagementController {

    @Autowired
    private ManagementProxy managementProxy;

    @Autowired
    private KafkaSender kafkaSender;

    class MgrData{
        public String code;
        public String name;

        public MgrData(String code, String name){
            this.code = code;
            this.name = name;
        }
    }

    // TO BE DELETED
    @GetMapping("/test")
    public void sendManagement(@RequestParam String str) {
        kafkaSender.SendManagement(str);
    }

    @GetMapping("/getMgrList")
    public JsonResult getMgrList(@RequestParam(required = false) String query) {

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
