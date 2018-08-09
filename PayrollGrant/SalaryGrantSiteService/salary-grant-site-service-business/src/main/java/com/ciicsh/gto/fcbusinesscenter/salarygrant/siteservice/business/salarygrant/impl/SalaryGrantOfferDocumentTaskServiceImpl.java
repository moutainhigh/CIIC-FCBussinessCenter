package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.OfferDocumentFileService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.OfferDocumentFileMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.OfferDocumentMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.*;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
    LogClientService logClientService;
    @Autowired
    CommonService commonService;
    @Autowired
    OfferDocumentFileService offerDocumentFileService;
    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    OfferDocumentMapper offerDocumentMapper;
    @Autowired
    OfferDocumentFileMapper offerDocumentFileMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    @Override
    public Page<SalaryGrantTaskBO> queryOfferDocumentTaskPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        page.setRecords(salaryGrantSubTaskMapper.queryOfferDocumentTaskPage(page, salaryGrantTaskBO));
        return getGrantTypeName(page);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<OfferDocumentBO> generateOfferDocument(String taskCode, String userId) {
        //根据任务单编号查询薪资发放报盘信息表中报盘文件记录
        List<OfferDocumentBO> documentBOList = offerDocumentMapper.queryOfferDocument(taskCode);
        //不存在报盘文件先生成，再查询报盘文件列表
        if (documentBOList.isEmpty()) {
            createOfferDocument(taskCode, userId);
            documentBOList = offerDocumentMapper.queryOfferDocument(taskCode);
        }
        return getBankCardName(documentBOList);
    }

    private void createOfferDocument(String taskCode, String userId) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("新生成薪资发放报盘文件"));
        //根据薪资发放任务单子表编号查询雇员信息
        EntityWrapper<SalaryGrantEmployeePO> entityWrapper = new EntityWrapper<>();
        entityWrapper.where("salary_grant_sub_task_code = {0}", taskCode);
        List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(entityWrapper);
        //雇员分组
        //List<SalaryGrantEmployeeGroupInfoBO> currencyCodeGroupInfoBOList = groupEmployeeInfoData(employeePOList, taskCode);
        List<SalaryGrantEmployeeGroupInfoBO> groupList = groupEmployee(taskCode, employeePOList);
        //生成报盘文件
        generateOfferDocumentRecords(groupList, userId);
    }

    /**
     * 雇员分组
     * @param taskCode
     * @param employeeList
     * @return
     */
    private List<SalaryGrantEmployeeGroupInfoBO> groupEmployee(String taskCode, List<SalaryGrantEmployeePO> employeeList) {
        List<SalaryGrantEmployeeGroupInfoBO> groupBoList = new ArrayList<>();
        if (!employeeList.isEmpty()) {
            Map<EmployeeOfferFileGroupExt, List<SalaryGrantEmployeePO>> extListMap = employeeList.parallelStream().collect(Collectors.groupingBy(EmployeeOfferFileGroupExtTranslator::getEmployeeOfferFileGroup));
            if (!extListMap.isEmpty()) {
                extListMap.forEach((groupingInfo, groupingEmployee) ->
                    {
                        SalaryGrantEmployeeGroupInfoBO groupInfoBO = BeanUtils.instantiate(SalaryGrantEmployeeGroupInfoBO.class);
                        groupInfoBO.setTaskCode(taskCode);
                        groupInfoBO.setCompanyId(groupingInfo.getCompanyId());
                        groupInfoBO.setBankcardType(groupingInfo.getBankcardType());
                        groupInfoBO.setCurrencyCode(groupingInfo.getCurrencyCode());
                        groupInfoBO.setPaymentAccountCode(groupingInfo.getPaymentAccountCode());
                        groupInfoBO.setCompanyName(groupingInfo.getCompanyName());
                        groupInfoBO.setPaymentAccountName(groupingInfo.getPaymentAccountName());
                        groupInfoBO.setPaymentAccountBankName(groupingInfo.getPaymentAccountBankName());
                        groupInfoBO.setPaymentCount(groupingEmployee.parallelStream().count());
                        groupInfoBO.setPaymentAmountSum(groupingEmployee.parallelStream().map(SalaryGrantEmployeePO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add));

                        groupInfoBO.setSalaryGrantEmployeePOList(groupingEmployee);

                        groupBoList.add(groupInfoBO);
                    }
                );
            }
        }
        return groupBoList;
    }

    /**
     * 新增报盘文件记录
     *
     * @param employeeGroupInfoBOList
     * @param userId
     */
    private void generateOfferDocumentRecords(List<SalaryGrantEmployeeGroupInfoBO> employeeGroupInfoBOList, String userId) {
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件 -> 雇员分组信息").setContent(JSON.toJSONString(employeeGroupInfoBOList)));
        if (!employeeGroupInfoBOList.isEmpty()) {
            employeeGroupInfoBOList.stream().forEach( x -> {
                //新增薪资发放报盘信息表记录
                OfferDocumentPO offerDocumentPO = new OfferDocumentPO();
                offerDocumentPO.setTaskCode(x.getTaskCode());
                offerDocumentPO.setBankcardType(x.getBankcardType());
                offerDocumentPO.setCompanyId(x.getCompanyId());
                offerDocumentPO.setCompanyName(x.getCompanyName());
                offerDocumentPO.setPaymentAccountCode(x.getPaymentAccountCode());
                offerDocumentPO.setPaymentAccountName(x.getPaymentAccountName());
                offerDocumentPO.setPaymentAccountBankName(x.getPaymentAccountBankName());
                //薪资发放总金额（RMB）
                offerDocumentPO.setPaymentTotalSum(x.getPaymentAmountSum());
                offerDocumentPO.setTotalPersonCount(x.getPaymentCount().intValue());
                offerDocumentPO.setActive(true);
                offerDocumentPO.setCreatedBy(userId);
                offerDocumentPO.setModifiedBy(userId);
                offerDocumentMapper.insert(offerDocumentPO);
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件 -> 薪资发放报盘信息").setContent(JSON.toJSONString(offerDocumentPO)));
                //生成薪资发放报盘文件
                List<OfferDocumentFilePO> documentFilePOList = commonService.generateOfferDocumentFile(x, offerDocumentPO);
                if (!documentFilePOList.isEmpty()) {
                    offerDocumentFileService.insertBatch(documentFilePOList);
                }
            });
        }
    }

    /**
     * 发放类型名称获取
     */
    private Page<SalaryGrantTaskBO> getGrantTypeName(Page<SalaryGrantTaskBO> page) {
        if (!page.getRecords().isEmpty()) {
            page.getRecords().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getGrantType())) {
                    x.setGrantTypeName(commonService.getNameByValue("sgmGrantType", String.valueOf(x.getGrantType())));
                }
            });
        }
        return page;
    }

    /**
     * 银行卡名称获取
     */
    private List<OfferDocumentBO> getBankCardName(List<OfferDocumentBO> list) {
        if (!list.isEmpty()) {
            list.parallelStream().forEach(x -> {
                if (!ObjectUtils.isEmpty(x.getBankcardType())) {
                    x.setBankcardName(commonService.getNameByValue("sgBankcardType", String.valueOf(x.getBankcardType())));
                }
            });
        }
        return list;
    }

