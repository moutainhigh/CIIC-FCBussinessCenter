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

    private final int C1=1;//批次已取消关账

    @Override
    public int checkBatch(String[] Ids) {

        //返回值
        int ri = 0;
        int count = 1;//默认验证失败

        count = constraintMapper.selectNumsForBatch(Ids);

        if(count > 0){
            return C1;
        }

        return ri;

    }

    @Override
    public int checkTask(String[] taskIds , String taskType) {

        //返回值
        int ri = 0;
        int count = 1;//默认验证失败

        if(taskType.equals(ConstraintService.TASK_MAIN)){
            count = constraintMapper.selectNumsForTaskMain(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_DECLARE)){
            count = constraintMapper.selectNumsForTaskDeclare(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_TRANSFER)){
            count = constraintMapper.selectNumsForTaskMoney(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_PAYMENT)){
            count = constraintMapper.selectNumsForTaskPayment(taskIds);
        }else if(taskType.equals(ConstraintService.TASK_SUB_SUPPLIER)){
            count = constraintMapper.selectNumsForTaskSupplier(taskIds);
        }

        if(count > 0){
            return C1;
        }

        return ri;

    }
}
