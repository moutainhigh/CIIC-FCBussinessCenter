package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcsupportcenter.util.mongo.EmpGroupMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

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

    public void batchInsertGroupEmployees(String empGroupId, List<String> empIds){

        List<EmployeeExtensionPO> employees = employeeMapper.getEmployees(Integer.parseInt(empGroupId));
        if(empIds == null || empIds.size() == 0){
            //employees = employeeMapper.getEmployees(Integer.parseInt(empGroupId));
        }else {
            employees = employees.stream().filter(emp -> {
                return empIds.contains(emp.getEmployeeId());
            }).collect(Collectors.toList());
        }

        List<DBObject> list = new ArrayList<>();
        if(employees != null){
            DBObject basicDBObject = null;
            for (EmployeeExtensionPO item: employees) {
                basicDBObject = new BasicDBObject();
                basicDBObject.put("emp_group_id",item.getEmpGroupId());
                basicDBObject.put("employee_id",item.getEmployeeId());
                basicDBObject.put("employee_name",item.getEmployeeName());
                basicDBObject.put("birthday",item.getBirthday());
                basicDBObject.put("department",item.getDepartment());
                basicDBObject.put("gender",item.getGender());
                basicDBObject.put("id_card_type",item.getIdCardType());
                basicDBObject.put("join_date",item.getJoinDate());
                basicDBObject.put("id_num",item.getIdNum());
                basicDBObject.put("position",item.getPosition());

                // 获取雇员服务协议
                //basicDBObject.put("servicePortal", "{ key : value}");

                // 雇员薪资数据
                //basicDBObject.put("item","{name:基本工资, val: 12, alias:}");

                list.add(basicDBObject);
            }
            try {
                empGroupMongoOpt.batchInsert(list);

            }
            catch (Exception e){
                logger.error(e.getMessage());
            }
        }
    }

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
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_id").is(groupId).andOperator(Criteria.where("employee_id").in(copyEmpIDs)));
        }else {
            rowAffected = empGroupMongoOpt.batchDelete(Criteria.where("emp_group_id").in(Arrays.asList(copyGroupIDs)));
        }

        logger.info("deleted affected rows :" + rowAffected);

        return rowAffected;
    }

}
