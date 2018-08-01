package com.ciicsh.gto.salarymanagementcommandservice.service.common;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.CloseAccountMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.TestBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

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

        PrCustBatchPO batchPO = custBatchPOList.get(0);

        List<BasicDBObject> payItems = getPayItemList(batchPO);

        int rowAffected = 0;

        if (employees != null && employees.size() > 0) {
            for (DBObject item : employees) {
                DBObject base_info = item.get("base_info") == null ? item : (DBObject) item.get("base_info");
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
                });
                rowAffected += updateBatchMongodb(batchPO, base_info, clonePyItems, batchType);
            }
        } else {
            rowAffected += updateBatchMongodb(batchPO, null, payItems, batchType);
        }
        try {
            //create index
            if (batchType == 4) {
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

        List<PrPayrollItemPO> prList = null;
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
            Optional<PayrollAccountItemRelationExtPO> find =
                    itemRelationExtsList.stream().filter(r -> r.getPayrollItemCode().equals(item.getItemCode()) && StringUtils.isNotEmpty(r.getPayrollItemAlias())).findFirst();
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

        return list;
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
        if (batchType == 4) { // 测试批次
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
        for (BasicDBObject basicDBObject : source) {
            cloneList.add((BasicDBObject) basicDBObject.copy());
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

                            if (StringUtils.isNotEmpty(empCode))
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

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = list.stream().map(dbObject -> {
            SimpleEmpPayItemDTO itemPO = new SimpleEmpPayItemDTO();
            itemPO.setEmpCode(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_CODE_CN)));
            itemPO.setCompanyId(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_COMPANY_ID)));

            DBObject calalog = (DBObject) dbObject.get("catalog");
            //DBObject empInfo = (DBObject)calalog.get("emp_info");
            //String name =  empInfo.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : (String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN);

            List<DBObject> items = (List<DBObject>) calalog.get("pay_items");
            List<SimplePayItemDTO> simplePayItemDTOList = new ArrayList<>();
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
                simplePayItemDTOList.add(simplePayItemDTO);
            });
            //设置显示顺序
            List<SimplePayItemDTO> payItemDTOS = simplePayItemDTOList.stream().sorted(Comparator.comparing(SimplePayItemDTO::getDisplay)).collect(Collectors.toList());
            itemPO.setPayItemDTOS(payItemDTOS);
            return itemPO;
        }).collect(Collectors.toList());

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
