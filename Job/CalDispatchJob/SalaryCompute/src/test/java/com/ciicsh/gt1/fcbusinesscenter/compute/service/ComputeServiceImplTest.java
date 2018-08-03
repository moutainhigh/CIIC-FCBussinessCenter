package com.ciicsh.gt1.fcbusinesscenter.compute.service;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.EmpPayItem;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import com.ciicsh.gt1.fcbusinesscenter.compute.App;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.*;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/26 0026
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class ComputeServiceImplTest {
    @Autowired
    private ComputeServiceImpl computeService;

    @Test
    public void fireRules() {
        String city = "武汉";

        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("最低工资标准");
        List<String> list = new ArrayList<>();
        list.add("城市");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("城市", city); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("最低工资标准");

        //触发规则
        computeService.fire(hashSet, context);

        System.out.println("最低工资标准: " + funcEntity.getResult());
    }

    /**
     * 劳务税
     */
    @Test
    public void serviceTax() {
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("劳务税");
        List<String> list = new ArrayList<>();
        list.add("800");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String, Object> payItems = new HashMap<>();
        payItems.put("薪资金额", "4000"); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("劳务税");

        //触发规则
        computeService.fire(hashSet, context);

        System.out.println("劳务税: " + funcEntity.getResult());
    }
}
