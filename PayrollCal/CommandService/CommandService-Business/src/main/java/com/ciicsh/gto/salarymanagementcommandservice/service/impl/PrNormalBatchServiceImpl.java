package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gt1.BathUpdateOptions;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
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

import java.io.IOException;
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
        List<DBObject> batchList = normalBatchMongoOpt.list(Criteria.where("batchCode").is(batchCode));

        //如果当前批次相关的雇员组里面有雇员，并且该导入属于覆盖导入，则不能进行覆盖导入
        if(batchList.size() > 1 && importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()) {
            return -1;
        }

        List<List<DBObject>> payItems = new ArrayList<>();

        batchList.forEach(batchItem ->{
            DBObject catalog = (DBObject)batchItem.get("catalog");
            List<DBObject> items = (List<DBObject>)catalog.get("pay_items");
            payItems.add(items);
        });

        DBObject dbObject = null;
        try {
            InputStream stream = file.getInputStream();
            PoiItemReader<List<DBObject>> reader = excelReader.getPrGroupReader(stream, importType, payItems);
            reader.open(new ExecutionContext());
            List<BathUpdateOptions> options = new ArrayList<>();
            List<DBObject> row = null;
            do {

                row = reader.read();
                if (row != null) {

                    BathUpdateOptions opt = new BathUpdateOptions();
                    String empCode = getEmpCode(row);
                    opt.setQuery(Query.query(Criteria.where("batch_code").is(batchCode).andOperator(
                                    Criteria.where(PayItemName.EMPLOYEE_CODE_CN).is(empCode) //雇员编码
                            )
                    ));

                    dbObject = new BasicDBObject();
                    dbObject.put("pay_items", row);
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

    private String getEmpCode(List<DBObject> list){

        DBObject dbObject = list.stream().filter(item -> {
            return item.get(PayItemName.EMPLOYEE_NAME_CN) != null;
        }).collect(Collectors.toList()).get(0);

        if(dbObject.equals(null)){
            return "";
        }
        return (String) dbObject.get(PayItemName.EMPLOYEE_NAME_CN);
    }

}
