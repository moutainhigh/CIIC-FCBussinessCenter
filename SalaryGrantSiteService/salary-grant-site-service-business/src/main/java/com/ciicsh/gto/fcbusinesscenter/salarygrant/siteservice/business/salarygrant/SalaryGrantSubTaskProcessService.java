package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;

/**
 * <p>
 * 薪资发放任务单子表处理 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-06-30
 */
public interface SalaryGrantSubTaskProcessService {

    /**
     * 任务单主表流程--提交
     * 提交任务单主表，拆分任务单为多个子表，根据多个拆分规则。按发放方式、发放账户拆分出客户自行的子任务单，新建薪资发放任务单子表sg_salarygrant_sub_task
     * 提交按钮后台业务逻辑：查询薪资发放日是否为空、查询批次对应的账单是否已核销/已代垫、如果是外区是否要发起代垫流程
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toSubmitMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);

    /**
     * 任务单主表流程--审批通过
     * 审批通过时，把发放方式为0-中智大库、1-中智代发（委托机构）的两种雇员数据进行归类建立子表，并按照发放账户进行拆分。
     * @param salaryGrantMainTaskPO
     * @return Boolean
     */
    Boolean toApproveMainTask(SalaryGrantMainTaskPO salaryGrantMainTaskPO);
}
