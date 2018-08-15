package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.ReprieveEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantSubTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    @Autowired
    CommonService commonService;
    @Autowired
    SalaryGrantTaskMapper salaryGrantTaskMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    LogClientService logClientService;

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

    /**
     *  根据主表任务单编号查询薪资发放任务单子表
     * @author gaoyang
     * @date 2018-05-18
     * @param subTaskBO
     * @return List<SalaryGrantSubTaskBO>
     */
    @Override
    public List<SalaryGrantSubTaskBO> getSubTask(SalaryGrantSubTaskBO subTaskBO) {
        List<SalaryGrantSubTaskBO> taskList = salaryGrantTaskMapper.listSubTask(subTaskBO.getTaskCode());
        taskList.forEach(salaryGrantSubTaskBO->{
            salaryGrantSubTaskBO.setGrantModeName(commonService.getNameByValue("sgGrantMode",String.valueOf(salaryGrantSubTaskBO.getGrantMode())));
            salaryGrantSubTaskBO.setTaskStatusName(commonService.getNameByValue("sgsTaskStatus",String.valueOf(salaryGrantSubTaskBO.getTaskStatus())));
        });
        return taskList;
    }

    /**
     * 修改雇员发放状态
     * @author chenpb
     * @date 2018-06-19
     * @param bo
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public ReprieveEmployeeBO updateForReprieveEmployee(ReprieveEmployeeBO bo) {
        List<String> ids = new ArrayList<>();
        if(!bo.getEmployeeIds().isEmpty()) {
            SalaryGrantEmployeePO po = BeanUtils.instantiate(SalaryGrantEmployeePO.class);
            po.setBatchCode(bo.getBatchCode());
            po.setSalaryGrantMainTaskCode(bo.getTaskCode());
            po.setGrantStatus(bo.getGrantStatus());
            bo.getEmployeeIds().forEach(x -> {
                po.setEmployeeId(x);
                Integer num = salaryGrantEmployeeMapper.updateForReprieveEmployee(po);
                if (num < 1) {
                    ids.add(x);
                }
            });
            bo.setEmployeeIds(ids);
        }
        return bo;
    }
}
