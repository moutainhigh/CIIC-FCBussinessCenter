package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantSupplierSubTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantSubTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantTaskHistoryMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantSubTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.math.RoundingMode;
import java.util.*;

/**
 * <p>
 * 薪资发放供应商任务单 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-24
 */
@Service
public class SalaryGrantSupplierSubTaskServiceImpl extends ServiceImpl<SalaryGrantSubTaskMapper, SalaryGrantSubTaskPO> implements SalaryGrantSupplierSubTaskService {

    @Autowired
    SalaryGrantSubTaskMapper salaryGrantSubTaskMapper;
    @Autowired
    SalaryGrantTaskHistoryMapper salaryGrantTaskHistoryMapper;
    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    CommonService commonService;

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForSubmitPageUpdateEmployee(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        Page<SalaryGrantTaskBO> retTaskBOPage = querySupplierSubTaskForSubmitPage(page, salaryGrantTaskBO);
        if (!ObjectUtils.isEmpty(retTaskBOPage)) {
            //获取当前页面子任务单记录
            List<SalaryGrantTaskBO> salaryGrantTaskBOList = retTaskBOPage.getRecords();
            if (!CollectionUtils.isEmpty(salaryGrantTaskBOList)) {
                int size = salaryGrantTaskBOList.size();
                SalaryGrantTaskBO subTaskBO;
                for (int i = 0; i < size; i++) {
                    //当前子任务单记录
                    subTaskBO = salaryGrantTaskBOList.get(i);

                    //是否当前任务单下雇员信息发生变化
                    boolean ifUpdateTask = false;

                    //根据子任务表task_code查询雇员列表
                    EntityWrapper<SalaryGrantEmployeePO> employeePOEntityWrapper = new EntityWrapper<>();
                    employeePOEntityWrapper.where("salary_grant_sub_task_code = {0} and is_active = 1", subTaskBO.getTaskCode());
                    List<SalaryGrantEmployeePO> employeePOList = salaryGrantEmployeeMapper.selectList(employeePOEntityWrapper);
                    if (!CollectionUtils.isEmpty(employeePOList)) {
                        for (SalaryGrantEmployeePO currentEmployeePO : employeePOList) {
                            //调用接口获取最新雇员信息
                            SalaryGrantEmployeePO newestEmployeePO = commonService.getEmployeeNewestInfo(currentEmployeePO);
                            //比较并更新雇员最新信息
                            boolean updateResult = compareAndUpdateEmployeeNewestInfo(currentEmployeePO, newestEmployeePO);

                            if (updateResult == true && ifUpdateTask == false) {
                                ifUpdateTask = true;
                            }
                        }
                    }

                    //子任务单下雇员信息发生变化时更新子任务单备注
                    if (ifUpdateTask == true) {
                        SalaryGrantSubTaskPO updateSubTaskPO = new SalaryGrantSubTaskPO();
                        updateSubTaskPO.setSalaryGrantSubTaskId(subTaskBO.getTaskId());
                        updateSubTaskPO.setRemark("雇员信息发生变化");
                        salaryGrantSubTaskMapper.updateById(updateSubTaskPO);

                        subTaskBO.setRemark("雇员信息发生变化");
                    }
                }
            }
        }

        return retTaskBOPage;
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForApprovePage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForApprovePage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForHaveApprovedPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForHaveApprovedPage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForPassPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantSubTaskMapper.querySupplierSubTaskForPassPage(page, salaryGrantTaskBO));
    }

    @Override
    public Page<SalaryGrantTaskBO> querySupplierSubTaskForRejectPage(Page<SalaryGrantTaskBO> page, SalaryGrantTaskBO salaryGrantTaskBO) {
        return page.setRecords(salaryGrantTaskHistoryMapper.querySupplierSubTaskForRejectPage(page, salaryGrantTaskBO));
    }

    /**
     * 比较并更新雇员最新信息
     *
     * @param currentEmployeePO
     * @param newestEmployeePO
     */
    @Transactional(rollbackFor = Exception.class)
    public boolean compareAndUpdateEmployeeNewestInfo(SalaryGrantEmployeePO currentEmployeePO, SalaryGrantEmployeePO newestEmployeePO) {
        //比较对象任一为空返回false
        if (ObjectUtils.isEmpty(currentEmployeePO) || ObjectUtils.isEmpty(newestEmployeePO)) {
            return false;
        }

        //变更日志
        String changeLog = currentEmployeePO.getChangeLog();
        if (!StringUtils.isEmpty(changeLog)) {
            //恢复雇员变更信息对象
            Map restoreOuterMap = JSONObject.parseObject(changeLog, Map.class);

            //变更日志
            Map<String, String> outerMap = new HashMap<>();
            //备注
            StringBuilder remarkStringBuilder = new StringBuilder();

            //更新时间
            Date updateDate = new Date();
            //旧时间 = 记录创建时间
            Date oldDate = currentEmployeePO.getCreatedTime();

            Map<String, Object> innerMap;

            //雇员基本信息表
            //国籍
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCountryCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCountryCode())) {
                if (!currentEmployeePO.getCountryCode().equals(newestEmployeePO.getCountryCode())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("country_code");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getCountryCode());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("country_code", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getCountryCode());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getCountryCode());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_employee");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("country_code", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("国籍、");

                    currentEmployeePO.setCountryCode(newestEmployeePO.getCountryCode());
                }
            }

            //雇员银行卡信息
            //银行卡种类
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankcardType()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankcardType())) {
                if (currentEmployeePO.getBankcardType().compareTo(newestEmployeePO.getBankcardType()) != 0) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("bankcard_type");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getBankcardType());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("bankcard_type", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getBankcardType());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getBankcardType());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("bankcard_type", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("银行卡种类、");

                    currentEmployeePO.setBankcardType(newestEmployeePO.getBankcardType());
                }
            }

            //收款行名称
            if (!ObjectUtils.isEmpty(currentEmployeePO.getDepositBank()) && !ObjectUtils.isEmpty(newestEmployeePO.getDepositBank())) {
                if (!currentEmployeePO.getDepositBank().equals(newestEmployeePO.getDepositBank())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("deposit_bank");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getDepositBank());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("deposit_bank", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getDepositBank());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getDepositBank());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("deposit_bank", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("收款行名称、");

                    currentEmployeePO.setDepositBank(newestEmployeePO.getDepositBank());
                }
            }

            //收款行行号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankCode())) {
                if (!currentEmployeePO.getBankCode().equals(newestEmployeePO.getBankCode())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("bank_code");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getBankCode());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("bank_code", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getBankCode());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getBankCode());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("bank_code", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("收款行行号、");

                    currentEmployeePO.setBankCode(newestEmployeePO.getBankCode());
                }
            }

            //收款人姓名
            if (!ObjectUtils.isEmpty(currentEmployeePO.getAccountName()) && !ObjectUtils.isEmpty(newestEmployeePO.getAccountName())) {
                if (!currentEmployeePO.getAccountName().equals(newestEmployeePO.getAccountName())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("account_name");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getAccountName());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("account_name", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getAccountName());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getAccountName());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("account_name", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("收款人姓名、");

                    currentEmployeePO.setAccountName(newestEmployeePO.getAccountName());
                }
            }

            //收款人账号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCardNum()) && !ObjectUtils.isEmpty(newestEmployeePO.getCardNum())) {
                if (!currentEmployeePO.getCardNum().equals(newestEmployeePO.getCardNum())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("card_num");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getCardNum());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("card_num", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getCardNum());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getCardNum());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("card_num", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("收款人账号、");

                    currentEmployeePO.setCardNum(newestEmployeePO.getCardNum());
                }
            }

            //银行卡币种
