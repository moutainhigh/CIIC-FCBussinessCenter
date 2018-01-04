package com.ciicsh.gto.fcbusinesscenter.tax.queryservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.dao.TaskSubProofDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.dao.TaskSubProofMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/12
 */
@Service
public class TaskSubProofServiceImpl extends ServiceImpl<TaskSubProofMapper, TaskSubProofPO> implements TaskSubProofService {


    @Autowired(required = false)
    private TaskMainProofMapper taskMainProofMapper;

    @Autowired(required = false)
    private TaskSubProofDetailMapper taskSubProofDetailMapper;

    /**
     * 根据完税主任务ID查询其下完税子任务
     * @param taskMainProofId
     * @return
     */
    @Override
    public List<TaskSubProofPO> queryTaskSubProofByMainId(Long taskMainProofId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofPO());
        wrapper.andNew(" task_main_proof_id = {0}",taskMainProofId);
        List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectList(wrapper);
        return taskSubProofPOList;
    }

    /**
     * 根据请求参数查询完税子任务信息
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForSubProof queryTaskSubProofByRes(RequestForProof requestForProof) {
        ResponseForSubProof responseForSubProof = new ResponseForSubProof();
        List<TaskSubProofBO> taskSubProofBOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofPO());
        //判断是否包含主键ID条件
        if(requestForProof.getId() != null){
            wrapper.andNew("id = {0}",requestForProof.getId());
        }
        wrapper.orderBy("created_time",false);
        //判断是否是分页查询
        if(requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0){
            Page<TaskSubProofPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取总数目
            int total = baseMapper.selectCount(wrapper);
            //将PO集合对象转换成BO集合对象
            for(TaskSubProofPO taskSubProofPO:taskSubProofPOList){
                TaskSubProofBO taskSubProofBO = new TaskSubProofBO();
                BeanUtils.copyProperties(taskSubProofPO,taskSubProofBO);
                taskSubProofBOList.add(taskSubProofBO);
            }
            responseForSubProof.setCurrentNum(requestForProof.getCurrentNum());
            responseForSubProof.setPageSize(requestForProof.getPageSize());
            responseForSubProof.setTotalNum(total);
            responseForSubProof.setRowList(taskSubProofBOList);
        }else {
            List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectList(wrapper);
            for(TaskSubProofPO taskSubProofPO:taskSubProofPOList){
                TaskSubProofBO taskSubProofBO = new TaskSubProofBO();
                BeanUtils.copyProperties(taskSubProofPO,taskSubProofBO);
                taskSubProofBOList.add(taskSubProofBO);
            }
            responseForSubProof.setRowList(taskSubProofBOList);
        }
        return responseForSubProof;
    }

    /**
     * 根据子任务ID复制相关数据
     * @param taskSubProofId
     * @return
     */
    @Transactional
    @Override
    public Boolean copyProofInfoBySubId(Long taskSubProofId) {
        Boolean flag = true;
        try {
            //根据子任务ID查询子任务信息
            TaskSubProofPO taskSubProofPO = baseMapper.selectById(taskSubProofId);
            //根据主任务ID查询主任务信息
            TaskMainProofPO taskMainProofPO = taskMainProofMapper.selectById(taskSubProofPO.getTaskMainProofId());
            //复制新的主任务
            taskMainProofPO.setId(null);
            taskMainProofPO.setTaskNo("MAIN201712221314520");
            taskMainProofPO.setStatus("00");
            taskMainProofPO.setCreatedTime(new Date());
            taskMainProofPO.setModifiedTime(new Date());
            taskMainProofMapper.insert(taskMainProofPO);
            //插入新的子任务
            taskSubProofPO.setId(null);
            taskSubProofPO.setTaskMainProofId(taskMainProofPO.getId());
            taskSubProofPO.setTaskNo("SUB201712221314520");
            taskSubProofPO.setStatus("00");
            taskSubProofPO.setCreatedTime(new Date());
            taskSubProofPO.setModifiedTime(new Date());
            baseMapper.insert(taskSubProofPO);
            //根据子任务ID查询相关申报明细，并重新插入
            List<TaskSubProofDetailPO> taskSubProofDetailPOList = taskSubProofDetailMapper.querySubProofDetailBySubId(taskSubProofId);
            for(TaskSubProofDetailPO taskSubProofDetailPO:taskSubProofDetailPOList){
                taskSubProofDetailPO.setId(null);
                taskSubProofDetailPO.setTaskSubProofId(taskSubProofPO.getId());
                taskSubProofDetailPO.setCreatedTime(new Date());
                taskSubProofDetailPO.setModifiedTime(new Date());
                taskSubProofDetailMapper.insert(taskSubProofDetailPO);
            }
            //重新统计复制的完税凭证任务人数
            baseMapper.updateSubHeadcountById(taskSubProofPO.getId());
            taskMainProofMapper.updateMainHeadcountById(taskMainProofPO.getId());
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        } finally {
            return flag;
        }

    }


}
