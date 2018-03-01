package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
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
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Stream;

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
        for (TaskSubProofDetailPO p : taskSubProofDetailPOList) {
            p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
            p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
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
            //修改申报明细为不可用状态
            updateTaskSubProofDetailActive(requestForSubDetail);
            //查询完税凭证子任务条件
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubProofPO());
            //主任务ID
            wrapper.andNew("task_main_proof_id = {0} ", requestForSubDetail.getTaskId());
            //任务为可用状态
            wrapper.andNew("is_active = {0} ", true);
            //根据主任务ID查询其下所有子任务
            List<TaskSubProofPO> subList = taskSubProofMapper.selectList(wrapper);
            //用于存申报账户
            Map<String, Long> subMap = new HashMap<>(16);
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
                        //设置主任务ID
                        taskSubProofPO.setTaskMainProofId(requestForSubDetail.getTaskId());
                        //设置申报账户
                        taskSubProofPO.setDeclareAccount(taskSubProofDetailBO.getDeclareAccount());
                        //设置子任务编号
                        taskSubProofPO.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_PROOF));
                        //设置任务为草稿状态
                        taskSubProofPO.setStatus("00");
                        // TODO 临时设置创建人
                        //设置创建人
                        taskSubProofPO.setCreatedBy("adminMain");
                        // TODO 临时设置修改人
                        //设置修改人
                        taskSubProofPO.setModifiedBy("adminMain");
                        //设置任务类型为手动
                        taskSubProofPO.setTaskType("02");
                        //新增完税凭证子任务
                        taskSubProofMapper.insert(taskSubProofPO);
                        subMap.put(taskSubProofDetailBO.getDeclareAccount(), taskSubProofPO.getId());
                    }
                    //设置申报明细
                    TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
                    BeanUtils.copyProperties(taskSubProofDetailBO, taskSubProofDetailPO);
                    if (taskSubProofDetailPO.getId() != null && !"".equals(taskSubProofDetailPO.getId())) {
                        // TODO 临时设置修改人
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                        taskSubProofDetailPO.setModifiedTime(LocalDateTime.now());
                    } else {
                        taskSubProofDetailPO.setTaskSubProofId(subMap.get(taskSubProofDetailBO.getDeclareAccount()));
                        // TODO 临时设置创建人
                        //创建人
                        taskSubProofDetailPO.setCreatedBy("adminMain");
                        // TODO 临时设置修改人
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                    }
                    taskSubProofDetailPOList.add(taskSubProofDetailPO);
                }
            }
            //mybatisPlus批量插入或者修改
            this.insertOrUpdateBatch(taskSubProofDetailPOList);
            //统计子任务人数
            for (String key : subMap.keySet()) {
                Long subId = subMap.get(key);
                Map<String,Object> subNumMap = baseMapper.queryPersonNumBySubProofId(subId);
                TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
                taskSubProofPO.setId(subId);
                //总人数
                taskSubProofPO.setHeadcount(Integer.parseInt(String.valueOf(subNumMap.get("headNumTotal"))));
                //中方人数
                taskSubProofPO.setChineseNum(Integer.parseInt(String.valueOf(subNumMap.get("chineseNumTotal"))));
                //外方人数
                taskSubProofPO.setForeignerNum(Integer.parseInt(String.valueOf(subNumMap.get("foreignerNumTotal"))));
                taskSubProofMapper.updateById(taskSubProofPO);
//                taskSubProofMapper.updateSubHeadcountById(subId);
            }
            //统计总任务人数
            Map<String,Object> mainNumMap = taskSubProofMapper.queryPersonNumByMainProofId(requestForSubDetail.getTaskId());
            TaskMainProofPO taskMainProofPO = new TaskMainProofPO();
            taskMainProofPO.setId(requestForSubDetail.getTaskId());
            //总人数
            taskMainProofPO.setHeadcount(Integer.parseInt(String.valueOf(mainNumMap.get("headNumTotal"))));
            //中方人数
            taskMainProofPO.setChineseNum(Integer.parseInt(String.valueOf(mainNumMap.get("chineseNumTotal"))));
            //外方人数
            taskMainProofPO.setForeignerNum(Integer.parseInt(String.valueOf(mainNumMap.get("foreignerNumTotal"))));
            taskMainProofMapper.updateById(taskMainProofPO);
