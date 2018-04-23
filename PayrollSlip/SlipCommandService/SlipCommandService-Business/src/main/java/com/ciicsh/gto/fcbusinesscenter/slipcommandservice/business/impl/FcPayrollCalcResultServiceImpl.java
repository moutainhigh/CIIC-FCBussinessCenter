package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.FcPayrollCalcResultMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 薪资计算批次结果表(雇员维度) 服务实现类
 *
 * @author taka
 * @since 2018-02-09
 */
@Service
public class FcPayrollCalcResultServiceImpl implements FcPayrollCalcResultService {

    @Autowired
    private FcPayrollCalcResultMapper fcPayrollCalcResultMapper;

//    @Autowired
//    private MongoClient mongoClient;


    @Override
    public List<FcPayrollCalcResultPO> listFcPayrollCalcResults(Map<String, Object> params) {

//        userManagementProxy.list(new HashMap<String, Object>(){
//            {
//                put("userId", "XTY00001");
//            }
//        });

//        Set<Map.Entry<String, Object>> set = mongoClient.getDatabase("payroll_db").getCollection("fc_payroll_calc_result_table").find().first().entrySet();

        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.list(params);

        return records;
    }

    @Override
    public Page<FcPayrollCalcResultPO> pageFcPayrollCalcResults(Map<String, Object> params) {
        int limit = 20;
        int offset = 0;

        int currentPage = params.get("currentPage") == null ? 1 : (int) params.get("currentPage");

        if (params.get("pageSize") != null) {
            limit =  (int) params.get("pageSize");
        }

        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        }

        params.put("limit", limit);
        params.put("offset", offset);

        int total = fcPayrollCalcResultMapper.total(params);
        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.list(params);
        Page<FcPayrollCalcResultPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public FcPayrollCalcResultPO getFcPayrollCalcResult(Map<String, Object> params) {
        return fcPayrollCalcResultMapper.get(params);
    }

    @Override
    public Boolean addFcPayrollCalcResult(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }



        fcPayrollCalcResultMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updateFcPayrollCalcResult(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }



        fcPayrollCalcResultMapper.update(params);

        return true;
    }


    @Override
    public List<FcPayrollCalcResultPO> listBatchIds(Map<String, Object> params) {

        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.listBatchIds(params);

        return records;
    }

    @Override
    public List<FcPayrollCalcResultPO> listPayrollTypes(Map<String, Object> params) {

        List<FcPayrollCalcResultPO> records = fcPayrollCalcResultMapper.listPayrollTypes(params);

        return records;
    }
}
