package com.ciicsh.caldispatchjob.compute.mongo;

import com.ciicsh.gt1.BaseOpt;
import com.ciicsh.gto.salarymanagement.entity.po.EmployeeExtensionPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.index.CompoundIndexDefinition;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/9.
 */
@Component
public class EmpGroupMongoOpt extends BaseOpt {

    private final static Logger logger = LoggerFactory.getLogger(EmpGroupMongoOpt.class);

    private static final String PR_EMPLOYEE_GROUP  = "pr_emp_group_table";

    @Autowired
    private PrEmployeeMapper employeeMapper;

    @Autowired
    private MongoTemplate mongoTemplate;

    public EmpGroupMongoOpt() {
        super(PR_EMPLOYEE_GROUP);
    }

    public void batchInsertGroupEmployees(String empGroupId, List<String> empIds){

        createIndex();

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
                list.add(basicDBObject);
            }
            try {
                this.batchInsert(list);

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
            rowAffected = this.batchDelete(Criteria.where("emp_group_id").is(groupId).andOperator(Criteria.where("employee_id").in(copyEmpIDs)));
        }else {
            rowAffected = this.batchDelete(Criteria.where("emp_group_id").in(Arrays.asList(copyGroupIDs)));
        }

        logger.info("deleted affected rows :" + rowAffected);

        return rowAffected;
    }

    public List<DBObject> getEmloyeesByGroupId(String empGroupId){
        return this.list(Criteria.where("emp_group_id").is(empGroupId));
    }

    private void createIndex(){
        DBObject indexOptions = new BasicDBObject();
        indexOptions.put("emp_group_id",1);
        indexOptions.put("employee_id",1);
        CompoundIndexDefinition indexDefinition = new CompoundIndexDefinition(indexOptions);
        mongoTemplate.indexOps(PR_EMPLOYEE_GROUP).ensureIndex(indexDefinition);
    }
}
