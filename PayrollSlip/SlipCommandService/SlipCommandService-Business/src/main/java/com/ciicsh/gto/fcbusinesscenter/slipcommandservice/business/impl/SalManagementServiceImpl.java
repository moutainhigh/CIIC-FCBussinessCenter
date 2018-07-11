package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.SalManagementMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.SalManagementPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.SalManagementService;
import java.util.List;
import java.util.Map;

/**
 * 管理方 服务实现类
 *
 * @author taka
 * @since 2018-02-05
 */
@Service
public class SalManagementServiceImpl implements SalManagementService {

    @Autowired
    private SalManagementMapper salManagementMapper;

    @Override
    public List<SalManagementPO> listSalManagements(Map<String, Object> params) {

        List<SalManagementPO> records = salManagementMapper.list(params);

        return records;
    }

    @Override
    public Page<SalManagementPO> pageSalManagements(Map<String, Object> params) {
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

        int total = salManagementMapper.total(params);
        List<SalManagementPO> records = salManagementMapper.list(params);
        Page<SalManagementPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public SalManagementPO getSalManagement(Map<String, Object> params) {
        return salManagementMapper.get(params);
    }

    @Override
    public Boolean addSalManagement(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }

        salManagementMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updateSalManagement(Map<String, Object> params) {
        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }

        salManagementMapper.update(params);

        return true;
    }
}
