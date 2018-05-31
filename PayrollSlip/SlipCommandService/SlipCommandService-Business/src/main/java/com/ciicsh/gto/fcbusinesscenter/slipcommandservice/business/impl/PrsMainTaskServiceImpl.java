package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserContext;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.bo.UserInfoBO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsMainTaskService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.List;
import java.util.Map;

/**
 * 工资单任务单主表 服务实现类
 *
 * @author taka
 * @since 2018-02-28
 */
@Service
public class PrsMainTaskServiceImpl implements PrsMainTaskService {

    @Autowired
    private PrsMainTaskMapper prsMainTaskMapper;

    @Override
    public List<PrsMainTaskPO> listPrsMainTasks(Map<String, Object> params) {

        List<PrsMainTaskPO> records = prsMainTaskMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsMainTaskPO> pagePrsMainTasks(Map<String, Object> params) {
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

        int total = prsMainTaskMapper.total(params);
        List<PrsMainTaskPO> records = prsMainTaskMapper.list(params);
        Page<PrsMainTaskPO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsMainTaskPO getPrsMainTask(Map<String, Object> params) {
        return prsMainTaskMapper.get(params);
    }

    @Override
    public Boolean addPrsMainTask(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("createdBy", currUser.getDisplayName());
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("createdBy", "1");
            params.put("modifiedBy", "1");
        }

        if (params.get("publishDate") != null) {
            if (params.get("publishDate").equals("")) {
                params.put("publishDate", null);
            } else {
                params.put("publishDate", new Date((long) params.get("publishDate")));
            }
        }
        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }
        if (params.get("uploadZxtDate") != null) {
            if (params.get("uploadZxtDate").equals("")) {
                params.put("uploadZxtDate", null);
            } else {
                params.put("uploadZxtDate", new Date((long) params.get("uploadZxtDate")));
            }
        }


        prsMainTaskMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsMainTask(Map<String, Object> params) {

        UserInfoBO currUser = UserContext.getUser();
        if (currUser != null) {
            params.put("modifiedBy", currUser.getDisplayName());
        } else {
            params.put("modifiedBy", "1");
        }

        if (params.get("publishDate") != null) {
            if (params.get("publishDate").equals("")) {
                params.put("publishDate", null);
            } else {
                params.put("publishDate", new Date((long) params.get("publishDate")));
            }
        }
        if (params.get("approveTime") != null) {
            if (params.get("approveTime").equals("")) {
                params.put("approveTime", null);
            } else {
                params.put("approveTime", new Date((long) params.get("approveTime")));
            }
        }


        prsMainTaskMapper.update(params);

        return true;
    }
}
