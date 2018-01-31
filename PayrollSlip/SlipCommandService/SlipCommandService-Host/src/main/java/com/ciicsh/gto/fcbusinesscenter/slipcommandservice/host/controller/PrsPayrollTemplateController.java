package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.api.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollTemplateService;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.DetailResponseDTO;
import com.ciicsh.gto.salecenter.apiservice.api.dto.management.GetManagementRequestDTO;
import com.ciicsh.gto.salecenter.apiservice.api.proxy.ManagementProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 *
 * @author taka
 * @since 2018-01-29
 */
@RestController
@RequestMapping("/api/payrollslipservice")
public class PrsPayrollTemplateController {
    ManagementProxy managementProxy;

    @RequestMapping(value = "/listManagements")
    public JsonResult listManagements(@RequestBody Map<String, Object> params) {
        GetManagementRequestDTO getManagementRequestDTO = new GetManagementRequestDTO();
        if (params.get("title") != null) {
            getManagementRequestDTO.setQueryWords((String)params.get("title"));
        }
        if (params.get("size") != null) {
            getManagementRequestDTO.setSize(50);
        }
        List<DetailResponseDTO> list = managementProxy.getManagement(getManagementRequestDTO).getObject();
        return JsonResult.success(list);
    }

    @Autowired
    private PrsPayrollTemplateService prsPayrollTemplateService;

    @RequestMapping(value = "/listPrsPayrollTemplates")
    public JsonResult list(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.listPrsPayrollTemplates(params));
    }

    @RequestMapping(value = "/pagePrsPayrollTemplates")
    public JsonResult page(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.pagePrsPayrollTemplates(params));
    }

    @RequestMapping(value = "/getPrsPayrollTemplate")
    public JsonResult get(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.getPrsPayrollTemplate(params));
    }

    @RequestMapping(value = "/addPrsPayrollTemplate")
    public JsonResult add(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.addPrsPayrollTemplate(params));
    }

    @RequestMapping(value = "/updatePrsPayrollTemplate")
    public JsonResult update(@RequestBody Map<String, Object> params) {
        return JsonResult.success(prsPayrollTemplateService.updatePrsPayrollTemplate(params));
    }

    @PostMapping(value = "/uploadPrsPayrollTemplate")
    public JsonResult upload(@RequestParam("file") MultipartFile multipartFile) {

        try {
            String dist = FileHandler.uploadFile(multipartFile.getInputStream());
            return JsonResult.success(dist);
        } catch (Exception e) {
            e.printStackTrace();
            return JsonResult.faultMessage();
        }
    }

}
