package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubMoneyBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubMoneyPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.money.RequestForSubMoney;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.money.ResponseForSubMoney;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.DisposableChargeProxyDTO;

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
    Boolean rejectTaskSubMoney(RequestForSubMoney requestForSubMoney);

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

    /**
     * 获取划款子任务来款情况列表
     * @param moneyIds
     * @return
     */
    DisposableChargeProxyDTO whetherHasMoneyBySubMoneyIds(Long[] moneyIds);

    /**
     * 根据划款子任务ID和状态查询相关子任务状态的数目
     * @param subMoneyIds
     * @param status
     * @return
     */
    int selectRejectCount(String[] subMoneyIds,String status);

}
