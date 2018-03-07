package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 工资单 服务实现类
 *
 * @author taka
 * @since 2018-02-09
 */
@Service
@Transactional
@SuppressWarnings("all")
public class PrsPayrollServiceImpl implements PrsPayrollService {

    @Autowired
    private PrsPayrollMapper prsPayrollMapper;

    @Override
    public List<PrsPayrollPO> listPrsPayrolls(Map<String, Object> params) {

        List<PrsPayrollPO> records = prsPayrollMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsPayrollPO> pagePrsPayrolls(Map<String, Object> params) {
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

        int total = prsPayrollMapper.total(params);
        List<PrsPayrollPO> records = prsPayrollMapper.list(params);
        Page<PrsPayrollPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsPayrollPO getPrsPayroll(Map<String, Object> params) {
        return prsPayrollMapper.get(params);
    }

    @Override
    public Boolean addPrsPayroll(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }

        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }

        prsPayrollMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsPayroll(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }

        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }

        prsPayrollMapper.update(params);

        return true;
    }
}
