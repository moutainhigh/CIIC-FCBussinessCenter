package com.ciicsh.caldispatchjob.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/1/17
 * 规则引擎 上下文对象，包括 雇员的薪资项列表与雇员相关的函数列表
 */
public class DroolsContext {

    private EmpPayItem empPayItem;
    private List<FuncEntity> funcEntityList;
    private BatchContext batchContext;

    public EmpPayItem getEmpPayItem() {
        return empPayItem;
    }

    public DroolsContext(){
        this.funcEntityList = new ArrayList<>();
    }

    public void setEmpPayItem(EmpPayItem empPayItem) {
        this.empPayItem = empPayItem;
    }

    public List<FuncEntity> getFuncEntityList() {
        return this.funcEntityList;
    }

    public BatchContext getBatchContext() {
        return batchContext;
    }

    public void setBatchContext(BatchContext batchContext) {
        this.batchContext = batchContext;
    }

    public boolean existFunction(String funcName){
        return this.funcEntityList.stream().anyMatch(p->p.getFuncName().equals(funcName));
    }

    /**
     * 获取薪资项值
     * @param itemCode
     * @return
     */
    public Object getItemValByCode(String itemCode){
        return empPayItem.getItems().get(itemCode);
    }

    /**
     * 获取函数对象实体
     * @param funcName
     * @return
     */
    public FuncEntity getFuncEntity(String funcName){
        return this.funcEntityList.stream().filter(p -> p.getFuncName().equals(funcName)).collect(Collectors.toList()).get(0);
    }

    public List<String> getParametersByFuncName(FuncEntity entity, String funcName){
        return entity.getParameters();
    }

    public BigDecimal getBigDecimal( Object value ) {
        BigDecimal ret = null;
        if( value != null ) {
            if( value instanceof BigDecimal ) {
                ret = (BigDecimal) value;
            } else if( value instanceof String ) {
                ret = new BigDecimal( (String) value );
            } else if( value instanceof BigInteger) {
                ret = new BigDecimal( (BigInteger) value );
            } else if( value instanceof Number ) {
                ret = new BigDecimal( ((Number)value).doubleValue() );
            } else {
                throw new ClassCastException("Not possible to coerce ["+value+"] from class "+value.getClass()+" into a BigDecimal.");
            }
        }
        return ret;
    }


}
