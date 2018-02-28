package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 获取任务编号
 *
 */
public class TaskNoService {

    private static final Logger logger = LoggerFactory.getLogger(TaskNoService.class);

    public static final String TASK_MAIN = "TM";//个税主任务
    public static final String TASK_SUB_DECLARE = "TSSB";//申报子任务
    public static final String TASK_SUB_TRANSFER = "TSHK";//划款子任务
    public static final String TASK_SUB_PAYMENT = "TSJN";//缴纳子任务
    public static final String TASK_SUB_SUPPLIER = "TSSU";//供应商处理子任务

    public static final String TASK_MAIN_PROOF = "TMP";//完税凭证主任务
    public static final String TASK_SUB_PROOF = "TSP";//完税凭证子任务

    public static final int RANDOM_LENGTH = 2;//随机数长度
    public static final int RANDOM_RANGE = 100;//随机数长度

    /**
     * 一次获取多个任务编号
     * @param taskType 任务类型
     * @param num 任务数量
     * @return
     */
    public static String[] getTaskNos(String taskType,int num){

        //任务编号
        String[] taskNo = new String[num];

        //已有的随机数(随机数不可重复)
        Set<String> s = new HashSet<>();

        for(int i=0 ; i<num ; i++){

            int r;
            String rs = "";

            //随机数
            do{
                r = (int) (Math.random() * RANDOM_RANGE);
                rs = String.valueOf(r);
            }while(s.contains(rs));

            //长度不足补齐
            while(rs.length() < RANDOM_LENGTH){
                rs = rs + "0";
            }

            //记录已出现的随机数
            s.add(rs);

            //时间戳
            String ts = DateTimeKit.format(new Date(),"yyyyMMddHHmmssSSS");

            //任务编号：任务类型+时间戳+随机数
            taskNo[i] = taskType + ts + rs;

            if(logger.isDebugEnabled()){

                logger.debug("任务类型："+taskType+" 编号："+taskNo[i]);
            }
        }

        return taskNo;

    }

    /**
     * 获取单个任务编号
     * @param taskType 任务类型
     * @return
     */
    public static String getTaskNo(String taskType){

        String[] taskNo = getTaskNos(taskType,1);

        return taskNo[0];

    }

    /*public static void main(String[] args){

        getTaskNos(TASK_SUB_DECLARE,10);
    }*/
}
