package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.billcenter.fcmodule.api.FcChargeDisposableProxy;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.FcChargeDisposableProxyDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMainDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author wuhua
 */
@Service
public class TaskMainServiceImpl extends ServiceImpl<TaskMainMapper, TaskMainPO> implements TaskMainService, Serializable {

    @Autowired
    private CalculationBatchTaskMainServiceImpl calculationBatchTaskMainService;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubMoneyServiceImpl taskSubMoneyService;

    @Autowired
    private TaskSubPaymentServiceImpl taskSubPaymentService;

    @Autowired
    private TaskSubSupplierServiceImpl taskSubSupplierService;

    /*@Autowired
    private CalculationBatchMapper calculationBatchMapper;*/

    @Autowired
    private TaskMainDetailMapper taskMainDetailMapper;

    @Autowired
    private FcChargeDisposableProxy fcChargeDisposableProxy;

    @Autowired
    private TaskSubMoneyDetailServiceImpl taskSubMoneyDetailService;

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailService;

    @Autowired
    private EmployeeInfoBatchImpl employeeInfoBatch;


    /**
     * 任务管理查询
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMains(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        Optional.ofNullable(requestForTaskMain.getManagerNos()).ifPresent(managerNos -> {
            wrapper.in("manager_no",managerNos);
        });
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        wrapper.orderBy("created_time",false);

        if(StrKit.notBlank(requestForTaskMain.getTabsName())){

            wrapper.in("status", EnumUtil.getMessage(EnumUtil.BUSINESS_STATUS_TYPE,requestForTaskMain.getTabsName().toUpperCase()));
        }

        wrapper.orderBy("created_time",false);
        return this.query(requestForTaskMain,wrapper);
    }

    /**
     * 查询草稿任务
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMainsForDraft(RequestForTaskMain requestForTaskMain) {

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        Optional.ofNullable(requestForTaskMain.getManagerNos()).ifPresent(managerNos -> {
            wrapper.in("manager_no",managerNos);
        });
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.in("status","00,03");
        wrapper.orderBy("created_time",false);
        return this.query(requestForTaskMain,wrapper);
    }

    /**
     * 查询审批中任务
     * @param requestForTaskMain
     * @return
     */
    @Override
    public ResponseForTaskMain queryTaskMainsForCheck(RequestForTaskMain requestForTaskMain) {

        EntityWrapper wrapper = new EntityWrapper();
//        //管理方名称
//        if(StrKit.isNotEmpty(requestForTaskMain.getManagerName())){
//            wrapper.like("manager_name",requestForTaskMain.getManagerName());
//        }
        //管理方名称
        Optional.ofNullable(requestForTaskMain.getManagerNos()).ifPresent(managerNos -> {
            wrapper.in("manager_no",managerNos);
        });
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForTaskMain.getTaskNo())){
            wrapper.like("task_no",requestForTaskMain.getTaskNo());
        }
        //wrapper.like("manager_name","恒大");里
        wrapper.in("status","01");
        wrapper.orderBy("created_time",false);

        return this.query(requestForTaskMain,wrapper);
    }

    private ResponseForTaskMain query(RequestForTaskMain requestForTaskMain , EntityWrapper wrapper){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForTaskMain.getCurrentNum(),requestForTaskMain.getPageSize());
        List<TaskMainPO> taskMainPOList = baseMapper.selectPage(page, wrapper);
        //查询主任务对应的批次号
        for(TaskMainPO p : taskMainPOList){
            Map<String, Object> columnMap = new HashMap<>();
            columnMap.put("task_main_id",p.getId());
            List<CalculationBatchTaskMainPO> l = calculationBatchTaskMainService.selectByMap(columnMap);
            //组合批次号
            String sb = l.stream().map(CalculationBatchTaskMainPO::getBatchNo).collect(Collectors.joining(", "));
            p.setBatchIds(sb);
        }
        responseForTaskMain.setRowList(taskMainPOList);
        responseForTaskMain.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMain.setTotalNum(page.getTotal());

        return responseForTaskMain;
    }

    /**
     * 提交主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain submitTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"01",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }

    /**
     * 审批通过主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain passTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"02",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }
    /**
     * 失效主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain invalidTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"05",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }
    /**
     * 退回主任务
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public ResponseForTaskMain rejectTaskMains(RequestForTaskMain requestForTaskMain){

        ResponseForTaskMain responseForTaskMain = new ResponseForTaskMain();

        String[] taskMainIds = requestForTaskMain.getTaskMainIds();

        this.updateTaskMainsStatus(taskMainIds,"03",requestForTaskMain.getStatus());

        return responseForTaskMain;
    }

    //更新主任务状态
    private void updateTaskMainsStatus(String[] taskMainIds,String status,String[] currentStatus){

        List<TaskMainPO> tps = new ArrayList<>();
        for(String taskMainId : taskMainIds){

            TaskMainPO taskMainPO =  new TaskMainPO();
            taskMainPO.setId(Long.valueOf(taskMainId));
            taskMainPO.setStatus(status);
            tps.add(taskMainPO);

            //更新子任务状态
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.andNew("task_main_id={0}",Long.valueOf(taskMainId));
            //只更新与主任务状态相同的子任务
            wrapper.andNew("status={0}",currentStatus);
            //申报
            TaskSubDeclarePO taskSubDeclarePO =  new TaskSubDeclarePO();
            taskSubDeclarePO.setStatus(status);
            this.taskSubDeclareService.update(taskSubDeclarePO,wrapper);
            //划款
            TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
            taskSubMoneyPO.setStatus(status);
            this.taskSubMoneyService.update(taskSubMoneyPO,wrapper);
            //缴纳
            TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
            taskSubPaymentPO.setStatus(status);
            this.taskSubPaymentService.update(taskSubPaymentPO,wrapper);
            //供应商处理
            TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
            taskSubSupplierPO.setStatus(status);
            this.taskSubSupplierService.update(taskSubSupplierPO,wrapper);

        }

        //更新主任务状态
        this.updateBatchById(tps);
    }

    /**
     * 查询主任务明细
     * @param requestForTaskMain
     * @return
     */
    public ResponseForTaskMainDetail queryTaskMainDetails(RequestForTaskMain requestForTaskMain){

        /*EntityWrapper wrapper = new EntityWrapper();
        wrapper.andNew("task_main_id={0}",requestForTaskMain.getTaskMainId());
        wrapper.isNull("task_main_detail_id");
        wrapper.andNew("is_combined={0}",requestForTaskMain.getIsCombined());*/
        Page<TaskMainDetailPO> page = new Page<TaskMainDetailPO>(requestForTaskMain.getCurrentNum(), requestForTaskMain.getPageSize());

        Map<String,Object> params = new HashMap<>();
        params.put("taskMainId",requestForTaskMain.getTaskMainId());//主任务id
        params.put("isCombined",requestForTaskMain.getIsCombined());//是否为合并明细
        params.put("employeeNo",requestForTaskMain.getEmployeeNo());
        params.put("employeeName",requestForTaskMain.getEmployeeName());
        params.put("idType",requestForTaskMain.getIdType());
        params.put("idNo",requestForTaskMain.getIdNo());

        List<TaskMainDetailBO> taskMainDetailBOs = taskMainDetailMapper.queryTaskMainDetails(page,params);

        ResponseForTaskMainDetail responseForTaskMainDetail = new ResponseForTaskMainDetail();

        responseForTaskMainDetail.setRowList(taskMainDetailBOs);
        responseForTaskMainDetail.setCurrentNum(requestForTaskMain.getCurrentNum());
        responseForTaskMainDetail.setPageSize(requestForTaskMain.getPageSize());
        responseForTaskMainDetail.setTotalNum(page.getTotal());

        return responseForTaskMainDetail;
    }

    /**
     * 更新主任务状态(子任务退回)
     * @param taskMainIds
     * @return
     */
    public void updateTaskMainStatus(Long[] taskMainIds){

        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("id",taskMainIds);
        TaskMainPO tmp = new TaskMainPO();
        tmp.setStatus("03");
        this.update(tmp,wrapper);//更新主任务
    }

    /**
     *
     * @param taskMainIds
     * @param status
     * @return
     */
    public boolean isStatusSame(String[] taskMainIds,String[] status){

        boolean flag = true;

        int i = 0;
        for(String st : status){
            if(st.equals("03")){
                EntityWrapper wrapper = new EntityWrapper();
                wrapper.andNew("task_main_id={0}",taskMainIds[i]);
                wrapper.and("status!='03'");
                int count = this.taskSubDeclareService.selectCount(wrapper);
                if(count>0){
                    return false;
                }else{
                    count = this.taskSubMoneyService.selectCount(wrapper);
                    if(count>0){
                        return false;
                    }else{
                        count = this.taskSubPaymentService.selectCount(wrapper);
                        if(count>0){
                            return false;
                        }else{
                            count = this.taskSubSupplierService.selectCount(wrapper);
                            if(count>0){
                                return false;
                            }
                        }
                    }
                }
            }
        }

        return flag;
    }

    /**
     * 根据主任务di查询主任务信息
     * @param id
     * @return
     */
    @Override
    public TaskMainPO queryTaskMainById(Long id) {
        return baseMapper.selectById(id);
    }

    /**
     * 生成一次性收费账单
     * @param taskMainIds
     */
    private void createOneTimeChargeBillByTaskMainIds(String[] taskMainIds){
        List<FcChargeDisposableProxyDTO> fcChargeDisposableProxyDTOList = new ArrayList<>();
        for(int i = 0 ;i < taskMainIds.length;i++){
            //主任务ID
            Long taskMainId =  Long.valueOf(taskMainIds[i]);
            //根据主任务ID查询划款子任务
            EntityWrapper moneyWrapper = new EntityWrapper();
            moneyWrapper.setEntity(new TaskSubMoneyPO());
            moneyWrapper.andNew("task_main_id = {0} ",taskMainId);
            moneyWrapper.andNew("overdue > 0 ").or(" fine > 0 ");
            List<TaskSubMoneyPO>  taskSubMoneyPOList = taskSubMoneyService.selectList(moneyWrapper);
            for(TaskSubMoneyPO taskSubMoneyPO : taskSubMoneyPOList){
                //划款子任务滞纳金和罚金总和
                BigDecimal totalMoney = taskSubMoneyPO.getOverdue().add(taskSubMoneyPO.getFine());
                //划款子任务总税金
                BigDecimal totalTaxReal = taskSubMoneyPO.getTaxAmount();
                //根据子任务id查询子任务明细
                EntityWrapper moneyDetailWrapper = new EntityWrapper();
                moneyDetailWrapper.setEntity(new TaskSubMoneyDetailPO());
                moneyDetailWrapper.and(" task_sub_money_id = {0} ",taskSubMoneyPO.getId());
                moneyDetailWrapper.and(" is_active = {0} ",true);
                List<TaskSubMoneyDetailPO> taskSubMoneyDetailPOList = taskSubMoneyDetailService.selectList(moneyDetailWrapper);
                //获取主任务明细ID
                List<Long> mainDetailIdsList = taskSubMoneyDetailPOList.stream().map(item -> item.getTaskMainDetailId()).collect(Collectors.toList());
                //根据主任务明细ID查询主任务明细
                EntityWrapper mainDetailWrapper = new EntityWrapper();
                mainDetailWrapper.in("id",mainDetailIdsList).or().in("task_main_detail_id",mainDetailIdsList);
                List<TaskMainDetailPO> taskMainDetailPOList = taskMainDetailService.selectList(mainDetailWrapper);
                //过滤出非合并任务的主任务明细
                List<TaskMainDetailPO> taskMainDetailPOListUnMerge = taskMainDetailPOList.stream().filter(item -> item.getCombined() != true).collect(Collectors.toList());
                //批次明细id集合
                List<Long> calBatchDetailIdsList =  taskMainDetailPOListUnMerge.stream().map(item -> item.getCalculationBatchDetailId()).collect(Collectors.toList());
                //根据批次明细ID查询雇员个税信息
                EntityWrapper employeeWrapper = new EntityWrapper();
                employeeWrapper.setEntity(new EmployeeInfoBatchPO());
                employeeWrapper.in("cal_batch_detail_id",calBatchDetailIdsList);
                List<EmployeeInfoBatchPO> employeeInfoBatchPOList = employeeInfoBatch.selectList(employeeWrapper);
                //根据公司分组雇员
                Map<String,List<EmployeeInfoBatchPO>> companyEmpMap = employeeInfoBatchPOList.stream().collect(groupingBy(EmployeeInfoBatchPO::getCompanyNo));
                for (String key : companyEmpMap.keySet()) {
                    List<EmployeeInfoBatchPO> employeeInfoBatchPOS = companyEmpMap.get(key);
                    //筛选出当前雇员编号集合
                    List<Long> calBatchDetailNoList = employeeInfoBatchPOS.stream().map(item -> item.getCalBatchDetailId()).collect(Collectors.toList());
                    List<TaskMainDetailPO> taskMainDetailPOS = taskMainDetailPOList.stream().filter(item -> calBatchDetailNoList.contains(item.getCalculationBatchDetailId())).collect(Collectors.toList());
                    //收费额
                    BigDecimal chargeAmount = new BigDecimal(0);
                    //雇员税金总和
                    BigDecimal taxRealTotal = taskMainDetailPOS.stream()
                            .map(TaskMainDetailPO::getTaxReal)
                            //使用reduce聚合函数,实现累加器
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
                    chargeAmount = taxRealTotal.divide(totalTaxReal).multiply(totalMoney).setScale(2,BigDecimal.ROUND_HALF_UP);
                    FcChargeDisposableProxyDTO fcChargeDisposableProxyDTO = new FcChargeDisposableProxyDTO();
                    //管理方编号
                    fcChargeDisposableProxyDTO.setManagementId(taskSubMoneyPO.getManagerNo());
                    //管理方名称
                    fcChargeDisposableProxyDTO.setManagementName(taskSubMoneyPO.getManagerName());
                    //收费对象：1-客户；2-雇员
                    fcChargeDisposableProxyDTO.setChargeObject(1);
                    //公司ID
                    fcChargeDisposableProxyDTO.setCompanyId(key);
                    //公司名称
                    fcChargeDisposableProxyDTO.setCompanyName(employeeInfoBatchPOS.size() > 0 ? employeeInfoBatchPOS.get(0).getCompanyName() : "");
                    //业务编码
                    fcChargeDisposableProxyDTO.setBusinessId(taskSubMoneyPO.getTaskNo());
                    //业务类型:(0-手工；1-FC个税)
                    fcChargeDisposableProxyDTO.setBusinessType(1);
                    //实际月份
                    fcChargeDisposableProxyDTO.setActualChargeMonth(taskSubMoneyPO.getPeriod() != null ? DateTimeFormatter.ofPattern("yyyyMM").format(taskSubMoneyPO.getPeriod()) : "");
                    //合同我方（账套）ID(1:AF;2:FC;3:BPO)
                    fcChargeDisposableProxyDTO.setFinanceAccountId(2);
                    //收费金额
                    fcChargeDisposableProxyDTO.setChargeAmount(chargeAmount);
                    //创建方式：0-直接导入;1-页面操作;2-外部接口;
                    fcChargeDisposableProxyDTO.setCreateType(2);
                    //备注
                    fcChargeDisposableProxyDTO.setRemark("个税滞纳金和罚金一次性账单!");
                    fcChargeDisposableProxyDTOList.add(fcChargeDisposableProxyDTO);
                }
            }
        }
        try {
            fcChargeDisposableProxy.batchInsert(fcChargeDisposableProxyDTOList);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
