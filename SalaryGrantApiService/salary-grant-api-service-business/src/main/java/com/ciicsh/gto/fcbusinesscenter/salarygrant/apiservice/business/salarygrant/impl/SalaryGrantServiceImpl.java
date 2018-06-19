package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantEmployeeRefundBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantSubTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

/**
 * <p>
 * 任务单 服务实现类
 * </p>
 *
 * @author chenpb
 * @since 2018-04-18
 */
@Service
public class SalaryGrantServiceImpl extends ServiceImpl<SalaryGrantTaskMapper, SalaryGrantTaskPO> implements SalaryGrantService {
    /**
     *  任务单
     */
    @Autowired
    SalaryGrantTaskMapper salaryGrantTaskMapper;
    @Autowired
    CommonService commonService;

     /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param bo
     * @return
     */
    @Override
    public List<SalaryGrantTaskBO> getTask(SalaryGrantTaskBO bo) {
        String batchCodeStr = bo.getBatchCode();
        List<String> batchCodeList = Arrays.asList(batchCodeStr.split(","));
        List<SalaryGrantTaskBO> taskList = salaryGrantTaskMapper.listTask(batchCodeList, bo.getTaskCode());
        taskList.forEach(salaryGrantTaskBO ->{
            salaryGrantTaskBO.setGrantTypeName(commonService.getNameByValue("sgmGrantType",String.valueOf(salaryGrantTaskBO.getGrantType())));
            salaryGrantTaskBO.setTaskStatusName(commonService.getNameByValue("sgmTaskStatus",String.valueOf(salaryGrantTaskBO.getTaskStatus())));
        });
        return taskList;
    }

    @Override
    public List<SalaryGrantSubTaskBO> getSubTask(SalaryGrantSubTaskBO subTaskBO) {
        List<SalaryGrantSubTaskBO> taskList = salaryGrantTaskMapper.listSubTask(subTaskBO.getTaskCode());
        taskList.forEach(salaryGrantSubTaskBO->{
            salaryGrantSubTaskBO.setGrantModeName(commonService.getNameByValue("sgGrantMode",String.valueOf(salaryGrantSubTaskBO.getGrantMode())));
            salaryGrantSubTaskBO.setTaskStatusName(commonService.getNameByValue("sgsTaskStatus",String.valueOf(salaryGrantSubTaskBO.getTaskStatus())));
        });
        return taskList;
    }

    @Override
    public Boolean toCreateRefundTask(List<SalaryGrantEmployeeRefundBO> employeeRefundList) {
        return null;
    }

    @Override
    public Boolean toRejectTask(SalaryGrantTaskBO salaryGrantTaskBO) {
        return null;
    }
}
