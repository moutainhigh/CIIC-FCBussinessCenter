package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.FcPayrollCalcResultMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.FcPayrollCalcResultService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Map;

/**
 * 薪资计算批次结果表(雇员维度) 服务实现类
 *
 * @author taka
 * @since 2018-02-09
 */
@Service
@Transactional
@SuppressWarnings("all")
public class FcPayrollCalcResultServiceImpl implements FcPayrollCalcResultService {

    @Autowired
    private FcPayrollCalcResultMapper fcPayrollCalcResultMapper;

    @Override
    public List<FcPayrollCalcResultPO> listFcPayrollCalcResults(Map<String, Object> params) {

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
        // TODO get current user
        params.put("createdBy", '1');
        params.put("modifiedBy", '1');



        fcPayrollCalcResultMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updateFcPayrollCalcResult(Map<String, Object> params) {
        // TODO get current user
        params.put("modifiedBy", '1');



        fcPayrollCalcResultMapper.update(params);

        return true;
    }
}
