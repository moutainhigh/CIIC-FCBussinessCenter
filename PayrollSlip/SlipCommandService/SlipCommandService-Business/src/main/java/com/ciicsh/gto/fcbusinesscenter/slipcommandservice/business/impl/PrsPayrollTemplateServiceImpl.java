package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollTemplateMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollTemplatePO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollTemplateService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 工资单模板 服务实现类
 *
 * @author taka
 * @since 2018-01-29
 */
@Service
@Transactional
@SuppressWarnings("all")
public class PrsPayrollTemplateServiceImpl implements PrsPayrollTemplateService {

    @Autowired
    private PrsPayrollTemplateMapper prsPayrollTemplateMapper;

    @Override
    public List<PrsPayrollTemplatePO> listPrsPayrollTemplates(Map<String, Object> params) {

        List<PrsPayrollTemplatePO> records = prsPayrollTemplateMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsPayrollTemplatePO> pagePrsPayrollTemplates(Map<String, Object> params) {
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

        int total = prsPayrollTemplateMapper.total(params);

        List<PrsPayrollTemplatePO> records = prsPayrollTemplateMapper.list(params);
        Page<PrsPayrollTemplatePO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsPayrollTemplatePO getPrsPayrollTemplate(Map<String, Object> params) {
        return prsPayrollTemplateMapper.get(params);
    }

    @Override
    public Boolean addPrsPayrollTemplate(Map<String, Object> params) {
        // TODO get current user
        params.put("createdBy", '1');
        params.put("modifiedBy", '1');

        if (params.get("effectiveTime") != null) {
            if (params.get("effectiveTime").equals("")) {
                params.put("effectiveTime", null);
            } else {
                params.put("effectiveTime", new Date((long) params.get("effectiveTime")));
            }
        }

        if (params.get("invalidTime") != null) {
            if (params.get("invalidTime").equals("")) {
                params.put("invalidTime", null);
            } else {
                params.put("invalidTime", new Date((long) params.get("invalidTime")));
            }
        }

        prsPayrollTemplateMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsPayrollTemplate(Map<String, Object> params) {
        // TODO get current user
        params.put("modifiedBy", '1');

        if (params.get("effectiveTime") != null) {
            if (params.get("effectiveTime").equals("")) {
                params.put("effectiveTime", null);
            } else {
                params.put("effectiveTime", new Date((long) params.get("effectiveTime")));
            }
        }

        if (params.get("invalidTime") != null) {
            if (params.get("invalidTime").equals("")) {
                params.put("invalidTime", null);
            } else {
                params.put("invalidTime", new Date((long) params.get("invalidTime")));
            }
        }

        prsPayrollTemplateMapper.update(params);

        return true;
    }
}
