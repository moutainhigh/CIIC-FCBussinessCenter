package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gt1.CalResultMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.JsonParseConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantEmployeeQueryService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.CalcResultItemBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.EmpCalcResultBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.SalaryGrantEmpHisOpt;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.JsonResult;
import com.mongodb.DBObject;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放雇员信息查询 服务实现类
 * </p>
 *
 * @author Rock.Jiang
 * @since 2018-04-23
 */
@Service
public class SalaryGrantEmployeeQueryServiceImpl extends ServiceImpl<SalaryGrantEmployeeMapper, SalaryGrantEmployeePO> implements SalaryGrantEmployeeQueryService {

    @Autowired
    private SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;
    @Autowired
    CommonService commonService;
    @Autowired
    CalResultMongoOpt calResultMongoOpt;
    @Autowired
    SalaryGrantEmpHisOpt salaryGrantEmpHisOpt;

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        Page<SalaryGrantEmployeeBO> employeeBOList;
        //查询主表的雇员信息
        if (SalaryGrantBizConsts.TASK_STATUS_REFUSE.equals(salaryGrantEmployeeBO.getTaskStatus()) || SalaryGrantBizConsts.TASK_STATUS_CANCEL.equals(salaryGrantEmployeeBO.getTaskStatus())) {
            employeeBOList = queryEmpHisInfo(salaryGrantEmployeeBO.getTaskId(), page.getCurrent(), page.getSize());
        } else if (salaryGrantEmployeeBO.getTaskType() == SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.intValue()) {
            salaryGrantEmployeeBO.setSalaryGrantMainTaskCode(salaryGrantEmployeeBO.getTaskCode());
            employeeBOList = queryEmployeeForMainTask(page, salaryGrantEmployeeBO);
        } else {
            //查询子表的雇员信息
            salaryGrantEmployeeBO.setSalaryGrantSubTaskCode(salaryGrantEmployeeBO.getTaskCode());
            employeeBOList = queryEmployeeForSubTask(page, salaryGrantEmployeeBO);
        }

        if (!employeeBOList.getRecords().isEmpty()) {
            employeeBOList.getRecords().forEach(salaryGrantTaskBO -> {
                //国籍转码
                if (!StringUtils.isEmpty(salaryGrantTaskBO.getCountryCode())) {
                    salaryGrantTaskBO.setCountryName(commonService.getCountryName(salaryGrantTaskBO.getCountryCode()));
                }

                //发放状态
                if (!ObjectUtils.isEmpty(salaryGrantTaskBO.getGrantStatus())) {
                    salaryGrantTaskBO.setGrantStatusName(commonService.getNameByValue("sgGrantStatus", String.valueOf(salaryGrantTaskBO.getGrantStatus())));
                }
            });
        }

