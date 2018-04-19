package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TasknoMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TasknoPO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * 获取任务编号
 * @author wuhua
 */
@Service
public class TaskNoService {

    private static final Logger logger = LoggerFactory.getLogger(TaskNoService.class);

    public static final String TASK_MAIN = "TM";//个税主任务
    public static final String TASK_SUB_DECLARE = "TSSB";//申报子任务
    public static final String TASK_SUB_TRANSFER = "TSHK";//划款子任务
    public static final String TASK_SUB_PAYMENT = "TSJN";//缴纳子任务
    public static final String TASK_SUB_SUPPLIER = "TSSU";//供应商处理子任务

    public static final String TASK_MAIN_PROOF = "TMP";//完税凭证主任务
    public static final String TASK_SUB_PROOF = "TSP";//完税凭证子任务

    public static final int TASK_LENGTH = 6;//任务编号数字位长度

    @Autowired
    public TasknoMapper tasknoMapper;

    /**
     * 一次获取多个任务编号
     * @param taskType 任务类型
     * @param num 任务数量
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public synchronized String[] getTaskNos(String taskType,int num){

        //任务编号
        String[] taskNos = new String[num];

        //当前最大编号
        int taskNoDefault = 0;

        EntityWrapper wrapper = new EntityWrapper();
        TasknoPO tasknoPO = new TasknoPO();
        tasknoPO.setTaskDate(LocalDate.now());
        tasknoPO.setTaskType(taskType);
        tasknoPO = tasknoMapper.selectOne(tasknoPO);

        //当天没有任务
        if(tasknoPO == null){
            tasknoPO = new TasknoPO();
            tasknoPO.setTaskType(taskType);
            tasknoPO.setTaskDate(LocalDate.now());
            tasknoPO.setTaskNum(null);
            this.tasknoMapper.insert(tasknoPO);
        }else{
            taskNoDefault = tasknoPO.getTaskNum();
        }

        //编号
        for(int i=0 ; i<num ; i++){
            taskNoDefault++;
            taskNos[i] = this.numberGenerater(taskType,taskNoDefault);
        }

        //更新最大编号
        tasknoPO.setTaskNum(taskNoDefault);
        this.tasknoMapper.updateById(tasknoPO);

        return taskNos;

    }

    /**
     * 获取单个任务编号
     * @param taskType 任务类型
     * @return
     */
    public String getTaskNo(String taskType){

        String[] taskNo = getTaskNos(taskType,1);

        return taskNo[0];

    }

    //编号组合
    private String numberGenerater(String taskType ,int num){

        //不足补零
        String no = String.format("%0" + TaskNoService.TASK_LENGTH + "d", num);

        StringBuilder sb = new StringBuilder();
        sb.append(taskType)
                .append(DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now()))
                .append(no);

        return sb.toString();
    }

    public static void main(String[] args){

//        getTaskNos(TASK_SUB_DECLARE,10);
//        String t = String.format("%05d", 10);
//        System.out.print("%%%%%%%%%t:");
//        System.out.print(t);

        Double.parseDouble("");
    }
}
