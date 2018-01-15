package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.SubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.EmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForEmployee;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.json.JsonResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/14
 */
@RestController
public class TaskSubProofDetailController extends BaseController {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubProofDetailController.class);

    @Autowired
    private TaskSubProofDetailService taskSubProofDetailService;


    /**
     * 查询完税申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @PostMapping(value = "/queryTaskSubProofDetail")
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
            logger.error("queryTaskSubProofDetail error " + e.toString());
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
    @PostMapping(value = "/queryEmployee")
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
                employeeBO1.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO1.getIdType()));
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
                employeeBO2.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO2.getIdType()));
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
                employeeBO3.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO3.getIdType()));
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
                employeeBO4.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO4.getIdType()));
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
                employeeBO5.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO5.getIdType()));
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
                employeeBO1.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO1.getIdType()));
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
                employeeBO2.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO2.getIdType()));
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
                employeeBO3.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO3.getIdType()));
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
                employeeBO4.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO4.getIdType()));
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
                employeeBO5.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,employeeBO5.getIdType()));
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
            logger.error("queryEmployee error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }


    /**
     * 批量保存完税凭证申请明细
     *
     * @param subProofDetailDTO
     * @return
     */
    @PostMapping(value = "/saveSubProofDetail")
    public JsonResult saveSubProofDetail(@RequestBody SubProofDetailDTO subProofDetailDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForSubDetail requestForSubDetail = new RequestForSubDetail();
            BeanUtils.copyProperties(subProofDetailDTO, requestForSubDetail);
            if (subProofDetailDTO.getOldDeleteIds() != null && subProofDetailDTO.getOldDeleteIds().length > 0) {
                requestForSubDetail.setOldDeleteIds(subProofDetailDTO.getOldDeleteIds());
            }
            //需要完税凭证明细的BO集合
            List<TaskSubProofDetailBO> taskSubProofDetailBOList = new ArrayList<>();
            List<TaskSubProofDetailDTO> taskSubProofDetailDTOList = subProofDetailDTO.getTaskSubProofDetailDTOList();
            //循环DTO集合对象转成对应BO集合对象
            for (TaskSubProofDetailDTO taskSubProofDetailDTO : taskSubProofDetailDTOList) {
                TaskSubProofDetailBO taskSubProofDetailBO = new TaskSubProofDetailBO();
                BeanUtils.copyProperties(taskSubProofDetailDTO, taskSubProofDetailBO);
                //如果个税期间止为空，则赋值为个税期间起
                if (taskSubProofDetailDTO.getIncomeStart() != null && !"".equals(taskSubProofDetailDTO.getIncomeStart()) && taskSubProofDetailDTO.getIncomeEnd() == null) {
                    taskSubProofDetailBO.setIncomeEnd(taskSubProofDetailDTO.getIncomeStart());
                }
                taskSubProofDetailBOList.add(taskSubProofDetailBO);
            }
            requestForSubDetail.setTaskSubProofDetailBOList(taskSubProofDetailBOList);
            taskSubProofDetailService.saveSubProofDetail(requestForSubDetail);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(true);
        } catch (Exception e) {
            logger.error("saveSubProofDetail error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        } finally {
            return jr;
        }
    }
}
