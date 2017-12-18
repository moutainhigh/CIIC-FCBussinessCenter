package com.ciicsh.gto.salarymanagementcommandservice.controller;

import com.ciicsh.gto.salarymanagementcommandservice.controller.NormalBatchController.MgrData;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import org.apache.poi.sl.draw.binding.STRectAlignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.print.DocFlavor.STRING;
import java.util.HashMap;
import java.util.Hashtable;

/**
 * Created by jiangtianning on 2017/12/7.
 * @author jiangtianning
 */
@RestController
public class BaseController {

    @Autowired
    protected CodeGenerator codeGenerator;

    public Hashtable<String,String> getManagement(){
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("glf-0009","微软中国（上海）");
        hashtable.put("glf-00091","微软中国（北京）");
        hashtable.put("glf-00092","微软中国（深圳）");
        hashtable.put("ymx-0001","亚马逊（上海）");
        hashtable.put("ymx-0002","亚马逊（北京）");
        hashtable.put("ymx-0003","亚马逊（深圳）");
        return hashtable;
    }

    public Hashtable<Integer,String> getDepartment(){
        Hashtable<Integer,String> hashtable = new Hashtable<>();
        hashtable.put(1,"研发部");
        hashtable.put(2,"市场部");
        hashtable.put(3,"销售部");
        hashtable.put(4,"公关部");
        return hashtable;
    }

    public Hashtable<Integer,String> getPosition(){
        Hashtable<Integer,String> hashtable = new Hashtable<>();
        hashtable.put(1,"测试工程");
        hashtable.put(2,"开发工程师");
        hashtable.put(3,"架构师");
        hashtable.put(4,"研发经理");
        hashtable.put(6,"产品");
        hashtable.put(4,"研发经理");
        return hashtable;
    }
}
