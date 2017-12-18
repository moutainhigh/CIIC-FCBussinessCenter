package com.ciicsh.gto.fcsupportcenter.tax.queryservice.host.controller;

import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.EmployeeBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForSubDetail;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForEmployee;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubDetail;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.RequestSubProofDetailDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.api.dto.TaskSubProofDetailDTO;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcsupportcenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/14
 */
@RestController
public class TaskSubProofDetailController {

    @Autowired
    private TaskSubProofDetailService taskSubProofDetailService;

    /**
     * 查询完税申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskSubProofDetail")
    public JsonResult queryTaskSubProofDetail(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubDetail responseForSubDetail = taskSubProofDetailService.queryTaskSubProofDetail(requestForProof);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(responseForSubDetail);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }

    /**
     * 查询雇员信息(测试接口)
     *
     * @param taskProofDTO
     * @return
     */
    @RequestMapping(value = "/queryEmployee")
    public JsonResult queryEmployee(@RequestBody TaskProofDTO taskProofDTO) {
        int begin = (taskProofDTO.getCurrentNum() - 1) * taskProofDTO.getPageSize() + 1;
        int end = taskProofDTO.getCurrentNum() * taskProofDTO.getPageSize() + 1;
        int pageEnd  = end > 18 ? 18 : end;
        int total = 18 ;
        JsonResult jr = new JsonResult();
        List<EmployeeBO> employeeBOList = new ArrayList<>();
        ResponseForEmployee responseForEmployee = new ResponseForEmployee();
        try {
            if ("".equals(taskProofDTO.getDeclareAccount())) {
                for (int i = begin; i < pageEnd; i++) {
                    EmployeeBO employeeBO = new EmployeeBO();
                    employeeBO.setId(Long.valueOf(i));
                    employeeBO.setEmployeeNo("NO20171215" + i);
                    employeeBO.setEmployeeName("test" + i);
                    employeeBO.setIdType("01");
                    employeeBO.setIdNo("32100019901010123" + i);
                    employeeBO.setManageNo("WP20171208175700" + i);
                    employeeBO.setManageName("百盛餐饮集" + i);
                    employeeBO.setDeclareAcount("WPSUB201712111130000" + i);
                    employeeBOList.add(employeeBO);
                }
            } else {
                for (int i = begin; i < pageEnd; i++) {
                    EmployeeBO employeeBO = new EmployeeBO();
                    employeeBO.setId(Long.valueOf(i));
                    employeeBO.setEmployeeNo("NO20171215" + i);
                    employeeBO.setEmployeeName("test" + i);
                    employeeBO.setIdType("01");
                    employeeBO.setIdNo("32100019901010123" + i);
                    employeeBO.setManageNo("WP20171208175700");
                    employeeBO.setManageName("百盛餐饮集");
                    employeeBO.setDeclareAcount("WPSUB2017121111300001");
                    employeeBOList.add(employeeBO);
                }
            }
            responseForEmployee.setTotalNum(total);
            responseForEmployee.setCurrentNum(taskProofDTO.getCurrentNum());
            responseForEmployee.setPageSize(taskProofDTO.getPageSize());
            responseForEmployee.setRowList(employeeBOList);
            jr.setErrorcode("200");
            jr.setErrormsg("success");
            jr.setData(responseForEmployee);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("500");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }


    /**
     * 批量保存完税凭证申请明细
     * @param requestSubProofDetailDTO
     * @return
     */
    @RequestMapping(value = "/saveSubProofDetail")
    public JsonResult saveSubProofDetail(@RequestBody RequestSubProofDetailDTO requestSubProofDetailDTO){
        RequestForSubDetail requestForSubDetail = new RequestForSubDetail();
        BeanUtils.copyProperties(requestSubProofDetailDTO,requestForSubDetail);
        if(requestSubProofDetailDTO.getOldDeleteIds() != null && requestSubProofDetailDTO.getOldDeleteIds().length > 0){
            requestForSubDetail.setOldDeleteIds(requestSubProofDetailDTO.getOldDeleteIds());
        }
        List<TaskSubProofDetailBO> taskSubProofDetailBOList = new ArrayList<>();
        List<TaskSubProofDetailDTO> taskSubProofDetailDTOList = requestSubProofDetailDTO.getTaskSubProofDetailDTOList();
        for(TaskSubProofDetailDTO taskSubProofDetailDTO:taskSubProofDetailDTOList){
            TaskSubProofDetailBO taskSubProofDetailBO = new TaskSubProofDetailBO();
            BeanUtils.copyProperties(taskSubProofDetailDTO,taskSubProofDetailBO);
//            taskSubProofDetailBO.setIncomeStart(taskSubProofDetailDTO.getIncomeStart());
            taskSubProofDetailBOList.add(taskSubProofDetailBO);
        }

        requestForSubDetail.setTaskSubProofDetailBOList(taskSubProofDetailBOList);
        Boolean flag = taskSubProofDetailService.saveSubProofDetail(requestForSubDetail);
        JsonResult jr = new JsonResult();
        jr.setErrorcode("200");
        jr.setErrormsg("success");
        jr.setData(flag);
        return jr;
    }

}
