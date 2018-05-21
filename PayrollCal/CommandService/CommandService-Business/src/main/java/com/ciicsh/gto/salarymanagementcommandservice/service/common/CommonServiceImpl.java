package com.ciicsh.gto.salarymanagementcommandservice.service.common;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.dto.SimpleEmpPayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.dto.SimplePayItemDTO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollAccountItemRelationExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
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
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private PrPayrollAccountItemRelationMapper accountItemRelationMapper;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    /**
     * 批量更新或者插入数据
     * @param batchCode
     * @return
     */
    public int batchInsertOrUpdateNormalBatch(String batchCode,List<DBObject> employees){

        PrCustBatchPO initialPO = new PrCustBatchPO();
        initialPO.setCode(batchCode);
        List<PrCustBatchPO> custBatchPOList = normalBatchMapper.selectBatchListByUseLike(initialPO);

        PrCustBatchPO batchPO = custBatchPOList.get(0);

        List<BasicDBObject> payItems = getPayItemList(batchPO);

        int rowAffected = 0;

        if(employees !=null && employees.size() > 0) {
            for (DBObject item: employees) {
                DBObject base_info =item.get("base_info") == null ? item :(DBObject)item.get("base_info");
                List<BasicDBObject> clonePyItems = cloneListDBObject(payItems);
                clonePyItems.forEach(p->{
                    if (p.get("item_name").equals(PayItemName.EMPLOYEE_NAME_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_NAME_CN));
                    }
                    else if (p.get("item_name").equals(PayItemName.EMPLOYEE_CODE_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_CODE_CN));
                    }
                    else if (p.get("item_name").equals(PayItemName.NATIONALITY)) {
                        p.put("item_value", base_info.get(PayItemName.NATIONALITY));
                    }
                    else if (p.get("item_name").equals(PayItemName.EMPLOYEE_ONBOARD_CN)) {
                        p.put("item_value", base_info.get(PayItemName.EMPLOYEE_ONBOARD_CN));
                    }
                    else if (p.get("item_name").equals(PayItemName.LEAVE_DATE)) {
                        p.put("item_value", base_info.get(PayItemName.LEAVE_DATE));
                    }
                });
                rowAffected += updateBatchMogodb(batchPO,item,clonePyItems);
            }
        }else {
            rowAffected += updateBatchMogodb(batchPO,null,payItems);
        }
        try {
            //create index
            normalBatchMongoOpt.createIndex();
            logger.info("add normal batch row affected : " + String.valueOf(rowAffected));
            return rowAffected;

        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return 0;
    }

    private DBObject BatchPOToDBObject(PrCustBatchPO batchPO){
        DBObject dbObject = new BasicDBObject();
        dbObject.put("pr_group_name",batchPO.getPrGroupName());
        dbObject.put("emp_group_name",batchPO.getEmpGroupName());
        dbObject.put("account_set_code",batchPO.getAccountSetCode());
        dbObject.put("account_set_name",batchPO.getAccountSetName());
        dbObject.put("begin_date",batchPO.getBeginDate());
        dbObject.put("end_date",batchPO.getEndDate());
        dbObject.put("begin_day",batchPO.getStartDay());
        dbObject.put("end_day",batchPO.getEndDay());
        dbObject.put("management_ID",batchPO.getManagementId());
        dbObject.put("management_Name",batchPO.getManagementName());
        dbObject.put("payroll_type",batchPO.getPayrollType());
        dbObject.put("period",batchPO.getPeriod());
        dbObject.put("status",batchPO.getStatus());
        return dbObject;
    }

    //生成薪资项列表
    private List<BasicDBObject> getPayItemList(PrCustBatchPO batchPO){

        List<PrPayrollItemPO> prList = null;
        List<BasicDBObject> list = new ArrayList<>();

        PayrollGroupExtPO payrollGroupExtPO = new PayrollGroupExtPO();
        payrollGroupExtPO.setManagementId(batchPO.getManagementId());
        if(batchPO.isTemplate()) {
            payrollGroupExtPO.setPayrollGroupCode("");
            payrollGroupExtPO.setPayrollGroupTemplateCode(batchPO.getPrGroupCode());
        }else {
            payrollGroupExtPO.setPayrollGroupCode(batchPO.getPrGroupCode());
            payrollGroupExtPO.setPayrollGroupTemplateCode("");

        }
        //获取薪资组或薪资组模版下的薪资项列表
        prList = prPayrollItemMapper.getPayrollItems(payrollGroupExtPO);

        //获取薪资帐套下的薪资项列表
        List<PayrollAccountItemRelationExtPO> itemRelationExtsList = accountItemRelationMapper.getAccountItemRelationExts(batchPO.getAccountSetCode());


        prList = prList.stream()
                .filter(p-> itemRelationExtsList.stream().anyMatch(f -> f.getPayrollItemCode().equals(p.getItemCode()) && f.getIsActive()))
                .sorted(Comparator.comparing(PrPayrollItemPO::getCalPriority))
                .collect(Collectors.toList());

        //显示优先级，计算顺序由小到大排列
        //prList.sort(Comparator.comparing(PrPayrollItemPO::getDisplayPriority).thenComparing(PrPayrollItemPO::getCalPriority));

        prList.forEach(item ->{
            BasicDBObject dbObject = new BasicDBObject();
            Optional<PayrollAccountItemRelationExtPO> find =
                    itemRelationExtsList.stream().filter(r -> r.getPayrollItemCode().equals(item.getItemCode()) && StringUtils.isNotEmpty(r.getPayrollItemAlias())).findFirst();
            if(find.isPresent()){
                //item.setItemName(find.get().getPayrollItemAlias());
                dbObject.put("item_name",find.get().getPayrollItemAlias()); // 设置别名
            }else {
                dbObject.put("item_name",item.getItemName());

            }
            dbObject.put("item_code",item.getItemCode());
            dbObject.put("item_value",item.getItemValue());
            dbObject.put("item_type",item.getItemType());
            dbObject.put("data_type",item.getDataType());
            //dbObject.put("management_id",item.getManagementId());
            dbObject.put("item_condition",item.getItemCondition());
            dbObject.put("formula_content",item.getFormulaContent());
            //dbObject.put("origin_formula",item.getOriginFormula());
            dbObject.put("cal_precision",item.getCalPrecision());
            dbObject.put("cal_priority",item.getCalPriority());
            dbObject.put("default_value_style",item.getDefaultValueStyle());
            dbObject.put("default_value",item.getDefaultValue());
            dbObject.put("decimal_process_type",item.getDecimalProcessType());
            dbObject.put("display_priority",item.getDisplayPriority());
            list.add(dbObject);
        });

        return list;
    }

    private int updateBatchMogodb( PrCustBatchPO batchPO, DBObject emp,List<BasicDBObject> payItems){

        DBObject basicDBObject = new BasicDBObject();

        Query query = null;

        if(emp != null) {
                    query = Query.query(Criteria.where("batch_code").is(batchPO.getCode())  //批次号
                    .andOperator(
                            Criteria.where("pr_group_code").is(batchPO.getPrGroupCode()),   //薪资组或薪资组模版编码
                            Criteria.where("emp_group_code").is(batchPO.getEmpGroupCode()), //雇员组编码
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(emp.get(PayItemName.EMPLOYEE_CODE_CN)) //雇员编码
                    )
            );
            DBObject empInfo = emp.get("base_info") == null ? emp :(DBObject)emp.get("base_info");
            basicDBObject.put("emp_info", empInfo);
        }else {
            query = Query.query(Criteria.where("batch_code").is(batchPO.getCode())  //批次号
                    .andOperator(
                            Criteria.where("pr_group_code").is(batchPO.getPrGroupCode()),   //薪资组或薪资组模版编码
                            Criteria.where("emp_group_code").is(batchPO.getEmpGroupCode()), //雇员组编码
                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is("") //雇员编码
                    )
            );
        }
        basicDBObject.put("batch_info", BatchPOToDBObject(batchPO));
        basicDBObject.put("pay_items", payItems);
        Update update = Update.update("catalog", basicDBObject);

        WriteResult result = normalBatchMongoOpt.getMongoTemplate().upsert(query,update,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        return result.getN();
    }

    private List<BasicDBObject> cloneListDBObject(List<BasicDBObject> source){
        List<BasicDBObject> cloneList = new ArrayList<>();
        for (BasicDBObject basicDBObject: source) {
            cloneList.add((BasicDBObject)basicDBObject.copy());
        }
        return cloneList;
    }

    /**
     * transfer List to HashMap
     * @param payItems
     * @return
     */
    public HashMap<String, List<BasicDBObject>> listToHashMap(List<DBObject> payItems){

        HashMap<String, List<BasicDBObject>> map = payItems.stream()
                .collect(
                        HashMap<String, List<BasicDBObject>>::new,
                        (m,c)->{
                            DBObject catalog = (DBObject)c.get("catalog");
                            List<BasicDBObject> list = (List<BasicDBObject>)catalog.get("pay_items");
                            /*list = list.stream().filter(p->{
                                int itemType = p.get("item_type") == null ? 0 : (int)p.get("item_type");
                                return itemType != ItemTypeEnum.CALC.getValue();
                            }).collect(Collectors.toList());*/

                            String empCode = c.get(PayItemName.EMPLOYEE_CODE_CN) == null ?
                                    "" : (String)c.get(PayItemName.EMPLOYEE_CODE_CN);

                            if(StringUtils.isNotEmpty(empCode))
                                m.put(empCode,list);
                        },
                        //(m,c) -> m.put((String)c.get("item_name"), c.get("item_value")),
                        (m,u)-> {}
                );

        return map;
    }

    public List<SimpleEmpPayItemDTO> translate(List<DBObject> list){

        if(list.size() == 0) return null;

        List<SimpleEmpPayItemDTO> simplePayItemDTOS = list.stream().map(dbObject -> {
            SimpleEmpPayItemDTO itemPO = new SimpleEmpPayItemDTO();
            itemPO.setEmpCode(String.valueOf(dbObject.get(PayItemName.EMPLOYEE_CODE_CN)));

            DBObject calalog = (DBObject)dbObject.get("catalog");
            //DBObject empInfo = (DBObject)calalog.get("emp_info");
            //String name =  empInfo.get(PayItemName.EMPLOYEE_NAME_CN) == null ? "" : (String)empInfo.get(PayItemName.EMPLOYEE_NAME_CN);

            List<DBObject> items = (List<DBObject>)calalog.get("pay_items");
            List<SimplePayItemDTO> simplePayItemDTOList = new ArrayList<>();
            items.stream().forEach(dbItem -> {
                SimplePayItemDTO simplePayItemDTO = new SimplePayItemDTO();
                simplePayItemDTO.setDataType(dbItem.get("data_type") == null ? -1 : (int) dbItem.get("data_type"));
                simplePayItemDTO.setItemType(dbItem.get("item_type") == null ? -1 : (int) dbItem.get("item_type"));
                simplePayItemDTO.setVal(dbItem.get("item_value") == null ? dbItem.get("default_value") : dbItem.get("item_value"));
                simplePayItemDTO.setName(dbItem.get("item_name") == null ? "" : (String) dbItem.get("item_name"));
                simplePayItemDTO.setDisplay(dbItem.get("display_priority") == null ? -1 : (int) dbItem.get("display_priority"));
                if(dbItem.get("item_name").equals(PayItemName.EMPLOYEE_NAME_CN)){ //雇员姓名
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

    public Map<String, Object> JSONString2Map(String json){
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> map = new HashMap<>();
            // convert JSON string to Map
            map = mapper.readValue(json, new TypeReference<Map<String, String>>() {});
            return map;
        }catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
}
