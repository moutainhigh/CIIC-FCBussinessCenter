package com.ciicsh.gt1.fcbusinesscenter.compute.Controller;

import com.ciicsh.caldispatchjob.compute.Cal.ComputeServiceImpl;
import com.ciicsh.caldispatchjob.entity.DroolsContext;
import com.ciicsh.caldispatchjob.entity.EmpPayItem;
import com.ciicsh.caldispatchjob.entity.FuncEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by bill on 18/5/25.
 */
@RestController
public class TestController {

    @Autowired
    private ComputeServiceImpl computeService;

    @PostMapping("/api/fireRules")
    public String fireRules(@RequestParam  String city){
        DroolsContext context = new DroolsContext();

        //设置函数信息
        FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("城市最低生活标准");
        List<String> list = new ArrayList<>();
        list.add("城市");
        funcEntity.setParameters(list);
        context.getFuncEntityList().add(funcEntity);
        //end

        //设置雇员信息
        EmpPayItem empPayItem = new EmpPayItem();
        Map<String,Object> payItems = new HashMap<>();
        payItems.put("城市",city); //薪资项名称 和 值
        empPayItem.setItems(payItems);
        context.setEmpPayItem(empPayItem);
        //end

        //设置需要触发的规则名称
        HashSet hashSet = new HashSet();
        hashSet.add("城市最低生活标准");
        //end

        //触发规则
        computeService.fire(hashSet,context);

        return String.valueOf(funcEntity.getResult());
    }
}
