package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.bo.ExcelItemBO;
import com.ciicsh.gto.salarymanagement.entity.bo.ExcelUploadStatistics;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBatchExcelMapService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.BizArith;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.excel.PRExcelColumnsReader;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.excel.PRItemExcelReader;
import com.ciicsh.gto.salarymanagementcommandservice.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.WriteResult;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/7.
 */
@Service
public class PrNormalBatchServiceImpl implements PrNormalBatchService {


    private final static Logger logger = LoggerFactory.getLogger(PrNormalBatchServiceImpl.class);

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private PRItemExcelReader excelReader;

    @Autowired
    private PRExcelColumnsReader excelColumnsReader;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private PrBatchExcelMapService batchExcelMapService;


    @Override
    public int insert(PrNormalBatchPO normalBatchPO) {
        return normalBatchMapper.insertNormalBatch(normalBatchPO);
    }

    @Override
    public PageInfo<PrCustBatchPO> getList(PrCustBatchPO param, Integer pageNum, Integer pageSize) {
        List<PrCustBatchPO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            resultList = normalBatchMapper.selectBatchListByUseLike(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrCustBatchPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public int update(PrNormalBatchPO normalBatchPO) {
        return normalBatchMapper.updateNormalBatch(normalBatchPO);
    }

    @Override
    public List<PrCustSubBatchPO> selectSubBatchList(String code) {
        return normalBatchMapper.selectSubBatchList(code);
    }

    @Override
    public Integer deleteBatchByCodes(List<String> codes) {
        return normalBatchMapper.deleteBatchByCodes(codes);
    }

    @Override
    public PrNormalBatchPO getBatchByCode(String code) {
        return normalBatchMapper.getNormalBatchByCode(code);
    }

    @Override
    public PrCustBatchPO getCustBatchInfo(String batchCode) {
        return normalBatchMapper.getCustBatchInfo(batchCode);
    }

    @Override
    public ExcelUploadStatistics uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, int batchType, int importType, MultipartFile file) {

        List<List<BasicDBObject>> excelRows = new ArrayList<>();// 读取 excel 数据，存储格式行索引，行内容的map

        PrBatchExcelMapPO batchExcelMapPO = batchExcelMapService.getBatchExcelMap(batchCode);
        Map<String,Object> identityMap = commonService.JSONString2Map(batchExcelMapPO.getIdentityResult()); //获取一个或多个字段唯一性集合
        List<String> excelIdentityCols = identityMap.keySet().stream().map(p-> p).collect(Collectors.toList());

        try {
            Map<String,Object> payItemMap = commonService.JSONString2Map(batchExcelMapPO.getMappingResult()); //获取多个薪资项与EXCEL列映射集合（KEY薪资项名称，VALUE EXCEL列名称）
            InputStream stream = file.getInputStream();
            PoiItemReader<List<BasicDBObject>> reader = excelReader.readExcel(payItemMap,identityMap,batchCode,batchType,stream); // read excel content: BasicDBObject each column like "{ colName:'', colValue:'' }"
            reader.open(new ExecutionContext());
            List<BasicDBObject> row = null;
            do {
                row = reader.read();
                if(row != null){
                    excelRows.add(row); // add excel row
                }
            } while (row != null);

        }catch (Exception ex){
            logger.error(ex.getMessage());
        }

        //按唯一性字段组合值分组
        Map<String,List<ExcelItemBO>> maps = excelRows.stream().map(rows -> {
            ExcelItemBO itemBO = new ExcelItemBO();
            StringBuilder groupBy = new StringBuilder();
            StringBuilder groupCols = new StringBuilder();

            excelIdentityCols.forEach(identityName -> {
                Optional<BasicDBObject> find = rows.stream().filter(item->item.get("col_name") != null && item.get("col_name").equals(identityName)).findFirst();
                if(find.isPresent()){
                    groupBy.append(find.get().get("col_value"));
                    groupBy.append(",");

                    groupCols.append(find.get().get("col_name"));
                    groupCols.append(",");
                }
            });

            String groupValues = groupBy.toString();
            String groupColumns = groupCols.toString();

            if(groupBy.length() > 0) {
                groupValues = StringUtils.removeEnd(groupValues,",");
                groupColumns = StringUtils.removeEnd(groupColumns,",");

            }
            itemBO.setGroupValues(groupValues);
            itemBO.setGroupColumns(groupColumns);
            int rowIndex = (int)rows.get(0).get("row_index");
            itemBO.setRowIndex(rowIndex);
            return itemBO;
        }).collect(Collectors.groupingBy(ExcelItemBO::getGroupValues));

        List<ExcelItemBO> successItems = new ArrayList<>();
        List<String> failedItems = new ArrayList<>();

        for (String key:maps.keySet()) {
            List<ExcelItemBO> find = maps.get(key);
            if(find.size() == 1){
                successItems.add(find.get(0));
            }else {
                String rowIndex = "";
                for (ExcelItemBO failedItem : find) {
                    rowIndex += String.valueOf(failedItem.getRowIndex()) + ",";
                }
                if(StringUtils.isNotEmpty(rowIndex)){
                    rowIndex = StringUtils.removeEnd(rowIndex,",");
                }
                failedItems.add(String.format("当前Excel文件行号为 %s  唯一性字段为 %s  含有相同的值",
                        rowIndex,find.get(0).getGroupColumns()));
            }
        }
        int rowAffect = 0;
        if(successItems.size() > 0){

            //获取可以导入的数据
            excelRows = excelRows.stream().filter(row -> {
                return successItems.stream().anyMatch(successItem -> {
                    return (int)row.get(0).get("row_index") == successItem.getRowIndex();
                });
            }).collect(Collectors.toList());

            rowAffect = processSuccessRows(batchCode,batchType,importType,excelRows);
        }

        ExcelUploadStatistics statistics = new ExcelUploadStatistics();
        int total = successItems.size() + failedItems.size();
        int success = rowAffect;
        int failed = total - success;
        if(failed > 0){
            statistics.setFailedCount(failed);
        }
        if(failedItems.size() > 0){
            statistics.setFailedContent(failedItems.toArray(new String[failedItems.size()]));
        }
        statistics.setSuccessCount(success);
        statistics.setTotal(total);

        return statistics;
    }

    private BasicDBObject getEmpInfoFromMongo(String batchCode, String empCode, String companyId, int batchType){
        List<DBObject> batchList = null;

        Criteria  criteria = Criteria.where("batch_code").is(batchCode).and(PayItemName.EMPLOYEE_CODE_CN).is(empCode).and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId);

        Query query = new Query(criteria);
        query.fields()
                /*.include("batch_code")
                .include("emp_group_code")
                .include("pr_group_code")
                .include(PayItemName.EMPLOYEE_CODE_CN)
                .include(PayItemName.EMPLOYEE_COMPANY_ID)*/
                .include("catalog.pay_items")
                /*.include("catalog.pay_items.item_type")
                .include("catalog.pay_items.data_type")
                .include("catalog.pay_items.cal_priority")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
                .include("catalog.pay_items.decimal_process_type")
                .include("catalog.pay_items.item_condition")
                .include("catalog.pay_items.formula_content")
                .include("catalog.pay_items.display_priority")*/
                ;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            //根据批次号获取雇员信息：雇员基础信息，雇员薪资信息，批次信息
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }
        if(batchList != null && batchList.size() == 1){
            return (BasicDBObject)batchList.get(0);
        }
        return null;

    }

    private BasicDBObject getPayItemsFromMongo(String batchCode, int batchType){
        List<DBObject> batchList = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);

        Query query = new Query(criteria);
        query.fields()
                .include("batch_code")
                .include("emp_group_code")
                .include("pr_group_code")
                .include("catalog.pay_items");

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }
        if(batchList != null && batchList.size() > 0 ){
            return (BasicDBObject)batchList.get(0);
        }
        return null;

    }


    private int processSuccessRows(String batchCode, int batchType, int importType, List<List<BasicDBObject>> excelRows){
        int rowAffected = 0;

        List<List<BasicDBObject>> filterRows = excelRows.stream().filter(row -> row.stream()
                .anyMatch(col -> (col.get("payItem_name") != null && !col.get("payItem_name").equals("")))).collect(Collectors.toList());
        BasicDBObject emp = null;

        for (List<BasicDBObject> row : filterRows) {
            String empCode = row.get(0).get("emp_code") == null ? "" : String.valueOf(row.get(0).get("emp_code"));
            String companyId = row.get(0).get("companyId") == null ? "" : String.valueOf(row.get(0).get("companyId"));

            emp = getEmpInfoFromMongo(batchCode,empCode,companyId,batchType);

            if(emp != null){ // 批次中查找到该雇员

                /*
                 2、修改导入
                 场景：计算批次雇员组中已经有雇员。导入时根据外部文件中提供的雇员基本信息匹配雇员：
                 1）. 如果基本信息匹配不上，则导入失败 (不用提示)。
                 2）. 如果匹配上，则使用文件中的值带入相应的薪资项。

                 3、追加导入
                 场景：计算批次中雇员组已经有雇员。导入的为当前批次里没有的雇员信息，业务逻辑和修改导入和覆盖导入类似。
                 1）.如果匹配上做修改导入
                 2）. 如果匹配不上做覆盖导入
                 */

                if (importType == CompExcelImportEmum.MODIFY_EXPORT.getValue() ||
                        importType == CompExcelImportEmum.APPEND_EXPORT.getValue()) {
                    BasicDBObject catalog = (BasicDBObject) emp.get("catalog");
                    List<BasicDBObject> payItems = (List<BasicDBObject>) catalog.get("pay_items");

                    payItems.forEach(item -> {
                        setItemValue(item,row);
                    });
                    rowAffected += updateItems(batchCode, empCode, companyId, true, null, null, null, batchType, payItems); // update items
                }

            }else { //批次中未找到该雇员

                 /*
                 1、覆盖导入
                 场景：计算批次雇员组中没有雇员，全部从外部文件导入。导入后系统将文件内雇员加入当前批次中雇员组，
                        并且验证雇员基本信息，如果和系统信息不一致：
                 1）. 如果不是姓名不一致则使用系统里的值
                 2）. 如果是姓名不一致则使用外部文件里的值，并且在计算结果中显示警告信息。

                 3、追加导入
                 场景：计算批次中雇员组已经有雇员。导入的为当前批次里没有的雇员信息，业务逻辑和修改导入和覆盖导入类似。
                 1）.如果匹配上做修改导入
                 2）.如果匹配不上做覆盖导入
                 是否覆盖导入一定要有
                 */
                if (importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue() ||
                        importType == CompExcelImportEmum.APPEND_EXPORT.getValue()) {

                    BasicDBObject batchInfo = getPayItemsFromMongo(batchCode,batchType);
                            //.include(PayItemName.EMPLOYEE_CODE_CN)
                            //.include(PayItemName.EMPLOYEE_COMPANY_ID)

                    BasicDBObject catalog = (BasicDBObject) batchInfo.get("catalog");
                    String empGroupCode = String.valueOf(batchInfo.get("emp_group_code"));
                    String prGroupCode = String.valueOf(batchInfo.get("pr_group_code"));

                    List<BasicDBObject> payItems = (List<BasicDBObject>) catalog.get("pay_items");
                    List<BasicDBObject> clonePayItems = cloneListDBObject(payItems);
                    for (BasicDBObject item : clonePayItems) {
                        setItemValue(item,row);
                    }
                    rowAffected += updateItems(batchCode, empCode, companyId, false, empGroupCode, prGroupCode, catalog, batchType, clonePayItems);
                }
            }

        }

        /*
        4、差异导入
         场景：和修改导入类似，根据外部文件中提供的雇员基本信息匹配雇员：
         1）. 如果基本信息匹配不上，则导入失败。
         2）. 如果匹配上，则使用文件中相应的薪资项差异值和运算符号改变现在系统里薪资项的值。
         3）. 运算符号有且仅有加减乘除四种。
         */
        if(importType == CompExcelImportEmum.DIFF_EXPORT.getValue()){
            // TODO
        }

        return rowAffected;
    }

    private void setItemValue(BasicDBObject item,List<BasicDBObject> row){
        Object colVal = getExcelColumnValue(row, item.get("item_name"));
        int dataType = item.get("data_type") == null ? 0 : (int)item.get("data_type");
        int precision = item.get("cal_precision") == null ? 2 : (int)item.get("cal_precision");
        if(dataType == DataTypeEnum.DATE.getValue() && colVal != null && !colVal.equals("")){
            item.put("item_value", TimeStamp2Date(colVal.toString()));
        }else if(dataType == DataTypeEnum.NUM.getValue()){
            if(colVal == null || colVal.equals("")){
                item.put("item_value", BizArith.round(0,precision));
            }else {
                item.put("item_value", BizArith.round(colVal,precision));
            }
        }
        else {
            item.put("item_value", colVal);
        }
    }

    private Object getExcelColumnValue(List<BasicDBObject> row, Object payItemName){
       Optional<BasicDBObject> find = row.stream().filter(col -> col.get("payItem_name") != null && col.get("payItem_name").equals(payItemName)).findFirst();
       if(find.isPresent()){
           return find.get().get("col_value");
       }
       return null;
    }

     private String TimeStamp2Date(String timestampString) {
        return DateUtil.getValidDate(timestampString);
    }

    private int updateItems(String batchCode,String empCode, String companyId, boolean empExistGroup, String empGroupCode, String prGroupCode,
                            BasicDBObject catalog, int batchType, List<BasicDBObject> items){

        WriteResult result = null;
        Criteria criteria = Criteria.where("batch_code").is(batchCode)
                .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                .and(PayItemName.EMPLOYEE_COMPANY_ID).is(companyId);

        Query query = null;
        Update update = null;
        if(empExistGroup) { // 该雇员在雇员组中有
            query = Query.query(criteria);
            update = Update.update("catalog.pay_items", items);
        }else {
            // 雇员在雇员组不存在
            criteria.and("pr_group_code").is(prGroupCode)
                    .and("emp_group_code").is(empGroupCode);

            query = Query.query(criteria);
            catalog.put("emp_info",null);
            catalog.put("pay_items",items);
            update = Update.update("catalog", catalog);
        }

        //items.remove(0); // 如果包含索引列，需要删除该列

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            result = normalBatchMongoOpt.getMongoTemplate().upsert(query,update,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            result = adjustBatchMongoOpt.getMongoTemplate().upsert(query,update,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            result = backTraceBatchMongoOpt.getMongoTemplate().upsert(query,update,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }
        if(result != null){
            return result.getN();
        }
        return 0;
    }

    private List<BasicDBObject> cloneListDBObject(List<BasicDBObject> source){
        List<BasicDBObject> cloneList = new ArrayList<>();
        for (BasicDBObject basicDBObject: source) {
            cloneList.add((BasicDBObject)basicDBObject.copy());
        }
        return cloneList;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy, String advancePeriod, String result) {

        if(!StringUtils.isNotEmpty(modifiedBy)){
            modifiedBy = "system";
        }

        if(status == BatchStatusEnum.COMPUTING.getValue()){
            return normalBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,advancePeriod,result);
        }
        if(UserContext.getUser() != null) {
            ApprovalHistoryPO historyPO = new ApprovalHistoryPO();
            int approvalResult = 0;
            if (status == BatchStatusEnum.NEW.getValue()) {
                approvalResult = ApprovalStatusEnum.DRAFT.getValue();
            } else if (status == BatchStatusEnum.PENDING.getValue()) {
                approvalResult = ApprovalStatusEnum.AUDITING.getValue();
            } else if (status == BatchStatusEnum.APPROVAL.getValue() || status == BatchStatusEnum.CLOSED.getValue()) {
                approvalResult = ApprovalStatusEnum.APPROVE.getValue();
            } else if (status == BatchStatusEnum.REJECT.getValue()) {
                approvalResult = ApprovalStatusEnum.DENIED.getValue();
            }
            historyPO.setApprovalResult(approvalResult);
            historyPO.setBizCode(batchCode);
            historyPO.setBizType(BizTypeEnum.NORMAL_BATCH.getValue());
            historyPO.setCreatedBy(UserContext.getUser().getUserId());
            historyPO.setCreatedName(UserContext.getUser().getDisplayName());
            historyPO.setComments(comments);
            approvalHistoryService.addApprovalHistory(historyPO);
        }

        return normalBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,advancePeriod,result);
    }

    @Override
    public int updateHasAdvance(List<String> batchCodes, int hasAdvance, String modifiedBy) {
        return normalBatchMapper.updateHasAdvance(batchCodes,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoney(List<String> batchCodes, boolean hasMoney, String modifiedBy) {
        return normalBatchMapper.updateHasMoney(batchCodes,hasMoney,modifiedBy);
    }

    @Override
    public List<PrNormalBatchPO> getAllBatchesByManagementId(String managementId) {
        return null;
    }

    @Override
    public PageInfo<PrNormalBatchPO> getAllBatchesByManagementId(String managementId, String batchCode, Integer pageNum, Integer pageSize) {

        List<PrNormalBatchPO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        resultList = normalBatchMapper.selectAllBatchCodesByManagementId(managementId, batchCode);
        PageInfo<PrNormalBatchPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public List<String> getBatchIdListByManagementId(String managementId) {
        return normalBatchMapper.getBatchIdListByManagementId(managementId);
    }

    @Override
    public List<PrNormalBatchPO> getHistoryBatchInfoList(List<String> mgrIds) {
        return normalBatchMapper.getHistoryBatchInfoList(mgrIds);
    }



    @Override
    public List<PrPayrollItemPO> getBatchPayrollSchema(String batchCode) {
        return normalBatchMapper.selectBatchPayrollSchema(batchCode);
    }

    @Override
    public String[] readExcelColumns(String batchCode, InputStream stream) {
        PoiItemReader<List<String>> reader = null;
        String[] cols = null;
        try {
            reader = excelColumnsReader.getReader(stream);
            reader.open(new ExecutionContext());
            List<String> coloumns = reader.read();
            cols = (String[]) coloumns.toArray();
        }catch (Exception ex){
            logger.info(ex.getMessage());
        }finally {
            if(reader != null){
                reader.close();
            }
        }
        return cols;
    }

}
