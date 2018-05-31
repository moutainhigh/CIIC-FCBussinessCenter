package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollPrintRecordMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPrintRecordPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollPrintRecordService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 工资单任务单打印记录 服务实现类
 *
 * @author taka
 * @since 2018-02-28
 */
@Service
public class PrsPayrollPrintRecordServiceImpl implements PrsPayrollPrintRecordService {

    @Autowired
    private PrsPayrollPrintRecordMapper prsPayrollPrintRecordMapper;

    @Override
    public List<PrsPayrollPrintRecordPO> listPrsPayrollPrintRecords(Map<String, Object> params) {

        List<PrsPayrollPrintRecordPO> records = prsPayrollPrintRecordMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsPayrollPrintRecordPO> pagePrsPayrollPrintRecords(Map<String, Object> params) {
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

        int total = prsPayrollPrintRecordMapper.total(params);
        List<PrsPayrollPrintRecordPO> records = prsPayrollPrintRecordMapper.list(params);
        Page<PrsPayrollPrintRecordPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsPayrollPrintRecordPO getPrsPayrollPrintRecord(Map<String, Object> params) {
        return prsPayrollPrintRecordMapper.get(params);
    }

    @Override
    public Boolean addPrsPayrollPrintRecord(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }



        prsPayrollPrintRecordMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsPayrollPrintRecord(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }



        prsPayrollPrintRecordMapper.update(params);

        return true;
    }
}
