package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.ConstraintService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.ConstraintMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 约束类
 * @author wuhua
 */
@Service
public class ConstraintServiceImpl implements ConstraintService {

    @Autowired
    private ConstraintMapper constraintMapper;

    @Override
    public int checkBatch(String[] Ids) {

        //返回值
        int ri = 0;
        int count = 1;//默认验证失败

        count = constraintMapper.selectNumsForBatch(Ids);

        if(count > 0){
            return ConstraintService.C1;
        }

        return ri;

    }

    @Override
    public int checkTask(String[] taskIds , String taskType) {

        //返回值
        int ri = 0;
        int count_version = 1;//默认验证失败
        int count_unclose = 1;//默认验证失败

        //批次是否为最新版本
        if(taskType.equals(ConstraintService.TASK_MAIN)){
            count_version = constraintMapper.selectNumsForTaskMainByVersion(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_DECLARE)){
            count_version = constraintMapper.selectNumsForTaskDeclare(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_TRANSFER)){
            count_version = constraintMapper.selectNumsForTaskMoney(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_PAYMENT)){
            count_version = constraintMapper.selectNumsForTaskPayment(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_SUPPLIER)){
            count_version = constraintMapper.selectNumsForTaskSupplier(taskIds);
        }
        if(count_version > 0){
            return C3;
        }

        //批次是否已取消关账
        if(taskType.equals(ConstraintService.TASK_MAIN)){
            count_unclose = constraintMapper.selectNumsForTaskMain(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_DECLARE)){
            count_unclose = constraintMapper.selectNumsForTaskDeclare(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_TRANSFER)){
            count_unclose = constraintMapper.selectNumsForTaskMoney(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_PAYMENT)){
            count_unclose = constraintMapper.selectNumsForTaskPayment(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_SUPPLIER)){
            count_unclose = constraintMapper.selectNumsForTaskSupplier(taskIds);
        }
        if(count_unclose > 0){
            return C2;
        }

        return ri;

    }
}
