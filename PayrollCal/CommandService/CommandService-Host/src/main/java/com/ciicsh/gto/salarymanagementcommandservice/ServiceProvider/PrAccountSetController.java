package com.ciicsh.gto.salarymanagementcommandservice.ServiceProvider;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.ciicsh.gto.salarymanagementcommandservice.api.PayrollAccountProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.Custom.AccountSetWithItemsDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollAccountSetDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrPayrollItemDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrAccountSetService;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.translator.ItemTranslator;
import com.ciicsh.gto.salarymanagementcommandservice.translator.PayrollAccountSetTranslator;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/2/8.
 */
@RestController
@RequestMapping("/api/prAccountSet")
public class PrAccountSetController implements PayrollAccountProxy {

    @Autowired
    private PrAccountSetService prAccountSetService;

    @Autowired
    private PrItemService itemService;

    @RequestMapping("/getAccountSets")
    public JsonResult getAccountSets(@RequestParam String managementId,
                                     @RequestParam(required = false, defaultValue = "20") Integer pageSize,
                                     @RequestParam(required = false, defaultValue = "1") Integer pageNum ) {

        PrPayrollAccountSetPO payrollAccountSetPO  = new PrPayrollAccountSetPO();
        payrollAccountSetPO.setManagementId(managementId);
        PageInfo<PrPayrollAccountSetPO> pageInfo = prAccountSetService.getPayrollAccountSets(payrollAccountSetPO, pageNum,pageSize);
        List<PrPayrollAccountSetDTO> prPayrollAccountSets = pageInfo.getList()
                .stream()
                .map(PayrollAccountSetTranslator::toPrPayrollAccountSetDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollAccountSetDTO> resultPage = new PageInfo<>(prPayrollAccountSets);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    @RequestMapping("/getPrItemList")
    public JsonResult getPrItemList(@RequestParam String accountSetCode,
                                    @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                    @RequestParam(required = false, defaultValue = "50") Integer pageSize) {

        PrPayrollAccountSetPO accountSetInfo = prAccountSetService.getAccountSetInfo(accountSetCode);

        PageInfo<PrPayrollItemPO> pageInfo;
        if (accountSetInfo.getIfGroupTemplate()) {
            // 获取薪资组模板的薪资项
            pageInfo = itemService.getListByGroupTemplateCode(accountSetInfo.getPayrollGroupTemplateCode(), pageNum, pageSize);
        } else {
            // 获取薪资组的薪资项
            pageInfo = itemService.getListByGroupCode(accountSetInfo.getPayrollGroupCode(), pageNum, pageSize);
        }
        List<PrPayrollItemDTO> resultList = pageInfo.getList()
                .stream()
                .map(ItemTranslator::toPrPayrollItemDTO)
                .collect(Collectors.toList());
        PageInfo<PrPayrollItemDTO> resultPage = new PageInfo<>(resultList);
        BeanUtils.copyProperties(pageInfo, resultPage, "list");
        return JsonResult.success(resultPage);
    }

    @Override
    @GetMapping("/getAccountSetsByManagementId")
    public List<AccountSetWithItemsDTO> getAccountSetsByManagementId(String managementId) {
        if (StringUtils.isEmpty(managementId)) {
            return null;
        }
        List<PrAccountSetOptPO> poList = prAccountSetService.getAccountSetWithItemsByManagementId(managementId);
        List<AccountSetWithItemsDTO> resultList = JSON.parseObject(JSON.toJSONString(poList)
                , new TypeReference<List<AccountSetWithItemsDTO>>(){});
        return resultList;
    }
}
