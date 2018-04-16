package com.ciicsh.gto.salarymanagementcommandservice.service;

import java.util.HashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/4/11.
 */
public interface FunctionService {

    /**
     * 获取函数名称列表
     * @return
     */
    List<HashMap<String, String>> getFunctionNameList();
}
