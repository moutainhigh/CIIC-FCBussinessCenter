package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/31.
 */
@Service
public class ComputeServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    public void processCompute(String batchCode){

        //根据batch code获取可用于计算的雇员列表
        List<DBObject> batchList = normalBatchMongoOpt.list(
                Criteria.where("batch_code").is(batchCode)
                        .and("catalog.emp_info.is_active").is(true)
        );

        batchList.stream().forEach(dbObject -> {
            DBObject calalog = (DBObject)dbObject.get("catalog");
            DBObject empInfo = (DBObject)calalog.get("emp_info");
            List<DBObject> items = (List<DBObject>)calalog.get("pay_items");
            items.forEach(item -> {
                int itemType = (int)item.get("item_type"); // 薪资项类型
                int dataType = (int)item.get("data_type"); // 数据格式: 1-文本,2-数字,3-日期,4-布尔

                //处理计算项
                if(itemType == ItemTypeEnum.CALC.getValue()){
                    int calPriority = (int)item.get("cal_priority");
                    String itemName = item.get("item_name") == null ? "" : (String) item.get("item_name");
                    logger.info(String.format("薪资项目－%s | 计算优先级－d%",itemName,calPriority));

                    int processType = item.get("decimal_process_type") == null ? 1 : (int)item.get("item_condition");//小数处理方式 1 - 四舍五入 2 - 简单去位

                    String itemCondition = item.get("item_condition") == null ? "" : (String)item.get("item_condition");//计算条件
                    String formulaContent = item.get("formula_content") == null ? "" : (String)item.get("formula_content"); //计算公式
                    //first name []replace item name item value
                    //second common function [[common function]]
                    //third eval

                    Object itemVal = item.get("item_value"); //计算结果值
                }
            });
        });

    }

}
