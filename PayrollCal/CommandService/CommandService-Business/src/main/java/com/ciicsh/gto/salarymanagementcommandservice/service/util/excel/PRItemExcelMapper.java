package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.salarymanagement.entity.enums.CompExcelImportEmum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmployeePO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrEmployeeMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRItemExcelMapper implements RowMapper<List<BasicDBObject>> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    public Map<String,List<BasicDBObject>> getList() {
        return list;
    }

    public void setList(Map<String,List<BasicDBObject>> list) {
        this.list = list;
    }

    public int getImportType() {
        return importType;
    }

    public void setImportType(int importType) {
        this.importType = importType;
    }

    private Map<String,List<BasicDBObject>> list;

    private int importType;

    public List<String> getIdentities() {
        return identities;
    }

    public void setIdentities(List<String> identities) {
        this.identities = identities;
    }

    private List<String> identities;



    @Autowired
    private PrEmployeeMapper employeeMapper;


    @Override
    public List<BasicDBObject> mapRow(RowSet rs) throws Exception {

        //int columnCount = rs.getMetaData().getColumnCount();
        String[] colNames = rs.getMetaData().getColumnNames();
        int rowIndex = rs.getCurrentRowIndex();
        //String sheetName = rs.getMetaData().getSheetName();

        if(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN) == null ){
            List<BasicDBObject> dbObjects = new ArrayList<>();
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("row_index",rowIndex);
            dbObject.put("error_msg","雇员编号不存在");
            return dbObjects;
        }
        //获取EXCEL列中的雇员编号（该字段必须存在）
        String empCode = String.valueOf(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN));

        PrEmployeePO employeePO = checkEmpDBExist(empCode);
        if(employeePO == null){ //如果数据库不存在，不让导入
            List<BasicDBObject> errorList = new ArrayList<>();
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("row_index",rs.getCurrentRowIndex());
            dbObject.put("error_msg",String.format("雇员编号:%s 不存在",empCode));
            errorList.add(dbObject);
            return errorList;
        }

        if(importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()){
            return overrideImport(rowIndex, rs, empCode, colNames);
        }else if(importType == CompExcelImportEmum.MODIFY_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return processMissEmpCode(rowIndex,empCode);
            }else {
                return modifyImport(dbObjects,rs,colNames);
            }
        } else if(importType == CompExcelImportEmum.APPEND_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return overrideImport(rowIndex, rs, empCode, colNames);
            }else {
                return modifyImport(dbObjects,rs,colNames);
            }
        } else if(importType == CompExcelImportEmum.DIFF_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return processMissEmpCode(rowIndex, empCode);
            }else {
                //TODO
            }
        }
        return null;
    }

    private List<BasicDBObject> getDbObjects(String empCode){
        return list.get(empCode);
    }

    /**
     * 检查该雇员是否在数据库存在
     */
    private PrEmployeePO checkEmpDBExist(String empCode){
        PrEmployeePO employeePO = new PrEmployeePO();
        employeePO.setEmployeeId(empCode);
        employeePO = employeeMapper.selectOne(employeePO);
        return employeePO;
    }


    /**
     * 处理四种导入逻辑
     * 1、覆盖导入
     场景：计算批次雇员组中没有雇员，全部从外部文件导入。导入后系统将文件内雇员加入当前批次中雇员组，并且验证雇员基本信息，如果和系统信息不一致：
     1）. 如果不是姓名不一致则使用系统里的值
     2）. 如果是姓名不一致则使用外部文件里的值，并且在计算结果中显示警告信息。

     2、修改导入
     场景：计算批次雇员组中已经有雇员。导入时根据外部文件中提供的雇员基本信息匹配雇员：
     1）. 如果基本信息匹配不上，则导入失败 (不用提示)。
     2）. 如果匹配上，则使用文件中的值带入相应的薪资项。

     3、追加导入
     场景：计算批次中雇员组已经有雇员。导入的为当前批次里没有的雇员信息，业务逻辑和修改导入和覆盖导入类似。
     1）.如果匹配上做修改导入
     2）. 如果匹配不上做覆盖导入

     4、差异导入
     场景：和修改导入类似，根据外部文件中提供的雇员基本信息匹配雇员：
     1）. 如果基本信息匹配不上，则导入失败。
     2）. 如果匹配上，则使用文件中相应的薪资项差异值和运算符号改变现在系统里薪资项的值。
     3）. 运算符号有且仅有加减乘除四种。

     * @param
     */
    private List<BasicDBObject> modifyImport(List<BasicDBObject> dbObjects,RowSet rs, String[] columNames){

        List<String> listNames = Arrays.asList(columNames);
        setImportVal(dbObjects,listNames,rs);
        return dbObjects;
    }

    /**
     * 1、覆盖导入
     场景：计算批次雇员组中没有雇员，全部从外部文件导入。导入后系统将文件内雇员加入当前批次中雇员组，并且验证雇员基本信息，如果和系统信息不一致：
     1）. 如果不是姓名不一致则使用系统里的值
     2）. 如果是姓名不一致则使用外部文件里的值，并且在计算结果中显示警告信息。
     * @param
     * @param
     * @param rs
     * @return
     */
    private List<BasicDBObject> overrideImport(int rowIndex,RowSet rs, String empCode, String[] columNames){

        List<BasicDBObject> existList = list.get(empCode);
        if(existList == null) {

            List<BasicDBObject> overrideList = null;

            for (Map.Entry<String, List<BasicDBObject>> entry : list.entrySet()) {
                overrideList = entry.getValue();
                break;
            }

            List<BasicDBObject> cloneList = cloneListDBObject(overrideList);

            List<String> listNames = Arrays.asList(columNames);

            setImportVal(cloneList,listNames,rs);
            return cloneList;
        }else {
            return processMissEmpCode(rowIndex,empCode,"该雇员在雇员组中已经存在");
        }
    }

    private void setImportVal(List<BasicDBObject> list, List<String> listNames,RowSet rs){

        list.forEach(item -> {
            int itemType = item.get("item_type") == null ? 0 : (int)item.get("item_type");
            if (itemType != ItemTypeEnum.CALC.getValue()) {
                Object itemName = item.get("item_name");
                Optional<String> findCol = listNames.stream().filter(f -> f.trim().equals(itemName)).findFirst();
                if(findCol.isPresent()){
                    item.put("item_value", rs.getProperties().get(findCol.get()));
                }
            }
        });
    }


    /**
     * 处理当前批次薪资组中雇员不存在该empCode
     * @param rowIndex
     * @param empCode
     * @return
     */
    private List<BasicDBObject> processMissEmpCode(int rowIndex, String empCode){
        List<BasicDBObject> dbObjects = new ArrayList<>();
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("row_index",rowIndex);
        dbObject.put("error_msg",String.format("雇员编号:%s 不存在",empCode));
        dbObjects.add(dbObject);
        return dbObjects;
    }

    private List<BasicDBObject> processMissEmpCode(int rowIndex, String empCode, String errorMsg){
        List<BasicDBObject> dbObjects = new ArrayList<>();
        BasicDBObject dbObject = new BasicDBObject();
        dbObject.put("row_index",rowIndex);
        dbObject.put("error_msg",String.format("雇员编号:%s %s",empCode,errorMsg));
        dbObjects.add(dbObject);
        return dbObjects;
    }

    private List<BasicDBObject> cloneListDBObject(List<BasicDBObject> source){
        List<BasicDBObject> cloneList = new ArrayList<>();
        for (BasicDBObject basicDBObject: source) {
            cloneList.add((BasicDBObject)basicDBObject.copy());
        }
        return cloneList;
    }

    /**
     * 验证多个字段雇员唯一性
     * @param identities
     * @param rs
     * @param employeePO
     * @return
     */
    private boolean IdentityEmployee(List<String> identities, RowSet rs, PrEmployeePO employeePO){
        //TODO
        boolean identity = true;
        Map<String,Object> rsMap = new HashMap<>();
        Iterator<String> iter =  identities.iterator();
        String fieldName = "";
        Object fieldVal = null;
        while (iter.hasNext()){
            fieldName = iter.next();
            fieldVal = rs.getProperties().get(fieldName);
            if( fieldVal == null){ // 判断一致性字段是否为空
                return false;
            }else {
                if(fieldName.equals(PayItemName.EMPLOYEE_CODE_CN) && !employeePO.getEmployeeId().equals(fieldVal)){
                    return false;
                }else if(fieldName.equals(PayItemName.EMPLOYEE_NAME_CN) && !employeePO.getEmployeeName().equals(fieldVal)){
                    return false;
                }else if(fieldName.equals(PayItemName.EMPLOYEE_BIRTHDAY_CN) && !employeePO.getBirthday().equals(fieldVal)){
                    return false;
                }else if(fieldName.equals(PayItemName.EMPLOYEE_CITY_CODE_CN) && !employeePO.getCityCode().equals(fieldVal)){
                    return false;
                }else if(fieldName.equals(PayItemName.EMPLOYEE_COUNTRY_CODE_CN) && !employeePO.getCountryCode().equals(fieldVal)){
                    return false;
                }else if(fieldName.equals(PayItemName.EMPLOYEE_DEP_CN) && !employeePO.getDepartment().equals(fieldVal)){
                    return false;
                }
            }
        }

        return identity;
    }
}