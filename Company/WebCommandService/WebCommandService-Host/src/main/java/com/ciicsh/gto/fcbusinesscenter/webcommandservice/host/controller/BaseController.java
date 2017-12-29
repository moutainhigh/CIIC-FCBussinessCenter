package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.controller;

import org.springframework.web.bind.annotation.RestController;

import java.util.Hashtable;

/**
 * @Author: guwei
 * @Description:
 * @Date: Created in 10:32 2017/12/28
 */
@RestController
public class BaseController {


    public Hashtable<String,String> getManagement(){
        Hashtable<String,String> hashtable = new Hashtable<>();
        hashtable.put("001","雅诗兰黛");
        hashtable.put("002","星巴克");
        hashtable.put("003","香奈儿");
        hashtable.put("004","亚马逊");
        return hashtable;
    }
}
