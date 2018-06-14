package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantReprieveEmployeeImportService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantReprieveEmployeeImportMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantReprieveEmployeeImportBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantReprieveEmployeeImportServiceImpl extends ServiceImpl<SalaryGrantReprieveEmployeeImportMapper, SalaryGrantReprieveEmployeeImportPO> implements SalaryGrantReprieveEmployeeImportService {
    @Autowired
    private SalaryGrantReprieveEmployeeImportMapper salaryGrantReprieveEmployeeImportMapper;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     * @author chenpb
     * @since 2018-05-23
     * @param bos
     * @param taskCode
     * @param taskType
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SalaryGrantEmployeeBO> deferEmployee(List<SalaryGrantEmployeeBO> bos, String taskCode, Integer taskType, String userId) {
        List<SalaryGrantEmployeeBO> failList = new ArrayList<>();
        bos.parallelStream().forEach(x -> {
            x.setTaskCode(taskCode);
            x.setTaskType(taskType);
            x.setModifiedBy(userId);
            Integer row = salaryGrantEmployeeMapper.deferEmployee(x);
            if (row==0) {
                failList.add(x);
            }
        });
        if (!failList.isEmpty()) {
            salaryGrantReprieveEmployeeImportMapper.deleteReprieveEmp(taskCode, taskType);
            salaryGrantReprieveEmployeeImportMapper.insertBatch(failList);
        }
        return failList;
    }

    /**
     * 根据任务单编号，任务单类型 查询暂缓失败雇员
     * @author chenpb
     * @since 2018-05-25
     * @param bo
     * @return
     */
    @Override
    public List<SalaryGrantReprieveEmployeeImportBO> selectDeferEmployee(SalaryGrantReprieveEmployeeImportBO bo) {
        return salaryGrantReprieveEmployeeImportMapper.selectDeferEmployee(bo);
    }
}