        return employeeBOList;
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeForMainTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        return page.setRecords(salaryGrantEmployeeMapper.selectBOList(page, salaryGrantEmployeeBO));
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeForSubTask(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        return page.setRecords(salaryGrantEmployeeMapper.selectBOList(page, salaryGrantEmployeeBO));
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmployeeInfoChanged(Page<SalaryGrantEmployeeBO> page, SalaryGrantEmployeeBO salaryGrantEmployeeBO) {
        if (salaryGrantEmployeeBO.getTaskType().intValue() == SalaryGrantBizConsts.SALARY_GRANT_TASK_TYPE_MAIN_TASK.intValue()) {
            salaryGrantEmployeeBO.setSalaryGrantMainTaskCode(salaryGrantEmployeeBO.getTaskCode());
        } else {
            salaryGrantEmployeeBO.setSalaryGrantSubTaskCode(salaryGrantEmployeeBO.getTaskCode());
        }
        return page.setRecords(salaryGrantEmployeeMapper.selectBOList(page, salaryGrantEmployeeBO));
    }

    @Override
    public List listAdjustCalcInfo(SalaryGrantEmployeePO salaryGrantEmployeePO) {
        // todo
        return null;
    }

    @Override
    public List<CalcResultItemBO> getSalaryCalcResultItemsList(Map batchParam) {
        List<CalcResultItemBO> salaryCalcResultItemsList = new ArrayList<>();

        String batchCode = (String) batchParam.get("batchCode");
        Integer batchType = (Integer) batchParam.get("batchType");
        List<DBObject> dbObjectList = calResultMongoOpt.list(Criteria.where("batch_id").is(batchCode).and("batch_type").is(batchType));
        if (!CollectionUtils.isEmpty(dbObjectList)) {
            List<DBObject> payItems = (List<DBObject>) dbObjectList.get(0).get("salary_calc_result_items");
            if (!CollectionUtils.isEmpty(payItems)) {
                payItems.forEach(dbObject -> {
                            CalcResultItemBO resultItemBO = new CalcResultItemBO();
                            resultItemBO.setItemCode(dbObject.get("item_code").toString());
                            resultItemBO.setItemName(dbObject.get("item_name").toString());

                            salaryCalcResultItemsList.add(resultItemBO);
                        }
                );
            }
        }

        return salaryCalcResultItemsList;
    }

    @Override
    public List<EmpCalcResultBO> getEmpCalcResultItemsList(String batchCode, int grantType) {
        //雇员薪资项结果
        List<EmpCalcResultBO> empCalcResultBOList = new ArrayList<>();

        List<DBObject> dbObjectList = calResultMongoOpt.list(Criteria.where("batch_id").is(batchCode).and("batch_type").is(grantType));
        if (!CollectionUtils.isEmpty(dbObjectList)) {
            dbObjectList.stream().forEach(dbObject -> {
                        EmpCalcResultBO empCalcResultBO = new EmpCalcResultBO();
                        //薪资项结果列表
                        List<CalcResultItemBO> empCalcResultItemsList = new ArrayList<>();
                        DBObject empInfo = (DBObject) (dbObject.get("emp_info"));
                        //雇员编号
                        empCalcResultBO.setEmployeeId(StringUtils.isEmpty(empInfo.get(PayItemName.EMPLOYEE_CODE_CN) ) ? null : empInfo.get(PayItemName.EMPLOYEE_CODE_CN) .toString());
                        //公司编号
                        empCalcResultBO.setCompanyId(StringUtils.isEmpty(empInfo.get(PayItemName.EMPLOYEE_COUNTRY_CODE_CN)) ? null : empInfo.get(PayItemName.EMPLOYEE_COUNTRY_CODE_CN).toString());

                        //发放币种:CNY-人民币，USD-美元，EUR-欧元
                        DBObject employeeServiceAgreement = (DBObject) empInfo.get(PayItemName.EMPLOYEE_SERVICE_AGREE);
                        if (!ObjectUtils.isEmpty(employeeServiceAgreement)) {
                            DBObject salaryGrantInfo = (DBObject) employeeServiceAgreement.get(JsonParseConsts.EMLOYEE_SERVICE_AGREEMENT_DATA_SALARY_GRANT_INFO);
                            if (!ObjectUtils.isEmpty(salaryGrantInfo)) {
                                empCalcResultBO.setCurrencyCode(ObjectUtils.isEmpty(salaryGrantInfo.get("currencyType")) ? null : salaryGrantInfo.get("currencyType").toString());
                            } else {
                                //解析发放币种失败时抛异常
                                throw new RuntimeException("雇员服务协议薪资发放信息中币种不能为空！");
                            }
                        }

                        String salaryCalcResultItemsJSONStr = StringUtils.isEmpty(dbObject.get("salary_calc_result_items")) ? null : dbObject.get("salary_calc_result_items").toString();
                        JSONArray salaryCalcResultItemsJSONArray = JSONArray.parseArray(salaryCalcResultItemsJSONStr);
                        int size = salaryCalcResultItemsJSONArray.size();
                        for (int i = 0; i < size; i++) {
                            JSONObject salaryCalcResultItemJSONObject = salaryCalcResultItemsJSONArray.getJSONObject(i);

                            CalcResultItemBO calcResultItemBO = new CalcResultItemBO();
                            calcResultItemBO.setItemCode(StringUtils.isEmpty(salaryCalcResultItemJSONObject.get("item_code")) ? null : salaryCalcResultItemJSONObject.get("item_code").toString());
                            calcResultItemBO.setItemName(StringUtils.isEmpty(salaryCalcResultItemJSONObject.get("item_name")) ? null : salaryCalcResultItemJSONObject.get("item_name").toString());
                            calcResultItemBO.setItemValue(StringUtils.isEmpty(salaryCalcResultItemJSONObject.get("item_value")) ? null : salaryCalcResultItemJSONObject.get("item_value").toString());

                            empCalcResultItemsList.add(calcResultItemBO);
                        }

                        empCalcResultBO.setEmpCalcResultItemsList(empCalcResultItemsList);

                        empCalcResultBOList.add(empCalcResultBO);
                    }
            );
        }

        return empCalcResultBOList;
    }

    @Override
    public List<EmpCalcResultBO> getEmployeeForBizList(List<CalcResultItemBO> checkedItemsList, SalaryGrantTaskBO salaryGrantTaskBO) {
        if (CollectionUtils.isEmpty(checkedItemsList)) {
            return null;
        }

        SalaryGrantEmployeeBO salaryGrantEmployeeBO = new SalaryGrantEmployeeBO();
        salaryGrantEmployeeBO.setTaskCode(salaryGrantTaskBO.getTaskCode());
        salaryGrantEmployeeBO.setTaskType(salaryGrantTaskBO.getTaskType());
        //查询任务单雇员信息
        Page<SalaryGrantEmployeeBO> page = new Page<>(1, Integer.MAX_VALUE);
        if (!ObjectUtils.isEmpty(salaryGrantTaskBO.getTaskType()) && salaryGrantTaskBO.getTaskType().intValue() == 0) {
            //发放方式:1-中智上海账户、2-中智代发（委托机构）、3-中智代发（客户账户）、4-客户自行
            salaryGrantTaskBO.setGrantMode("12");
        }
        Page<SalaryGrantEmployeeBO> salaryGrantEmployeeBOPage = queryEmployeeTask(page, salaryGrantEmployeeBO);
        List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList = salaryGrantEmployeeBOPage.getRecords();

        //查询计算批次结果的雇员信息数据
        List<EmpCalcResultBO> empCalcResultBOList = getEmpCalcResultItemsList(salaryGrantTaskBO.getBatchCode(), salaryGrantTaskBO.getGrantType());

        //根据薪资项选择列表筛选批次结果的雇员薪资项数据
        List<EmpCalcResultBO> filterCalcResultBOList = filterEmpCalcResultBOList(empCalcResultBOList, checkedItemsList);

        //比较批次雇员信息和任务单雇员信息
        List<EmpCalcResultBO> calcResultBOList = compareBatchAndTaskEmployeeInfo(filterCalcResultBOList, salaryGrantEmployeeBOList);

        return calcResultBOList;
    }

    /**
     * 比较批次雇员信息和任务单雇员信息
     *
     * @param filterCalcResultBOList
     * @param salaryGrantEmployeeBOList
     * @return
     */
    private List<EmpCalcResultBO> compareBatchAndTaskEmployeeInfo(List<EmpCalcResultBO> filterCalcResultBOList, List<SalaryGrantEmployeeBO> salaryGrantEmployeeBOList) {
        List<EmpCalcResultBO> retCalcResultBOList = new ArrayList<>();

        //实发工资薪资项code
        String salaryItemName = "实发工资";

        //根据筛选后的雇员信息数据和任务单雇员信息数据比较(公司编号，雇员编号)，批次结果与任务单查询雇员信息一对多，若任务单雇员信息中存在多条数据，则将批次结果中雇员信息
        //复制多条，与任务单雇员信息记录数一致，雇员信息中币种，发放金额与任务单雇员信息记录中保持一致
        filterCalcResultBOList.stream().forEach(empCalcResultBO -> {
            //根据批次中雇员编号、公司编号查询当前雇员的任务单雇员信息
            List<SalaryGrantEmployeeBO> grantEmployeeBOList = salaryGrantEmployeeBOList.stream().filter(employeeBO -> empCalcResultBO.getEmployeeId().equals(employeeBO.getEmployeeId()) && empCalcResultBO.getCompanyId().equals(employeeBO.getCompanyId())).collect(Collectors.toList());
            if (!CollectionUtils.isEmpty(grantEmployeeBOList)) {
                if (grantEmployeeBOList.size() == 1) {
                    retCalcResultBOList.add(empCalcResultBO);
                } else {
                    //任务单雇员信息多条记录时
                    grantEmployeeBOList.stream().forEach(employeeBO -> {
                        EmpCalcResultBO calcResultBO = new EmpCalcResultBO();
                        BeanUtils.copyProperties(empCalcResultBO, calcResultBO);

                        //设置发放币种 = 当前任务单雇员记录发放币种
                        calcResultBO.setCurrencyCode(employeeBO.getCurrencyCode());

                        //获取雇员薪资项列表，设置薪资项中发放金额 = 任务单雇员信息的发放金额
                        List<CalcResultItemBO> resultItemBOList = calcResultBO.getEmpCalcResultItemsList();
                        if (!CollectionUtils.isEmpty(resultItemBOList)) {
                            int resultItemBOListSize = resultItemBOList.size();
                            //更新薪资项列表中发放金额的值 = 任务单雇员信息的发放金额
                            for (int i = 0; i < resultItemBOListSize; i++) {
                                CalcResultItemBO calcResultItemBO = resultItemBOList.get(i);
                                if (salaryItemName.equals(calcResultItemBO.getItemName())) {
                                    //发放金额
                                    calcResultItemBO.setItemValue(ObjectUtils.isEmpty(employeeBO.getPaymentAmount()) ? null : employeeBO.getPaymentAmount().toString());
                                }
                            }
                        }

                        retCalcResultBOList.add(calcResultBO);
                    });
                }
            }
        });

        return retCalcResultBOList;
    }

    /**
     * 根据选定薪资项字段筛选薪资项结果
     *
     * @param empCalcResultBOList
     * @param checkedItemsList
     * @return
     */
    private List<EmpCalcResultBO> filterEmpCalcResultBOList(List<EmpCalcResultBO> empCalcResultBOList, List<CalcResultItemBO> checkedItemsList) {
        List<EmpCalcResultBO> empCalcResultBOFilterList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(empCalcResultBOList) && !CollectionUtils.isEmpty(checkedItemsList)) {
            empCalcResultBOList.stream().forEach(empCalcResultBO -> {
                EmpCalcResultBO calcResultBO = new EmpCalcResultBO();
                calcResultBO.setEmployeeId(empCalcResultBO.getEmployeeId());
                calcResultBO.setCompanyId(empCalcResultBO.getCompanyId());
                calcResultBO.setCurrencyCode(empCalcResultBO.getCurrencyCode());

                //当前雇员薪资项列表
                List<CalcResultItemBO> empCalcResultItemsList = empCalcResultBO.getEmpCalcResultItemsList();
                //当前雇员选定薪资项列表
                List<CalcResultItemBO> newResultItemBOList = new ArrayList<>();

                //赋值选定薪资项数据
                checkedItemsList.stream().forEach(calcResultItemBO -> {
                    String itemCode = calcResultItemBO.getItemCode();

                    //根据薪资项code在雇员薪资项列表中查询薪资项
                    Optional<CalcResultItemBO> resultItemBOOptional = empCalcResultItemsList.stream().filter(resultItemBO -> itemCode.equals(resultItemBO.getItemCode())).findFirst();
                    if (resultItemBOOptional.isPresent()) {
                        CalcResultItemBO resultItemBO = new CalcResultItemBO();
                        resultItemBO.setItemCode(resultItemBOOptional.get().getItemCode());
                        resultItemBO.setItemName(resultItemBOOptional.get().getItemName());
                        resultItemBO.setItemValue(resultItemBOOptional.get().getItemValue());

                        newResultItemBOList.add(resultItemBO);
                    }
                });
                //设置筛选后薪资项
                calcResultBO.setEmpCalcResultItemsList(newResultItemBOList);

                empCalcResultBOFilterList.add(calcResultBO);
            });
        }

        return empCalcResultBOFilterList;
    }

