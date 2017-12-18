package com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcsupportcenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcsupportcenter.tax.entity.request.RequestForSubDetail;
import com.ciicsh.gto.fcsupportcenter.tax.entity.response.voucher.ResponseForSubDetail;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcsupportcenter.tax.queryservice.dao.TaskSubProofDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/14
 */
@Service
public class TaskSubProofDetailServiceImpl extends ServiceImpl<TaskSubProofDetailMapper, TaskSubProofDetailPO> implements TaskSubProofDetailService, Serializable {

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
                    BeanUtils.copyProperties(taskSubProofDetailBO,taskSubProofDetailPO);
                    if(taskSubProofDetailPO.getId() != null && !"".equals(taskSubProofDetailPO)){
                        taskSubProofDetailPO.setModifiedBy("yuantqUpdate");
                        taskSubProofDetailPO.setModifiedTime(new Date());
                    }else{
                        if("sub".equals(requestForSubDetail.getDetailType())){
                            taskSubProofDetailPO.setTaskSubProofId(requestForSubDetail.getTaskId());
                        }
                        taskSubProofDetailPO.setCreatedBy("yuantq");
                        taskSubProofDetailPO.setModifiedBy("yuantq");
                    }
                    taskSubProofDetailPOList.add(taskSubProofDetailPO);
                }
                //mybatisPlus批量插入或者修改
                this.insertOrUpdateBatch(taskSubProofDetailPOList);
            }
        } catch (Exception e) {
            flag = false;
            e.printStackTrace();
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return flag;
        }


    }
}
