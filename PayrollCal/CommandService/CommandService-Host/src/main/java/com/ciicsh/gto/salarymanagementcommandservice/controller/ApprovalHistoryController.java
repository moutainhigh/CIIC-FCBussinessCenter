package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.ApprovalHistoryDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ApprovalHistoryTranslator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by NeoJiang on 2018/2/2.
 *
 * @author NeoJiang
 */
@RestController
@RequestMapping(value = "/api/salaryManagement")
public class ApprovalHistoryController {

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @GetMapping(value = "/approvalHistory")
    public JsonResult getApprovalHistoryList(@RequestParam String bizCode, @RequestParam Integer bizType) {
        List<ApprovalHistoryPO> approvalHistoryPOList = approvalHistoryService.getApprovalHistory(bizType, bizCode);
        List<ApprovalHistoryDTO> resultList = approvalHistoryPOList.stream()
                .map(ApprovalHistoryTranslator::toApprovalHistoryDTO)
                .sorted(Comparator.comparing(ApprovalHistoryDTO::getId).reversed()) // id 降序排列
                .collect(Collectors.toList());
        return JsonResult.success(resultList);
    }

    @GetMapping(value = "/test")
    public JsonResult test(){
        String url = "http://172.16.9.28:9100/5,07e54c5f9981";
        boolean check = FileHandler.checkFileExists(url);

        return JsonResult.success(check);

    }
}
