package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.CompExcelImportEmum;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrNormalBatchService;
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
    public List<PrCustSubBatchPO> selectSubBatchList(String code, Integer status) {
        return normalBatchMapper.selectSubBatchList(code, status);
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
    public int uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, int importType, MultipartFile file) {

        //根据批次号获取雇员信息：雇员基础信息，雇员薪资信息，批次信息
        List<DBObject> batchList = normalBatchMongoOpt.list(Criteria.where("batch_code").is(batchCode).and("emp_group_code").is(empGroupCode));

        //如果当前批次相关的雇员组里面有雇员，并且该导入属于覆盖导入，则不能进行覆盖导入
        if(batchList.size() > 1 && importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()) {
            return -1;
        }

        List<List<BasicDBObject>> payItems = new ArrayList<>();

        String pr_group_code = "";

        for (DBObject batchItem: batchList) {
            pr_group_code = (String) batchItem.get("pr_group_code");
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
                    dbObject.put("emp_info",getEmpBaseInfos(row));

                    opt.setUpdate(Update.update("catalog", dbObject));
                    opt.setMulti(true);
                    opt.setUpsert(true);
                    options.add(opt);
                }
            } while (row != null);

            return normalBatchMongoOpt.doBathUpdate(options,true);
        }catch (Exception ex){
            logger.error(ex.getMessage());
        }
        return 0;
    }

    @Override
    public int updateBatchStatus(String batchCode, int status,String modifiedBy) {
        PrNormalBatchPO param = new PrNormalBatchPO();
        param.setCode(batchCode);
        PrNormalBatchPO batchPO = normalBatchMapper.selectOne(param);
        /*if(batchPO.getStatus() == BatchStatusEnum.NEW.getValue()){
            if(status == BatchStatusEnum.PENDING.getValue() || status == BatchStatusEnum.COMPUTING.getValue()){
                return normalBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
            }
        }else if(batchPO.getStatus() == BatchStatusEnum.PENDING.getValue()){
            if(status == BatchStatusEnum.APPROVAL.getValue() || status == BatchStatusEnum.REJECT.getValue()){
                return normalBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
            }
        }else if(batchPO.getStatus() == BatchStatusEnum.CLOSED.getValue()){
            if(status == BatchStatusEnum.ISSUED.getValue()){
                return normalBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
            }
        }else if(batchPO.getStatus() == BatchStatusEnum.ISSUED.getValue()){
            if(status == BatchStatusEnum.TAX_DECLARED.getValue()){
                return normalBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
            }
        }*/

        return normalBatchMapper.updateBatchStatus(batchCode,status,modifiedBy);
    }

    @Override
    public int auditBatch(String batchCode, String comments, int status, String modifiedBy) {
        return normalBatchMapper.auditBatch(batchCode,comments,status,modifiedBy);
    }

    @Override
    public int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy) {
        return normalBatchMapper.updateHasAdvance(batchCode,hasAdvance,modifiedBy);
    }

    @Override
    public int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy) {
        return normalBatchMapper.updateHasMoneny(batchCode,hasMoney,modifiedBy);
    }

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

    }

}
