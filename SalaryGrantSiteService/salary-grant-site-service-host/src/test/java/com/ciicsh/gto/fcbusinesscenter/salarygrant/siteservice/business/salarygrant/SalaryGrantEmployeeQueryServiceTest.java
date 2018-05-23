package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.CalResultMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.CalcResultItemBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.EmpCalcResultBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.App;
import com.mongodb.DBObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/23 0023
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class SalaryGrantEmployeeQueryServiceTest {
    @Autowired
    private SalaryGrantEmployeeQueryService queryService;
    @Autowired
    private CalResultMongoOpt calResultMongoOpt;

    /**
     * 查询批次雇员薪资项
     */
    @Test
    public void getSalaryResultItemsList() {
        String batchCode = "GL1800255_201804_0000000257";
        Integer batchType = 1;
        List<DBObject> dbObjectList = calResultMongoOpt.list(Criteria.where("batch_id").is(batchCode).and("batch_type").is(batchType));
        if (!CollectionUtils.isEmpty(dbObjectList)) {
            List<DBObject> payItems = (List<DBObject>) dbObjectList.get(0).get("salary_calc_result_items");
            if (!CollectionUtils.isEmpty(payItems)) {
                payItems.forEach(dbObject -> System.out.printf("item_code:%-20s item_name: %-20s item_value:%-20s \n", dbObject.get("item_code").toString(), dbObject.get("item_name").toString(), dbObject.get("item_value").toString()));
            }
        }
    }

    /**
     * 查询主表的雇员信息
     */
    @Test
    public void queryEmployeeForMainTask() {
        Page<SalaryGrantEmployeeBO> page = new Page<>();
        page.setCurrent(1);
        page.setSize(10);

        SalaryGrantEmployeeBO salaryGrantEmployeeBO = new SalaryGrantEmployeeBO();
        salaryGrantEmployeeBO.setSalaryGrantMainTaskCode("SGT2018032800000001");
        salaryGrantEmployeeBO.setActive(true);

        Page<SalaryGrantEmployeeBO> retPage = queryService.queryEmployeeForMainTask(page, salaryGrantEmployeeBO);
        System.out.println("查询主表的雇员信息 page: " + retPage + " 记录: " + retPage.getRecords());
    }

    @Test
    public void listAdjustCalcInfo() {
        SalaryGrantEmployeePO salaryGrantEmployeePO = new SalaryGrantEmployeePO();

        queryService.listAdjustCalcInfo(salaryGrantEmployeePO);
    }

    @Test
    public void getSalaryCalcResultItemsList() {
        Map<String, Object> batchParam = new HashMap();
        batchParam.put("batchCode", "GL000007_201801_0000000179");
        batchParam.put("batchType", 1);
        List<CalcResultItemBO> resultItemBOList = queryService.getSalaryCalcResultItemsList(batchParam);
        if (!CollectionUtils.isEmpty(resultItemBOList)) {
            resultItemBOList.stream().forEach(itemBO -> System.out.printf("getItemCode: %-20s etItemName: %-30s getItemValue: %-20s\n", itemBO.getItemCode(), itemBO.getItemName(), itemBO.getItemValue()));
        }
    }

    @Test
    public void getEmpCalcResultItemsList() {
        String batchCode = "GL1800255_201804_0000000257";
        int grantType = 1;
        List<EmpCalcResultBO> empCalcResultBOList = queryService.getEmpCalcResultItemsList(batchCode, grantType);
        System.out.println("empCalcResultBOList: " + JSONObject.toJSONString(empCalcResultBOList));
    }

    @Test
    public void getEmployeeForBizList() {
        List<CalcResultItemBO> checkedItemsList = new ArrayList<>();
        CalcResultItemBO calcResultItemBO = new CalcResultItemBO();
        calcResultItemBO.setItemCode("XZX_GL000007_05006");
        calcResultItemBO.setItemName("雇员名称");
        calcResultItemBO.setItemValue("孙一");
        checkedItemsList.add(calcResultItemBO);

        calcResultItemBO = new CalcResultItemBO();
        calcResultItemBO.setItemCode("XZX_GL000007_05007");
        calcResultItemBO.setItemName("雇员编号");
        calcResultItemBO.setItemValue("18016350");
        checkedItemsList.add(calcResultItemBO);

        calcResultItemBO = new CalcResultItemBO();
        calcResultItemBO.setItemCode("XZX_GL000007_05016");
        calcResultItemBO.setItemName("实发工资");
        calcResultItemBO.setItemValue("22950.0");
        checkedItemsList.add(calcResultItemBO);

        SalaryGrantEmployeeBO salaryGrantEmployeeBO = new SalaryGrantEmployeeBO();
        salaryGrantEmployeeBO.setTaskCode("LTB20180328000000002");
        salaryGrantEmployeeBO.setTaskType(1);
        salaryGrantEmployeeBO.setBatchCode("GL1800255_201804_0000000257");

        List<EmpCalcResultBO> empCalcResultBOList = queryService.getEmployeeForBizList(checkedItemsList, salaryGrantEmployeeBO);
    }

    @Test
    public void queryEmpHisInfo() {
        long task_his_id = 1;
        Integer pageNum = 2;
        Integer pageSize = 20;
        Page<SalaryGrantEmployeeBO> employeeBOPage = queryService.queryEmpHisInfo(task_his_id, pageNum, pageSize);
        System.out.println("-----------");
    }
}
