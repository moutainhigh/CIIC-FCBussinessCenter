package com.ciicsh.caldispatchjob.entity;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/1/17.
 */
public class DroolsContext {

    private EmpPayItem empPayItem;
    private List<FuncEntity> funcEntityList;

    public EmpPayItem getEmpPayItem() {
        return empPayItem;
    }

    public DroolsContext(){

        this.funcEntityList = new ArrayList<>();
        /*FuncEntity funcEntity = new FuncEntity();
        funcEntity.setFuncName("城市最低生活标准");
        this.funcEntityList.add(funcEntity);

        funcEntity = new FuncEntity();
        funcEntity.setFuncName("实际工龄");
        this.funcEntityList.add(funcEntity);*/

    }

    public void setEmpPayItem(EmpPayItem empPayItem) {
        this.empPayItem = empPayItem;
    }

    public List<FuncEntity> getFuncEntityList() {
        return this.funcEntityList;
    }

    public void clearFuncResult(){
        for (FuncEntity funcEntity: this.getFuncEntityList()) {
            funcEntity.setResult(null);
        }
    }

    /**
     * 获取薪资项值
     * @param payItemName
     * @return
     */
    public Object getItemValByItemName(String payItemName){
        return empPayItem.getItems().get(payItemName);
    }

    /**
     * 获取函数对象实体
     * @param funcName
     * @return
     */
    public FuncEntity getFuncEntity(String funcName){
        return this.funcEntityList.stream().filter(p -> p.getFuncName().equals(funcName)).collect(Collectors.toList()).get(0);
    }

    /**
     * 计算工龄
     * @param onbaordDate
     * @param leaveDate
     * @return
     */
    public BigDecimal getWorkDate(String onbaordDate, String leaveDate) {

        YearMonth currentYearMonth = YearMonth.now();
        currentYearMonth.lengthOfMonth();

        /*
        Date date = null;
        double result = 0.0;
        SimpleDateFormat bartDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        if(leaveDate == null || leaveDate == ""){
            date = new Date();//获得当前日期
        }
        else {
            try {
                date = bartDateFormat.parse(leaveDate);
            }catch (Exception ex){
                System.out.println(ex.getMessage());
            }
        }
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);//获得年份
        int month = cal.get(Calendar.MONTH);//获得月份

        try {
            Date d = bartDateFormat.parse(onbaordDate);
            cal.setTime(d);
            int year2 = cal.get(Calendar.YEAR);
            int month2 = cal.get(Calendar.MONTH);
            int t = year - year2;//得到年差
            int m = month - month2;//得到月差
            if (m < 0) {
                t = t - 1;
                m = 12 + m;
            }
            result = t + m/12;
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }*/

        return BigDecimal.valueOf(2);
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
