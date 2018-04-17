package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.PrFunctionsMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.FunctionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/4/11.
 */
@Service
public class FunctionServiceImpl implements FunctionService {

    @Autowired
    private PrFunctionsMapper prFunctionsMapper;

    @Override
    public List<HashMap<String, String>> getFunctionNameList() {
        List<HashMap<String, String>> result = new ArrayList<>();
        result = prFunctionsMapper.selectFunctionNameList();
        return result;
    }
}
