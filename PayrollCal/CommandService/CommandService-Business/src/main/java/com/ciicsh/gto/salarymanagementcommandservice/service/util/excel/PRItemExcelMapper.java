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
public class PRItemExcelMapper implements RowMapper<List<DBObject>> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    public List<List<DBObject>> getList() {
        return list;
    }

    public void setList(List<List<DBObject>> list) {
        this.list = list;
    }

    public int getImportType() {
        return importType;
    }

    public void setImportType(int importType) {
        this.importType = importType;
    }

    private List<List<DBObject>> list;

    private int importType;

    @Autowired
    private PrEmployeeMapper employeeMapper;


    @Override
    public List<DBObject> mapRow(RowSet rs) throws Exception {

        int columnCount = rs.getMetaData().getColumnCount();
        String[] columNames = rs.getMetaData().getColumnNames();
        int rowIndex = rs.getCurrentRowIndex();
        String sheetName = rs.getMetaData().getSheetName();

        if(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN) == null ){
            logger.info("employee code does not exit");
            return null;
        }
        //获取EXCEL列中的雇员编号（该字段必须存在）
        String empCode = String.valueOf(rs.getProperties().get(PayItemName.EMPLOYEE_CODE_CN));

        if(importType == CompExcelImportEmum.OVERRIDE_EXPORT.getValue()){
            List<DBObject> overrideList = new ArrayList<>();
            String cloName = null;
            PrEmployeePO employeePO = new PrEmployeePO();
            employeePO.setEmpId(empCode);
            employeePO = employeeMapper.selectOne(employeePO);

            String empName = rs.getProperties().getProperty(PayItemName.EMPLOYEE_NAME_CN);
            //如果姓名相同，使用系统里的值
            //如果姓名不相同，使用外部文件里的值，并且在计算结果中显示警告信息
            boolean hasSameName = empName == employeePO.getEmpName();

            for (int i=0; i< columnCount; i++){
                DBObject dbObject = new BasicDBObject();
                cloName = columNames[i];
                String excelVal = rs.getProperties().getProperty(cloName);
                if(cloName == PayItemName.EMPLOYEE_NAME_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getEmpName());
                }else if(cloName == PayItemName.EMPLOYEE_CODE_CN){
                    dbObject.put(cloName,employeePO.getEmpId());
                }else if(cloName == PayItemName.EMPLOYEE_DEP_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getDepartment());
                }else if(cloName == PayItemName.EMPLOYEE_BIRTHDAY_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getBirthday());
                }else if(cloName == PayItemName.EMPLOYEE_ID_NUM_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getIdNum());
                }else if(cloName == PayItemName.EMPLOYEE_ID_TYPE_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getIdCardType());
                }else if(cloName == PayItemName.EMPLOYEE_POSITION_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getPosition());
                }else if(cloName == PayItemName.EMPLOYEE_SEX_CN && hasSameName){
                    dbObject.put(cloName,employeePO.getGender());
                }
                else {
                    dbObject.put(cloName,excelVal);
                }
                overrideList.add(dbObject);
            }
            return overrideList;

        }else {
            List<DBObject> dbObjects = getDbObjects(empCode);
            processImport(importType,dbObjects,rs);
            return dbObjects;
        }

    }

    private List<DBObject> getDbObjects(String empCode){

        List<DBObject> objList = null;
        for (List<DBObject> dbObjects: list) {
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

     * @param importType
     */
    private void processImport(int importType,List<DBObject> dbObjects,RowSet rs){

        dbObjects.forEach(item -> {

            Object prName = item.get("item_name");          // 薪资项名称
            Object prValue = item.get("item_value");        // 薪资项值
            Object val = rs.getProperties().get(prName);    // 薪资项值

            if(importType == CompExcelImportEmum.APPEND_EXPORT.getValue()){

            }else if(importType == CompExcelImportEmum.DIFF_EXPORT.getValue()){

            }else {

            }

            item.put("item_value",val);                  // 薪资项值
        });


    }
}
