package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.RequestSubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.EmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForEmployee;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubDetail;

import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
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
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForSubDetail);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
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
//        int begin = (taskProofDTO.getCurrentNum() - 1) * taskProofDTO.getPageSize() + 1;
//        int end = taskProofDTO.getCurrentNum() * taskProofDTO.getPageSize() + 1;
//        int pageEnd  = end > 18 ? 18 : end;
//        int total = 18 ;
        JsonResult jr = new JsonResult();
        List<EmployeeBO> employeeBOList = new ArrayList<>();
        ResponseForEmployee responseForEmployee = new ResponseForEmployee();
        try {
            if ("main".equals(taskProofDTO.getDetailType())) {
                EmployeeBO employeeBO1 = new EmployeeBO();
                employeeBO1.setId(Long.valueOf(1));
                employeeBO1.setEmployeeNo("17A13012");
                employeeBO1.setEmployeeName("李晓");
                employeeBO1.setIdType("01");
                employeeBO1.setIdNo("321000199010101234");
                employeeBO1.setManagerNo("GL170001");
                employeeBO1.setManagerName("蓝天科技");
                employeeBO1.setDeclareAccount("蓝天科技上海独立户");
                employeeBOList.add(employeeBO1);
                EmployeeBO employeeBO2 = new EmployeeBO();
                employeeBO2.setId(Long.valueOf(2));
                employeeBO2.setEmployeeNo("17A13497");
                employeeBO2.setEmployeeName("张名");
                employeeBO2.setIdType("01");
                employeeBO2.setIdNo("323891199003103290");
                employeeBO2.setManagerNo("GL170001");
                employeeBO2.setManagerName("蓝天科技");
                employeeBO2.setDeclareAccount("中智上海财务咨询公司大库");
                employeeBOList.add(employeeBO2);
                EmployeeBO employeeBO3 = new EmployeeBO();
                employeeBO3.setId(Long.valueOf(3));
                employeeBO3.setEmployeeNo("17A13513");
                employeeBO3.setEmployeeName("刘文华");
                employeeBO3.setIdType("01");
                employeeBO3.setIdNo("321110197108239090");
                employeeBO3.setManagerNo("GL170001");
                employeeBO3.setManagerName("蓝天科技");
                employeeBO3.setDeclareAccount("蓝天科技苏州独立户");
                employeeBOList.add(employeeBO3);
                EmployeeBO employeeBO4 = new EmployeeBO();
                employeeBO4.setId(Long.valueOf(3));
                employeeBO4.setEmployeeNo("17A13542");
                employeeBO4.setEmployeeName("蒋文文");
                employeeBO4.setIdType("01");
                employeeBO4.setIdNo("322140199211116510");
                employeeBO4.setManagerNo("GL170001");
                employeeBO4.setManagerName("蓝天科技");
                employeeBO4.setDeclareAccount("无锡供应商A大库");
                employeeBOList.add(employeeBO4);
                EmployeeBO employeeBO5 = new EmployeeBO();
                employeeBO5.setId(Long.valueOf(3));
                employeeBO5.setEmployeeNo("17A13101");
                employeeBO5.setEmployeeName("许强");
                employeeBO5.setIdType("01");
                employeeBO5.setIdNo("320171198810106787");
                employeeBO5.setManagerNo("GL170001");
                employeeBO5.setManagerName("蓝天科技");
                employeeBO5.setDeclareAccount("蓝天科技无锡独立户");
                employeeBOList.add(employeeBO5);
//                for (int i = begin; i < pageEnd; i++) {
//                    EmployeeBO employeeBO = new EmployeeBO();
//                    employeeBO.setId(Long.valueOf(i));
//                    employeeBO.setEmployeeNo("NO20171215" + i);
//                    employeeBO.setEmployeeName("main" + i);
//                    employeeBO.setIdType("01");
//                    employeeBO.setIdNo("32100019901010123" + i);
//                    employeeBO.setManagerNo("WP20171208175700" + i);
//                    employeeBO.setManagerName("百盛餐饮集");
//                    employeeBO.setDeclareAccount("WPSUB201712111130000" + i);
//                    employeeBOList.add(employeeBO);
//                }
            } else {
                EmployeeBO employeeBO1 = new EmployeeBO();
                employeeBO1.setId(Long.valueOf(1));
                employeeBO1.setEmployeeNo("17A13012");
                employeeBO1.setEmployeeName("李晓");
                employeeBO1.setIdType("01");
                employeeBO1.setIdNo("321000199010101234");
                employeeBO1.setManagerNo("GL170001");
                employeeBO1.setManagerName("蓝天科技");
                employeeBO1.setDeclareAccount("蓝天科技上海独立户");
                employeeBOList.add(employeeBO1);
                EmployeeBO employeeBO2 = new EmployeeBO();
                employeeBO2.setId(Long.valueOf(2));
                employeeBO2.setEmployeeNo("17A13497");
                employeeBO2.setEmployeeName("张名");
                employeeBO2.setIdType("01");
                employeeBO2.setIdNo("323891199003103290");
                employeeBO2.setManagerNo("GL170001");
                employeeBO2.setManagerName("蓝天科技");
                employeeBO2.setDeclareAccount("中智上海财务咨询公司大库");
                employeeBOList.add(employeeBO2);
                EmployeeBO employeeBO3 = new EmployeeBO();
                employeeBO3.setId(Long.valueOf(3));
                employeeBO3.setEmployeeNo("17A13513");
                employeeBO3.setEmployeeName("刘文华");
                employeeBO3.setIdType("01");
                employeeBO3.setIdNo("321110197108239090");
                employeeBO3.setManagerNo("GL170001");
                employeeBO3.setManagerName("蓝天科技");
                employeeBO3.setDeclareAccount("蓝天科技苏州独立户");
                employeeBOList.add(employeeBO3);
                EmployeeBO employeeBO4 = new EmployeeBO();
                employeeBO4.setId(Long.valueOf(3));
                employeeBO4.setEmployeeNo("17A13542");
                employeeBO4.setEmployeeName("蒋文文");
                employeeBO4.setIdType("01");
                employeeBO4.setIdNo("322140199211116510");
                employeeBO4.setManagerNo("GL170001");
                employeeBO4.setManagerName("蓝天科技");
                employeeBO4.setDeclareAccount("无锡供应商A大库");
                employeeBOList.add(employeeBO4);
                EmployeeBO employeeBO5 = new EmployeeBO();
                employeeBO5.setId(Long.valueOf(3));
                employeeBO5.setEmployeeNo("17A13101");
                employeeBO5.setEmployeeName("许强");
                employeeBO5.setIdType("01");
                employeeBO5.setIdNo("320171198810106787");
                employeeBO5.setManagerNo("GL170001");
                employeeBO5.setManagerName("蓝天科技");
                employeeBO5.setDeclareAccount("蓝天科技无锡独立户");
                employeeBOList.add(employeeBO5);
//                for (int i = begin; i < pageEnd; i++) {
//                    EmployeeBO employeeBO = new EmployeeBO();
//                    employeeBO.setId(Long.valueOf(i));
//                    employeeBO.setEmployeeNo("NO20171215" + i);
//                    employeeBO.setEmployeeName("sub" + i);
//                    employeeBO.setIdType("01");
//                    employeeBO.setIdNo("32100019901010123" + i);
//                    employeeBO.setManagerNo("WP20171208175700");
//                    employeeBO.setManagerName("百盛餐饮集");
//                    employeeBO.setDeclareAccount(taskProofDTO.getDeclareAccount());
//                    employeeBOList.add(employeeBO);
//                }
            }
            responseForEmployee.setTotalNum(5);
            responseForEmployee.setCurrentNum(taskProofDTO.getCurrentNum());
            responseForEmployee.setPageSize(taskProofDTO.getPageSize());
            responseForEmployee.setRowList(employeeBOList);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForEmployee);
        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
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
        JsonResult jr = new JsonResult();

        try {
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
                if(taskSubProofDetailDTO.getIncomeStart() != null && !"".equals(taskSubProofDetailDTO.getIncomeStart()) && taskSubProofDetailDTO.getIncomeEnd() == null ){
                    taskSubProofDetailBO.setIncomeEnd(taskSubProofDetailDTO.getIncomeStart());
                }
                taskSubProofDetailBOList.add(taskSubProofDetailBO);
            }
            requestForSubDetail.setTaskSubProofDetailBOList(taskSubProofDetailBOList);
            Boolean flag = taskSubProofDetailService.saveSubProofDetail(requestForSubDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (BeansException e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }
}