//            taskMainProofMapper.updateMainHeadcountById(requestForSubDetail.getTaskId());
        } else if (subType.equals(requestForSubDetail.getDetailType())) {
            //修改申报明细为不可用状态
            updateTaskSubProofDetailActive(requestForSubDetail);
            if (requestForSubDetail.getTaskSubProofDetailBOList() != null) {
                List<TaskSubProofDetailPO> taskSubProofDetailPOList = new ArrayList<>();
                List<TaskSubProofDetailBO> taskSubProofDetailBOList = requestForSubDetail.getTaskSubProofDetailBOList();
                for (TaskSubProofDetailBO taskSubProofDetailBO : taskSubProofDetailBOList) {
                    TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
                    BeanUtils.copyProperties(taskSubProofDetailBO, taskSubProofDetailPO);
                    if (taskSubProofDetailPO.getId() != null && !"".equals(taskSubProofDetailPO)) {
                        // TODO 临时设置修改人
                        //修改人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                        taskSubProofDetailPO.setModifiedTime(LocalDateTime.now());
                    } else {
                        taskSubProofDetailPO.setTaskSubProofId(requestForSubDetail.getTaskId());
                        // TODO 临时设置修改人
                        //修改人
                        taskSubProofDetailPO.setCreatedBy("adminMain");
                        // TODO 临时设置创建人
                        //创建人
                        taskSubProofDetailPO.setModifiedBy("adminMain");
                    }
                    taskSubProofDetailPOList.add(taskSubProofDetailPO);
                }
                //mybatisPlus批量插入或者修改
                this.insertOrUpdateBatch(taskSubProofDetailPOList);
            }
            //统计子任务总人数
//            taskSubProofMapper.updateSubHeadcountById(requestForSubDetail.getTaskId());
            Map<String,Object> subNumMap = baseMapper.queryPersonNumBySubProofId(requestForSubDetail.getTaskId());
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            taskSubProofPO.setId(requestForSubDetail.getTaskId());
            //总人数
            taskSubProofPO.setHeadcount(Integer.parseInt(String.valueOf(subNumMap.get("headNumTotal"))));
            //中方人数
            taskSubProofPO.setChineseNum(Integer.parseInt(String.valueOf(subNumMap.get("chineseNumTotal"))));
            //外方人数
            taskSubProofPO.setForeignerNum(Integer.parseInt(String.valueOf(subNumMap.get("foreignerNumTotal"))));
            //TODO 临时修改人
            taskSubProofPO.setModifiedBy("adminCopy");
            //修改时间
            taskSubProofPO.setModifiedTime(LocalDateTime.now());
            taskSubProofMapper.updateById(taskSubProofPO);
            //统计总任务人数
            TaskSubProofPO taskSubProofPOInfo = taskSubProofMapper.selectById(requestForSubDetail.getTaskId());
//            taskMainProofMapper.updateMainHeadcountById(taskSubProofPOInfo.getTaskMainProofId());
            Map<String,Object> mainNumMap = taskSubProofMapper.queryPersonNumByMainProofId(taskSubProofPOInfo.getTaskMainProofId());
            TaskMainProofPO taskMainProofPO = new TaskMainProofPO();
            taskMainProofPO.setId(taskSubProofPOInfo.getTaskMainProofId());
            //总人数
            taskMainProofPO.setHeadcount(Integer.parseInt(String.valueOf(mainNumMap.get("headNumTotal"))));
            //中方人数
            taskMainProofPO.setChineseNum(Integer.parseInt(String.valueOf(mainNumMap.get("chineseNumTotal"))));
            //外方人数
            taskMainProofPO.setForeignerNum(Integer.parseInt(String.valueOf(mainNumMap.get("foreignerNumTotal"))));
            //TODO 临时修改人
            taskMainProofPO.setModifiedBy("adminCopy");
            //修改时间
            taskMainProofPO.setModifiedTime(LocalDateTime.now());
            taskMainProofMapper.updateById(taskMainProofPO);
        }
    }

    /**
     * 修改申报明细为不可用状态
     *
     * @param requestForSubDetail
     */
    private void updateTaskSubProofDetailActive(RequestForSubDetail requestForSubDetail) {
        if (requestForSubDetail.getOldDeleteIds() != null && requestForSubDetail.getOldDeleteIds().length > 0) {
            //根据主键ID将完税凭证申请明细修改为不可用
            TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
            //设置为不可用
            taskSubProofDetailPO.setActive(false);
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubProofDetailPO());
            //任务为可用状态
            wrapper.andNew("is_active = {0} ", true);
            //完税凭证申请明细主键 IN条件
            wrapper.in("id", requestForSubDetail.getOldDeleteIds());
            baseMapper.update(taskSubProofDetailPO, wrapper);
        }
    }
}
