package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.ConfigTaskMsgDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollConfigTaskPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrGroupConfigTaskMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupConfigTaskService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author
 * @createTime 2018-07-11 16:48
 * @description 薪资组实例变更任务单实现类
 */
@Service
public class PrGroupConfigTaskServiceImpl implements PrGroupConfigTaskService {

    @Autowired
    private PrGroupConfigTaskMapper prGroupConfigTaskMapper;

    private static final Logger log = LoggerFactory.getLogger(PrGroupConfigTaskServiceImpl.class);

    @Override
    public PageInfo<PrPayrollConfigTaskPO> selectPrGroupConfigTaskList(PrPayrollConfigTaskPO prPayrollConfigTaskPO,
                                                                    Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<PrPayrollConfigTaskPO> resultList = prGroupConfigTaskMapper.selectPrGroupConfigTaskList(prPayrollConfigTaskPO);

        return new PageInfo<>(resultList);
    }

    @Override
    public void filterToSave(ConfigTaskMsgDTO taskMsgDTO) {
        if ("1".equals(taskMsgDTO.getTaskType())){
            log.info("Received from message:" + taskMsgDTO.toString());
            PrPayrollConfigTaskPO prPayrollConfigTaskPO = new PrPayrollConfigTaskPO();
            prPayrollConfigTaskPO.setConfigTaskId(taskMsgDTO.getCmyFcConfigureTaskId());
            prPayrollConfigTaskPO.setStatus(1);
            prPayrollConfigTaskPO.setType(Integer.valueOf(taskMsgDTO.getTaskType()));
            prPayrollConfigTaskPO.setManagementId(taskMsgDTO.getManagementId());
            prPayrollConfigTaskPO.setTargetId(taskMsgDTO.getTargetId());
            prPayrollConfigTaskPO.setCreatedBy("sys");
            prPayrollConfigTaskPO.setModifiedBy("sys");
            prPayrollConfigTaskPO.setRemark(taskMsgDTO.getRemark());
            log.info("Information after conversion:" + prPayrollConfigTaskPO.toString());
            prGroupConfigTaskMapper.insertPrPayrollConfigTask(prPayrollConfigTaskPO);
        }
    }
}
