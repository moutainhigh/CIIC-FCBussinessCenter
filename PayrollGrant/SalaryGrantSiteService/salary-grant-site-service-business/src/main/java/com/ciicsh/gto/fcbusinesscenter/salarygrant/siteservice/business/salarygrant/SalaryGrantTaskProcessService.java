package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

import java.util.Map;

/**
 * <p>
 * 薪资发放任务单处理 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-01-22
 */
public interface SalaryGrantTaskProcessService extends IService<SalaryGrantMainTaskPO> {

    /**
     *  计算引擎关账，根据计算批次信息创建薪资发放任务单及雇员信息
     * @param closingMsg
     * @return
     */
    void closing(ClosingMsg closingMsg);
    /**
     *  根据计算批次编号判断薪资发放任务单主表是否已存在
     * @param batchParam
     * @return Boolean
     */
    Boolean isExistSalaryGrantMainTask(Map batchParam);

    // 1、接收计算引擎消息，获取计算批次号，根据计算批次号查询批次信息、批次计算结果数据及雇员服务协议信息。（调用计算批次接口--数据表结构待定）
    // 后台处理薪资发放任务单入口方法，调用多个子方法包括（获取批次业务表信息、生成entity_id、调用工作流引擎回写任务单工作流的流程编号、创建薪资发放任务单、创建薪资发放雇员信息表-带入任务单编号entity_id）
    /**
     *  调用工作流引擎，获取工作流的流程编号。后台开始处理薪资发放任务单入口方法。内部调用listPayrollCalcResult方法
     * @param batchParam 计算批次号batchCode
     * @return
     */
//    void createSalaryGrantMainTask(Map batchParam);

    /**
     *  当批次已进入业务处理，其他模块可能会对批次要求进行重算处理，薪资发放模块要对批次任务做相应的处理。
     * @param salaryGrantMainTaskPO
     * @return
     */
    void modifySalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     *  根据计算批次号查询批次业务表的批次数据信息，包括计算结果数据及雇员服务协议信息。
     * @param batchParam 计算批次号batchCode
     * @return List 对应批次的雇员信息列表
     */
//    List listPayrollCalcResult(Map batchParam);

    /**
     * 修改任务单信息（薪资发放日、上下午标识、审批意见、失效原因等）
     * @param salaryGrantMainTaskPO
     * @return Integer
     */
    Integer updateSalaryGrantMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 查询任务单主表明细信息
     * @param queryCondition
     * @return SalaryGrantMainTaskPO
     */
    SalaryGrantMainTaskPO getSalaryGrantMainTaskPO(SalaryGrantMainTaskPO queryCondition);

    /**
     * 查询子任务单明细信息
     * @param queryCondition
     * @return SalaryGrantSubTaskPO
     */
    SalaryGrantSubTaskPO getSalaryGrantSubTaskPO(SalaryGrantSubTaskPO queryCondition);
}
