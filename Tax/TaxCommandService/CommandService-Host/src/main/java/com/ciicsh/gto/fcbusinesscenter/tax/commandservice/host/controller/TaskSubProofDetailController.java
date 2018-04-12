package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.SubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskProofDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubProofDetailDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.TaskSubProofDetailProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.EmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForEmployee;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author yuantongqing on 2017/12/14
 */
@RestController
public class TaskSubProofDetailController extends BaseController implements TaskSubProofDetailProxy {


    @Autowired
    private TaskSubProofDetailService taskSubProofDetailService;


    /**
     * 查询完税申请明细
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForSubDetail> queryTaskSubProofDetail(@RequestBody TaskProofDTO taskProofDTO) {
        JsonResult<ResponseForSubDetail> jr = new JsonResult<>();
        try {
            RequestForProof requestForProof = new RequestForProof();
            BeanUtils.copyProperties(taskProofDTO, requestForProof);
            ResponseForSubDetail responseForSubDetail = taskSubProofDetailService.queryTaskSubProofDetail(requestForProof);
            jr.fill(responseForSubDetail);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("detailType", taskProofDTO.getDetailType());
            tags.put("id", taskProofDTO.getId().toString());
            tags.put("employeeNo", taskProofDTO.getEmployeeNo());
            tags.put("employeeName", taskProofDTO.getEmployeeName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofDetailController.queryTaskSubProofDetail", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 查询雇员信息 TODO 临时接口方法
     *
     * @param taskProofDTO
     * @return
     */
    @Override
    public JsonResult<ResponseForEmployee> queryEmployee(@RequestBody TaskProofDTO taskProofDTO) {
        //主任务标识
        String main = "main";
        JsonResult<ResponseForEmployee> jr = new JsonResult<>();
        List<EmployeeBO> employeeBOList = new ArrayList<>();
        ResponseForEmployee responseForEmployee = new ResponseForEmployee();
        try {
            if (main.equals(taskProofDTO.getDetailType())) {
                if ("蓝天科技".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO1 = new EmployeeBO();
                    employeeBO1.setId(Long.valueOf(1));
                    employeeBO1.setEmployeeNo("17A13012");
                    employeeBO1.setEmployeeName("李晓");
                    employeeBO1.setIdType("01");
                    employeeBO1.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO1.getIdType()));
                    employeeBO1.setIdNo("321000199010101234");
                    employeeBO1.setManagerNo("GL170001");
                    employeeBO1.setManagerName("蓝天科技");
                    employeeBO1.setDeclareAccount("蓝天科技独立户");
                    employeeBOList.add(employeeBO1);
                } else if ("西门子".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO2 = new EmployeeBO();
                    employeeBO2.setId(Long.valueOf(2));
                    employeeBO2.setEmployeeNo("17A13497");
                    employeeBO2.setEmployeeName("张名");
                    employeeBO2.setIdType("01");
                    employeeBO2.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO2.getIdType()));
                    employeeBO2.setIdNo("323891199003103290");
                    employeeBO2.setManagerNo("GL170002");
                    employeeBO2.setManagerName("西门子");
                    employeeBO2.setDeclareAccount("西门子独立户");
                    employeeBOList.add(employeeBO2);
                } else if ("联想".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO3 = new EmployeeBO();
                    employeeBO3.setId(Long.valueOf(3));
                    employeeBO3.setEmployeeNo("17A13513");
                    employeeBO3.setEmployeeName("刘文华");
                    employeeBO3.setIdType("01");
                    employeeBO3.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO3.getIdType()));
                    employeeBO3.setIdNo("321110197108239090");
                    employeeBO3.setManagerNo("GL170003");
                    employeeBO3.setManagerName("联想");
                    employeeBO3.setDeclareAccount("联想独立户");
                    employeeBOList.add(employeeBO3);
                }
            } else {
                if ("蓝天科技".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO1 = new EmployeeBO();
                    employeeBO1.setId(Long.valueOf(1));
                    employeeBO1.setEmployeeNo("17A13012");
                    employeeBO1.setEmployeeName("李晓");
                    employeeBO1.setIdType("01");
                    employeeBO1.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO1.getIdType()));
                    employeeBO1.setIdNo("321000199010101234");
                    employeeBO1.setManagerNo("GL170001");
                    employeeBO1.setManagerName("蓝天科技");
                    employeeBO1.setDeclareAccount("蓝天科技独立户");
                    employeeBOList.add(employeeBO1);
                } else if ("西门子".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO2 = new EmployeeBO();
                    employeeBO2.setId(Long.valueOf(2));
                    employeeBO2.setEmployeeNo("17A13497");
                    employeeBO2.setEmployeeName("张名");
                    employeeBO2.setIdType("01");
                    employeeBO2.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO2.getIdType()));
                    employeeBO2.setIdNo("323891199003103290");
                    employeeBO2.setManagerNo("GL170002");
                    employeeBO2.setManagerName("西门子");
                    employeeBO2.setDeclareAccount("西门子独立户");
                    employeeBOList.add(employeeBO2);
                } else if ("联想".equals(taskProofDTO.getManagerName())) {
                    EmployeeBO employeeBO3 = new EmployeeBO();
                    employeeBO3.setId(Long.valueOf(3));
                    employeeBO3.setEmployeeNo("17A13513");
                    employeeBO3.setEmployeeName("刘文华");
                    employeeBO3.setIdType("01");
                    employeeBO3.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, employeeBO3.getIdType()));
                    employeeBO3.setIdNo("321110197108239090");
                    employeeBO3.setManagerNo("GL170003");
                    employeeBO3.setManagerName("联想");
                    employeeBO3.setDeclareAccount("联想独立户");
                    employeeBOList.add(employeeBO3);
                }
            }
            responseForEmployee.setTotalNum(1);
            responseForEmployee.setCurrentNum(taskProofDTO.getCurrentNum());
            responseForEmployee.setPageSize(taskProofDTO.getPageSize());
            responseForEmployee.setRowList(employeeBOList);
            jr.fill(responseForEmployee);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("detailType", taskProofDTO.getDetailType());
            tags.put("taskId", taskProofDTO.getTaskId().toString());
            tags.put("declareAccount", taskProofDTO.getDeclareAccount());
            tags.put("managerName", taskProofDTO.getManagerName());
            tags.put("employeeNo", taskProofDTO.getEmployeeNo());
            tags.put("employeeName", taskProofDTO.getEmployeeName());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofDetailController.queryEmployee", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }


    /**
     * 批量保存完税凭证申请明细
     *
     * @param subProofDetailDTO
     * @return
     */
    @Override
    public JsonResult<Boolean> saveSubProofDetail(@RequestBody SubProofDetailDTO subProofDetailDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForSubDetail requestForSubDetail = new RequestForSubDetail();
            BeanUtils.copyProperties(subProofDetailDTO, requestForSubDetail);
            //需要完税凭证明细的BO集合
            List<TaskSubProofDetailBO> taskSubProofDetailBOList = new ArrayList<>();
            List<TaskSubProofDetailDTO> taskSubProofDetailDTOList = subProofDetailDTO.getTaskSubProofDetailDTOList();
            //循环DTO集合对象转成对应BO集合对象
            for (TaskSubProofDetailDTO taskSubProofDetailDTO : taskSubProofDetailDTOList) {
                TaskSubProofDetailBO taskSubProofDetailBO = new TaskSubProofDetailBO();
                BeanUtils.copyProperties(taskSubProofDetailDTO, taskSubProofDetailBO);
                //设置个税期间起
                if (taskSubProofDetailDTO.getIncomeStart() != null && !"".equals(taskSubProofDetailDTO.getIncomeStart())) {
                    taskSubProofDetailBO.setIncomeStart(DateTimeKit.dateToLocalDate(taskSubProofDetailDTO.getIncomeStart()));
                }
                //设置个税期间止
                if (taskSubProofDetailDTO.getIncomeEnd() != null && !"".equals(taskSubProofDetailDTO.getIncomeEnd())) {
                    taskSubProofDetailBO.setIncomeEnd(DateTimeKit.dateToLocalDate(taskSubProofDetailDTO.getIncomeEnd()));
                }
                //如果个税期间止为空，则赋值为个税期间起
                if (taskSubProofDetailDTO.getIncomeStart() != null && !"".equals(taskSubProofDetailDTO.getIncomeStart()) && taskSubProofDetailDTO.getIncomeEnd() == null) {
                    taskSubProofDetailBO.setIncomeEnd(DateTimeKit.dateToLocalDate(taskSubProofDetailDTO.getIncomeStart()));
                }
                taskSubProofDetailBOList.add(taskSubProofDetailBO);
            }
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            requestForSubDetail.setModifiedBy(userInfoResponseDTO.getLoginName());
            requestForSubDetail.setTaskSubProofDetailBOList(taskSubProofDetailBOList);
            taskSubProofDetailService.saveSubProofDetail(requestForSubDetail);
            ////jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("detailType", subProofDetailDTO.getDetailType());
            tags.put("taskId", subProofDetailDTO.getTaskId().toString());
            tags.put("oldDeleteIds", subProofDetailDTO.getOldDeleteIds().toString());
            tags.put("taskSubProofDetailDTOList", subProofDetailDTO.getTaskSubProofDetailDTOList().toString());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskSubProofDetailController.saveSubProofDetail", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "05"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }
}
