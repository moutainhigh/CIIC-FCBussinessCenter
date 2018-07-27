package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantJobService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantMainTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.EventName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.FCBizTransactionMongoOpt;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放定时任务实现类
 * </p>
 *
 * @author chenpb
 * @since 2018-07-26
 */
@Service
public class SalaryGrantJobServiceImpl extends ServiceImpl<SalaryGrantMainTaskMapper, SalaryGrantMainTaskPO> implements SalaryGrantJobService {
    @Autowired
    CommonService commonService;
    @Autowired
    private SalaryGrantMainTaskMapper salaryGrantMainTaskMapper;
    @Autowired
    private SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    private FCBizTransactionMongoOpt fcBizTransactionMongoOpt;
    /**
     * 薪资发放付款任务
     *
     * @param
     * @return
     * @author chenpb
     * @since 2018-07-26
     */
    @Transactional(rollbackFor = {Exception.class})
    @Override
    public void handle() {

//        System.out.println("薪资发放定时任务 启动时间: " + LocalDateTime.now());

        //获取次日日期
        String grantDate = LocalDate.now().plusDays(1).format(DateTimeFormatter.ofPattern("yyyyMMdd"));

        //查询薪资发放任务单子表记录列表
        List<SalaryGrantTaskPaymentBO> waitForPaymentTaskList = salaryGrantSubTaskMapper.queryWaitForPaymentTaskList(grantDate);
        if (!CollectionUtils.isEmpty(waitForPaymentTaskList)) {
            waitForPaymentTaskList.forEach(salaryGrantTaskPaymentBO -> {
                //任务单子表编号
                String taskCode = salaryGrantTaskPaymentBO.getTaskCode();
                //任务单主表编号
                String mainTaskCode = salaryGrantTaskPaymentBO.getMainTaskCode();

                //根据任务单编号查询雇员信息，查询语句添加过滤条件去掉暂缓雇员（手动+自动）和工资为0的雇员 update by gy on 2018-07-26
                List<SalaryGrantEmployeePaymentBO> waitForPaymentEmpList = salaryGrantEmployeeMapper.queryWaitForPaymentEmpList(taskCode);

                //设置任务单记录字段
                //正常发放雇员的实发工资之和
                BigDecimal payAmount = null;
                //正常发放雇员的雇员数量
                Integer payEmployeeCount = null;
                //正常发放雇员的实发工资之和雇员列表
                List<SalaryGrantEmployeePaymentBO> payAmountPaymentBOList;
                //正常发放雇员的雇员数量雇员列表
                List<SalaryGrantEmployeePaymentBO> payEmployeeCountPaymentBOList;
                // 如果查询任务单中雇员个数为0则不同步数据到结算中心 update by gy on 2018-07-26
                if (!CollectionUtils.isEmpty(waitForPaymentEmpList)) {
                    //汇总金额去掉 sg_salary_grant_employee.grant_status暂缓（1-自动、2-手动）的雇员发放金额
                    payAmountPaymentBOList = waitForPaymentEmpList.stream().filter(paymentBO ->
                            !ObjectUtils.isEmpty(paymentBO.getGrantStatus()) && paymentBO.getGrantStatus() != 1 && paymentBO.getGrantStatus() != 2
                    ).collect(Collectors.toList());
                    //需要发放的雇员数量，去掉暂缓雇员grant_status（1-自动、2-手动）和 发放金额payment_amount为0的雇员
                    payEmployeeCountPaymentBOList = waitForPaymentEmpList.stream().filter(paymentBO ->
                            (!ObjectUtils.isEmpty(paymentBO.getGrantStatus()) && paymentBO.getGrantStatus() != 1 && paymentBO.getGrantStatus() != 2) ||
                                    new BigDecimal(0).compareTo(paymentBO.getPaymentAmount()) != 0
                    ).collect(Collectors.toList());

                    if (!CollectionUtils.isEmpty(payAmountPaymentBOList)) {
                        payAmount = payAmountPaymentBOList.stream().map(SalaryGrantEmployeePaymentBO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add);
                    }

                    payEmployeeCount = payEmployeeCountPaymentBOList.size();

                    //正常发放雇员的实发工资之和
                    salaryGrantTaskPaymentBO.setPayAmount(payAmount);
                    //正常发放雇员的雇员数量
                    salaryGrantTaskPaymentBO.setPayEmployeeCount(payEmployeeCount);

                    //调用结算中心发放工资清单接口
                    SalaryBatchDTO salaryBatchDTO = commonService.saveSalaryBatchData(salaryGrantTaskPaymentBO, waitForPaymentEmpList);
                    if (!ObjectUtils.isEmpty(salaryBatchDTO)) {
                        //更新任务单子表状态
                        //更新字段
                        SalaryGrantSubTaskPO salaryGrantSubTaskPO = new SalaryGrantSubTaskPO();
                        salaryGrantSubTaskPO.setTaskStatus("6");
                        //更新条件
                        EntityWrapper<SalaryGrantSubTaskPO> subTaskPOEntityWrapper = new EntityWrapper<>();
                        subTaskPOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", taskCode);
                        salaryGrantSubTaskMapper.update(salaryGrantSubTaskPO, subTaskPOEntityWrapper);

                        //更新任务单主表状态
                        //更新字段
                        SalaryGrantMainTaskPO salaryGrantMainTaskPO = new SalaryGrantMainTaskPO();
                        salaryGrantMainTaskPO.setTaskStatus("6");
                        //更新条件
                        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper = new EntityWrapper<>();
                        mainTaskPOEntityWrapper.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskCode);
                        salaryGrantMainTaskMapper.update(salaryGrantMainTaskPO, mainTaskPOEntityWrapper);

                        //查询任务单主表记录
                        EntityWrapper<SalaryGrantMainTaskPO> mainTaskPOEntityWrapper1 = new EntityWrapper<>();
                        mainTaskPOEntityWrapper1.where("salary_grant_main_task_code = {0} and is_active = 1", mainTaskCode);
                        List<SalaryGrantMainTaskPO> mainTaskPOList = salaryGrantMainTaskMapper.selectList(mainTaskPOEntityWrapper1);
                        if (!CollectionUtils.isEmpty(mainTaskPOList)) {
                            mainTaskPOList.stream().forEach(salaryGrantMainTaskPO1 -> {
                                //更新mongodb中标记为1
                                fcBizTransactionMongoOpt.commitEvent(salaryGrantMainTaskPO1.getBatchCode(), salaryGrantMainTaskPO1.getGrantType(), EventName.FC_GRANT_EVENT, 1);
                            });
                        }
                    }
                }
            });
        }

//        System.out.println("薪资发放定时任务 完成时间: " + LocalDateTime.now());
    }
}
