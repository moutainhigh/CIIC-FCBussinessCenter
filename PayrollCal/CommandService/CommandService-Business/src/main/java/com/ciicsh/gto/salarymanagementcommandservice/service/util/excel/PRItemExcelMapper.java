package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.ciicsh.gto.fcbusinesscenter.util.constants.PayItemName;
import com.ciicsh.gto.salarymanagement.entity.enums.CompExcelImportEmum;
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
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRItemExcelMapper implements RowMapper<List<BasicDBObject>> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    public List<List<BasicDBObject>> getList() {
        return list;
    }

    public void setList(List<List<BasicDBObject>> list) {
        this.list = list;
    }

    public int getImportType() {
        return importType;
    }

    public void setImportType(int importType) {
        this.importType = importType;
    }

    private List<List<BasicDBObject>> list;

    private int importType;

    @Autowired
    private PrEmployeeMapper employeeMapper;


    @Override
    public List<BasicDBObject> mapRow(RowSet rs) throws Exception {

        int columnCount = rs.getMetaData().getColumnCount();
        String[] columNames = rs.getMetaData().getColumnNames();
        int rowIndex = rs.getCurrentRowIndex();
        String sheetName = rs.getMetaData().getSheetName();

        if(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN) == null ){
            List<BasicDBObject> dbObjects = new ArrayList<>();
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("row_index",rowIndex);
            dbObject.put("error_msg","雇员编号不存在");
            return dbObjects;
        }
        //获取EXCEL列中的雇员编号（该字段必须存在）
        String empCode = String.valueOf(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN));

        if(importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()){
           return overrideImport(empCode,columNames,rs);
        }else if(importType == CompExcelImportEmum.MODIFY_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return  processMissEmpCode(rowIndex,empCode);
            }else {
                modifyImport(dbObjects,rs);
                return dbObjects;
            }
        } else if(importType == CompExcelImportEmum.APPEND_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return overrideImport(empCode,columNames,rs);
            }else {
                modifyImport(dbObjects,rs);
                return dbObjects;
            }
        } else if(importType == CompExcelImportEmum.DIFF_EXPORT.getValue()){
            List<BasicDBObject> dbObjects = getDbObjects(empCode);
            if(dbObjects == null){
                return processMissEmpCode(rowIndex,empCode);
            }else {
               //TODO
            }
        }
        return null;
    }

    private List<BasicDBObject> getDbObjects(String empCode){

        List<BasicDBObject> objList = null;
        for (List<BasicDBObject> dbObjects: list) {
            objList = dbObjects.stream().filter(dbObject ->{
                return String.valueOf(dbObject.get(PayItemName.EMPLOYEE_CODE_CN)).equals(empCode);
            }).collect(Collectors.toList());

            if (objList != null)
                break;
        }
        return objList;

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
    private void modifyImport(List<BasicDBObject> dbObjects,RowSet rs){

        dbObjects.forEach(item -> {
            Object prName = item.get("item_name");          // 薪资项名称
            Object val = rs.getProperties().get(prName);    // 薪资项值
            item.put("item_value",val);                  // 薪资项值
        });
    }

    /**
     * 1、覆盖导入
     场景：计算批次雇员组中没有雇员，全部从外部文件导入。导入后系统将文件内雇员加入当前批次中雇员组，并且验证雇员基本信息，如果和系统信息不一致：
     1）. 如果不是姓名不一致则使用系统里的值
     2）. 如果是姓名不一致则使用外部文件里的值，并且在计算结果中显示警告信息。
     * @param empCode
     * @param columNames
     * @param rs
     * @return
     */
    private List<BasicDBObject> overrideImport(String empCode, String[] columNames,RowSet rs){
        List<BasicDBObject> overrideList = null;
        String cloName = null;
        PrEmployeePO employeePO = new PrEmployeePO();
        employeePO.setEmpId(empCode);
        employeePO = employeeMapper.selectOne(employeePO);
        if(employeePO == null){
            overrideList = new ArrayList<>();
            BasicDBObject dbObject = new BasicDBObject();
            dbObject.put("row_index",rs.getCurrentRowIndex());
            dbObject.put("error_msg",String.format("雇员编号:%s 不存在",empCode));
            overrideList.add(dbObject);
            return overrideList;
        }

        //雇员信息：雇员信息，雇员服务协议，雇员扩展字段
        /*DBObject empInfoObj = new BasicDBObject();
        List<DBObject> empBasicFields = new ArrayList<>();
        empInfoObj.put("emp_info",empBasicFields);
        overrideList.add(1,empInfoObj);*/
        //end

        overrideList = cloneListDBObject(list.get(0)); // clone 薪资项 LIST

        String empName = rs.getProperties().getProperty(PayItemName.EMPLOYEE_NAME_CN);
        //如果姓名相同，使用系统里的值
        //如果姓名不相同，使用外部文件里的值，并且在计算结果中显示警告信息
        boolean hasSameName = empName == employeePO.getEmpName();

        for (DBObject prItem: overrideList) {
            for (int i = 0; i < columNames.length; i++) { //索引从1开始，0 为 emp_code
                cloName = columNames[i];
                String excelVal = rs.getProperties().getProperty(cloName); // 外部文件系统值
                String prItemName = prItem.get("item_name") == null ? "" : (String) prItem.get("item_name");
                if (!prItemName.equals(cloName)) {
                    continue;
                }
                if (hasSameName) //使用外部系统的值
                    prItem.put("item_value", excelVal);
                else { // 使用系统内部数据
                    if (prItemName.equals(PayItemName.EMPLOYEE_NAME_CN)) {
                        prItem.put("item_value", employeePO.getEmpName());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_CODE_CN)) {
                        prItem.put("item_value", employeePO.getEmpId());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_DEP_CN)) {
                        prItem.put("item_value", employeePO.getDepartment());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_BIRTHDAY_CN)) {
                        prItem.put("item_value", employeePO.getBirthday());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_ID_NUM_CN)) {
                        prItem.put("item_value", employeePO.getIdNum());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_ID_TYPE_CN)) {
                        prItem.put("item_value", employeePO.getIdCardType());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_POSITION_CN)) {
                        prItem.put("item_value", employeePO.getPosition());
                    } else if (prItemName.equals(PayItemName.EMPLOYEE_SEX_CN)) {
                        prItem.put("item_value", employeePO.getGender());
                    } else {
                        prItem.put("item_value", excelVal);
                    }
                }
                continue;
            }
        }
        //雇员薪资项编码
        BasicDBObject empCodeObj = new BasicDBObject();
        empCodeObj.put("emp_code",empCode);
        overrideList.add(0,empCodeObj);
        //end
        return overrideList;
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

    private List<BasicDBObject> cloneListDBObject(List<BasicDBObject> source){
        List<BasicDBObject> cloneList = new ArrayList<>();
        for (BasicDBObject basicDBObject: source) {
            cloneList.add((BasicDBObject)basicDBObject.copy());
        }
        return cloneList;
    }
}
