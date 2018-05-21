package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.OfferDocumentFileMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.OfferDocumentMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeGroupInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放报盘任务单 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Service
public class SalaryGrantOfferDocumentTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantOfferDocumentTaskService {
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    OfferDocumentMapper offerDocumentMapper;
    @Autowired
    OfferDocumentFileMapper offerDocumentFileMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    CommonService commonService;

    @Override
    public Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.queryOfferDocumentTaskPage(page, salaryGrantTaskBO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public List<OfferDocumentBO> generateOfferDocument(String taskCode, String userId) {
        //1.根据任务单编号查询薪资发放报盘信息表中报盘文件记录
        List<OfferDocumentBO> documentBOList = queryOfferDocument(taskCode);

        //2.存在报盘文件记录时直接查询报盘文件列表返回
        if (!CollectionUtils.isEmpty(documentBOList)) {
            return documentBOList;
        }

        //3.不存在报盘文件记录时先生成报盘文件，再查询报盘文件列表返回
        createOfferDocument(taskCode, userId);

        return queryOfferDocument(taskCode);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createOfferDocument(String taskCode, String userId) {
        //根据薪资发放任务单子表编号查询雇员信息
        EntityWrapper<SalaryGrantEmployeePO> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("salary_grant_sub_task_code = {0}", taskCode);
        List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(entityWrapper);

        //分组雇员信息数据
        List<SalaryGrantEmployeeGroupInfoBO> currencyCodeGroupInfoBOList = groupEmployeeInfoData(employeePOList, taskCode);

        //新增报盘文件记录
        insertOfferDocumentRecords(currencyCodeGroupInfoBOList, userId);
    }

    /**
     * 分组雇员信息数据
     *
     * @param employeePOList
     * @return
     */
    private List<SalaryGrantEmployeeGroupInfoBO> groupEmployeeInfoData(List<SalaryGrantEmployeePO> employeePOList, String taskCode) {
        if (!CollectionUtils.isEmpty(employeePOList)) {
            SalaryGrantEmployeeGroupInfoBO employeeGroupInfoBO;

            //雇员信息按公司ID分组
            List<SalaryGrantEmployeeGroupInfoBO> companyGroupInfoBOList = new ArrayList<>();
            //获取查询结果中公司ID
            Set<String> companyIdSet = employeePOList.stream().map(SalaryGrantEmployeePO::getCompanyId).collect(Collectors.toSet());
            for (String companyId : companyIdSet) {
                employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
                employeeGroupInfoBO.setCompanyId(companyId); //公司编号
                employeeGroupInfoBO.setCompanyName(employeePOList.stream().filter(employeePO -> companyId.equals(employeePO.getCompanyId())).findFirst().get().getCompanyName()); //公司名称
                //过滤当前公司ID的雇员数据
                employeeGroupInfoBO.setSalaryGrantEmployeePOList(employeePOList.stream().filter(employeePO -> companyId.equals(employeePO.getCompanyId())).collect(Collectors.toList()));

                companyGroupInfoBOList.add(employeeGroupInfoBO);
            }

            //雇员信息按公司ID、银行卡种类分组
            List<SalaryGrantEmployeeGroupInfoBO> bankcardTypeGroupInfoBOList = new ArrayList<>();
            Set<Integer> bankcardTypeSet;
            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : companyGroupInfoBOList) {
                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
                    //获取当前公司银行卡种类
                    bankcardTypeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getBankcardType).collect(Collectors.toSet());

                    for (Integer cardType : bankcardTypeSet) {
                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());     //公司编号
                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName()); //公司名称
                        employeeGroupInfoBO.setBankcardType(cardType);                    //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
                        //过滤当前公司ID的当前银行卡种类的雇员数据
                        employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> cardType.compareTo(employeePO.getBankcardType()) == 0).collect(Collectors.toList()));

