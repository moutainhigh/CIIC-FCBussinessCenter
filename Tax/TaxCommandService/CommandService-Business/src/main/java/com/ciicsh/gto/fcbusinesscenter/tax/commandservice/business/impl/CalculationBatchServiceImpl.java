package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.TaskNoService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.TaskMainDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.collector.CollectorsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchServiceImpl extends ServiceImpl<CalculationBatchMapper, CalculationBatchPO> implements CalculationBatchService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchServiceImpl.class);

    @Autowired
    private CalculationBatchDetailServiceImpl calculationBatchDetailServiceImpl;

    @Autowired
    private TaskMainServiceImpl taskMainService;

    @Autowired
    private TaskMainDetailServiceImpl taskMainDetailService;

    @Autowired
    private CalculationBatchTaskMainServiceImpl calculationBatchTaskMainService;

    @Autowired
    private TaskSubDeclareServiceImpl taskSubDeclareService;

    @Autowired
    private TaskSubDeclareDetailServiceImpl taskSubDeclareDetailService;

    @Autowired
    private TaskSubMoneyServiceImpl taskSubMoneyService;

    @Autowired
    private TaskSubMoneyDetailServiceImpl taskSubMoneyDetailService;

    @Autowired
    private TaskSubPaymentServiceImpl taskSubPaymentService;

    @Autowired
    private TaskSubPaymentDetailServiceImpl taskSubPaymentDetailService;

    @Autowired
    private TaskSubSupplierServiceImpl taskSubSupplierService;

    @Autowired
    private TaskSubSupplierDetailServiceImpl taskSubSupplierDetailService;

    @Autowired
    private TaskMainMapper taskMainMapper;

    @Override
    public ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch) {

        List<CalculationBatchPO> calculationBatchPOList;
        ResponseForCalBatch responseForCalBatch = new ResponseForCalBatch();
//        EntityWrapper wrapper = new EntityWrapper();
//        wrapper.setEntity(new CalculationBatchPO());
        //查询条件
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.like("batch_no",requestForCalBatch.getBatchNo());
        }
        wrapper.orderBy("created_time",false);
        Page page = new Page(requestForCalBatch.getCurrentNum(),requestForCalBatch.getPageSize());
        calculationBatchPOList = baseMapper.selectPage(page,wrapper);//baseMapper.selectPage(page, wrapper);


        for(CalculationBatchPO p: calculationBatchPOList){
            //状态中文转化
            p.setStatusName(EnumUtil.getMessage(EnumUtil.BATCH_NO_STATUS,p.getStatus()));
            List<TaskMainPO> tps = baseMapper.queryTaskMainsByCalBatch(p.getId());
            //查询由当前批次创建的任务
            StringBuilder sb = new StringBuilder();
            int k =0;
            for(TaskMainPO tp : tps){
                if(k>0){
                    sb.append(",");
                }
                sb.append(tp.getTaskNo());
                k++;
            }
            p.setTaskNos(sb.toString());

        }
        responseForCalBatch.setRowList(calculationBatchPOList);
        responseForCalBatch.setCurrentNum(requestForCalBatch.getCurrentNum());
        responseForCalBatch.setTotalNum(page.getTotal());

        return responseForCalBatch;
    }

    /**
     * 查询批次明细
     * @param requestForEmployees
     * @return
     */
    @Override
    public ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees) {
        {
            ResponseForCalBatchDetail responseForCalBatchDetail = new ResponseForCalBatchDetail();
            //EntityWrapper wrapper = new EntityWrapper();
            //wrapper.setEntity(new CalculationBatchDetailPO());
            //批次主表id
            /*if (requestForEmployees.getBatchid() != null) {
                wrapper.andNew("calculation_batch_id = {0}", requestForEmployees.getBatchid());
            }else{
                return responseForCalBatchDetail;
            }
            //证件类型
            if (StrKit.isNotEmpty(requestForEmployees.getIdType())) {
                wrapper.andNew("id_type = {0}", requestForEmployees.getIdType());
            }
            //证件号
            if (StrKit.isNotEmpty(requestForEmployees.getIdNo())) {
                wrapper.andNew("id_no = {0}", requestForEmployees.getIdNo());
            }
            //雇员编号
            if (StrKit.isNotEmpty(requestForEmployees.getEmployeeNo())) {
                wrapper.andNew("employee_no = {0}", requestForEmployees.getEmployeeNo());
            }
            //雇员姓名
            if (StrKit.isNotEmpty(requestForEmployees.getEmployeeName())) {
                wrapper.andNew("employee_name = {0}", requestForEmployees.getEmployeeName());
            }
            //查询已申报的
            wrapper.orderBy("id", true);*/

            //Page<CalculationBatchDetailBO> page = new Page<CalculationBatchDetailBO>(0,10);

            Page<CalculationBatchDetailBO> page = new Page<CalculationBatchDetailBO>(requestForEmployees.getCurrentNum(), requestForEmployees.getPageSize());

            CalculationBatchDetailBO calculationBatchDetailBO = new CalculationBatchDetailBO();
            BeanUtils.copyProperties(requestForEmployees, calculationBatchDetailBO);
            Long[] batchIds = new Long[1];
            batchIds[0] = calculationBatchDetailBO.getCalculationBatchId();
            List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryCalculationBatchDetails(page,batchIds);

            /*for(CalculationBatchDetailBO p : calculationBatchDetailBOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));//证件类型中文显示
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));//个税所得项目中文显示
            }*/

            responseForCalBatchDetail.setRowList(calculationBatchDetailBOList);
            responseForCalBatchDetail.setCurrentNum(requestForEmployees.getCurrentNum());
            responseForCalBatchDetail.setPageSize(requestForEmployees.getPageSize());
            responseForCalBatchDetail.setTotalNum(page.getTotal());

            return responseForCalBatchDetail;
        }
    }

    /**
     * 根据批次创建任务
     * @param requestForMainTask
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createMainTask(RequestForTaskMain requestForMainTask) {

        String[] batchIds = requestForMainTask.getBatchIds();
        String[] batchNos = requestForMainTask.getBatchNos();

        Map<String,String> mapParams = new HashMap<>();
        mapParams.put("ManagerName",requestForMainTask.getManagerName());
        mapParams.put("ManagerNo",requestForMainTask.getManagerNo());

        //新建主任务
        TaskMainPO p = this.createTaskMain(mapParams,batchIds,batchNos);

        //主任务明细合并处理
        this.merge(p.getId());

        Long taskmainId = p.getId();

            List<TaskMainDetailBO> taskMainDetailBOList = taskMainMapper.queryTaskMainDetails(taskmainId);

            //已创建的申报子任务(K:申报账户+个税期间，V:申报子任务id)
            HashMap<String, Long> declareTask = new HashMap<String, Long>();
            //申报子任务明细，用于批量新增
            List<TaskSubDeclareDetailPO> declareTaskDetail = new ArrayList<TaskSubDeclareDetailPO>();
            //已创建的划款子任务(K:缴纳账户+个税期间，V:划款子任务id)
            HashMap<String, Long> moneyTask = new HashMap<String, Long>();
            //划款子任务明细，用于批量新增
            List<TaskSubMoneyDetailPO> moneyTaskDetail = new ArrayList<TaskSubMoneyDetailPO>();
            //已创建的缴纳子任务(K:缴纳账户+个税期间，V:缴纳子任务id)
            HashMap<String, Long> paymentTask = new HashMap<String, Long>();
            //缴纳子任务明细，用于批量新增
            List<TaskSubPaymentDetailPO> paymentTaskDetail = new ArrayList<TaskSubPaymentDetailPO>();
            //已创建的供应商处理子任务(K:供应商+申报账户+个税期间，V:供应商处理子任务id)
            HashMap<String, Long> supportTask = new HashMap<String, Long>();
            //供应商处理子任务明细，用于批量新增
            List<TaskSubSupplierDetailPO> supportTaskDetail = new ArrayList<TaskSubSupplierDetailPO>();

            //新建子任务和子任务明细
            for (TaskMainDetailBO cbd : taskMainDetailBOList) {

                if (cbd.getDeclare() != null && cbd.getDeclare()) {

                    //申报子任务关键字(申报账户，所得期间)
                    String key = cbd.getDeclareAccount() + DateTimeFormatter.ofPattern("yyyy-MM").format(cbd.getPeriod());

                    TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
                    BeanUtils.copyProperties(cbd, taskSubDeclareDetailPO);//copy计算批次明细信息
                    taskSubDeclareDetailPO.setTaskMainDetailId(cbd.getId());//计算批次明细id

                    //申报子任务未创建
                    if (declareTask.get(key) != null) {
                        taskSubDeclareDetailPO.setTaskSubDeclareId(declareTask.get(key));//申报子任务id
                        //申报子任务已创建
                    } else {
                        TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
                        taskSubDeclarePO.setTaskMainId(p.getId());
                        taskSubDeclarePO.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_DECLARE));
                        taskSubDeclarePO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                        taskSubDeclarePO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubDeclarePO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubDeclarePO.setManagerName(p.getManagerName());//管理方名称
                        taskSubDeclarePO.setStatus("00");//草稿
                        //新增申报子任务
                        taskSubDeclareService.insert(taskSubDeclarePO);
                        //记录申报子任务
                        declareTask.put(key, taskSubDeclarePO.getId());
                        taskSubDeclareDetailPO.setTaskSubDeclareId(taskSubDeclarePO.getId());//申报子任务id
                    }

                    //待新增的申报子任务明细
                    declareTaskDetail.add(taskSubDeclareDetailPO);

                }
                if (cbd.getTranfer() != null && cbd.getTranfer()) {

                    //划款子任务关键字(缴纳账户，所得期间)
                    String key = cbd.getPayAccount() + DateTimeFormatter.ofPattern("yyyy-MM").format(cbd.getPeriod());

                    TaskSubMoneyDetailPO taskSubMoneyDetailPO = new TaskSubMoneyDetailPO();
                    BeanUtils.copyProperties(cbd, taskSubMoneyDetailPO);//copy计算批次明细信息
                    taskSubMoneyDetailPO.setTaskMainDetailId(cbd.getId());//计算批次明细id

                    //划款子任务未创建
                    if (moneyTask.get(key) != null) {
                        taskSubMoneyDetailPO.setTaskSubMoneyId(moneyTask.get(key));//划款子任务id
                        //划款子任务已创建
                    } else {
                        TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
                        taskSubMoneyPO.setTaskMainId(p.getId());
                        taskSubMoneyPO.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_TRANSFER));
                        taskSubMoneyPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                        taskSubMoneyPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubMoneyPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubMoneyPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubMoneyPO.setStatus("00");//草稿
                        //新增划款子任务
                        taskSubMoneyService.insert(taskSubMoneyPO);
                        //记录划款子任务
                        moneyTask.put(key, taskSubMoneyPO.getId());
                        taskSubMoneyDetailPO.setTaskSubMoneyId(taskSubMoneyPO.getId());//划款子任务id
                    }

                    //待新增的划款子任务明细
                    moneyTaskDetail.add(taskSubMoneyDetailPO);
                }
                if (cbd.getPay() != null && cbd.getPay()) {
                    //缴纳子任务关键字(缴纳账户，所得期间)
                    String key = cbd.getPayAccount() + DateTimeFormatter.ofPattern("yyyy-MM").format(cbd.getPeriod());

                    TaskSubPaymentDetailPO taskSubPaymentDetailPO = new TaskSubPaymentDetailPO();
                    BeanUtils.copyProperties(cbd, taskSubPaymentDetailPO);//copy计算批次明细信息
                    taskSubPaymentDetailPO.setTaskMainDetailId(cbd.getId());//计算批次明细id

                    //缴纳子任务未创建
                    if (paymentTask.get(key) != null) {
                        taskSubPaymentDetailPO.setTaskSubPaymentId(paymentTask.get(key));//缴纳子任务id
                        //缴纳子任务已创建
                    } else {
                        TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
                        taskSubPaymentPO.setTaskMainId(p.getId());
                        taskSubPaymentPO.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_PAYMENT));
                        taskSubPaymentPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                        taskSubPaymentPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubPaymentPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubPaymentPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubPaymentPO.setStatus("00");//草稿
                        //新增划款子任务
                        taskSubPaymentService.insert(taskSubPaymentPO);
                        //记录缴纳子任务已创建
                        paymentTask.put(key, taskSubPaymentPO.getId());
                        taskSubPaymentDetailPO.setTaskSubPaymentId(taskSubPaymentPO.getId());//缴纳子任务id
                    }

                    //待新增的缴纳子任务明细
                    paymentTaskDetail.add(taskSubPaymentDetailPO);
                }

                //供应商处理
                if (cbd.getSupport() != null && cbd.getSupport()) {

                    //供应商处理子任务关键字(供应商，申报账户，所得期间)
                    String key = cbd.getSupportName() + cbd.getDeclareAccount() + DateTimeFormatter.ofPattern("yyyy-MM").format(cbd.getPeriod());

                    TaskSubSupplierDetailPO taskSubSupplierDetailPO = new TaskSubSupplierDetailPO();
                    BeanUtils.copyProperties(cbd, taskSubSupplierDetailPO);//copy计算批次明细信息
                    taskSubSupplierDetailPO.setTaskMainDetailId(cbd.getId());//计算批次明细id

                    //供应商处理子任务未创建
                    if (supportTask.get(key) != null) {
                        taskSubSupplierDetailPO.setTaskSubSupplierId(supportTask.get(key));//供应商处理子任务id
                        //供应商处理子任务已创建
                    } else {
                        TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
                        taskSubSupplierPO.setTaskMainId(p.getId());
                        taskSubSupplierPO.setSupportName(cbd.getSupportName());//供应商名称
                        taskSubSupplierPO.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_SUB_SUPPLIER));
                        taskSubSupplierPO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                        taskSubSupplierPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubSupplierPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubSupplierPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubSupplierPO.setStatus("00");//草稿
                        //新增供应商处理子任务
                        taskSubSupplierService.insert(taskSubSupplierPO);
                        //记录供应商处理子任务已创建
                        supportTask.put(key, taskSubSupplierPO.getId());
                        taskSubSupplierDetailPO.setTaskSubSupplierId(taskSubSupplierPO.getId());//供应商处理子任务id
                    }

                    //待新增的供应商处理子任务明细
                    supportTaskDetail.add(taskSubSupplierDetailPO);
                }
            }

            //批量新增申报子任务明细
            if (declareTaskDetail != null && declareTaskDetail.size() > 0) {
                taskSubDeclareDetailService.insertBatch(declareTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = declareTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, CollectorsUtil.summingBigDecimal(TaskSubDeclareDetailPO::getTaxAmount)));

                //总人数
                Map<Long, Long> mapheadcount = declareTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = declareTaskDetail.stream().filter(x -> "01".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubDeclareId : mapTaxAmount.keySet()) {

                    TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
                    taskSubDeclarePO.setId(taskSubDeclareId);
                    taskSubDeclarePO.setTaxAmount(mapTaxAmount.get(taskSubDeclareId));//个税总金额
                    taskSubDeclarePO.setHeadcount(mapheadcount.get(taskSubDeclareId).intValue());//总人数
                    taskSubDeclarePO.setChineseNum(mapChineseNum.get(taskSubDeclareId).intValue());//中方总人数
                    taskSubDeclarePO.setForeignerNum(mapheadcount.get(taskSubDeclareId).intValue() - mapChineseNum.get(taskSubDeclareId).intValue());//外方总人数
                    taskSubDeclareService.updateById(taskSubDeclarePO);
                }


            }
            //批量新增划款子任务明细
            if (moneyTaskDetail != null && moneyTaskDetail.size() > 0) {
                taskSubMoneyDetailService.insertBatch(moneyTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = moneyTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, CollectorsUtil.summingBigDecimal(TaskSubMoneyDetailPO::getTaxAmount)));

                //总人数
                Map<Long, Long> mapheadcount = moneyTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = moneyTaskDetail.stream().filter(x -> "01".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubMoneyId : mapTaxAmount.keySet()) {

                    TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
                    taskSubMoneyPO.setId(taskSubMoneyId);
                    taskSubMoneyPO.setTaxAmount(mapTaxAmount.get(taskSubMoneyId));//个税总金额
                    taskSubMoneyPO.setHeadcount(mapheadcount.get(taskSubMoneyId).intValue());//总人数
                    taskSubMoneyPO.setChineseNum(mapChineseNum.get(taskSubMoneyId).intValue());//中方总人数
                    taskSubMoneyPO.setForeignerNum(mapheadcount.get(taskSubMoneyId).intValue() - mapChineseNum.get(taskSubMoneyId).intValue());//外方总人数
                    taskSubMoneyService.updateById(taskSubMoneyPO);
                }
            }
            //批量新增缴纳子任务明细
            if (paymentTaskDetail != null && paymentTaskDetail.size() > 0) {
                taskSubPaymentDetailService.insertBatch(paymentTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = paymentTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, CollectorsUtil.summingBigDecimal(TaskSubPaymentDetailPO::getTaxAmount)));

                //总人数
                Map<Long, Long> mapheadcount = paymentTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = paymentTaskDetail.stream().filter(x -> "01".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubPaymentId : mapTaxAmount.keySet()) {

                    TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
                    taskSubPaymentPO.setId(taskSubPaymentId);
                    taskSubPaymentPO.setTaxAmount(mapTaxAmount.get(taskSubPaymentId));//个税总金额
                    taskSubPaymentPO.setHeadcount(mapheadcount.get(taskSubPaymentId).intValue());//总人数
                    taskSubPaymentPO.setChineseNum(mapChineseNum.get(taskSubPaymentId).intValue());//中方总人数
                    taskSubPaymentPO.setForeignerNum(mapheadcount.get(taskSubPaymentId).intValue() - mapChineseNum.get(taskSubPaymentId).intValue());//外方总人数
                    taskSubPaymentService.updateById(taskSubPaymentPO);
                }
            }
            //批量新增供应商处理子任务明细
            if (supportTaskDetail != null && supportTaskDetail.size() > 0) {
                taskSubSupplierDetailService.insertBatch(supportTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = supportTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, CollectorsUtil.summingBigDecimal(TaskSubSupplierDetailPO::getTaxAmount)));

                //总人数
                Map<Long, Long> mapheadcount = supportTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = supportTaskDetail.stream().filter(x -> "01".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubSupplierId : mapTaxAmount.keySet()) {

                    TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
                    taskSubSupplierPO.setId(taskSubSupplierId);
                    taskSubSupplierPO.setTaxAmount(mapTaxAmount.get(taskSubSupplierId));//个税总金额
                    taskSubSupplierPO.setHeadcount(mapheadcount.get(taskSubSupplierId).intValue());//总人数
                    taskSubSupplierPO.setChineseNum(mapChineseNum.get(taskSubSupplierId).intValue());//中方总人数
                    taskSubSupplierPO.setForeignerNum(mapheadcount.get(taskSubSupplierId).intValue() - mapChineseNum.get(taskSubSupplierId).intValue());//外方总人数
                    taskSubSupplierService.updateById(taskSubSupplierPO);
                }
            }
    }


    /**
     * 创建主任务(主任务、明细)
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskMainPO createTaskMain(Map<String,String> mapParams, String[] batchIds,String[] batchNos) {

        //新增主任务
        TaskMainPO p = new TaskMainPO();
        p.setTaskNo(TaskNoService.getTaskNo(TaskNoService.TASK_MAIN));
        p.setManagerName(mapParams.get("ManagerName"));
        p.setManagerNo(mapParams.get("ManagerNo"));
        p.setStatus("00");//草稿
        taskMainService.insert(p);

        int i = 0;
        for(String batchId : batchIds){

            CalculationBatchTaskMainPO calculationBatchTaskMainPO = new CalculationBatchTaskMainPO();
            calculationBatchTaskMainPO.setTaskMainId(p.getId());//主任务id
            calculationBatchTaskMainPO.setCalBatchId(new Long(batchId));//计算批次id
            calculationBatchTaskMainPO.setBatchNo(batchNos[i]);//计算批次号
            //新增批次和主任务的关联记录
            calculationBatchTaskMainService.insert(calculationBatchTaskMainPO);
            i++;
        }

        //组装查询条件
        Long[] cbatchIds = new Long[batchIds.length];
        int k = 0;
        for(String bid : batchIds){
            cbatchIds[k] = new Long(bid);
            k++;
        }

        List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryCalculationBatchDetails(cbatchIds);
        List<TaskMainDetailPO> taskMainDetailPOList = new ArrayList<>();
        for(CalculationBatchDetailBO cb : calculationBatchDetailBOList){
            TaskMainDetailPO taskMainDetailPO = new TaskMainDetailPO();
            BeanUtils.copyProperties(cb,taskMainDetailPO);
            taskMainDetailPO.setTaskMainId(p.getId());//主任务id
            taskMainDetailPO.setCalculationBatchDetailId(cb.getId());//批次明细id
            taskMainDetailPOList.add(taskMainDetailPO);
        }

        if(taskMainDetailPOList.size() > 0){
            taskMainDetailService.insertBatch(taskMainDetailPOList);
        }
        return p;//主任务
    }

    /**
     * 主任务明细合并处理
     */
    @Transactional(rollbackFor = Exception.class)
    public void merge(Long taskMainId) {

        List<TaskMainDetailPO>  taskMainDetailPOList = new ArrayList<>();
        Map<String, Object> columnMap = new HashMap<>();
        columnMap.put("task_main_id",taskMainId);
        taskMainDetailPOList = taskMainDetailService.selectByMap(columnMap);

        //按照雇员、所得期间、所得项目分组
        Map<String, List<TaskMainDetailPO>> groupbys = taskMainDetailPOList.stream()
                .collect(Collectors.groupingBy(TaskMainDetailPO::groupBys));

        for(Map.Entry<String, List<TaskMainDetailPO>> entry : groupbys.entrySet()){

            if(entry.getValue().size()>1){

                TaskMainDetailPO taskMainDetailPO = new TaskMainDetailPO();
                taskMainDetailPO.setCombined(true);//为合并明细
                taskMainDetailPO.setTaskMainId(entry.getValue().get(0).getTaskMainId());//主任务id
                taskMainDetailPO.setCalculationBatchDetailId(entry.getValue().get(0).getCalculationBatchDetailId());//批次明细id
                taskMainDetailPO.setEmployeeNo(entry.getValue().get(0).getEmployeeNo());//雇员编号
                taskMainDetailPO.setEmployeeName(entry.getValue().get(0).getEmployeeName());//雇员姓名
                taskMainDetailPO.setIdType(entry.getValue().get(0).getIdType());//证件类型
                taskMainDetailPO.setIdNo(entry.getValue().get(0).getIdNo());//证件编号
                taskMainDetailPO.setDeclareAccount(entry.getValue().get(0).getDeclareAccount());//申报账号
                taskMainDetailPO.setPayAccount(entry.getValue().get(0).getPayAccount());//缴纳账号
                taskMainDetailPO.setPeriod(entry.getValue().get(0).getPeriod());//个税期间
                taskMainDetailPO.setIncomeSubject(entry.getValue().get(0).getIncomeSubject());//所得项目
                //新建合并后的明细
                this.taskMainDetailService.insert(taskMainDetailPO);

                List<Long> taskMainDetailIds = new ArrayList<>();

                Long tmId = taskMainId;//主任务id
                String employeeNo = "";//雇员编号
                String employeeName = "";//雇员姓名
                String idType = "";//证件类型
                String idNo = "";//证件编号
                String declareAccount = "";//申报账号
                String payAccount = "";//缴纳账号
                String incomeSubject = "";//所得项目
                BigDecimal incomeTotal=new BigDecimal(0);//收入额
                BigDecimal deductRetirementInsurance=new BigDecimal(0);//基本养老保险费（税前扣除项目）
                BigDecimal deductMedicalInsurance=new BigDecimal(0);//基本医疗保险费（税前扣除项目）
                BigDecimal deductDlenessInsurance=new BigDecimal(0);//失业保险费（税前扣除项目）
                BigDecimal deductHouseFund=new BigDecimal(0);//住房公积金（税前扣除项目）
                BigDecimal deduction=new BigDecimal(0);//减除费用(3500;4800)
                BigDecimal taxAmount=new BigDecimal(0);//应纳税额

                List<TaskMainDetailPO> tps = entry.getValue();
                //计算合并后的各项值
                for(TaskMainDetailPO tp : tps){

                    taskMainDetailIds.add(tp.getId());

                    incomeTotal = incomeTotal.add(tp.getIncomeTotal());
                    deductRetirementInsurance = deductRetirementInsurance.add(tp.getDeductRetirementInsurance());
                    deductMedicalInsurance = deductMedicalInsurance.add(tp.getDeductMedicalInsurance());
                    deductDlenessInsurance = deductDlenessInsurance.add(tp.getDeductDlenessInsurance());
                    deductHouseFund = deductHouseFund.add(tp.getDeductHouseFund());
                    deduction = deduction.add(tp.getDeduction());
                    taxAmount = taxAmount.add(tp.getTaxAmount());
                }

                taskMainDetailPO.setIncomeTotal(incomeTotal);
                taskMainDetailPO.setDeductRetirementInsurance(deductRetirementInsurance);
                taskMainDetailPO.setDeductMedicalInsurance(deductMedicalInsurance);
                taskMainDetailPO.setDeductDlenessInsurance(deductDlenessInsurance);
                taskMainDetailPO.setDeductHouseFund(deductHouseFund);
                taskMainDetailPO.setDeduction(deduction);
                taskMainDetailPO.setTaxAmount(taxAmount);
                //更新合并后数据值
                this.taskMainDetailService.updateById(taskMainDetailPO);

                EntityWrapper wrapper = new EntityWrapper();
                wrapper.in("id",taskMainDetailIds);
                TaskMainDetailPO tpo = new TaskMainDetailPO();
                tpo.setTaskMainDetailId(taskMainDetailPO.getId());
                    this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细

                TaskMainPO tmp = new TaskMainPO();
                tmp.setId(taskMainId);
                tmp.setCombined(true);
                this.taskMainMapper.updateById(tmp);//更新主任务信息，标记任务存在合并的明细

            }
        }
    }
}
