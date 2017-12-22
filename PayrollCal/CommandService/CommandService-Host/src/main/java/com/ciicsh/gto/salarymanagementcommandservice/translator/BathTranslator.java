package com.ciicsh.gto.salarymanagementcommandservice.translator;

import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom.PrCustomBatchDTO;
import com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.PrEmpGroupDTO;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.PeriodTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.util.BatchUtils;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bill on 17/12/11.
 */
public class BathTranslator {

    public static PrCustBatchPO toPrBatchPO(PrCustomBatchDTO PrCustomBatchDTO){
        PrCustBatchPO custBatchPO = new PrCustBatchPO();

        custBatchPO.setAcccSetName(PrCustomBatchDTO.getAcccSetName());
        custBatchPO.setCode(PrCustomBatchDTO.getCode());
        custBatchPO.setEmpGroupName(PrCustomBatchDTO.getEmpGroupName());
        custBatchPO.setStatus(PrCustomBatchDTO.getStatus());
        custBatchPO.setPeriod(PrCustomBatchDTO.getPeriod());
        custBatchPO.setManagementName(PrCustomBatchDTO.getManagementName());

        return custBatchPO;
    }

    public static List<DBObject> transPrPayrollItemPOToDBObject(List<PrPayrollItemPO> prPayrollItemPOS){
        List<DBObject> list = new ArrayList<>();
        prPayrollItemPOS.forEach(item ->{
            DBObject dbObject = new BasicDBObject();
            dbObject.put("item_code",item.getItemCode());
            dbObject.put("item_name",item.getItemName());
            dbObject.put("item_value",item.getItemValue());
            dbObject.put("item_type",item.getItemType());
            dbObject.put("data_type",item.getDataType());
            dbObject.put("management_id",item.getManagementId());
            dbObject.put("item_condition",item.getItemCondition());
            dbObject.put("formula_content",item.getFormulaContent());
            dbObject.put("origin_formula",item.getOriginFormula());
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
}