    @Override
    public Page<SalaryGrantEmployeeBO> queryEmpHisInfo(long task_his_id, Integer pageNum, Integer pageSize) {
        Page<SalaryGrantEmployeeBO> employeeBOPage = new Page<>();
        employeeBOPage.setCurrent(pageNum);
        employeeBOPage.setSize(pageSize);

        DBObject dbObject = salaryGrantEmpHisOpt.get(Criteria.where("task_his_id").is(task_his_id));
        if (!ObjectUtils.isEmpty(dbObject)) {
            List<SalaryGrantEmployeeBO> employeeBOList = (List<SalaryGrantEmployeeBO>) dbObject.get("employeeInfo");
            if (!CollectionUtils.isEmpty(employeeBOList)) {

                //雇员记录数大于等于起始分页记录数时，若雇员记录数大于等于雇员总记录数，则返回分页记录数；否则，返回起始分页记录数之后的雇员记录数
                int fromIndex = (pageNum - 1) * pageSize;
                int toIndex = pageNum * pageSize;

                if (employeeBOList.size() >= fromIndex) {
                    if (employeeBOList.size() >= toIndex) {
                        employeeBOPage.setTotal(employeeBOList.size());
                        employeeBOPage.setRecords(employeeBOList.subList(fromIndex, toIndex));
                    } else {
                        employeeBOPage.setTotal(employeeBOList.size());
                        employeeBOPage.setRecords(employeeBOList.subList(fromIndex, employeeBOList.size()));
                    }
                }
            }
        }

        return employeeBOPage;
    }
}
