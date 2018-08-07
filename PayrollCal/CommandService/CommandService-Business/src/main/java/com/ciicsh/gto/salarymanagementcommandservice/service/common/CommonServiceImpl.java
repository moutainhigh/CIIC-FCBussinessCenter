package com.ciicsh.gto.salarymanagementcommandservice.service.common;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeExtendFieldProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmployeeExtendFieldRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmployeeExtendFieldResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.CloseAccountMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.TestBatchMongoOpt;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.EmpExtendFieldTemplateProxy;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmployeeExtendFieldDTO;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/4/4.
 */
@Service
public class CommonServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(CommonServiceImpl.class);

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private TestBatchMongoOpt testBatchMongoOpt;

    @Autowired
    private PrItemService itemService;

    @Autowired
    private PrPayrollAccountItemRelationMapper accountItemRelationMapper;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private CloseAccountMongoOpt closeAccountMongoOpt;

    @Autowired
    private EmployeeExtendFieldProxy employeeExtendFieldProxy;

    @Autowired
    private EmpExtendFieldTemplateProxy empExtendFieldTemplateProxy;

    @Autowired
    private PrPayrollGroupMapper prPayrollGroupMapper;

    @Autowired
    private CodeGenerator codeGenerator;

    /**
     * 批量更新或者插入数据
     *
     * @param batchCode
     * @return
     */
    public int batchInsertOrUpdateNormalBatch(String batchCode, int batchType, List<DBObject> employees) {

        PrCustBatchPO initialPO = new PrCustBatchPO();
        initialPO.setCode(batchCode);
        List<PrCustBatchPO> custBatchPOList = normalBatchMapper.selectBatchListByUseLike(initialPO);

        PrCustBatchPO batchPO = null;
        List<BasicDBObject> payItems = null;
        if (!CollectionUtils.isEmpty(custBatchPOList)) {
            batchPO = custBatchPOList.get(0);
            payItems = getPayItemList(batchPO);
        }

        int rowAffected = 0;

        if (employees != null && employees.size() > 0) {
            for (DBObject item : employees) {
                DBObject base_info = item.get("base_info") == null ? item : (DBObject) item.get("base_info");

                //雇员扩展薪资项List
                List<EmployeeExtendFieldResponseDTO> employeeExtendFieldsValueList = new ArrayList<>();

                if (!ObjectUtils.isEmpty(batchPO) && !batchPO.isTemplate()) {
                    //根据薪资组code获取薪资组记录
                    PrPayrollGroupPO paramPayrollGroupPO = new PrPayrollGroupPO();
                    paramPayrollGroupPO.setGroupCode(batchPO.getPrGroupCode());
                    PrPayrollGroupPO prPayrollGroupPO = prPayrollGroupMapper.selectOne(paramPayrollGroupPO);
                    if (!ObjectUtils.isEmpty(prPayrollGroupPO)) {
                        //获取雇员扩展薪资项的值
                        EmployeeExtendFieldRequestDTO extendFieldRequestDTO = new EmployeeExtendFieldRequestDTO();
                        extendFieldRequestDTO.setCompanyId(base_info.get(PayItemName.EMPLOYEE_COMPANY_ID).toString());
                        extendFieldRequestDTO.setEmployeeId(base_info.get(PayItemName.EMPLOYEE_CODE_CN).toString());
                        extendFieldRequestDTO.setEmpExtendFieldTemplateId(prPayrollGroupPO.getEmpExtendFieldTemplateId());
                        employeeExtendFieldsValueList = getEmployeeExtendFieldsValue(extendFieldRequestDTO);
                    }
                }

                //复制雇员扩展薪资项List
                final List<EmployeeExtendFieldResponseDTO> employeeExtendFieldsValueList2 = new ArrayList<>(employeeExtendFieldsValueList);

                // 复制薪资项模板中薪资项List，包括雇员扩展字段模板中薪资项List
                List<BasicDBObject> clonePyItems = cloneListDBObject(payItems);
                clonePyItems.forEach(p -> {
                    if (p.get("item_name").equals(PayItemName.EMPLOYEE_NAME_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_NAME_CN));
                    } else if (p.get("item_name").equals(PayItemName.EMPLOYEE_CODE_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_CODE_CN));
                    } else if (p.get("item_name").equals(PayItemName.NATIONALITY)) {
                        p.put("item_value", base_info.get(PayItemName.NATIONALITY));
                    } else if (p.get("item_name").equals(PayItemName.EMPLOYEE_ONBOARD_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_ONBOARD_CN));
                    } else if (p.get("item_name").equals(PayItemName.LEAVE_DATE)) {
                        p.put("item_value", base_info.get(PayItemName.LEAVE_DATE));
                    } else if (p.get("item_name").equals(PayItemName.EMPLOYEE_COMPANY_ID)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_COMPANY_ID));
                    }

                    if (!CollectionUtils.isEmpty(employeeExtendFieldsValueList2)) {
                        String itemName = p.get("item_name").toString();

                        //在雇员扩展薪资项List中查找当前薪资项名称
                        EmployeeExtendFieldResponseDTO fieldResponseDTO = employeeExtendFieldsValueList2.stream().filter(responseDTO -> responseDTO.getFieldName().equals(itemName)).findAny().orElse(null);

                        if (!ObjectUtils.isEmpty(fieldResponseDTO)) {
                            p.put("item_value", fieldResponseDTO.getValue());
                        }
                    }
                });

                rowAffected += updateBatchMongodb(batchPO, base_info, clonePyItems, batchType);
            }
        } else {
            rowAffected += updateBatchMongodb(batchPO, null, payItems, batchType);
        }
        try {
            //create index
            if (batchType == BatchTypeEnum.Test.getValue()) {
                testBatchMongoOpt.createIndex();
                logger.info("add test batch row affected : " + String.valueOf(rowAffected));
            } else {
                normalBatchMongoOpt.createIndex();
                logger.info("add normal batch row affected : " + String.valueOf(rowAffected));
            }
            return rowAffected;

        } catch (Exception e) {
            logger.error(e.getMessage());
        }
        return 0;
    }

    /**
     * 获取雇员扩展薪资项的值
     *
     * @param extendFieldRequestDTO
     * @return
     */
    private List<EmployeeExtendFieldResponseDTO> getEmployeeExtendFieldsValue(EmployeeExtendFieldRequestDTO extendFieldRequestDTO) {
        List<EmployeeExtendFieldResponseDTO> employeeExtendFieldValueList = new ArrayList<>();

        try {
            com.ciicsh.common.entity.JsonResult<List<EmployeeExtendFieldResponseDTO>> listJsonResult = employeeExtendFieldProxy.getEmployeeExtendFieldDetails(extendFieldRequestDTO);
            if (!ObjectUtils.isEmpty(listJsonResult)) {
                List<EmployeeExtendFieldResponseDTO> extendFieldResponseDTOList = listJsonResult.getData();
                if (!CollectionUtils.isEmpty(extendFieldResponseDTOList)) {
                    employeeExtendFieldValueList.addAll(extendFieldResponseDTOList);
                }
            }

        } catch (Exception e) {
            return new ArrayList<>();
        }

        //测试代码 - 待删除 开始
//        EmployeeExtendFieldResponseDTO fieldResponseDTO = new EmployeeExtendFieldResponseDTO();
//        fieldResponseDTO.setFieldName("字段名称2");
//        fieldResponseDTO.setFieldType(1);
//        fieldResponseDTO.setValue("字段名称2Value");
//        employeeExtendFieldValueList.add(fieldResponseDTO);
//
//        fieldResponseDTO = new EmployeeExtendFieldResponseDTO();
//        fieldResponseDTO.setFieldName("工资发放地");
//        fieldResponseDTO.setFieldType(1);
//        fieldResponseDTO.setValue("工资发放地Value");
//        employeeExtendFieldValueList.add(fieldResponseDTO);
        //测试代码 - 待删除 结束

        logger.debug("获取雇员扩展薪资项的值 返回: " + JSONObject.toJSONString(employeeExtendFieldValueList));
        return employeeExtendFieldValueList;
    }

    private DBObject BatchPOToDBObject(PrCustBatchPO batchPO) {
        DBObject dbObject = new BasicDBObject();
        dbObject.put("pr_group_name", batchPO.getPrGroupName());
        dbObject.put("emp_group_name", batchPO.getEmpGroupName());
        dbObject.put("account_set_code", batchPO.getAccountSetCode());
        dbObject.put("account_set_name", batchPO.getAccountSetName());
        dbObject.put("begin_date", batchPO.getBeginDate());
        dbObject.put("end_date", batchPO.getEndDate());
        dbObject.put("begin_day", batchPO.getStartDay());
        dbObject.put("end_day", batchPO.getEndDay());
        dbObject.put("management_ID", batchPO.getManagementId());
        dbObject.put("management_Name", batchPO.getManagementName());
        dbObject.put("payroll_type", batchPO.getPayrollType());
        dbObject.put("period", batchPO.getPeriod());
        dbObject.put("status", batchPO.getStatus());
        dbObject.put("actual_period", batchPO.getActualPeriod());
        return dbObject;
    }

    //生成薪资项列表
    private List<BasicDBObject> getPayItemList(PrCustBatchPO batchPO) {

        List<PrPayrollItemPO> prList;
        List<BasicDBObject> list = new ArrayList<>();

        //获取薪资组或薪资组模版下的薪资项列表
        if (batchPO.isTemplate()) {
            prList = itemService.getListByGroupTemplateCode(batchPO.getPrGroupCode());
            //payrollGroupExtPO.setPayrollGroupCode("");
            //payrollGroupExtPO.setPayrollGroupTemplateCode(batchPO.getPrGroupCode());
        } else {
            //payrollGroupExtPO.setPayrollGroupCode(batchPO.getPrGroupCode());
            //payrollGroupExtPO.setPayrollGroupTemplateCode("");
            prList = itemService.getListByGroupCode(batchPO.getPrGroupCode());
        }

        //获取薪资帐套下的薪资项列表
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("accountSetCode", batchPO.getAccountSetCode());
        paramMap.put("payrollGroupCode", batchPO.getPrGroupCode());
        paramMap.put("managementId", batchPO.getManagementId());
        List<PayrollAccountItemRelationExtPO> itemRelationExtsList = accountItemRelationMapper.getAccountItemRelationExts(paramMap);

        prList = prList.stream()
                .filter(p -> itemRelationExtsList.stream().anyMatch(f -> f.getPayrollItemCode().equals(p.getItemCode()) && f.getIsActive()))
                .sorted(Comparator.comparing(PrPayrollItemPO::getCalPriority))
                .collect(Collectors.toList());

        //显示优先级，计算顺序由小到大排列
        //prList.sort(Comparator.comparing(PrPayrollItemPO::getDisplayPriority).thenComparing(PrPayrollItemPO::getCalPriority));

        prList.forEach(item -> {
            BasicDBObject dbObject = new BasicDBObject();
            Optional<PayrollAccountItemRelationExtPO> find = itemRelationExtsList.stream().filter(r -> r.getPayrollItemCode().equals(item.getItemCode()) && !StringUtils.isEmpty(r.getPayrollItemAlias())).findFirst();
            if (find.isPresent()) {
                dbObject.put("item_name", find.get().getPayrollItemAlias()); // 设置别名
            } else {
                dbObject.put("item_name", item.getItemName());

            }
            int dataType = item.getDataType();
            dbObject.put("data_type", item.getDataType());
            dbObject.put("item_type", item.getItemType());
            dbObject.put("item_code", item.getItemCode());
            dbObject.put("default_value", item.getDefaultValue());
            setPayItemValue(dataType, item.getDefaultValue(), dbObject);
            dbObject.put("item_condition", item.getItemCondition());
            dbObject.put("formula_content", item.getFormulaContent());
            dbObject.put("canLock", item.getCanLock());
            //dbObject.put("origin_formula",item.getOriginFormula());
            dbObject.put("cal_precision", item.getCalPrecision());
            dbObject.put("cal_priority", item.getCalPriority());
            dbObject.put("default_value_style", item.getDefaultValueStyle());
            dbObject.put("decimal_process_type", item.getDecimalProcessType());
            dbObject.put("display_priority", item.getDisplayPriority());
            list.add(dbObject);
        });

        //薪资项扩展字段处理
        List<BasicDBObject> employeeExtendFieldsList = getTemplateEmployeeExtendFields(batchPO.getPrGroupCode());
        list.addAll(employeeExtendFieldsList);

        return list;
    }

    /**
     * 获取模板雇员扩展字段列表
     *
     * @param prGroupCode
     * @return
     */
    private List<BasicDBObject> getTemplateEmployeeExtendFields(String prGroupCode) {
        List<BasicDBObject> extendFieldsList = new ArrayList<>();

        if (StringUtils.isEmpty(prGroupCode)) {
            return extendFieldsList;
        }

        PrPayrollGroupPO paramPayrollGroupPO = new PrPayrollGroupPO();
        paramPayrollGroupPO.setGroupCode(prGroupCode);
        PrPayrollGroupPO prPayrollGroupPO = prPayrollGroupMapper.selectOne(paramPayrollGroupPO);
        if (ObjectUtils.isEmpty(prPayrollGroupPO)) {
            return extendFieldsList;
        }

        try {
            if (!ObjectUtils.isEmpty(prPayrollGroupPO.getEmpExtendFieldTemplateId())) {
                JsonResult<List<EmployeeExtendFieldDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplateFields(prPayrollGroupPO.getEmpExtendFieldTemplateId());
                if (!ObjectUtils.isEmpty(listJsonResult)) {
                    List<EmployeeExtendFieldDTO> extendFieldDTOList = listJsonResult.getData();
                    if (!CollectionUtils.isEmpty(extendFieldDTOList)) {
                        extendFieldDTOList.forEach(extendFieldDTO -> {
                            BasicDBObject dbObject = new BasicDBObject();

                            dbObject.put("item_name", extendFieldDTO.getFieldName());
                            dbObject.put("item_type", 1); //薪资项类型：1-固定输入项;2-可变输入项;3-计算项;4-系统项

                            Integer fieldType = extendFieldDTO.getFieldType(); //接口中DTO 字段类型（1.数值 2.文本 3日期 4选项）
                            switch (fieldType) {
                                case 1:
                                    dbObject.put("data_type", 2); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                                    break;
                                case 2:
                                case 4:
                                case 3:
                                    dbObject.put("data_type", 1); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                                    break;
                                default:
                                    break;
                            }

                            dbObject.put("item_code", codeGenerator.genPrItemCode(prPayrollGroupPO.getManagementId())); //薪资项编码
                            dbObject.put("default_value", ""); //默认数字
                            dbObject.put("item_condition", ""); //薪资项取值范围的条件描述
                            dbObject.put("formula_content", ""); //公式内容
                            dbObject.put("canLock", false); //是否可以上锁
                            dbObject.put("cal_precision", 0); //计算精度
                            dbObject.put("cal_priority", 0); //计算顺序
                            dbObject.put("default_value_style", 2); //默认值处理方式： 1 - 使用历史数据 2 - 使用基本值
                            dbObject.put("decimal_process_type", 1); //小数处理方式： 1 - 四舍五入 2 - 简单去位
                            dbObject.put("display_priority", 0); //显示顺序

                            extendFieldsList.add(dbObject);
                        });
                    }
                }
            }

        } catch (Exception e) {
            return new ArrayList<>();
        }

        logger.debug("获取模板雇员扩展字段列表 返回: " + JSONObject.toJSONString(extendFieldsList));
        return extendFieldsList;
    }

    private int updateBatchMongodb(PrCustBatchPO batchPO, DBObject emp, List<BasicDBObject> payItems, int batchType) {

        BasicDBObject catalog = new BasicDBObject();
        Query query;

        if (emp != null) {

            Criteria criteria = Criteria.where("batch_code").is(batchPO.getCode()).
                    andOperator(
                            Criteria.where("pr_group_code").is(batchPO.getPrGroupCode()),   //薪资组或薪资组模版编码
                            Criteria.where("emp_group_code").is(batchPO.getEmpGroupCode()), //雇员组编码
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(emp.get(PayItemName.EMPLOYEE_CODE_CN)), //雇员编码
                            Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is(emp.get(PayItemName.EMPLOYEE_COMPANY_ID)) //公司ID emp.get(PayItemName.EMPLOYEE_COMPANY_ID)
                    );
            query = Query.query(criteria);
            catalog.put("emp_info", emp);

        } else {
            query = Query.query(Criteria.where("batch_code").is(batchPO.getCode())  //批次号
                    .andOperator(
                            Criteria.where("pr_group_code").is(batchPO.getPrGroupCode()),   //薪资组或薪资组模版编码
                            Criteria.where("emp_group_code").is(batchPO.getEmpGroupCode()), //雇员组编码
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(""), //雇员编码
                            Criteria.where(PayItemName.EMPLOYEE_COMPANY_ID).is("") //公司ID
                    )
            );
        }
        catalog.put("batch_info", BatchPOToDBObject(batchPO));
        catalog.put("pay_items", payItems);
        Update update = new Update();
        update.set("catalog", catalog);

        WriteResult result;
        if (batchType == BatchTypeEnum.Test.getValue()) { // 测试批次
            result = testBatchMongoOpt.getMongoTemplate().upsert(query, update, TestBatchMongoOpt.PR_TEST_BATCH);
        } else {
            result = normalBatchMongoOpt.getMongoTemplate().upsert(query, update, NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }
        //count = normalBatchMongoOpt.upsert(query,update);

        return result.getN();
        //WriteResult result = normalBatchMongoOpt.getMongoTemplate().upsert(query,update,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        //return result.getN();
    }

    private List<BasicDBObject> cloneListDBObject(List<BasicDBObject> source) {
        List<BasicDBObject> cloneList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(source)) {
            for (BasicDBObject basicDBObject : source) {
                cloneList.add((BasicDBObject) basicDBObject.copy());
            }
        }

        return cloneList;
    }

    /**
     * transfer List to HashMap
     *
     * @param payItems
     * @return
     */
    public HashMap<String, List<BasicDBObject>> listToHashMap(List<DBObject> payItems) {

        HashMap<String, List<BasicDBObject>> map = payItems.stream()
                .collect(
                        HashMap<String, List<BasicDBObject>>::new,
                        (m, c) -> {
                            DBObject catalog = (DBObject) c.get("catalog");
                            List<BasicDBObject> list = (List<BasicDBObject>) catalog.get("pay_items");
                            /*list = list.stream().filter(p->{
                                int itemType = p.get("item_type") == null ? 0 : (int)p.get("item_type");
                                return itemType != ItemTypeEnum.CALC.getValue();
                            }).collect(Collectors.toList());*/

                            String empCode = c.get(PayItemName.EMPLOYEE_CODE_CN) == null ?
                                    "" : (String) c.get(PayItemName.EMPLOYEE_CODE_CN);

                            if (!StringUtils.isEmpty(empCode))
                                m.put(empCode, list);
                        },
                        //(m,c) -> m.put((String)c.get("item_name"), c.get("item_value")),
                        (m, u) -> {
                        }
                );

        return map;
    }

    public List<SimpleEmpPayItemDTO> translate(List<DBObject> list) {

        if (list.size() == 0) return null;

        Map<String, Double> statics = new HashMap<>();

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = list.stream().map(dbObject -> {
            SimpleEmpPayItemDTO itemPO = new SimpleEmpPayItemDTO();
            itemPO.setEmpCode(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_CODE_CN)));
            itemPO.setCompanyId(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_COMPANY_ID)));
            DBObject calalog = (DBObject) dbObject.get("catalog");

            List<SimplePayItemDTO> simplePayItemDTOList = new ArrayList<>();

            if (dbObject.get("adjust_val") != null) {
                SimplePayItemDTO adjust = new SimplePayItemDTO();
                adjust.setName("调整值");
                adjust.setVal(dbObject.get("adjust_val"));
                simplePayItemDTOList.add(adjust);
            }
            //DBObject empInfo = (DBObject)calalog.get("emp_info");
            //String name =  empInfo.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : (String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN);

            List<DBObject> items = (List<DBObject>) calalog.get("pay_items");
            items.stream().forEach(dbItem -> {
                SimplePayItemDTO simplePayItemDTO = new SimplePayItemDTO();
                simplePayItemDTO.setDataType(dbItem.get("data_type") == null ? -1 : (int) dbItem.get("data_type"));
                simplePayItemDTO.setItemType(dbItem.get("item_type") == null ? -1 : (int) dbItem.get("item_type"));
                simplePayItemDTO.setVal(dbItem.get("item_value") == null ? dbItem.get("default_value") : dbItem.get("item_value"));
                simplePayItemDTO.setName(dbItem.get("item_name") == null ? "" : (String) dbItem.get("item_name"));
                simplePayItemDTO.setDisplay(dbItem.get("display_priority") == null ? -1 : (int) dbItem.get("display_priority"));
                simplePayItemDTO.setCanLock((boolean) dbItem.get("canLock"));
                if (dbItem.get("isLocked") == null) {
                    simplePayItemDTO.setLocked(false);
                } else {
                    simplePayItemDTO.setLocked((boolean) dbItem.get("isLocked"));
                }

                if (dbItem.get("item_name").equals(PayItemName.EMPLOYEE_NAME_CN)) { //雇员姓名
                    itemPO.setEmpName(String.valueOf(dbItem.get("item_value")));
                }

                // 根据每个薪资项名称求和，存入hashmap
                if (simplePayItemDTO.getDataType() == DataTypeEnum.NUM.getValue()) {
                    if (statics.containsKey(simplePayItemDTO.getName())) {
                        double current = statics.get(simplePayItemDTO.getName());
                        statics.put(simplePayItemDTO.getName(), current + Double.valueOf(simplePayItemDTO.getVal().toString()));
                    } else {
                        statics.put(simplePayItemDTO.getName(), Double.valueOf(simplePayItemDTO.getVal().toString()));
                    }
                }//end

                simplePayItemDTOList.add(simplePayItemDTO);
            });
            //设置显示顺序
            List<SimplePayItemDTO> payItemDTOS = simplePayItemDTOList.stream().sorted(Comparator.comparing(SimplePayItemDTO::getDisplay)).collect(Collectors.toList());
            itemPO.setPayItemDTOS(payItemDTOS);
            return itemPO;
        }).collect(Collectors.toList());

        for (SimpleEmpPayItemDTO empPayItemDTO : simplePayItemDTOS) { // 为每个薪资项设置SUM结果
            empPayItemDTO.getPayItemDTOS().parallelStream().forEach(p -> {
                if (statics.containsKey(p.getName())) {
                    p.setAmount(statics.get(p.getName()));
                }
            });
        }

        return simplePayItemDTOS;
    }

    public Map<String, Object> JSONString2Map(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<Map<String, String>>() {
            });
            return map;
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setPayItemValue(int dataType, String defaultValue, BasicDBObject dbObject) {
        if (StringUtils.isEmpty(defaultValue)) {
            if (dataType == DataTypeEnum.NUM.getValue()) {
                dbObject.put("item_value", 0);
            } else {
                dbObject.put("item_value", "");
            }
        } else {
            if (dataType == DataTypeEnum.NUM.getValue()) {
                dbObject.put("item_value", Double.parseDouble(defaultValue));
            } else {
                dbObject.put("item_value", defaultValue);
            }
        }
    }

    public long getBatchVersion(String batchCode) {
        long version = 0;
        Criteria criteria = Criteria.where("batch_id").is(batchCode);
        Query query = Query.query(criteria);
        query.fields().include("version");

        List<DBObject> list = closeAccountMongoOpt.getMongoTemplate().find(query, DBObject.class, CloseAccountMongoOpt.PR_PAYROLL_CAL_RESULT);
        if (list == null || list.size() == 0) {
            return version;
        }

        Optional<DBObject> versionObj = list.stream().findFirst();
        if (versionObj.isPresent()) {
            version = versionObj.get().get("version") == null ? 0 : (long) versionObj.get().get("version");
        }
        return version;
    }
}
