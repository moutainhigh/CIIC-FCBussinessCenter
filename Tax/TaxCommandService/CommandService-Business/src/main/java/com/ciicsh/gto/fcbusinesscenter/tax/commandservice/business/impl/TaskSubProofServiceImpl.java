package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
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
     *
     * @param taskMainProofId
     * @return
     */
    @Override
    public List<TaskSubProofPO> queryTaskSubProofByMainId(long taskMainProofId) {
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofPO());
        wrapper.andNew(" task_main_proof_id = {0}", taskMainProofId);
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectList(wrapper);
        return taskSubProofPOList;
    }

    /**
     * 根据请求参数查询完税子任务信息
     *
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
        if (requestForProof.getId() != null) {
            wrapper.andNew("id = {0}", requestForProof.getId());
        }
        wrapper.orderBy("created_time", false);
        //判断是否是分页查询
        if (requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0) {
            Page<TaskSubProofPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectPage(pageInfo, wrapper);
            pageInfo.setRecords(taskSubProofPOList);
            //将PO集合对象转换成BO集合对象
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOList) {
                TaskSubProofBO taskSubProofBO = new TaskSubProofBO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofBO);
                //获取完税凭证任务状态
                taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,taskSubProofBO.getStatus()));
                taskSubProofBOList.add(taskSubProofBO);
            }
            responseForSubProof.setCurrentNum(requestForProof.getCurrentNum());
            responseForSubProof.setPageSize(requestForProof.getPageSize());
            responseForSubProof.setTotalNum(pageInfo.getTotal());
            responseForSubProof.setRowList(taskSubProofBOList);
        } else {
            List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectList(wrapper);
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOList) {
                TaskSubProofBO taskSubProofBO = new TaskSubProofBO();
                BeanUtils.copyProperties(taskSubProofPO, taskSubProofBO);
                //获取完税凭证任务状态
                taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,taskSubProofBO.getStatus()));
                taskSubProofBOList.add(taskSubProofBO);
            }
            responseForSubProof.setRowList(taskSubProofBOList);
        }
        return responseForSubProof;
    }

    /**
     * 根据子任务ID复制相关数据
     *
     * @param taskSubProofId
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copyProofInfoBySubId(long taskSubProofId) {
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
        for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
            taskSubProofDetailPO.setId(null);
            taskSubProofDetailPO.setTaskSubProofId(taskSubProofPO.getId());
            taskSubProofDetailPO.setCreatedTime(new Date());
            taskSubProofDetailPO.setModifiedTime(new Date());
            taskSubProofDetailMapper.insert(taskSubProofDetailPO);
        }
        //重新统计复制的完税凭证任务人数
        baseMapper.updateSubHeadcountById(taskSubProofPO.getId());
        taskMainProofMapper.updateMainHeadcountById(taskMainProofPO.getId());
    }

    /**
     * 多表查询完税凭证子任务
     *
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForSubProof querySubProofInfoByTaskType(RequestForProof requestForProof) {
        ResponseForSubProof responseForSubProof = new ResponseForSubProof();
        TaskSubProofBO taskSubProofBO = new TaskSubProofBO();
        BeanUtils.copyProperties(requestForProof, taskSubProofBO);
        //如果有个税期间条件，将字符型的个税期间转成日期型
        if (StrKit.notBlank(requestForProof.getPeriod())) {
            taskSubProofBO.setPeriod(DateTimeKit.parse(requestForProof.getPeriod(), DateTimeKit.NORM_DATE_PATTERN));
        }
        Page<TaskSubProofBO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
        List<TaskSubProofBO> taskSubProofBOList = baseMapper.querySubProofInfoByTaskType(pageInfo, taskSubProofBO);
        pageInfo.setRecords(taskSubProofBOList);
        //获取完税凭证任务状态中文名
        for(TaskSubProofBO bo: taskSubProofBOList){
            bo.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,bo.getStatus()));
        }
        responseForSubProof.setRowList(taskSubProofBOList);
        responseForSubProof.setCurrentNum(requestForProof.getCurrentNum());
        responseForSubProof.setPageSize(requestForProof.getPageSize());
        responseForSubProof.setTotalNum(pageInfo.getTotal());
        return responseForSubProof;
    }

    /**
     * 合并完税凭证子任务
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void combineTaskProofByRes(RequestForProof requestForProof) {
        List<TaskSubProofPO> taskSubProofPOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        //未合并的id集合
        List<Long> unCombinedIds = new ArrayList<>();
        //如果完税凭证子任务数组不为空则查询数组内ID的任务信息
        if (requestForProof.getSubProofIds() != null && !"".equals(requestForProof.getSubProofIds())) {
            taskSubProofPOList = baseMapper.querySubTaskProofBySubIds(requestForProof.getSubProofIds());
        }
        if (taskSubProofPOList.size() > 0) {
            //总人数
            int headcount = 0;
            //中方人数
            int chineseNum = 0;
            //外方人数
            int foreignerNum = 0;
            //计算总人数，中方人数，外方人数
            for (TaskSubProofPO taskSubProofPO : taskSubProofPOList) {
                //如果总人数为null,默认为0 )
                if (taskSubProofPO.getHeadcount() != null && !"".equals(taskSubProofPO.getHeadcount())) {
                    headcount += taskSubProofPO.getHeadcount();
                } else {
                    headcount += 0;
                }
                //如果中方人数为null，默认为0
                if (taskSubProofPO.getChineseNum() != null && !"".equals(taskSubProofPO.getChineseNum())) {
                    chineseNum += taskSubProofPO.getChineseNum();
                } else {
                    chineseNum += 0;
                }
                //如果外放人数为null,默认为0
                if (taskSubProofPO.getForeignerNum() != null && !"".equals(taskSubProofPO.getForeignerNum())) {
                    foreignerNum += taskSubProofPO.getForeignerNum();
                } else {
                    foreignerNum += 0;
                }
                //判断是不是合并后的任务
                if (taskSubProofPO.getTaskMainProofId() == null || "".equals(taskSubProofPO.getTaskMainProofId())) {
                    sbCombinedParams.append(taskSubProofPO.getId() + ",");
                } else {
                    unCombinedIds.add(taskSubProofPO.getId());
                }
            }
            TaskSubProofPO newTaskSubProof = new TaskSubProofPO();
            String dateTimeStr = new SimpleDateFormat("yyyyMMddHHmmssSSS").format(new Date());
            newTaskSubProof.setTaskNo("CTAX" + dateTimeStr);
            newTaskSubProof.setDeclareAccount(taskSubProofPOList.get(0).getDeclareAccount());
            newTaskSubProof.setPeriod(taskSubProofPOList.get(0).getPeriod());
            newTaskSubProof.setHeadcount(headcount);
            newTaskSubProof.setChineseNum(chineseNum);
            newTaskSubProof.setForeignerNum(foreignerNum);
            newTaskSubProof.setStatus(taskSubProofPOList.get(0).getStatus());
            newTaskSubProof.setCreatedBy(requestForProof.getModifiedBy());
            newTaskSubProof.setModifiedBy(requestForProof.getModifiedBy());
            newTaskSubProof.setTaskType(taskSubProofPOList.get(0).getTaskType());
            newTaskSubProof.setCombined(true);
            //新增完税凭证子任务
            baseMapper.insert(newTaskSubProof);
            if (sbCombinedParams.length() > 0) {
                String str = sbCombinedParams.substring(0, sbCombinedParams.length() - 1);
                //根据合并id获取子任务ID集合
                List<Long> combinedSubIds = baseMapper.querySubIdsByCombinedIds(str);
                if (combinedSubIds.size() > 0) {
                    unCombinedIds.addAll(combinedSubIds);
                }
                //将合并的id置为不可用
                baseMapper.updateSubIdsByCombinedIds(str);
            }
            //修改完税凭证子任务
            baseMapper.updateSubTaskProofBySubIds(newTaskSubProof.getId(), unCombinedIds, requestForProof.getModifiedBy());
        }
    }

    /**
     * 拆分合并的数据(即：将合并数据置为无效状态,并置空原子任务的合并ID)
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void splitTaskProofByRes(RequestForProof requestForProof) {
        if (requestForProof.getId() != null && !"".equals(requestForProof.getId())) {
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            taskSubProofPO.setId(requestForProof.getId());
            taskSubProofPO.setModifiedBy(requestForProof.getModifiedBy());
            taskSubProofPO.setModifiedTime(new Date());
            taskSubProofPO.setActive(false);
            //将合并的任务置为失效状态
            baseMapper.updateById(taskSubProofPO);
            //根据合并的ID将原子任务的合并ID置为空
            baseMapper.updateSubTaskProofSubId(taskSubProofPO);
        }
    }

    /**
     * 批量完成完税凭证子任务
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void completeTaskProofByRes(RequestForProof requestForProof) {
        if (requestForProof.getSubProofIds() != null && !"".equals(requestForProof.getSubProofIds())) {
            //修改完税凭证子任务合并任务ID为空的状态为完成
            baseMapper.updateTaskProofStatusByIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            //修改完税凭证子任务合并任务ID在子任务数组ID的状态为完成
            baseMapper.updateTaskProofStatusBySubIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
        }
    }

    /**
     * 批量退回完税凭证子任务
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void rejectTaskProofByRes(RequestForProof requestForProof) {
        if (requestForProof.getSubProofIds() != null && !"".equals(requestForProof.getSubProofIds())) {
            //修改完税凭证子任务合并任务ID为空的状态为退回
            baseMapper.updateTaskProofStatusByIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            //修改完税凭证子任务合并任务ID在子任务数组ID的状态为退回
            baseMapper.updateTaskProofStatusBySubIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
        }
    }

    /**
     * 批量失效完税凭证子任务
     *
     * @param requestForProof
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void invalidTaskProofByRes(RequestForProof requestForProof) {
            if (requestForProof.getSubProofIds() != null && !"".equals(requestForProof.getSubProofIds())) {
                //修改完税凭证子任务合并任务ID为空的状态为失效
                baseMapper.updateTaskProofStatusByIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
                //修改完税凭证子任务合并任务ID在子任务数组ID的状态为失效
                baseMapper.updateTaskProofStatusBySubIds(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy());
            }

    }

    /**
     * 根据子任务ID查询子任务详细信息
     *
     * @param subProofId
     * @return
     */
    @Override
    public TaskSubProofBO queryApplyDetailsBySubId(long subProofId) {
        TaskSubProofBO taskSubProofBO = baseMapper.queryApplyDetailsBySubId(subProofId);
        taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.VOUCHER_STATUS,taskSubProofBO.getStatus()));
        return taskSubProofBO;
    }

    /**
     * 根据子任务ID分页查询完税凭证子任务申请明细
     *
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForSubProofDetail queryTaskSubProofDetail(RequestForProof requestForProof) {
        //返回的ResponseForSubDetail对象
        ResponseForSubProofDetail responseForSubProofDetail = new ResponseForSubProofDetail();
        //返回的ResponseForSubDetail对象中的结果集
        List<TaskSubProofDetailPO> taskSubProofDetailPOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        sbCombinedParams.append(requestForProof.getId());
        //根据子任务ID判断该子任务是不是合并任务
        List<Long> longList = baseMapper.querySubIdsByCombinedIds(sbCombinedParams.toString());
        //如果集合大小为0,则该任务不是合并任务，顾将子任务ID放到集合中
        if (longList.size() < 1) {
            longList.add(requestForProof.getId());
        }
        //根据子任务ID分页查询完税凭证申报明细
        Page<TaskSubProofDetailPO> page = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
        taskSubProofDetailPOList = taskSubProofDetailMapper.queryApplyDetailsBySubIdsAndEmp(page, longList, requestForProof.getEmployeeNo(), requestForProof.getEmployeeName());
        page.setRecords(taskSubProofDetailPOList);
        //获取证件类型中文和所得项目中文名
        for(TaskSubProofDetailPO p: taskSubProofDetailPOList){
            p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
            p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
        }
        responseForSubProofDetail.setRowList(taskSubProofDetailPOList);
        responseForSubProofDetail.setCurrentNum(requestForProof.getCurrentNum());
        responseForSubProofDetail.setPageSize(requestForProof.getPageSize());
        responseForSubProofDetail.setTotalNum(page.getTotal());

        return responseForSubProofDetail;
    }
}