                        bankcardTypeGroupInfoBOList.add(employeeGroupInfoBO);

                    }
                }
            }

            //雇员信息按公司ID、银行卡种类、付款账号分组
            List<SalaryGrantEmployeeGroupInfoBO> accountCodeGroupInfoBOList = new ArrayList<>();
            Set<String> paymentAccountCodeSet;
            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : bankcardTypeGroupInfoBOList) {
                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
                    //获取当前公司当前银行卡种类的付款账号
                    paymentAccountCodeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getPaymentAccountCode).collect(Collectors.toSet());

                    for (String accountCode : paymentAccountCodeSet) {
                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());       //公司编号
                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName());   //公司名称
                        employeeGroupInfoBO.setBankcardType(groupInfoBO.getBankcardType()); //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
                        employeeGroupInfoBO.setPaymentAccountCode(accountCode);             //付款账号
                        //过滤当前公司ID的当前银行卡种类的当前付款账号的雇员数据
                        if (StringUtils.isEmpty(accountCode)) {
                            employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> StringUtils.isEmpty(employeePO.getPaymentAccountCode())).collect(Collectors.toList()));
                        } else {
                            employeeGroupInfoBO.setPaymentAccountName(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).findFirst().get().getPaymentAccountName()); //付款账户名
                            employeeGroupInfoBO.setPaymentAccountBankName(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).findFirst().get().getPaymentAccountBankName()); //付款银行名

                            employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).collect(Collectors.toList()));
                        }

                        accountCodeGroupInfoBOList.add(employeeGroupInfoBO);
                    }
                }
            }

            //雇员信息按公司ID、银行卡种类、付款账号、发放币种分组
            List<SalaryGrantEmployeeGroupInfoBO> currencyCodeGroupInfoBOList = new ArrayList<>();
            Set<String> currencyCodeSet;
            List<SalaryGrantEmployeePO> salaryGrantEmployeePOList;
            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : accountCodeGroupInfoBOList) {
                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
                    //获取当前公司当前银行卡种类的当前付款账号的发放币种
                    currencyCodeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getCurrencyCode).collect(Collectors.toSet());

                    for (String currencyCode : currencyCodeSet) {
                        salaryGrantEmployeePOList = groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> currencyCode.equals(employeePO.getCurrencyCode())).collect(Collectors.toList());

                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
                        employeeGroupInfoBO.setTaskCode(taskCode);
                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());                           //公司编号
                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName());                       //公司名称
                        employeeGroupInfoBO.setBankcardType(groupInfoBO.getBankcardType());                     //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
                        employeeGroupInfoBO.setPaymentAccountCode(groupInfoBO.getPaymentAccountCode());         //付款账号
                        employeeGroupInfoBO.setPaymentAccountName(groupInfoBO.getPaymentAccountName());         //付款账户名
                        employeeGroupInfoBO.setPaymentAccountBankName(groupInfoBO.getPaymentAccountBankName()); //付款银行名
                        employeeGroupInfoBO.setCurrencyCode(currencyCode);                                      //发放币种:CNY-人民币，USD-美元，EUR-欧元
                        employeeGroupInfoBO.setPaymentCount(salaryGrantEmployeePOList.stream().count());        //发薪人数合计
                        employeeGroupInfoBO.setPaymentAmountSum(salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add)); //发放金额合计
                        //过滤当前公司ID的当前银行卡种类的当前付款账号的当前发放币种的雇员数据
                        employeeGroupInfoBO.setSalaryGrantEmployeePOList(salaryGrantEmployeePOList);

                        currencyCodeGroupInfoBOList.add(employeeGroupInfoBO);
                    }
                }
            }

            return currencyCodeGroupInfoBOList;
        } else {
            return null;
        }
    }

    /**
     * 新增报盘文件记录
     *
     * @param employeeGroupInfoBOList
     * @param userId
     */
    @Transactional(rollbackFor = Exception.class)
    public void insertOfferDocumentRecords(List<SalaryGrantEmployeeGroupInfoBO> employeeGroupInfoBOList, String userId) {
        if (!CollectionUtils.isEmpty(employeeGroupInfoBOList)) {
            //新增薪资发放报盘信息表记录
            OfferDocumentPO offerDocumentPO;
            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : employeeGroupInfoBOList) {
                offerDocumentPO = new OfferDocumentPO();
                offerDocumentPO.setTaskCode(groupInfoBO.getTaskCode());                             //任务单编号
                offerDocumentPO.setBankcardType(groupInfoBO.getBankcardType());                     //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
                offerDocumentPO.setCompanyId(groupInfoBO.getCompanyId());                           //公司编号
                offerDocumentPO.setCompanyName(groupInfoBO.getCompanyName());                       //公司名称
                offerDocumentPO.setPaymentAccountCode(groupInfoBO.getPaymentAccountCode());         //付款账号
                offerDocumentPO.setPaymentAccountName(groupInfoBO.getPaymentAccountName());         //付款账户名
                offerDocumentPO.setPaymentAccountBankName(groupInfoBO.getPaymentAccountBankName()); //付款银行名
                offerDocumentPO.setPaymentTotalSum(groupInfoBO.getPaymentAmountSum());              //薪资发放总金额（RMB）
                offerDocumentPO.setTotalPersonCount(groupInfoBO.getPaymentCount().intValue());      //发薪人数
                offerDocumentPO.setActive(true);
                offerDocumentPO.setCreatedBy(userId);
                offerDocumentPO.setCreatedTime(new Date());

                offerDocumentMapper.insert(offerDocumentPO);

                //新增薪资发放报盘文件表记录
                List<OfferDocumentFilePO> documentFilePOList = commonService.generateOfferDocumentFile(groupInfoBO, offerDocumentPO);
                if (!CollectionUtils.isEmpty(documentFilePOList)) {
                    for (OfferDocumentFilePO documentFilePO : documentFilePOList) {
                        offerDocumentFileMapper.insert(documentFilePO);
                    }
                }
            }
        }
    }

    @Override
    public List<OfferDocumentBO> queryOfferDocument(String taskCode) {
        return offerDocumentMapper.queryOfferDocument(taskCode);
    }
}
