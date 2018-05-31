package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.FcBaseEmpRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.FcBaseEmpResponseDTO;
import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.AdjustBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.BackTraceBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.enums.BatchTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRItemExcelMapper implements RowMapper<List<BasicDBObject>> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private AdjustBatchMongoOpt adjustBatchMongoOpt;

    @Autowired
    private BackTraceBatchMongoOpt backTraceBatchMongoOpt;

    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;


    private String batchCode;

    private int batchType;

    private Map<String,Object> payItemMap; //key payItem name; value: excel column name

    private Map<String,Object> identityMap;

    public Map<String, Object> getIdentityMap() {
        return identityMap;
    }

    public void setIdentityMap(Map<String, Object> identityMap) {
        this.identityMap = identityMap;
    }

    public Map<String, Object> getPayItemMap() {
        return payItemMap;
    }

    public void setPayItemMap(Map<String, Object> payItemMap) {
        this.payItemMap = payItemMap;
    }


    public String getBatchCode() {
        return batchCode;
    }

    public void setBatchCode(String batchCode) {
        this.batchCode = batchCode;
    }

    public int getBatchType() {
        return batchType;
    }

    public void setBatchType(int batchType) {
        this.batchType = batchType;
    }

    private String[] excelCols = null;

    @Override
    public List<BasicDBObject> mapRow(RowSet rs) throws Exception {

        if(excelCols == null) {
            excelCols = rs.getMetaData().getColumnNames();
        }

        BasicDBObject rowIndex = new BasicDBObject();
        rowIndex.put("row_index",rs.getCurrentRowIndex());
        setEmp(rs,rowIndex);
        //rowIndex.put("emp_code",setEmp(rs,rowIndex));

        List<BasicDBObject> excelContents = Arrays.asList(excelCols).stream().map(col ->{
            BasicDBObject dbObject = new BasicDBObject();
            for (Map.Entry<String, Object> entry : payItemMap.entrySet()) {
                if (Objects.equals(col, entry.getValue())) {
                    dbObject.put("payItem_name", entry.getKey());
                }
            }
            dbObject.put("col_name",col);
            dbObject.put("col_value",rs.getProperties().get(col));
            return dbObject;

        }).collect(Collectors.toList());

        excelContents.add(0,rowIndex); // add row index object

        return excelContents;
    }

    private void setEmp(RowSet rs, BasicDBObject rowIndex){

            Object empCodeObj = identityMap.get(PayItemName.EMPLOYEE_CODE_CN);
            Object empNameObj = identityMap.get(PayItemName.EMPLOYEE_NAME_CN);
            Object idNumObj = identityMap.get(PayItemName.IDENTITY_NUM);
            Object empIdObj = identityMap.get(PayItemName.EMPLOYEE_ID);

            String empCode = empCodeObj == null ? "" : rs.getProperties().get(empCodeObj) == null ? "":(String)rs.getProperties().get(empCodeObj);
            String empName = empNameObj == null ? "":rs.getProperties().get(empNameObj) == null ? "":(String)rs.getProperties().get(empNameObj);
            String idNum = idNumObj == null ? "" : rs.getProperties().get(idNumObj) == null ? "":(String)rs.getProperties().get(idNumObj);
            String empId = empIdObj == null ? "" : rs.getProperties().get(empIdObj) == null ? "":(String)rs.getProperties().get(empIdObj);
            if(StringUtils.isNotEmpty(empId)){
                rowIndex.put("emp_id",empId);
            }
            BasicDBObject emp = getEmpInfo(batchCode,batchType,empCode, empName,idNum,empId);

            if(emp != null){
                rowIndex.put("emp_code", String.valueOf(emp.get(PayItemName.EMPLOYEE_CODE_CN)));
            }else {
                int IDType = 1; //1:身份证 2:护照 3:军(警)官证 4:士兵证 5:台胞证 6:回乡证 7:其他
                FcBaseEmpRequestDTO empRequestDTO = new FcBaseEmpRequestDTO();
                empRequestDTO.setIdCardType(IDType);
                empRequestDTO.setIdNum(idNum);
                empRequestDTO.setEmployeeCode(empCode);
                empRequestDTO.setEmployeeName(empName);
                JsonResult<FcBaseEmpResponseDTO> result = employeeServiceProxy.getFcBaseEmpInfos(empRequestDTO); //TODO empID, empcode, companyId
                if(result.isSuccess() && result.getData() != null) {
                    rowIndex.put("emp_code",result.getData().getEmployeeId());
                    rowIndex.put("companyId", result.getData().getCompanyId());
                }
            }
    }

    private BasicDBObject getEmpInfo(String batchCode, int batchType, String empCode, String empName, String idNum, String empId){
        List<DBObject> batchList = null;
        String searchPrefix = "catalog.emp_info.";

        Criteria criteria = Criteria.where("batch_code").is(batchCode);
        if(StringUtils.isNotEmpty(empCode)){
            criteria = criteria.and(PayItemName.EMPLOYEE_CODE_CN).is(empCode);
        }
        if(StringUtils.isNotEmpty(empName)){
            criteria = criteria.and(searchPrefix+PayItemName.EMPLOYEE_NAME_CN).is(empName);
        }
        if(StringUtils.isNotEmpty(idNum)){
            criteria = criteria.and(searchPrefix+PayItemName.IDENTITY_NUM).is(idNum);
        }
        if(StringUtils.isNotEmpty(empId)){
            criteria = criteria.and(searchPrefix+PayItemName.EMPLOYEE_ID).is(empId);
        }
        Query query = new Query(criteria);
        query.fields()
                .include("batch_code")
                .include(PayItemName.EMPLOYEE_CODE_CN)
                .include(searchPrefix+PayItemName.EMPLOYEE_NAME_CN)
                .include(searchPrefix+PayItemName.IDENTITY_NUM)
                .include(searchPrefix+PayItemName.EMPLOYEE_ID)
                ;
                /*.include("catalog.pay_items.item_type")
                .include("catalog.pay_items.data_type")
                .include("catalog.pay_items.cal_priority")
                .include("catalog.pay_items.item_name")
                .include("catalog.pay_items.item_code")
                .include("catalog.pay_items.item_value")
                .include("catalog.pay_items.decimal_process_type")
                .include("catalog.pay_items.item_condition")
                .include("catalog.pay_items.formula_content")
                .include("catalog.pay_items.display_priority");*/

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

}