//            if (!ObjectUtils.isEmpty(currentEmployeePO.getCurrencyCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCurrencyCode())) {
//                if (!currentEmployeePO.getCurrencyCode().equals(newestEmployeePO.getCurrencyCode())) {
//                    //从已存在变更信息中取出当前字段信息
//                    Object mapObject = restoreOuterMap.get("currency_code");
//                    if (!ObjectUtils.isEmpty(mapObject)) {
//                        //已存在变更信息中存在当前字段信息时
//                        //恢复当前字段信息对象
//                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);
//
//                        //仅更新当前字段的新值
//                        innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
//                        innerMap.put("new_date", updateDate);
//
//                        //将更新后的当前字段变更信息放进变更日志信息中
//                        outerMap.put("currency_code", JSONObject.toJSONString(innerMap));
//                    } else {
//                        //已存在变更信息中不存在当前字段信息时
//                        innerMap = new HashMap<>();
//                        innerMap.put("old_value", currentEmployeePO.getCurrencyCode());
//                        innerMap.put("old_date", oldDate);
//                        innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
//                        innerMap.put("new_date", updateDate);
//                        innerMap.put("type", "emp_fc_bankcard");
//
//                        //将新建的当前字段变更信息放进变更日志信息中
//                        outerMap.put("currency_code", JSONObject.toJSONString(innerMap));
//                    }
//
//                    remarkStringBuilder.append("银行卡币种、");
//
//                    currentEmployeePO.setCurrencyCode(newestEmployeePO.getCurrencyCode());
//                }
//            }

            //银行卡汇率
            if (!ObjectUtils.isEmpty(currentEmployeePO.getExchange()) && !ObjectUtils.isEmpty(newestEmployeePO.getExchange())) {
                if (!currentEmployeePO.getExchange().equals(newestEmployeePO.getExchange())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("exchange");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getExchange());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("exchange", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getExchange());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getExchange());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("exchange", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("银行卡汇率、");

                    currentEmployeePO.setExchange(newestEmployeePO.getExchange());
                }
            }

            //银行国际代码
            if (!ObjectUtils.isEmpty(currentEmployeePO.getSwiftCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getSwiftCode())) {
                if (!currentEmployeePO.getSwiftCode().equals(newestEmployeePO.getSwiftCode())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("swift_code");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getSwiftCode());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("swift_code", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getSwiftCode());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getSwiftCode());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("swift_code", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("银行国际代码、");

                    currentEmployeePO.setSwiftCode(newestEmployeePO.getSwiftCode());
                }
            }

            //国际银行账户号码
            if (!ObjectUtils.isEmpty(currentEmployeePO.getIban()) && !ObjectUtils.isEmpty(newestEmployeePO.getIban())) {
                if (!currentEmployeePO.getIban().equals(newestEmployeePO.getIban())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("iban");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getIban());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("iban", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getIban());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getIban());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("iban", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("国际银行账户号码、");

                    currentEmployeePO.setIban(newestEmployeePO.getIban());
                }
            }

            //是否默认卡
            if (!ObjectUtils.isEmpty(currentEmployeePO.getDefaultCard()) && !ObjectUtils.isEmpty(newestEmployeePO.getDefaultCard())) {
                if (!currentEmployeePO.getDefaultCard().equals(newestEmployeePO.getDefaultCard())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("is_default_card");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getDefaultCard());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("is_default_card", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getDefaultCard());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getDefaultCard());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "emp_fc_bankcard");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("is_default_card", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("是否默认卡、");

                    currentEmployeePO.setDefaultCard(newestEmployeePO.getDefaultCard());
                }
            }

            //薪资发放规则信息
            //银行卡编号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankcardId()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankcardId())) {
                if (!currentEmployeePO.getBankcardId().equals(newestEmployeePO.getBankcardId())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("bankcard_id");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getBankcardId());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("bankcard_id", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getBankcardId());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getBankcardId());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("bankcard_id", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("银行卡编号、");

                    currentEmployeePO.setBankcardId(newestEmployeePO.getBankcardId());
                }
            }

            //发放币种
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCurrencyCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCurrencyCode())) {
                if (!currentEmployeePO.getCurrencyCode().equals(newestEmployeePO.getCurrencyCode())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("currency_code2");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("currency_code2", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getCurrencyCode());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("currency_code2", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("发放币种、");

                    currentEmployeePO.setCurrencyCode(newestEmployeePO.getCurrencyCode());
                }
            }

            //规则金额
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleAmount()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleAmount())) {
                if (!currentEmployeePO.getRuleAmount().equals(newestEmployeePO.getRuleAmount())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("rule_amount");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getRuleAmount());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_amount", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getRuleAmount());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getRuleAmount());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_amount", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("规则金额、");

                    currentEmployeePO.setRuleAmount(newestEmployeePO.getRuleAmount());
                }
            }

            //规则比例
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleRatio()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleRatio())) {
                if (!currentEmployeePO.getRuleRatio().equals(newestEmployeePO.getRuleRatio())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("rule_ratio");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getRuleRatio());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_ratio", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getRuleRatio());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getRuleRatio());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_ratio", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("规则比例、");

                    currentEmployeePO.setRuleRatio(newestEmployeePO.getRuleRatio());
                }
            }

            //规则类型
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleType()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleType())) {
                if (!currentEmployeePO.getRuleType().equals(newestEmployeePO.getRuleType())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("rule_type");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getRuleType());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_type", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getRuleType());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getRuleType());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("rule_type", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("规则类型、");

                    currentEmployeePO.setRuleType(newestEmployeePO.getRuleType());
                }
            }

            //雇员服务协议
            //发放方式
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantMode()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantMode())) {
                if (!currentEmployeePO.getGrantMode().equals(newestEmployeePO.getGrantMode())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("grant_mode");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getGrantMode());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_mode", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getGrantMode());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getGrantMode());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_employee_service_agreement");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_mode", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("发放方式、");

                    currentEmployeePO.setGrantMode(newestEmployeePO.getGrantMode());
                }
            }

            //服务周期规则表
            //薪资发放日期
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantDate()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantDate())) {
                if (!currentEmployeePO.getGrantDate().equals(newestEmployeePO.getGrantDate())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("grant_date");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getGrantDate());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_date", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getGrantDate());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getGrantDate());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_cycle_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_date", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("薪资发放日期、");

                    currentEmployeePO.setGrantDate(newestEmployeePO.getGrantDate());
                }
            }

            //薪资发放时段
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantTime()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantTime())) {
                if (!currentEmployeePO.getGrantTime().equals(newestEmployeePO.getGrantTime())) {
                    //从已存在变更信息中取出当前字段信息
                    Object mapObject = restoreOuterMap.get("grant_time");
                    if (!ObjectUtils.isEmpty(mapObject)) {
                        //已存在变更信息中存在当前字段信息时
                        //恢复当前字段信息对象
                        innerMap = JSONObject.parseObject((String) mapObject, Map.class);

                        //仅更新当前字段的新值
                        innerMap.put("new_value", newestEmployeePO.getGrantTime());
                        innerMap.put("new_date", updateDate);

                        //将更新后的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_time", JSONObject.toJSONString(innerMap));
                    } else {
                        //已存在变更信息中不存在当前字段信息时
                        innerMap = new HashMap<>();
                        innerMap.put("old_value", currentEmployeePO.getGrantTime());
                        innerMap.put("old_date", oldDate);
                        innerMap.put("new_value", newestEmployeePO.getGrantTime());
                        innerMap.put("new_date", updateDate);
                        innerMap.put("type", "cmy_fc_cycle_rule");

                        //将新建的当前字段变更信息放进变更日志信息中
                        outerMap.put("grant_time", JSONObject.toJSONString(innerMap));
                    }

                    remarkStringBuilder.append("薪资发放时段、");

                    currentEmployeePO.setGrantTime(newestEmployeePO.getGrantTime());
                }
            }

            //当前雇员信息发生变化时更新
            if (!CollectionUtils.isEmpty(outerMap)) {
                currentEmployeePO.setChangeLog(JSONObject.toJSONString(outerMap));

                remarkStringBuilder.deleteCharAt(remarkStringBuilder.length() - 1);
                remarkStringBuilder.append("发生变化");
                currentEmployeePO.setRemark(remarkStringBuilder.toString());

                currentEmployeePO.setModifiedTime(updateDate);
                salaryGrantEmployeeMapper.updateById(currentEmployeePO);

                return true;
            }
        } else {
            //变更日志
            Map<String, String> outerMap = new HashMap<>();
            //备注
            StringBuilder remarkStringBuilder = new StringBuilder();

            //更新时间
            Date updateDate = new Date();
            //旧信息时间，修改时间存在时取修改时间，否则取创建时间
            Date oldDate = currentEmployeePO.getCreatedTime();

            Map<String, Object> innerMap;

            //雇员基本信息表
            //国籍
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCountryCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCountryCode())) {
                if (!currentEmployeePO.getCountryCode().equals(newestEmployeePO.getCountryCode())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getCountryCode());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getCountryCode());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_employee");

                    outerMap.put("country_code", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("国籍、");

                    currentEmployeePO.setCountryCode(newestEmployeePO.getCountryCode());
                }
            }

            //雇员银行卡信息
            //银行卡种类
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankcardType()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankcardType())) {
                if (currentEmployeePO.getBankcardType().compareTo(newestEmployeePO.getBankcardType()) != 0) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getBankcardType());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getBankcardType());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("bankcard_type", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("银行卡种类、");

                    currentEmployeePO.setBankcardType(newestEmployeePO.getBankcardType());
                }
            }

            //收款行名称
            if (!ObjectUtils.isEmpty(currentEmployeePO.getDepositBank()) && !ObjectUtils.isEmpty(newestEmployeePO.getDepositBank())) {
                if (!currentEmployeePO.getDepositBank().equals(newestEmployeePO.getDepositBank())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getDepositBank());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getDepositBank());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("deposit_bank", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("收款行名称、");

                    currentEmployeePO.setDepositBank(newestEmployeePO.getDepositBank());
                }
            }

            //收款行行号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankCode())) {
                if (!currentEmployeePO.getBankCode().equals(newestEmployeePO.getBankCode())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getBankCode());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getBankCode());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("bank_code", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("收款行行号、");

                    currentEmployeePO.setBankCode(newestEmployeePO.getBankCode());
                }
            }

            //收款人姓名
            if (!ObjectUtils.isEmpty(currentEmployeePO.getAccountName()) && !ObjectUtils.isEmpty(newestEmployeePO.getAccountName())) {
                if (!currentEmployeePO.getAccountName().equals(newestEmployeePO.getAccountName())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getAccountName());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getAccountName());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("account_name", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("收款人姓名、");

                    currentEmployeePO.setAccountName(newestEmployeePO.getAccountName());
                }
            }

            //收款人账号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCardNum()) && !ObjectUtils.isEmpty(newestEmployeePO.getCardNum())) {
                if (!currentEmployeePO.getCardNum().equals(newestEmployeePO.getCardNum())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getCardNum());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getCardNum());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("card_num", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("收款人账号、");

                    currentEmployeePO.setCardNum(newestEmployeePO.getCardNum());
                }
            }

            //银行卡币种
