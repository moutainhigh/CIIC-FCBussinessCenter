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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author yuantongqing on 2017/12/14
 */
@Service
public class TaskSubProofDetailServiceImpl extends ServiceImpl<TaskSubProofDetailMapper, TaskSubProofDetailPO> implements TaskSubProofDetailService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubProofDetailServiceImpl.class);

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
        String mainType = "main";
        String subType = "sub";
        //返回的ResponseForSubDetail对象
        ResponseForSubDetail responseForSubDetail = new ResponseForSubDetail();
        //返回的ResponseForSubDetail对象中的结果集
        List<TaskSubProofDetailBO> taskSubProofDetailBOList = new ArrayList<>();
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
        for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
            TaskSubProofDetailBO taskSubProofDetail = new TaskSubProofDetailBO();
            BeanUtils.copyProperties(taskSubProofDetailPO, taskSubProofDetail);
            taskSubProofDetailBOList.add(taskSubProofDetail);
        }
        responseForSubDetail.setRowList(taskSubProofDetailBOList);
        return responseForSubDetail;
    }

    /**
     * 批量保存完税凭证申请明细
     *
     * @param requestForSubDetail
     * @return
     */
    @Transactional
    @Override
    public Boolean saveSubProofDetail(RequestForSubDetail requestForSubDetail) {
        Boolean flag = true;
        if ("main".equals(requestForSubDetail.getDetailType())) {
            try {
                if (requestForSubDetail.getOldDeleteIds() != null && requestForSubDetail.getOldDeleteIds().length > 0) {
                    //根据主键ID将完税凭证申请明细修改为失效状态
                    baseMapper.invalidSubProofDetailByIds(requestForSubDetail.getOldDeleteIds());
                }
                //根据主任务ID查询其下所有子任务
                List<TaskSubProofPO> subList = taskSubProofMapper.selectSubTaskMapByMainId(requestForSubDetail.getTaskId());
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
                            taskSubProofDetailPO.setModifiedBy("adminMain");
                            taskSubProofDetailPO.setModifiedTime(new Date());
                        } else {
                            taskSubProofDetailPO.setTaskSubProofId(subMap.get(taskSubProofDetailBO.getDeclareAccount()));
                            taskSubProofDetailPO.setCreatedBy("adminMain");
                            taskSubProofDetailPO.setModifiedBy("adminMain");
                        }
                        taskSubProofDetailPOList.add(taskSubProofDetailPO);
                    }
                }
                this.insertOrUpdateBatch(taskSubProofDetailPOList);
                //统计子任务总人数
                for (String key : subMap.keySet()) {
                    Long subId = subMap.get(key);
                    taskSubProofMapper.updateSubHeadcountById(subId);
                }
                //统计总任务人数
                taskMainProofMapper.updateMainHeadcountById(requestForSubDetail.getTaskId());
            } catch (Exception e) {
                logger.error("ServiceImpl main saveSubProofDetail error " + e.toString());
                flag = false;
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } finally {
                return flag;
            }
        } else if ("sub".equals(requestForSubDetail.getDetailType())) {
            try {
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
                            taskSubProofDetailPO.setModifiedBy("adminMain");
                            taskSubProofDetailPO.setModifiedTime(new Date());
                        } else {
                            taskSubProofDetailPO.setTaskSubProofId(requestForSubDetail.getTaskId());
                            taskSubProofDetailPO.setCreatedBy("adminMain");
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
            } catch (Exception e) {
                flag = false;
                logger.error("ServiceImpl sub saveSubProofDetail error " + e.toString());
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            } finally {
                return flag;
            }
        } else {
            return false;
        }
    }
}
