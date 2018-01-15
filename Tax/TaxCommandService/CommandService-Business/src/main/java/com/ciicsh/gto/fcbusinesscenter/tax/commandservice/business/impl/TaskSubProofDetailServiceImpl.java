package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuantongqing on 2017/12/14
 */
@Service
public class TaskSubProofDetailServiceImpl extends ServiceImpl<TaskSubProofDetailMapper, TaskSubProofDetailPO> implements TaskSubProofDetailService, Serializable {

    @Autowired(required = false)
    private TaskMainProofMapper taskMainProofMapper;

    @Autowired(required = false)
    private TaskSubProofMapper taskSubProofMapper;

    /**
     * 查询完税申请明细
     *
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForSubDetail queryTaskSubProofDetail(RequestForProof requestForProof) {
        //完税凭证主任务类型
        String mainType = "main";
        //完税凭证子任务类型
        String subType = "sub";
        //返回的ResponseForSubDetail对象
        ResponseForSubDetail responseForSubDetail = new ResponseForSubDetail();
        //查询的BO对象
        TaskSubProofDetailBO taskSubProofDetailBO = new TaskSubProofDetailBO();
        BeanUtils.copyProperties(requestForProof, taskSubProofDetailBO);
        //查询的结果集
        List<TaskSubProofDetailPO> taskSubProofDetailPOList = null;
        if (mainType.equals(requestForProof.getDetailType())) {
            taskSubProofDetailPOList = baseMapper.queryTaskSubProofDetailByMainId(taskSubProofDetailBO);
        } else if (subType.equals(requestForProof.getDetailType())) {
            taskSubProofDetailPOList = baseMapper.queryTaskSubProofDetailBySubId(taskSubProofDetailBO);
        }
        //获取证件类型中文和所得项目中文名
        for(TaskSubProofDetailPO p: taskSubProofDetailPOList){
            p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
            p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
        }
        responseForSubDetail.setRowList(taskSubProofDetailPOList);
        return responseForSubDetail;
    }

    /**
     * 批量保存完税凭证申请明细
     *
     * @param requestForSubDetail
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSubProofDetail(RequestForSubDetail requestForSubDetail) {
        //完税凭证主任务类型
        String mainType = "main";
        //完税凭证子任务类型
        String subType = "sub";
        if (mainType.equals(requestForSubDetail.getDetailType())) {
            if (requestForSubDetail.getOldDeleteIds() != null && requestForSubDetail.getOldDeleteIds().length > 0) {
                //根据主键ID将完税凭证申请明细修改为失效状态
                baseMapper.invalidSubProofDetailByIds(requestForSubDetail.getOldDeleteIds());
            }
            //根据主任务ID查询其下所有子任务
            List<TaskSubProofPO> subList = taskSubProofMapper.selectSubTaskMapByMainId(requestForSubDetail.getTaskId());
            //用于存申报账户
            Map<String, Long> subMap = new HashMap<>();
            for (TaskSubProofPO taskSubProofPO : subList) {
                subMap.put(taskSubProofPO.getDeclareAccount(), taskSubProofPO.getId());
            }
            List<TaskSubProofDetailBO> taskSubProofDetailBOList = requestForSubDetail.getTaskSubProofDetailBOList();
            List<TaskSubProofDetailPO> taskSubProofDetailPOList = new ArrayList<>();
            for (TaskSubProofDetailBO taskSubProofDetailBO : taskSubProofDetailBOList) {
                if (taskSubProofDetailBO.getDeclareAccount() != null && !"".equals(taskSubProofDetailBO.getDeclareAccount())) {
                    //判断是否含有该申报账户的子任务
                    if (subMap == null || !subMap.containsKey(taskSubProofDetailBO.getDeclareAccount())) {
                        TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
                        String dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
                        taskSubProofPO.setTaskMainProofId(requestForSubDetail.getTaskId());
                        taskSubProofPO.setDeclareAccount(taskSubProofDetailBO.getDeclareAccount());
                        taskSubProofPO.setTaskNo("TAX" + dateTimeStr);
                        taskSubProofPO.setStatus("00");
                        taskSubProofPO.setCreatedBy("adminMain");
                        taskSubProofPO.setModifiedBy("adminMain");
                        taskSubProofPO.setTaskType("02");
                        taskSubProofMapper.addTaskSubProof(taskSubProofPO);
                        subMap.put(taskSubProofDetailBO.getDeclareAccount(), taskSubProofPO.getId());
                    }
                    //设置申报明细
                    TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
                    BeanUtils.copyProperties(taskSubProofDetailBO, taskSubProofDetailPO);
                    if (taskSubProofDetailPO.getId() != null && !"".equals(taskSubProofDetailPO.getId())) {
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                        taskSubProofDetailPO.setModifiedTime(new Date());
                    } else {
                        taskSubProofDetailPO.setTaskSubProofId(subMap.get(taskSubProofDetailBO.getDeclareAccount()));
                        //创建人
                        taskSubProofDetailPO.setCreatedBy("adminMain");
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                    }
                    taskSubProofDetailPOList.add(taskSubProofDetailPO);
                }
            }
            //mybatisPlus批量插入或者修改
            this.insertOrUpdateBatch(taskSubProofDetailPOList);
            //统计子任务总人数
            for (String key : subMap.keySet()) {
                Long subId = subMap.get(key);
                taskSubProofMapper.updateSubHeadcountById(subId);
            }
            //统计总任务人数
            taskMainProofMapper.updateMainHeadcountById(requestForSubDetail.getTaskId());
        } else if (subType.equals(requestForSubDetail.getDetailType())) {
            if (requestForSubDetail.getOldDeleteIds() != null && requestForSubDetail.getOldDeleteIds().length > 0) {
                //根据主键ID将完税凭证申请明细修改为失效状态
                baseMapper.invalidSubProofDetailByIds(requestForSubDetail.getOldDeleteIds());
            }
            if (requestForSubDetail.getTaskSubProofDetailBOList() != null) {
                List<TaskSubProofDetailPO> taskSubProofDetailPOList = new ArrayList<>();
                List<TaskSubProofDetailBO> taskSubProofDetailBOList = requestForSubDetail.getTaskSubProofDetailBOList();
                for (TaskSubProofDetailBO taskSubProofDetailBO : taskSubProofDetailBOList) {
                    TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
                    BeanUtils.copyProperties(taskSubProofDetailBO, taskSubProofDetailPO);
                    if (taskSubProofDetailPO.getId() != null && !"".equals(taskSubProofDetailPO)) {
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                        taskSubProofDetailPO.setModifiedTime(new Date());
                    } else {
                        taskSubProofDetailPO.setTaskSubProofId(requestForSubDetail.getTaskId());
                        //修改人
                        taskSubProofDetailPO.setCreatedBy("adminMain");
                        //创建人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                    }
                    taskSubProofDetailPOList.add(taskSubProofDetailPO);
                }
                //mybatisPlus批量插入或者修改
                this.insertOrUpdateBatch(taskSubProofDetailPOList);
            }
            //统计子任务总人数
            taskSubProofMapper.updateSubHeadcountById(requestForSubDetail.getTaskId());
            //统计总任务人数
            TaskSubProofPO taskSubProofPO = taskSubProofMapper.selectById(requestForSubDetail.getTaskId());
            taskMainProofMapper.updateMainHeadcountById(taskSubProofPO.getTaskMainProofId());
        }
    }
}
