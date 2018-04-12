package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.util.JSON;
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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class EmpGroupServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupServiceImpl.class);

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

        List<EmployeeExtensionPO> employees = employeeMapper.getEmployees(empGroupCode,"","");
        if (empIds != null ) {
            employees = employees.stream().filter(emp -> {
                return empIds.contains(emp.getEmployeeId());
            }).collect(Collectors.toList());
        }

        Format formatter = new SimpleDateFormat("yyyy-MM-dd");

        List<BathUpdateOptions> options = new ArrayList<>();
        DBObject basicDBObject = null;

        for (EmployeeExtensionPO item : employees) {
            BathUpdateOptions opt = new BathUpdateOptions();
            opt.setQuery(Query.query(Criteria.where("emp_group_code").is(item.getEmpGroupCode()).andOperator(Criteria.where("雇员编号").is(item.getEmployeeId()))));
            basicDBObject = new BasicDBObject();
            basicDBObject.put(PayItemName.EMPLOYEE_CODE_CN,item.getEmployeeId());
            basicDBObject.put(PayItemName.EMPLOYEE_NAME_CN,item.getEmployeeName());
            basicDBObject.put(PayItemName.EMPLOYEE_BIRTHDAY_CN,formatter.format(item.getBirthday()));
            basicDBObject.put(PayItemName.EMPLOYEE_DEP_CN,item.getDepartment());
            basicDBObject.put(PayItemName.EMPLOYEE_SEX_CN,item.getGender() == true ? "男":"女");
            basicDBObject.put(PayItemName.EMPLOYEE_ID_TYPE_CN,item.getIdCardType());
            basicDBObject.put(PayItemName.EMPLOYEE_ONBOARD_CN,formatter.format(item.getJoinDate()));
            basicDBObject.put(PayItemName.EMPLOYEE_ID_NUM_CN,item.getIdNum());
            basicDBObject.put(PayItemName.EMPLOYEE_POSITION_CN,item.getPosition());

            basicDBObject.put(PayItemName.EMPLOYEE_FORMER_CN,"");
            basicDBObject.put(PayItemName.EMPLOYEE_COUNTRY_CODE_CN,"");
            basicDBObject.put(PayItemName.EMPLOYEE_PROVINCE_CODE_CN,"");
            basicDBObject.put(PayItemName.EMPLOYEE_CITY_CODE_CN,"");


            //TODO 获取雇员服务协议和雇员扩展字段接口
            String emp_json_agreement = "{'薪资计算':{'薪资类型':{'复杂度':'复杂'}},'频率':'人月','金额':'1800／天'}";
            basicDBObject.put("雇员服务协议", (DBObject)JSON.parse(emp_json_agreement));

            String emp_json_extend = "{'field1':'56','field2':'特性字段','field3':'其他扩展字段'}";
            basicDBObject.put("雇员扩展字段", (DBObject)JSON.parse(emp_json_extend));

            opt.setUpdate(Update.update("base_info", basicDBObject));

            opt.setMulti(true);
            opt.setUpsert(true);

            options.add(opt);
        }

        empGroupMongoOpt.createIndex();
        int rowAffected = empGroupMongoOpt.doBathUpdate(options,false);
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

        String[] groupIDs = empGroupIds.toString().replace("[","").replace("]","").split(",");
        List<String> copyGroupIDs = Arrays.asList(groupIDs);

        logger.info("Group IDs : " + copyGroupIDs);

        int rowAffected = 0;
        if(empIds != null && empIds.size() > 0) {
            String groupId =  groupIDs[0];
            String[] ids = empIds.toString().replace("[","").replace("]","").split(",");
            List<String> copyEmpIDs = Arrays.asList(ids);
            logger.info("emp Ids :" + copyEmpIDs);
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_code").is(groupId).andOperator(Criteria.where(PayItemName.EMPLOYEE_CODE_CN).in(copyEmpIDs)));
        }else {
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_code").in(Arrays.asList(copyGroupIDs)));
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

}