//    /**
//     * 分组雇员信息数据
//     *
//     * @param employeePOList
//     * @return
//     */
//    private List<SalaryGrantEmployeeGroupInfoBO> groupEmployeeInfoData(List<SalaryGrantEmployeePO> employeePOList, String taskCode) {
//        if (!CollectionUtils.isEmpty(employeePOList)) {
//            SalaryGrantEmployeeGroupInfoBO employeeGroupInfoBO;
//
//            //雇员信息按公司ID分组
//            List<SalaryGrantEmployeeGroupInfoBO> companyGroupInfoBOList = new ArrayList<>();
//            //获取查询结果中公司ID
//            Set<String> companyIdSet = employeePOList.stream().map(SalaryGrantEmployeePO::getCompanyId).collect(Collectors.toSet());
//            for (String companyId : companyIdSet) {
//                employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
//                employeeGroupInfoBO.setCompanyId(companyId); //公司编号
//                employeeGroupInfoBO.setCompanyName(employeePOList.stream().filter(employeePO -> companyId.equals(employeePO.getCompanyId())).findFirst().get().getCompanyName()); //公司名称
//                //过滤当前公司ID的雇员数据
//                employeeGroupInfoBO.setSalaryGrantEmployeePOList(employeePOList.stream().filter(employeePO -> companyId.equals(employeePO.getCompanyId())).collect(Collectors.toList()));
//
//                companyGroupInfoBOList.add(employeeGroupInfoBO);
//            }
//
//            //雇员信息按公司ID、银行卡种类分组
//            List<SalaryGrantEmployeeGroupInfoBO> bankcardTypeGroupInfoBOList = new ArrayList<>();
//            Set<Integer> bankcardTypeSet;
//            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : companyGroupInfoBOList) {
//                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
//                    //获取当前公司银行卡种类
//                    bankcardTypeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getBankcardType).collect(Collectors.toSet());
//
//                    for (Integer cardType : bankcardTypeSet) {
//                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
//                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());     //公司编号
//                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName()); //公司名称
//                        employeeGroupInfoBO.setBankcardType(cardType);                    //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
//                        //过滤当前公司ID的当前银行卡种类的雇员数据
//                        employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> cardType.compareTo(employeePO.getBankcardType()) == 0).collect(Collectors.toList()));
//
//                        bankcardTypeGroupInfoBOList.add(employeeGroupInfoBO);
//
//                    }
//                }
//            }
//
//            //雇员信息按公司ID、银行卡种类、付款账号分组
//            List<SalaryGrantEmployeeGroupInfoBO> accountCodeGroupInfoBOList = new ArrayList<>();
//            Set<String> paymentAccountCodeSet;
//            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : bankcardTypeGroupInfoBOList) {
//                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
//                    //获取当前公司当前银行卡种类的付款账号
//                    paymentAccountCodeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getPaymentAccountCode).collect(Collectors.toSet());
//
//                    for (String accountCode : paymentAccountCodeSet) {
//                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
//                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());       //公司编号
//                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName());   //公司名称
//                        employeeGroupInfoBO.setBankcardType(groupInfoBO.getBankcardType()); //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
//                        employeeGroupInfoBO.setPaymentAccountCode(accountCode);             //付款账号
//                        //过滤当前公司ID的当前银行卡种类的当前付款账号的雇员数据
//                        if (StringUtils.isEmpty(accountCode)) {
//                            employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> StringUtils.isEmpty(employeePO.getPaymentAccountCode())).collect(Collectors.toList()));
//                        } else {
//                            employeeGroupInfoBO.setPaymentAccountName(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).findFirst().get().getPaymentAccountName()); //付款账户名
//                            employeeGroupInfoBO.setPaymentAccountBankName(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).findFirst().get().getPaymentAccountBankName()); //付款银行名
//
//                            employeeGroupInfoBO.setSalaryGrantEmployeePOList(groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> accountCode.equals(employeePO.getPaymentAccountCode())).collect(Collectors.toList()));
//                        }
//
//                        accountCodeGroupInfoBOList.add(employeeGroupInfoBO);
//                    }
//                }
//            }
//
//            //雇员信息按公司ID、银行卡种类、付款账号、发放币种分组
//            List<SalaryGrantEmployeeGroupInfoBO> currencyCodeGroupInfoBOList = new ArrayList<>();
//            Set<String> currencyCodeSet;
//            List<SalaryGrantEmployeePO> salaryGrantEmployeePOList;
//            for (SalaryGrantEmployeeGroupInfoBO groupInfoBO : accountCodeGroupInfoBOList) {
//                if (!CollectionUtils.isEmpty(groupInfoBO.getSalaryGrantEmployeePOList())) {
//                    //获取当前公司当前银行卡种类的当前付款账号的发放币种
//                    currencyCodeSet = groupInfoBO.getSalaryGrantEmployeePOList().stream().map(SalaryGrantEmployeePO::getCurrencyCode).collect(Collectors.toSet());
//
//                    for (String currencyCode : currencyCodeSet) {
//                        salaryGrantEmployeePOList = groupInfoBO.getSalaryGrantEmployeePOList().stream().filter(employeePO -> currencyCode.equals(employeePO.getCurrencyCode())).collect(Collectors.toList());
//
//                        employeeGroupInfoBO = new SalaryGrantEmployeeGroupInfoBO();
//                        employeeGroupInfoBO.setTaskCode(taskCode);
//                        employeeGroupInfoBO.setCompanyId(groupInfoBO.getCompanyId());                           //公司编号
//                        employeeGroupInfoBO.setCompanyName(groupInfoBO.getCompanyName());                       //公司名称
//                        employeeGroupInfoBO.setBankcardType(groupInfoBO.getBankcardType());                     //银行卡种类:1:中国银行2:建设银行3:工商银行4:招商银行5:其他银行
//                        employeeGroupInfoBO.setPaymentAccountCode(groupInfoBO.getPaymentAccountCode());         //付款账号
//                        employeeGroupInfoBO.setPaymentAccountName(groupInfoBO.getPaymentAccountName());         //付款账户名
//                        employeeGroupInfoBO.setPaymentAccountBankName(groupInfoBO.getPaymentAccountBankName()); //付款银行名
//                        employeeGroupInfoBO.setCurrencyCode(currencyCode);                                      //发放币种:CNY-人民币，USD-美元，EUR-欧元
//                        employeeGroupInfoBO.setPaymentCount(salaryGrantEmployeePOList.stream().count());        //发薪人数合计
//                        employeeGroupInfoBO.setPaymentAmountSum(salaryGrantEmployeePOList.stream().map(SalaryGrantEmployeePO::getPaymentAmount).reduce(BigDecimal.ZERO, BigDecimal::add)); //发放金额合计
//                        //过滤当前公司ID的当前银行卡种类的当前付款账号的当前发放币种的雇员数据
//                        employeeGroupInfoBO.setSalaryGrantEmployeePOList(salaryGrantEmployeePOList);
//
//                        currencyCodeGroupInfoBOList.add(employeeGroupInfoBO);
//                    }
//                }
//            }
//
//            return currencyCodeGroupInfoBOList;
//        } else {
//            return null;
//        }
//    }

}
