package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.*;
import com.ciicsh.gto.salarymanagement.entity.po.ApprovalHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.excel.PRItemExcelReader;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/7.
 */
@Service
public class PrNormalBatchServiceImpl implements PrNormalBatchService {


    private final static Logger logger = LoggerFactory.getLogger(PrNormalBatchServiceImpl.class);

    @Autowired
    PrNormalBatchMapper normalBatchMapper;

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
    public int uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, int batchType, int importType, MultipartFile file) {

        List<DBObject> batchList = null;
        if(batchType == BatchTypeEnum.NORMAL.getValue()) {
            //根据批次号获取雇员信息：雇员基础信息，雇员薪资信息，批次信息
            batchList = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("emp_group_code").is(empGroupCode).and("catalog.emp_info.is_active").in(true));
        }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
            batchList = adjustBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("emp_group_code").is(empGroupCode).and("catalog.emp_info.is_active").in(true));
        }else {
            batchList = backTraceBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("emp_group_code").is(empGroupCode).and("catalog.emp_info.is_active").in(true));

        }

        //如果当前批次相关的雇员组里面有雇员，并且该导入属于覆盖导入，则不能进行覆盖导入
        if(batchList.size() > 1 && importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()) {
            return -1;
        }

        List<List<BasicDBObject>> payItems = new ArrayList<>(); //多个雇员薪资项

        String pr_group_code = "";

        for (DBObject batchItem: batchList) {
            if(StringUtils.isEmpty(pr_group_code)){
                pr_group_code = (String) batchItem.get("pr_group_code");
            }
            DBObject catalog = (DBObject)batchItem.get("catalog");
            List<BasicDBObject> items = (List<BasicDBObject>)catalog.get("pay_items");
            payItems.add(items);
        }

        DBObject dbObject = null;
        try {
            InputStream stream = file.getInputStream();
            PoiItemReader<List<BasicDBObject>> reader = excelReader.getPrGroupReader(stream, importType, payItems);
            reader.open(new ExecutionContext());
            List<BathUpdateOptions> options = new ArrayList<>();
            List<BasicDBObject> row = null;
            do {

                row = reader.read();

                if (row != null) {
                    if(row.size() == 1){ // 读取一行错误，或者忽略改行
                        break;
                    }
                    if(importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()){
                        //TODO 增加雇员信息：雇员基本信息，雇员服务协议，雇员扩展字段
                    }
                    BathUpdateOptions opt = new BathUpdateOptions();
                    String empCode = (String) row.get(0).get("emp_code"); // row 的第一列为雇员编码
                    opt.setQuery(Query.query(Criteria.where("batch_code").is(batchCode)
                                    .andOperator(
                                            Criteria.where("pr_group_code").is(pr_group_code),
                                            Criteria.where("emp_group_code").is(empGroupCode),
                                            Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode)
                                    )
                    ));

                    dbObject = new BasicDBObject();
                    row.remove(0); // 删除row 的第一列为雇员编码
                    dbObject.put("pay_items", row);
                    dbObject.put("emp_info",getEmpBaseInfo(row));

                    opt.setUpdate(Update.update("catalog", dbObject));
                    opt.setMulti(true);
                    opt.setUpsert(true);
                    options.add(opt);
                }
            } while (row != null);

            if(batchType == BatchTypeEnum.NORMAL.getValue()) {
                //根据批次号获取雇员信息：雇员基础信息，雇员薪资信息，批次信息
                return normalBatchMongoOpt.doBathUpdate(options,true);
            }else if(batchType == BatchTypeEnum.ADJUST.getValue()) {
                return adjustBatchMongoOpt.doBathUpdate(options,true);
            }else {
                return backTraceBatchMongoOpt.doBathUpdate(options,true);
            }

        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return 0;
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
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return normalBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return normalBatchMapper.updateHasMoneny(batchCode,hasMoney,modifiedBy);
    }

    private BasicDBObject getEmpBaseInfo(List<BasicDBObject> prItems){
        BasicDBObject empBaseInfo = new BasicDBObject();
        empBaseInfo.put("is_active",true);
        prItems.stream().forEach(item ->{
            if(item.get("item_name").equals(PayItemName.EMPLOYEE_NAME_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_NAME_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_BIRTHDAY_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_BIRTHDAY_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_DEP_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_DEP_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_ID_NUM_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_ID_NUM_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_ID_TYPE_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_ID_TYPE_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_ONBOARD_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_ONBOARD_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_POSITION_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_POSITION_CN,item.get("item_value"));
            }else if(item.get("item_name").equals(PayItemName.EMPLOYEE_SEX_CN)){
                empBaseInfo.put(PayItemName.EMPLOYEE_SEX_CN,item.get("item_value"));
            }
        });
        return empBaseInfo;
    }

    /*
    private BasicDBObject getEmpBaseInfos(List<BasicDBObject> prItems){
        BasicDBObject empBaseInfo = new BasicDBObject();
        empBaseInfo.put("is_active",true);
        empBaseInfo.put(PayItemName.EMPLOYEE_NAME_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_NAME_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_BIRTHDAY_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_BIRTHDAY_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_DEP_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_DEP_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_ID_NUM_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_ID_NUM_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_ID_TYPE_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_ID_TYPE_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_ONBOARD_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_ONBOARD_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_POSITION_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_POSITION_CN));
        empBaseInfo.put(PayItemName.EMPLOYEE_SEX_CN,getPRItemValue(prItems,PayItemName.EMPLOYEE_SEX_CN));
        return empBaseInfo;
    }

    private Object getPRItemValue(List<BasicDBObject> prItems, String itemName){
        List<DBObject> findItems = prItems.stream().filter(item-> item.get("item_name").equals(itemName)).collect(Collectors.toList());
        if(findItems != null && findItems.size() > 0){
            return findItems.get(0).get("item_value");
        }
        return null;

    }*/

}
