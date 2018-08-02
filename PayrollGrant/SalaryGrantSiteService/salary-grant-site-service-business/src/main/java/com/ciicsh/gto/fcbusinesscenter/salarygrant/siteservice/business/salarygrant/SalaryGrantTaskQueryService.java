package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowResultBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.WorkFlowTaskInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.PayapplySalaryDTO;

import java.util.List;

/**
 * <p>
 * 薪资发放任务单查询 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-17
 */
public interface SalaryGrantTaskQueryService extends IService<SalaryGrantMainTaskPO> {

    /**
     * 刷新草稿状态任务单信息
     * @author chenpb
     * @since 2018-07-05
     * @param salaryGrantTaskBO
     * @return
     */
    Page<SalaryGrantTaskBO> refreshDraftTask(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 查询薪资发放任务单列表
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return Page<SalaryGrantTaskBO>
     */
    Page<SalaryGrantTaskBO> salaryGrantList(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据任务单编号查询任务单
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    SalaryGrantTaskBO selectTaskByTaskCode(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据任务单编号查询操作记录
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    Page<WorkFlowTaskInfoBO> operation(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 根据主表任务单编号查询子表任务单
     * @author chenpb
     * @since 2018-05-10
     * @param salaryGrantTaskBO
     * @return
     */
    List<SalaryGrantTaskBO> querySubTask(SalaryGrantTaskBO salaryGrantTaskBO);

    /**
     * 同步结算中心支付状态
     * @author chenpb
     * @since 2018-06-06
     * @param list
     */
    void syncPayStatus(List<PayapplySalaryDTO> list);

    /**
     * 取消关账
     * @author chenpb
     * @since 2018-06-07
     * @param msg
     * @throws Exception
     */
    void cancelClosing(CancelClosingMsg msg) throws Exception;

    /**
     * 提交
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @return
     * @throws Exception
     */
    WorkFlowResultBO submit(Boolean flag, SalaryGrantTaskBO bo) throws Exception;

    /**
     * 审批通过
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @return
     * @throws Exception
     */
    WorkFlowResultBO approvalPass(Boolean flag, SalaryGrantTaskBO bo) throws Exception;

    /**
     * 审批退回
     * @author chenpb
     * @since 2018-06-08
     * @param flag
     * @param bo
     * @return
     * @throws Exception
     */
    WorkFlowResultBO approvalReject(Boolean flag, SalaryGrantTaskBO bo) throws Exception;

    /**
     * 抢占资源锁
     * @author chenpb
     * @since 2018-06-22
     * @param bo
     * @return
     */
    Integer lockMainTask(SalaryGrantTaskBO bo);

    /**
     * 抢占资源锁
     * @author chenpb
     * @since 2018-06-22
     * @param bo
     * @return
     */
    Integer lockSubTask(SalaryGrantTaskBO bo);
}
