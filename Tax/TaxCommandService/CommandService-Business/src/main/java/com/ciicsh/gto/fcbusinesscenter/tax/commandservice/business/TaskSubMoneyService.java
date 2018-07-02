package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;

import java.math.BigDecimal;

/**
 * @author yuantongqing
 * on create 2018/1/8
 */
public interface TaskSubMoneyService {

    /**
     * 条件查询划款子任务
     * @param requestForSubMoney
     * @return
     */
    ResponseForSubMoney querySubMoney(RequestForSubMoney requestForSubMoney);

    /**
     * 批量完成划款子任务
     * @param requestForSubMoney
     */
    void completeTaskSubMoney(RequestForSubMoney requestForSubMoney);

    /**
     * 批量退回划款子任务
     * @param requestForSubMoney
     */
    void rejectTaskSubMoney(RequestForSubMoney requestForSubMoney);

    /**
     * 根据划款子任务ID查询划款任务信息
     * @param subMoneyId
     * @return
     */
    TaskSubMoneyPO querySubMoneyById(long subMoneyId);

    /**
     * 根据BO对象修改划款任务信息
     * @param taskSubMoneyBO
     */
    void updateTaskSubMoneyById(TaskSubMoneyBO taskSubMoneyBO);

    /**
     * 更新划款滞纳金和罚金
     * @param subMoneyId
     * @param overdue
     * @param fine
     */
    void updateTaskSubMoneyOverdueAndFine(Long subMoneyId, BigDecimal overdue,BigDecimal fine);

}