//            if (!ObjectUtils.isEmpty(currentEmployeePO.getCurrencyCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCurrencyCode())) {
//                if (!currentEmployeePO.getCurrencyCode().equals(newestEmployeePO.getCurrencyCode())) {
//                    innerMap = new HashMap<>();
//                    innerMap.put("old_value", currentEmployeePO.getCurrencyCode());
//                    innerMap.put("old_date", oldDate);
//                    innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
//                    innerMap.put("new_date", updateDate);
//                    innerMap.put("type", "emp_fc_bankcard");
//
//                    outerMap.put("currency_code", JSONObject.toJSONString(innerMap));
//
//                    remarkStringBuilder.append("银行卡币种、");
//
//                    currentEmployeePO.setCurrencyCode(newestEmployeePO.getCurrencyCode());
//                }
//            }

            //银行卡汇率
            if (!ObjectUtils.isEmpty(currentEmployeePO.getExchange()) && !ObjectUtils.isEmpty(newestEmployeePO.getExchange())) {
                if (!currentEmployeePO.getExchange().equals(newestEmployeePO.getExchange())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getExchange().setScale(4, RoundingMode.HALF_UP));
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getExchange().setScale(4, RoundingMode.HALF_UP));
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("exchange", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("银行卡汇率、");

                    currentEmployeePO.setExchange(newestEmployeePO.getExchange());
                }
            }

            //银行国际代码
            if (!ObjectUtils.isEmpty(currentEmployeePO.getSwiftCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getSwiftCode())) {
                if (!currentEmployeePO.getSwiftCode().equals(newestEmployeePO.getSwiftCode())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getSwiftCode());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getSwiftCode());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("swift_code", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("银行国际代码、");

                    currentEmployeePO.setSwiftCode(newestEmployeePO.getSwiftCode());
                }
            }

            //国际银行账户号码
            if (!ObjectUtils.isEmpty(currentEmployeePO.getIban()) && !ObjectUtils.isEmpty(newestEmployeePO.getIban())) {
                if (!currentEmployeePO.getIban().equals(newestEmployeePO.getIban())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getIban());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getIban());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("iban", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("国际银行账户号码、");

                    currentEmployeePO.setIban(newestEmployeePO.getIban());
                }
            }

            //是否默认卡
            if (!ObjectUtils.isEmpty(currentEmployeePO.getDefaultCard()) && !ObjectUtils.isEmpty(newestEmployeePO.getDefaultCard())) {
                if (!currentEmployeePO.getDefaultCard().equals(newestEmployeePO.getDefaultCard())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getDefaultCard());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getDefaultCard());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "emp_fc_bankcard");

                    outerMap.put("is_default_card", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("是否默认卡、");

                    currentEmployeePO.setDefaultCard(newestEmployeePO.getDefaultCard());
                }
            }

            //薪资发放规则信息
            //银行卡编号
            if (!ObjectUtils.isEmpty(currentEmployeePO.getBankcardId()) && !ObjectUtils.isEmpty(newestEmployeePO.getBankcardId())) {
                if (!currentEmployeePO.getBankcardId().equals(newestEmployeePO.getBankcardId())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getBankcardId());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getBankcardId());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                    outerMap.put("bankcard_id", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("银行卡编号、");

                    currentEmployeePO.setBankcardId(newestEmployeePO.getBankcardId());
                }
            }

            //发放币种
            if (!ObjectUtils.isEmpty(currentEmployeePO.getCurrencyCode()) && !ObjectUtils.isEmpty(newestEmployeePO.getCurrencyCode())) {
                if (!currentEmployeePO.getCurrencyCode().equals(newestEmployeePO.getCurrencyCode())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getCurrencyCode());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getCurrencyCode());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                    outerMap.put("currency_code2", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("发放币种、");

                    currentEmployeePO.setCurrencyCode(newestEmployeePO.getCurrencyCode());
                }
            }

            //规则金额
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleAmount()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleAmount())) {
                if (!currentEmployeePO.getRuleAmount().equals(newestEmployeePO.getRuleAmount())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getRuleAmount().setScale(2, RoundingMode.HALF_UP));
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getRuleAmount().setScale(2, RoundingMode.HALF_UP));
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                    outerMap.put("rule_amount", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("规则金额、");

                    currentEmployeePO.setRuleAmount(newestEmployeePO.getRuleAmount());
                }
            }

            //规则比例
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleRatio()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleRatio())) {
                if (!currentEmployeePO.getRuleRatio().equals(newestEmployeePO.getRuleRatio())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getRuleRatio().setScale(2, RoundingMode.HALF_UP));
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getRuleRatio().setScale(2, RoundingMode.HALF_UP));
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                    outerMap.put("rule_ratio", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("规则比例、");

                    currentEmployeePO.setRuleRatio(newestEmployeePO.getRuleRatio());
                }
            }

            //规则类型
            if (!ObjectUtils.isEmpty(currentEmployeePO.getRuleType()) && !ObjectUtils.isEmpty(newestEmployeePO.getRuleType())) {
                if (!currentEmployeePO.getRuleType().equals(newestEmployeePO.getRuleType())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getRuleType());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getRuleType());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_salary_grant_rule");

                    outerMap.put("rule_type", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("规则类型、");

                    currentEmployeePO.setRuleType(newestEmployeePO.getRuleType());
                }
            }

            //雇员服务协议
            //发放方式
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantMode()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantMode())) {
                if (!currentEmployeePO.getGrantMode().equals(newestEmployeePO.getGrantMode())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getGrantMode());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getGrantMode());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_employee_service_agreement");

                    outerMap.put("grant_mode", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("发放方式、");

                    currentEmployeePO.setGrantMode(newestEmployeePO.getGrantMode());
                }
            }

            //服务周期规则表
            //薪资发放日期
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantDate()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantDate())) {
                if (!currentEmployeePO.getGrantDate().equals(newestEmployeePO.getGrantDate())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getGrantDate());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getGrantDate());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_cycle_rule");

                    outerMap.put("grant_date", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("薪资发放日期、");

                    currentEmployeePO.setGrantDate(newestEmployeePO.getGrantDate());
                }
            }

            //薪资发放时段
            if (!ObjectUtils.isEmpty(currentEmployeePO.getGrantTime()) && !ObjectUtils.isEmpty(newestEmployeePO.getGrantTime())) {
                if (!currentEmployeePO.getGrantTime().equals(newestEmployeePO.getGrantTime())) {
                    innerMap = new HashMap<>();
                    innerMap.put("old_value", currentEmployeePO.getGrantTime());
                    innerMap.put("old_date", oldDate);
                    innerMap.put("new_value", newestEmployeePO.getGrantTime());
                    innerMap.put("new_date", updateDate);
                    innerMap.put("type", "cmy_fc_cycle_rule");

                    outerMap.put("grant_time", JSONObject.toJSONString(innerMap));

                    remarkStringBuilder.append("薪资发放时段、");

                    currentEmployeePO.setGrantTime(newestEmployeePO.getGrantTime());
                }
            }

            //当前雇员信息发生变化时更新
            if (!CollectionUtils.isEmpty(outerMap)) {
                currentEmployeePO.setChangeLog(JSONObject.toJSONString(outerMap));

                remarkStringBuilder.deleteCharAt(remarkStringBuilder.length() - 1);
                remarkStringBuilder.append("发生变化");
                currentEmployeePO.setRemark(remarkStringBuilder.toString());

                currentEmployeePO.setModifiedTime(updateDate);
                salaryGrantEmployeeMapper.updateById(currentEmployeePO);

                return true;
            }
        }

        return false;
    }
}
