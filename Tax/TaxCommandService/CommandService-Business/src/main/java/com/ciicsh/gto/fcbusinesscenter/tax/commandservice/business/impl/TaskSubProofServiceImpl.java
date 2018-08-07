package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.basicdataservice.api.CityServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CityDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchAccountService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubProofService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubProofMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubDeclareDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskSubProofBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForSubProofDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author yuantongqing on 2017/12/12
 */
@Service
public class TaskSubProofServiceImpl extends ServiceImpl<TaskSubProofMapper, TaskSubProofPO> implements TaskSubProofService {

    @Autowired
    private TaskMainProofMapper taskMainProofMapper;

    @Autowired
    private TaskSubProofDetailMapper taskSubProofDetailMapper;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubProofDetailServiceImpl taskSubProofDetailService;

    @Autowired
    private CalculationBatchAccountService calculationBatchAccountService;

    @Autowired
    private CityServiceProxy cityServiceProxy;

    @Autowired
    public TaskNoService taskNoService;
    private List<TaskSubProofPO> taskSubProofPOS;

    /**
     * 根据完税主任务ID查询其下完税子任务
     *
     * @param taskMainProofId
     * @return
     */
    @Override
    public List<TaskSubProofPO> queryTaskSubProofByMainId(long taskMainProofId) {
        //构造查询条件
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofPO());
        //主任务ID
        wrapper.and(" task_main_proof_id = {0}", taskMainProofId);
        //是否可用
        wrapper.and("is_active = {0} ", true);
        //修改时间排序
        wrapper.orderBy("modified_time", false);
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
            wrapper.and("id = {0}", requestForProof.getId());
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
                taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofBO.getStatus()));
                if(taskSubProofBO.getCityCode() != null && !"".equals(taskSubProofBO.getCityCode())){
                    //设置城市
                    CityDTO cityDTO = cityServiceProxy.selectByCityCode(taskSubProofBO.getCityCode());
                    taskSubProofBO.setCity(cityDTO == null ? "" : cityDTO.getCityName());
                }
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
                taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofBO.getStatus()));
                if(taskSubProofBO.getCityCode() != null && !"".equals(taskSubProofBO.getCityCode())){
                    //设置城市
                    CityDTO cityDTO = cityServiceProxy.selectByCityCode(taskSubProofBO.getCityCode());
                    taskSubProofBO.setCity(cityDTO == null ? "" : cityDTO.getCityName());
                }
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
     * @param modifiedBy
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void copyProofInfoBySubId(long taskSubProofId, String modifiedBy) {
        //根据子任务ID查询子任务信息
        TaskSubProofPO taskSubProofPO = baseMapper.selectById(taskSubProofId);
        //根据主任务ID查询主任务信息
        TaskMainProofPO taskMainProofPO = taskMainProofMapper.selectById(taskSubProofPO.getTaskMainProofId());
        //复制新的主任务
        taskMainProofPO.setId(null);
        //设置任务编号
        taskMainProofPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_MAIN_PROOF));
        //设置任务状态为草稿
        taskMainProofPO.setStatus("00");
        taskMainProofMapper.insert(taskMainProofPO);

        //插入新的子任务
        taskSubProofPO.setId(null);
        //设置主任务ID
        taskSubProofPO.setTaskMainProofId(taskMainProofPO.getId());
        //设置任务编号
        taskSubProofPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_PROOF));
        //设置任务状态为草稿
        taskSubProofPO.setStatus("00");
        baseMapper.insert(taskSubProofPO);

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofDetailPO());
        wrapper.and("task_sub_proof_id = {0}", taskSubProofId);
        wrapper.and("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);
        //根据子任务ID查询相关申报明细，并重新插入
        List<TaskSubProofDetailPO> taskSubProofDetailPOList = taskSubProofDetailMapper.selectList(wrapper);
        for (TaskSubProofDetailPO taskSubProofDetailPO : taskSubProofDetailPOList) {
            taskSubProofDetailPO.setId(null);
            taskSubProofDetailPO.setTaskSubProofId(taskSubProofPO.getId());
            taskSubProofDetailMapper.insert(taskSubProofDetailPO);
        }
        //重新统计复制的完税凭证任务人数
        baseMapper.updateSubHeadcountById(taskSubProofPO.getId(), modifiedBy, LocalDateTime.now());
        taskMainProofMapper.updateMainHeadcountById(taskMainProofPO.getId(), modifiedBy, LocalDateTime.now());
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
            taskSubProofBO.setPeriod(LocalDate.parse(requestForProof.getPeriod()));
        }
        Page<TaskSubProofBO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
        List<TaskSubProofBO> taskSubProofBOList = baseMapper.querySubProofInfoByTaskType(pageInfo, taskSubProofBO);
        pageInfo.setRecords(taskSubProofBOList);
        //获取完税凭证任务状态中文名
        for (TaskSubProofBO bo : taskSubProofBOList) {
            bo.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, bo.getStatus()));
            if(bo.getCityCode() != null && !"".equals(bo.getCityCode())){
                //设置城市
                CityDTO cityDTO = cityServiceProxy.selectByCityCode(bo.getCityCode());
                bo.setCity(cityDTO == null ? "" : cityDTO.getCityName());
            }
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
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubProofPO());
            wrapper.isNull("task_sub_proof_id");
            wrapper.and("is_active = {0} ", true);
            wrapper.in("id", requestForProof.getSubProofIds());
            wrapper.orderBy("modified_time", false);
            wrapper.orderBy("created_time", false);
            taskSubProofPOList = baseMapper.selectList(wrapper);
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
            //设置任务编号
            newTaskSubProof.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_PROOF));
            //设置申报账户
            newTaskSubProof.setDeclareAccount(taskSubProofPOList.get(0).getDeclareAccount());
            //设置个税期间
            newTaskSubProof.setPeriod(taskSubProofPOList.get(0).getPeriod());
            //设置总人数
            newTaskSubProof.setHeadcount(headcount);
            //设置中方人数
            newTaskSubProof.setChineseNum(chineseNum);
            //设置外方人数
            newTaskSubProof.setForeignerNum(foreignerNum);
            //设置任务状态
            newTaskSubProof.setStatus(taskSubProofPOList.get(0).getStatus());
            //设置任务类型
            newTaskSubProof.setTaskType(taskSubProofPOList.get(0).getTaskType());
            //设置是否为合并任务
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
                TaskSubProofPO taskSubProofPOCombine = new TaskSubProofPO();
                taskSubProofPOCombine.setActive(false);
                EntityWrapper wrapperPO = new EntityWrapper();
                wrapperPO.setEntity(new TaskSubProofPO());
                wrapperPO.in(" id ", str);
                baseMapper.update(taskSubProofPOCombine, wrapperPO);

            }
            //修改完税凭证子任务
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            //子任务ID
            taskSubProofPO.setTaskSubProofId(newTaskSubProof.getId());
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubProofPO());
            wrapper.and("status = {0}", "01");
            wrapper.and("is_active = {0}", true);
            //将list转为数组
            Long[] ids = unCombinedIds.toArray(new Long[unCombinedIds.size()]);
            wrapper.in("id", ids);
            baseMapper.update(taskSubProofPO, wrapper);

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
            TaskSubProofPO taskSubProofPOCombine = new TaskSubProofPO();
            //主键ID
            taskSubProofPOCombine.setId(requestForProof.getId());
            //是否可用
            taskSubProofPOCombine.setActive(false);
            //将合并的任务置为失效状态
            baseMapper.updateById(taskSubProofPOCombine);

            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            //设置合并后的任务ID为空
            taskSubProofPO.setTaskSubProofId(null);
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new TaskSubProofPO());
            wrapper.and("is_active = {0}", true);
            wrapper.and("task_sub_proof_id = {0}", requestForProof.getId());
            //根据合并的ID将原子任务的合并ID置为空
            baseMapper.update(taskSubProofPO, wrapper);

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
            //修改完税凭证子任务状态
            updateTaskProofStatus(requestForProof);
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
        //修改完税凭证子任务状态
        updateTaskProofStatus(requestForProof);
    }

    /**
     * 修改完税凭证子任务状态
     *
     * @param requestForProof
     */
    private void updateTaskProofStatus(RequestForProof requestForProof) {
        if (requestForProof.getSubProofIds() != null && !"".equals(requestForProof.getSubProofIds())) {
            TaskSubProofPO taskSubProofPO = new TaskSubProofPO();
            //任务状态
            taskSubProofPO.setStatus(requestForProof.getStatus());

            EntityWrapper wrapperId = new EntityWrapper();
            wrapperId.setEntity(new TaskSubProofPO());
            wrapperId.in("id", requestForProof.getSubProofIds());
            wrapperId.and("is_active = {0}", true);
            //修改完税凭证子任务合并任务ID为空的状态为退回
            baseMapper.update(taskSubProofPO, wrapperId);
            //修改完税凭证子任务合并任务ID在子任务数组ID的状态
            baseMapper.updateTaskProofStatus(requestForProof.getSubProofIds(), requestForProof.getStatus(), requestForProof.getModifiedBy(), LocalDateTime.now());
            //获取选择的子任务集合
            List<TaskSubProofPO> taskSubProofPOList = baseMapper.selectList(wrapperId);
            //获取合并子任务集合
            List<TaskSubProofPO> taskSubProofPOSMerge = taskSubProofPOList.stream().filter(x -> x.getCombined()).collect(Collectors.toList());
            if(taskSubProofPOSMerge.size() > 0){
                List<Long> mergeSubIds  = taskSubProofPOSMerge.stream().map(y -> y.getId()).collect(Collectors.toList());
                EntityWrapper wrapperMerge = new EntityWrapper();
                wrapperMerge.setEntity(new TaskSubProofPO());
                wrapperMerge.in("task_sub_proof_id", mergeSubIds);
                wrapperMerge.and("is_active = {0}", true);
                List<TaskSubProofPO> taskSubProofPOUnmergeList = baseMapper.selectList(wrapperMerge);
                //去除合并子任务集合
                taskSubProofPOList = taskSubProofPOList.stream().filter(item -> !taskSubProofPOSMerge.contains(item)).collect(Collectors.toList());
                taskSubProofPOList.addAll(taskSubProofPOUnmergeList);
            }
            //筛选中人工的完税凭证子任务集合
            List<TaskSubProofPO> artificialTaskSubProofPOList = taskSubProofPOList.stream().filter(item -> "02".equals(item.getTaskType())).collect(Collectors.toList());
            if(artificialTaskSubProofPOList.size() > 0) {
                List<Long> mainIdList = artificialTaskSubProofPOList.stream().collect(Collectors.groupingBy(TaskSubProofPO::getTaskMainProofId)).entrySet().stream()
                        .map(x -> x.getKey()).collect(Collectors.toList());
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.setEntity(new TaskSubProofPO());
                wrapper.in("task_main_proof_id", mainIdList);
                wrapper.and("is_active = {0}", true);
                //获取主任务下所有子任务信息
                List<TaskSubProofPO> taskSubProofPOS = baseMapper.selectList(wrapper);
                Map<Long, List<TaskSubProofPO>> taskSubProofMap = taskSubProofPOS.stream()
                        .collect(Collectors.groupingBy(TaskSubProofPO::getTaskMainProofId));
                mainIdList.forEach(mainId -> {
                    //根据主任务ID获取主任务信息
                    TaskMainProofPO taskMainProofPO = taskMainProofMapper.selectById(mainId);
                    //判断主任务下的子任务
                    List<TaskSubProofPO> taskSubProofPOSS = taskSubProofMap.get(mainId);
                    List<TaskSubProofPO> taskSubProofPOSByStatus = taskSubProofPOSS.stream().filter(taskSubProofPO1 -> requestForProof.getStatus().equals(taskSubProofPO1.getStatus())).collect(Collectors.toList());
                    //任务状态
                    String status = taskMainProofPO.getStatus();
                    //任务状态:03:退回；04;已完成；06:部分完成；07;部分退回
                    if ("04".equals(requestForProof.getStatus())) {
                        status = taskSubProofPOSS.size() == taskSubProofPOSByStatus.size() ? "04" : ("07".equals(status) ? "07" : "06");
                    } else if ("03".equals(requestForProof.getStatus())) {
                        status = taskSubProofPOSS.size() == taskSubProofPOSByStatus.size() ? "03" : "07";
                    }
                    taskMainProofPO.setStatus(status);
                    //修改完税凭证主任务状态
                    taskMainProofMapper.updateById(taskMainProofPO);
                });
            }
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
        //修改完税凭证子任务状态
        updateTaskProofStatus(requestForProof);

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
        taskSubProofBO.setStatusName(EnumUtil.getMessage(EnumUtil.TASK_STATUS, taskSubProofBO.getStatus()));
        if(taskSubProofBO.getCityCode() != null && !"".equals(taskSubProofBO.getCityCode())){
            //设置城市
            CityDTO cityDTO = cityServiceProxy.selectByCityCode(taskSubProofBO.getCityCode());
            taskSubProofBO.setCity(cityDTO == null ? "" : cityDTO.getCityName());
        }
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
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofDetailPO());
        Long[] ids = longList.toArray(new Long[longList.size()]);
        //雇员编号模糊查询条件
        if (StrKit.notBlank(requestForProof.getEmployeeNo())) {
            wrapper.like("employee_no", requestForProof.getEmployeeNo());
        }
        //雇员名称模糊查询条件
        if (StrKit.notBlank(requestForProof.getEmployeeName())) {
            wrapper.like("employee_name", requestForProof.getEmployeeName());
        }
        wrapper.in("task_sub_proof_id", ids);
        wrapper.andNew("is_active = {0}", true);
        wrapper.orderBy("modified_time", false);
        //根据子任务ID分页查询完税凭证申报明细
        Page<TaskSubProofDetailPO> page = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
        taskSubProofDetailPOList = taskSubProofDetailMapper.selectPage(page, wrapper);
        page.setRecords(taskSubProofDetailPOList);
        //获取证件类型中文和所得项目中文名
        for (TaskSubProofDetailPO p : taskSubProofDetailPOList) {
            p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE, p.getIdType()));
            p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT, p.getIncomeSubject()));
        }
        responseForSubProofDetail.setRowList(taskSubProofDetailPOList);
        responseForSubProofDetail.setCurrentNum(requestForProof.getCurrentNum());
        responseForSubProofDetail.setPageSize(requestForProof.getPageSize());
        responseForSubProofDetail.setTotalNum(page.getTotal());

        return responseForSubProofDetail;
    }

    /**
     * 根据完税凭证子任务ID查询申请记录
     *
     * @param subProofId
     * @return
     */
    @Override
    public List<TaskSubProofDetailPO> querySubProofDetailList(Long subProofId) {
        List<TaskSubProofDetailPO> taskSubProofDetailPOList = new ArrayList<>();
        StringBuffer sbCombinedParams = new StringBuffer();
        sbCombinedParams.append(subProofId);
        //根据子任务ID判断该子任务是不是合并任务
        List<Long> longList = baseMapper.querySubIdsByCombinedIds(subProofId.toString());
        if (longList.size() < 1) {
            longList.add(subProofId);
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new TaskSubProofDetailPO());
        Long[] ids = longList.toArray(new Long[longList.size()]);
        wrapper.in("task_sub_proof_id", ids);
        wrapper.and("is_active = {0}", true);
        taskSubProofDetailPOList = taskSubProofDetailMapper.selectList(wrapper);
        return taskSubProofDetailPOList;
    }

    /**
     * 申报完成自动生成完税凭证任务
     *
     * @param
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createTaskSubProof(List<Long> taskSubDeclareIds) {


        if (taskSubDeclareIds != null) {

            for (Long taskSubDeclareId : taskSubDeclareIds) {

                //申报子任务
                TaskSubDeclarePO taskSubDeclarePO = this.taskSubDeclareService.selectById(taskSubDeclareId);

                //申报子任务明细
                List<TaskSubDeclareDetailBO> taskSubDeclareDetailPOs = this.baseMapper.querySubDeclareDetailsForProof(taskSubDeclareId);

                //完税凭证子任务
                TaskSubProofPO taskSubProofPO = null;
                //完税凭证子任务明细
                List<TaskSubProofDetailPO> taskSubProofDetailPOs = null;

                for (TaskSubDeclareDetailBO taskSubDeclareDetailBO : taskSubDeclareDetailPOs) {

                    //如果有完税凭证服务则创建完税凭证明细
                    if (taskSubDeclareDetailBO.getProof() != null && taskSubDeclareDetailBO.getProof()) {

                        if (taskSubProofDetailPOs == null) {

                            taskSubProofDetailPOs = new ArrayList<>();
                        }

                        CalculationBatchAccountPO calculationBatchAccountPO = calculationBatchAccountService.getCalculationBatchAccountInfoByAccountNo(taskSubDeclarePO.getDeclareAccount());
                        //新建完税凭证子任务
                        if (taskSubProofPO == null) {
                            taskSubProofPO = new TaskSubProofPO();
                            taskSubProofPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_PROOF));
                            taskSubProofPO.setDeclareAccount(taskSubDeclarePO.getDeclareAccount());
                            taskSubProofPO.setPeriod(taskSubDeclarePO.getPeriod());
                            taskSubProofPO.setStatus("01");
                            taskSubProofPO.setTaskType("01");//任务类型：01自动
                            taskSubProofPO.setTaskSubDeclareId(taskSubDeclareId);
                            taskSubProofPO.setManagerNo(taskSubDeclarePO.getManagerNo());
                            taskSubProofPO.setManagerName(taskSubDeclarePO.getManagerName());
                            //申报账户中文名称
                            taskSubProofPO.setDeclareAccountName(taskSubDeclarePO.getDeclareAccountName());
                            taskSubProofPO.setCityCode(calculationBatchAccountPO.getCityCode());
                            taskSubProofPO.setStation(calculationBatchAccountPO.getStation());
                            this.baseMapper.insert(taskSubProofPO);
                        }

                        //新建完税凭证子任务明细
                        TaskSubProofDetailPO taskSubProofDetailPO = new TaskSubProofDetailPO();
                        taskSubProofDetailPO.setTaskSubProofId(taskSubProofPO.getId());
                        taskSubProofDetailPO.setEmployeeNo(taskSubDeclareDetailBO.getEmployeeNo());
                        taskSubProofDetailPO.setEmployeeName(taskSubDeclareDetailBO.getEmployeeName());
                        taskSubProofDetailPO.setIdType(taskSubDeclareDetailBO.getIdType());
                        taskSubProofDetailPO.setIdNo(taskSubDeclareDetailBO.getIdNo());
                        taskSubProofDetailPO.setDeclareAccount(taskSubDeclareDetailBO.getDeclareAccount());
                        taskSubProofDetailPO.setIncomeSubject(taskSubDeclareDetailBO.getIncomeSubject());
                        taskSubProofDetailPO.setIncomeStart(taskSubDeclareDetailBO.getPeriod());
                        taskSubProofDetailPO.setIncomeForTax(taskSubDeclareDetailBO.getIncomeForTax());
                        taskSubProofDetailPO.setWithholdedAmount(taskSubDeclareDetailBO.getTaxAmount());
                        taskSubProofDetailPO.setDeclareAccountName(taskSubDeclarePO.getDeclareAccountName());

                        taskSubProofDetailPOs.add(taskSubProofDetailPO);
                    }

                }

                if (taskSubProofDetailPOs != null) {

                    //批量新增明细
                    taskSubProofDetailService.insertBatch(taskSubProofDetailPOs);

                    int headcount = taskSubProofDetailPOs.size();
                    taskSubProofPO.setHeadcount(headcount);
                    Long chineseNum = taskSubProofDetailPOs.stream().filter(x -> "1".equals(x.getIdType())).collect(Collectors.counting());
                    if (chineseNum != null) {

                        taskSubProofPO.setChineseNum(chineseNum.intValue());
                        taskSubProofPO.setForeignerNum(headcount - chineseNum.intValue());
                    } else {

                        taskSubProofPO.setChineseNum(0);
                        taskSubProofPO.setForeignerNum(headcount);
                    }

                    //更新完税凭证子任务：总人数、中方人数、外放人数
                    this.baseMapper.updateById(taskSubProofPO);

                }
            }
        }

    }
}