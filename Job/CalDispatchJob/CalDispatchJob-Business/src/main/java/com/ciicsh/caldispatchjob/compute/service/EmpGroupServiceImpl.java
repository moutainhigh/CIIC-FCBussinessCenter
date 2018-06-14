package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeContractProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.GetEmployeeContractsDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmployeeContractResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import com.mongodb.util.JSON;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class EmpGroupServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupServiceImpl.class);

    @Autowired
    private EmployeeContractProxy employeeContractProxy;

    @Autowired
    private PrEmployeeMapper employeeMapper;

    @Autowired
    private EmpGroupMongoOpt empGroupMongoOpt;

    /**
     * 当雇员组增加雇员时：Mongodb 表 pr_emp_group_table - 更新或者添加雇员基本信息字段，雇员服务协议字段，雇员扩展字段
     * @param empGroupCode
     * @param empIds
     * @return
     */
    public int batchInsertOrUpdateGroupEmployees(String empGroupCode, List<String> empIds) {

        List<PrEmployeePO> employees = employeeMapper.getEmployeesByGroupCode(empGroupCode);
        HashMap<String,EmployeeContractResponseDTO> mapList = new HashMap<>();
        if (empIds != null ) {
            employees = employees.stream().filter(emp -> {
                return empIds.contains(emp.getEmployeeId());
            }).collect(Collectors.toList());

            GetEmployeeContractsDTO contractsDTO = new GetEmployeeContractsDTO();
            contractsDTO.setEmpIds(empIds);
            JsonResult<List<EmployeeContractResponseDTO>> result = employeeContractProxy.getEmployeeContracts(contractsDTO);
            if(result.isSuccess()){
                mapList = result.getData().stream().collect(
                        HashMap<String, EmployeeContractResponseDTO>::new,
                        (m,c) -> m.put(c.getEmpId(),c),
                        (m,u)-> {}
                );
            }else {
                logger.info(result.getMessage());
                return 0;
            }

        }
        if(mapList.size() == 0){
            logger.info("该雇员组中没有雇员服务协议");
            //return 0;
        }

        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        int rowAffected = 0;

        for (PrEmployeePO item : employees) {
            /*if(checkExistEmp(empGroupCode,item.getEmployeeId())) //存在忽略
                break;
            emp = new BasicDBObject();
            emp.put("emp_group_code",empGroupCode);
            emp.put(PayItemName.EMPLOYEE_CODE_CN,item.getEmployeeId());*/
            BasicDBObject basicDBObject = new BasicDBObject();
            //emp.put("base_info",basicDBObject);
            basicDBObject.put(PayItemName.EMPLOYEE_CODE_CN,item.getEmployeeId());
            basicDBObject.put(PayItemName.EMPLOYEE_ID,item.getEmpCode());
            basicDBObject.put(PayItemName.EMPLOYEE_NAME_CN,item.getEmployeeName());
            basicDBObject.put(PayItemName.EMPLOYEE_BIRTHDAY_CN, item.getBirthday() == null ? "" : formatter.format(item.getBirthday()));
            //basicDBObject.put(PayItemName.EMPLOYEE_DEP_CN,item.getDepartment());
            basicDBObject.put(PayItemName.EMPLOYEE_SEX_CN, item.getGender()? "男":"女");
            basicDBObject.put(PayItemName.EMPLOYEE_ID_TYPE_CN,item.getIdCardType());
            basicDBObject.put(PayItemName.EMPLOYEE_ONBOARD_CN, item.getInDate() == null ? "" : formatter.format(item.getInDate()));
            basicDBObject.put(PayItemName.EMPLOYEE_ID_NUM_CN,item.getIdNum());
            //basicDBObject.put(PayItemName.EMPLOYEE_POSITION_CN,item.getPosition());
            basicDBObject.put(PayItemName.EMPLOYEE_COMPANY_ID,item.getCompanyId());
            basicDBObject.put(PayItemName.EMPLOYEE_FORMER_CN, item.getFormerName());
            basicDBObject.put(PayItemName.EMPLOYEE_COUNTRY_CODE_CN,item.getCountryCode());
            basicDBObject.put(PayItemName.EMPLOYEE_PROVINCE_CODE_CN,item.getProvinceCode());
            basicDBObject.put(PayItemName.EMPLOYEE_CITY_CODE_CN,item.getCityCode());
            basicDBObject.put(PayItemName.NATIONALITY, item.getCountryName());
            basicDBObject.put(PayItemName.LEAVE_DATE, item.getOutDate() == null ? "" : formatter.format(item.getOutDate()));

            String emp_json_agreement = com.alibaba.fastjson.JSON.toJSONString(mapList.get(item.getEmployeeId()));
            if (StringUtils.isNotEmpty(emp_json_agreement)) {
                basicDBObject.put(PayItemName.EMPLOYEE_SERVICE_AGREE, (DBObject) JSON.parse(emp_json_agreement));
            }
            Criteria criteria = Criteria.where("emp_group_code").is(empGroupCode).
                    andOperator(Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(item.getEmployeeId()));
            Query query = Query.query(criteria);
            Update update = Update.update("base_info",basicDBObject);

            WriteResult result = empGroupMongoOpt.getMongoTemplate().upsert(query,update,EmpGroupMongoOpt.PR_EMPLOYEE_GROUP);
            if(result != null){
                rowAffected += result.getN();
            }
        }
        empGroupMongoOpt.createIndex();
        return rowAffected;
    }

    public int batchInsertOrUpdateGroupEmployees(String empGroupCode) {
        return batchInsertOrUpdateGroupEmployees(empGroupCode,null);
    }


    /**
     * 雇员组中删除雇员：Mongodb 表 pr_emp_group_table 删除雇员
     * @param empGroupIds
     * @param empIds
     * @return
     */
    public int batchDelGroupEmployees(List<String> empGroupIds, List<String> empIds){

        int rowAffected = 0;
        if(empIds != null && empIds.size() > 0) {
            String groupId =  empGroupIds.get(0);
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_code").is(groupId)
                    .andOperator(Criteria.where(PayItemName.EMPLOYEE_CODE_CN).in(empIds)));
        }else {
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_code").in(empGroupIds));
        }

        logger.info("deleted affected rows :" + rowAffected);

        return rowAffected;
    }


    /**
     * 根据雇员组Code 获取 雇员列表：逻辑－ mongodb是否存在，如果不存在，mysqlDB 获取雇员列表
     * @param empGroupCode
     * @return
     */
    public List<DBObject> getEmployeesByEmpGroupCode(String empGroupCode){
        List<DBObject> list = null;

        list = empGroupMongoOpt.list(Criteria.where("emp_group_code").is(empGroupCode));
        if(list == null || list.size() == 0){
            batchInsertOrUpdateGroupEmployees(empGroupCode);
            return empGroupMongoOpt.list(Criteria.where("emp_group_code").is(empGroupCode));
        }
        return list;
    }

    private boolean checkExistEmp(String empGroupCode, String empCode) {
        boolean exist = false;
        Criteria criteria = Criteria.where("emp_group_code").is(empGroupCode).
                andOperator(Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode));
        Query query = Query.query(criteria);
        query.fields().include("emp_group_code");
        long total = empGroupMongoOpt.getMongoTemplate().count(query, EmpGroupMongoOpt.PR_EMPLOYEE_GROUP);
        if (total > 0) {
            exist = true;
        }
        return exist;
    }

}
