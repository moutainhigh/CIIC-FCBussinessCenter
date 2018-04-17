package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmpEmployeeQryRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmpEmployeeResponseDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.service.EmployeeTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by houwanhua on 2017/12/8.
 */
@RestController
@RequestMapping("/api/employeetest")
public class EmployTestController extends BaseController{

    @Autowired
    private EmployeeTestService employeeTestService;

    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;

//    @RequestMapping("/add")
//    public String add() throws ParseException {
//        Integer flag = 1;
//        SimpleDateFormat sdf  =   new SimpleDateFormat("yyyyMMdd");
//        for(Map.Entry<String, String> management:getManagement().entrySet()){
//            for(Integer i = 0;i<50;i++){
//                for(Map.Entry<Integer,String> depart:getDepartment().entrySet()){
//                    for (Map.Entry<Integer,String> pos:getPosition().entrySet()){
//                        PrEmployeePO employeeTestPO = new PrEmployeePO();
//                        employeeTestPO.setEmployeeId(IDCardUtils.randomEmployeeId(flag));
//                        employeeTestPO.setIdCardType(1);
//                        employeeTestPO.setIdNum(IDCardUtils.getIdCard());
//                        employeeTestPO.setEmployeeName(ChinesNameUtil.generatorName());
//                        employeeTestPO.setFormerName(ChinesNameUtil.generatorName());
//                        employeeTestPO.setManagementId(management.getKey());
//                        employeeTestPO.setGender(pos.getKey()%2 == 0 ? true:false);
//                        employeeTestPO.setBirthday(sdf.parse(IDCardUtils.randomBirth(18,70)));
//                        employeeTestPO.setJoinDate(sdf.parse("20170416"));
//                        employeeTestPO.setCountryCode(IDCardUtils.randomCityCode(4));
//                        employeeTestPO.setProvinceCode(IDCardUtils.randomCityCode(4));
//                        employeeTestPO.setCityCode(IDCardUtils.randomCityCode(4));
//                        employeeTestPO.setDepartment(depart.getValue());
//                        employeeTestPO.setPosition(pos.getValue());
//                        employeeTestPO.setCreatedTime(new Date());
//                        employeeTestPO.setModifiedTime(new Date());
//                        employeeTestPO.setCreatedBy("macor");
//                        employeeTestPO.setModifiedBy("macor");
//                        employeeTestService.addEmployeeTest(employeeTestPO);
//                        flag ++;
//                    }
//                }
//            }
//        }
//        return "添加成功!";
//    }

    @PostMapping("/getEmployees")
    public JsonResult getEmployees(@RequestBody PrEmployeePO employeeTestPO,
                                        @RequestParam(required = false, defaultValue = "1") Integer pageNum,
                                        @RequestParam(required = false, defaultValue = "50")  Integer pageSize) {
//        PageInfo<PrEmployeePO> pageInfo =  employeeTestService.getEmployees(employeeTestPO, pageNum,pageSize);
//        List<PrEmployeePO> employees = pageInfo.getList()
//                .stream()
//                .collect(Collectors.toList());
//        PageInfo<PrEmployeePO> resultPage = new PageInfo<>(employees);
//        BeanUtils.copyProperties(pageInfo, resultPage, "list");

        EmpEmployeeQryRequestDTO param = new EmpEmployeeQryRequestDTO();
        param.setPageNo(pageNum);
        param.setPageSize(pageSize);
        com.ciicsh.gto.companycenter.webcommandservice.api.JsonResult<Page<EmpEmployeeResponseDTO>> result
                = employeeServiceProxy.PageEmpEmployee(param);
//        Page<EmpEmployeeResponseDTO> data = result.getData();

        return JsonResult.success(result.getData());
    }

}
