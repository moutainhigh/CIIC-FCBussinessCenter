package com.ciicsh.gto.salarymanagementcommandservice.util.excel;

import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.mongodb.DBObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 17/12/15.
 */
public class PRItemExcelMapper implements RowMapper<List<PrPayrollItemPO>> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    private List<DBObject> empList;

    private List<PrPayrollItemPO> list;


    public List<DBObject> getEmpList() {
        return empList;
    }

    public void setEmpList(List<DBObject> empList) {
        this.empList = empList;
    }

    public List<PrPayrollItemPO> getList() {
        return list;
    }

    public void setList(List<PrPayrollItemPO> list) {
        this.list = list;
    }

    @Override
    public List<PrPayrollItemPO> mapRow(RowSet rs) throws Exception {

        int totalCount = rs.getMetaData().getColumnCount();
        String sheetName = rs.getMetaData().getSheetName();
        if(totalCount != list.size()){
            logger.info("column count is not equal");
        }

        if(rs.getProperties().get("雇员编码") == null ){
            logger.info("employee code does not exit");
            return null;
        }
        String empCode = String.valueOf(rs.getProperties().get("雇员编码"));
        DBObject dbObject = getDbObject(empCode);

        list.stream().forEach(item -> {
            String prName = item.getItemName();             // 薪资项名称
            Object val = rs.getProperties().get(prName);    // 薪资项值
            Object baseVal = dbObject.get(prName);          // 基础薪资项值

            if(baseVal != null) {
                item.setItemValue(String.valueOf(baseVal));
            }else if(val != null){
                item.setItemValue(String.valueOf(val));                       //把EXCEL的值 赋予 该薪资项
            }else{
                logger.info("不存在列名：" + prName);
            }
        });
        return list;
    }

    private DBObject getDbObject(String empCode){

        return empList
                    .stream()
                    .filter(dbObject -> String.valueOf(dbObject.get("雇员编码")) == empCode)
                    .collect(Collectors.toList()).get(0);
    }
}
