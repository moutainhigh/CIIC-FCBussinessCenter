package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollExpressMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollExpressPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollExpressService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 工资单邮寄快递信息 服务实现类
 *
 * @author taka
 * @since 2018-02-28
 */
@Service
@Transactional
@SuppressWarnings("all")
public class PrsPayrollExpressServiceImpl implements PrsPayrollExpressService {

    @Autowired
    private PrsPayrollExpressMapper prsPayrollExpressMapper;

    @Override
    public List<PrsPayrollExpressPO> listPrsPayrollExpresss(Map<String, Object> params) {

        List<PrsPayrollExpressPO> records = prsPayrollExpressMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsPayrollExpressPO> pagePrsPayrollExpresss(Map<String, Object> params) {
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

        int total = prsPayrollExpressMapper.total(params);
        List<PrsPayrollExpressPO> records = prsPayrollExpressMapper.list(params);
        Page<PrsPayrollExpressPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsPayrollExpressPO getPrsPayrollExpress(Map<String, Object> params) {
        return prsPayrollExpressMapper.get(params);
    }

    @Override
    public Boolean addPrsPayrollExpress(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }



        prsPayrollExpressMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsPayrollExpress(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }



        prsPayrollExpressMapper.update(params);

        return true;
    }
}
