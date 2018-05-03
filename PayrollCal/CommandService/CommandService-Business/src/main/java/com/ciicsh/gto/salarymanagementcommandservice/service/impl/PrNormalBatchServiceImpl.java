package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.excel.PRItemExcelReader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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
import java.util.*;

/**
 * Created by bill on 17/12/7.
 */
@Service
public class PrNormalBatchServiceImpl implements PrNormalBatchService {


    private final static Logger logger = LoggerFactory.getLogger(PrNormalBatchServiceImpl.class);

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    @Autowired
    private PrEmployeeMapper employeeMapper;

    @Autowired
    private PRItemExcelReader excelReader;

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
        PrNormalBatchPO param = new PrNormalBatchPO();
        param.setCode(code);
        PrNormalBatchPO result = normalBatchMapper.selectOne(param);
        return result;
    }

    @Override
    public PrCustBatchPO getCustBatchInfo(String batchCode) {
        return normalBatchMapper.getCustBatchInfo(batchCode);
    }

    @Override
    public int uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, String itemNames, int batchType, int importType, MultipartFile file) {

        List<String> identityList = Arrays.asList(itemNames.split(","));

        List<DBObject> batchList = null;

        Criteria criteria = Criteria.where("batch_code").is(batchCode);// ItemTypeEnum.CALC.getValue()
        Query query = new Query(criteria);
        query.fields().include("batch_code");
        query.fields().
                include("pr_group_code")
                .include(PayItemName.EMPLOYEE_CODE_CN)
                .include("catalog.pay_items.item_type")
                .include("catalog.pay_items.data_type")
                .include("catalog.pay_items.cal_priority")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
                .include("catalog.pay_items.decimal_process_type")
                .include("catalog.pay_items.item_condition")
                .include("catalog.pay_items.formula_content")
        ;

        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            //根据批次号获取雇员信息：雇员基础信息，雇员薪资信息，批次信息
            batchList = normalBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,NormalBatchMongoOpt.PR_NORMAL_BATCH);
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,AdjustBatchMongoOpt.PR_ADJUST_BATCH);
        }else {
            batchList = backTraceBatchMongoOpt.getMongoTemplate().find(query,DBObject.class,BackTraceBatchMongoOpt.PR_BACK_TRACE_BATCH);
        }

        String prGroupCode = null;
        int rowAffected = 0;

        Optional<DBObject> firstObj = batchList.stream().findFirst();
        if(firstObj.isPresent()){
            prGroupCode = (String) firstObj.get().get("pr_group_code");
        }
        Map<String,List<BasicDBObject>> payItems = commonService.listToHashMap(batchList); //多个雇员薪资项

        try {
            InputStream stream = file.getInputStream();
            PoiItemReader<List<BasicDBObject>> reader = excelReader.getPrGroupReader(stream, importType, payItems, identityList);
            reader.open(new ExecutionContext());
            List<BasicDBObject> row = null;
            do {
                row = reader.read();
                if (row != null) {
                    if(row.size() == 1){ // 读取一行错误，或者忽略改行
                        continue; // todo
                    }

                    Optional<BasicDBObject> find = row.stream().filter(p-> p.get("item_name").equals(PayItemName.EMPLOYEE_CODE_CN)).findFirst();
                    String empCode = (String) find.get().get("item_value");
                    //opt.setQuery(Query.query(Criteria.where("batch_code").is(batchCode)));

                    Criteria cri = Criteria.where("batch_code").is(batchCode)
                            .and(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                            .and("pr_group_code").is(prGroupCode);
                    Query updateQuy = new Query(cri);
                    Update update = Update.update("catalog.pay_items", row);
                    if(payItems.get(empCode) == null){ //该雇员在雇员组中不存在，但在雇员中心存在
                        update = update.set("catalog.emp_info",getEmployeeInfo(empCode));
                    }
                    if(batchType == BatchTypeEnum.NORMAL.getValue()) {
                        rowAffected += normalBatchMongoOpt.upsert(updateQuy,update);
                    }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
                        rowAffected += adjustBatchMongoOpt.upsert(updateQuy,update);
                    }else {
                        rowAffected += backTraceBatchMongoOpt.upsert(updateQuy,update);
                    }
                }
            } while (row != null);

        }catch (Exception ex){
            logger.error(ex.getMessage());
        }

        logger.info("上传成功纪录条数：" + String.valueOf(rowAffected));
        return rowAffected;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result) {
        if(status == BatchStatusEnum.COMPUTING.getValue()){
            return normalBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
        }
        ApprovalHistoryPO historyPO = new ApprovalHistoryPO();
        int approvalResult = 0;
        if(status == BatchStatusEnum.NEW.getValue()){
            approvalResult = ApprovalStatusEnum.DRAFT.getValue();
        }else if(status == BatchStatusEnum.PENDING.getValue()){
            approvalResult = ApprovalStatusEnum.AUDITING.getValue();
        }else if(status == BatchStatusEnum.APPROVAL.getValue() || status == BatchStatusEnum.CLOSED.getValue()){
            approvalResult = ApprovalStatusEnum.APPROVE.getValue();
        }else if(status == BatchStatusEnum.REJECT.getValue()){
            approvalResult = ApprovalStatusEnum.DENIED.getValue();
        }
        historyPO.setApprovalResult(approvalResult);
        historyPO.setBizCode(batchCode);
        historyPO.setBizType(BizTypeEnum.NORMAL_BATCH.getValue());
        historyPO.setCreatedBy("bill"); //TODO
        historyPO.setCreatedName("bill");
        historyPO.setComments(comments);
        approvalHistoryService.addApprovalHistory(historyPO);
        return normalBatchMapper.auditBatch(batchCode,comments,status,modifiedBy,result);
    }

    @Override
    public int updateHasAdvance(List<String> batchCodes, boolean hasAdvance, String modifiedBy) {
        return normalBatchMapper.updateHasAdvance(batchCodes,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(List<String> batchCodes, boolean hasMoney, String modifiedBy) {
        return normalBatchMapper.updateHasMoneny(batchCodes,hasMoney,modifiedBy);
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

    private BasicDBObject getEmployeeInfo(String empCode){
        BasicDBObject basicDBObject = new BasicDBObject();

        PrEmployeePO employeePO = new PrEmployeePO();
        employeePO.setEmployeeId(empCode);
        employeePO = employeeMapper.selectOne(employeePO);

        basicDBObject.put(PayItemName.EMPLOYEE_CODE_CN,employeePO.getEmployeeId());
        basicDBObject.put(PayItemName.EMPLOYEE_NAME_CN,employeePO.getEmployeeName());
        basicDBObject.put(PayItemName.EMPLOYEE_BIRTHDAY_CN, employeePO.getBirthday());
        basicDBObject.put(PayItemName.EMPLOYEE_DEP_CN,employeePO.getDepartment());
        basicDBObject.put(PayItemName.EMPLOYEE_SEX_CN,employeePO.getGender()? "男":"女");
        basicDBObject.put(PayItemName.EMPLOYEE_ID_TYPE_CN,employeePO.getIdCardType());
        basicDBObject.put(PayItemName.EMPLOYEE_ONBOARD_CN,employeePO.getJoinDate());
        basicDBObject.put(PayItemName.EMPLOYEE_ID_NUM_CN,employeePO.getIdNum());
        basicDBObject.put(PayItemName.EMPLOYEE_POSITION_CN,employeePO.getPosition());

        basicDBObject.put(PayItemName.EMPLOYEE_FORMER_CN,employeePO.getFormerName());
        basicDBObject.put(PayItemName.EMPLOYEE_COUNTRY_CODE_CN,employeePO.getCountryCode());
        basicDBObject.put(PayItemName.EMPLOYEE_PROVINCE_CODE_CN,employeePO.getProvinceCode());
        basicDBObject.put(PayItemName.EMPLOYEE_CITY_CODE_CN,employeePO.getCityCode());

        return basicDBObject;
    }


    @Override
    public List<PrPayrollItemPO> getBatchPayrollSchema(String batchCode) {
        return normalBatchMapper.selectBatchPayrollSchema(batchCode);
    }

}
