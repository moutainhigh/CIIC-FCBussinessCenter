package com.ciicsh.gto.salarymanagementcommandservice.util;

import com.ciicsh.gto.salarymanagement.entity.enums.PeriodTypeEnum;
import org.apache.commons.lang.StringUtils;


/**
 * Created by bill on 17/12/11.
 */
public class BatchUtils {

    public static String[] getPRDates(String period, int startDay, int endDay, int periodType){
        String[] dates = new String[3];
        int year = Integer.parseInt(period.substring(0,4));
        int month = Integer.parseInt(period.substring(4));
        if(periodType == PeriodTypeEnum.LAST_MONTH.getValue()){

            if(month == 1) {
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year - 1) + "11" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year - 1) + "12" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                else {
                    dates[0] = String.valueOf(year - 1) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year - 1) + "12" + StringUtils.leftPad(String.valueOf(endDay),2,"0");

                }
                dates[2] = String.valueOf(year - 1) + "12";

            }
            else if (month == 2) {
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year - 1) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                else {
                    dates[0] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }

                dates[2] = String.valueOf(year) + "01";

            }
            else {
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year) + String.valueOf(month - 2) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month -1) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                else {
                    dates[0] = String.valueOf(year) + String.valueOf(month - 1) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month - 1) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }

                dates[2] = String.valueOf(year) + String.valueOf(month - 1);

            }
        }else if(periodType == PeriodTypeEnum.NEXT_MONTH.getValue()){
            if(month == 12){
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year + 1) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year + 1) + "01" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year + 1) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }

                dates[2] = String.valueOf(year+1) + "01";

            }
            if(month == 1){
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year-1) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year + 1) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year) + "02" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "02" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                dates[2] = String.valueOf(year) + "02";
            }else {
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year) + String.valueOf(month) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month + 1) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year) + String.valueOf(month + 1) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month + 1) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }

                dates[2] = String.valueOf(year) + String.valueOf(month + 1);

            }
        }
        else {
            if(month == 12){
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year) + "11" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "12" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "12" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }

                dates[2] = String.valueOf(year) + "12";

            }
            if(month == 1){
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year-1) + "12" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + "01" + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                dates[2] = String.valueOf(year) + "01";
            }else {
                if (startDay > endDay) {
                    dates[0] = String.valueOf(year) + String.valueOf(month-1) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }else {
                    dates[0] = String.valueOf(year) + String.valueOf(month) + StringUtils.leftPad(String.valueOf(startDay),2,"0");
                    dates[1] = String.valueOf(year) + String.valueOf(month) + StringUtils.leftPad(String.valueOf(endDay),2,"0");
                }
                dates[2] = String.valueOf(year) + StringUtils.leftPad(String.valueOf(month),2,"0");
            }
        }

        return dates;
    }

    public static String getPeriod(String period, int periodType) {
        String result = null;
        int year = Integer.parseInt(period.substring(0, 4));
        int month = Integer.parseInt(period.substring(4));
        if (periodType == PeriodTypeEnum.LAST_MONTH.getValue()) {
            if(month == 1){
                result = String.valueOf(year-1) + "01";
            }
            else {
                result = String.valueOf(year) + StringUtils.leftPad(String.valueOf(month-1),2,"0");
            }
        }
        else if (periodType == PeriodTypeEnum.NEXT_MONTH.getValue()) {
            if(month == 12){
                result = String.valueOf(year+1) + "01";
            }
            else {
                result = String.valueOf(year) + StringUtils.leftPad(String.valueOf(month+1),2,"0");
            }
        }
        else {
            result = String.valueOf(year) + StringUtils.leftPad(String.valueOf(month),2,"0");

        }

        return result;
    }
}
