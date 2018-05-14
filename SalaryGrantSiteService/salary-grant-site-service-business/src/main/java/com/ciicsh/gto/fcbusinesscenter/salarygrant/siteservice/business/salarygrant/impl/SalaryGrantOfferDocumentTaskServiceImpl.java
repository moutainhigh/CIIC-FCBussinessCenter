package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.OfferDocumentDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.OfferDocumentMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <p>
 * 薪资发放报盘任务单 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Service
public class SalaryGrantOfferDocumentTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantOfferDocumentTaskService {
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    OfferDocumentMapper offerDocumentMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.queryOfferDocumentTaskPage(page, salaryGrantTaskBO));
    }

    @Override
    public List<OfferDocumentBO> generateOfferDocument(String taskCode) {
        //1.根据任务单编号查询薪资发放报盘信息表中报盘文件记录
        List<OfferDocumentBO> documentBOList = queryOfferDocument(taskCode);

        //2.存在报盘文件记录时直接查询报盘文件列表返回
        if (!CollectionUtils.isEmpty(documentBOList)) {
            return documentBOList;
        }

        //3.不存在报盘文件记录时先生成报盘文件，再查询报盘文件列表返回
        createOfferDocument(taskCode);

        return queryOfferDocument(taskCode);
    }

    @Override
    public void createOfferDocument(String taskCode) {
        //根据薪资发放任务单子表编号查询雇员信息，按公司ID、银行卡种类、付款账号分组
        EntityWrapper<SalaryGrantEmployeePO> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("salary_grant_sub_task_code = {0}", taskCode).groupBy("company_id, bankcard_type, payment_account_code");
        List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(entityWrapper);

        if (!CollectionUtils.isEmpty(employeePOList)) {

        }
    }

    @Override
    public List<OfferDocumentBO> queryOfferDocument(String taskCode) {
        return offerDocumentMapper.queryOfferDocument(taskCode);
    }
}
