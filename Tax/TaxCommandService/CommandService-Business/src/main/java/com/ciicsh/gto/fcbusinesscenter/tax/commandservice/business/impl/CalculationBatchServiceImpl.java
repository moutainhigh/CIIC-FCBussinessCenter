package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.DroolsService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.WorkflowService;
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
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.IncomeSubject;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.collector.CollectorsUtil;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchServiceImpl extends ServiceImpl<CalculationBatchMapper, CalculationBatchPO> implements CalculationBatchService, Serializable {

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

    @Autowired
    private TaskNoService taskNoService;

    @Autowired
    private WorkflowService workflowService;

    @Autowired
    private DroolsService droolsService;

    @Override
    public ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch) {

        List<CalculationBatchPO> calculationBatchPOList;
        ResponseForCalBatch responseForCalBatch = new ResponseForCalBatch();
//        EntityWrapper wrapper = new EntityWrapper();
//        wrapper.setEntity(new CalculationBatchPO());
        //查询条件
        EntityWrapper wrapper = new EntityWrapper();
        //管理方名称
        Optional.ofNullable(requestForCalBatch.getManagerNos()).ifPresent(managerNos -> {
            wrapper.in("manager_no",managerNos);
        });
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.like("batch_no",requestForCalBatch.getBatchNo());
        }
        wrapper.orderBy("created_time",false);
        Page page = new Page(requestForCalBatch.getCurrentNum(),requestForCalBatch.getPageSize());
        calculationBatchPOList = baseMapper.selectPage(page,wrapper);//baseMapper.selectPage(page, wrapper);

        for(CalculationBatchPO p: calculationBatchPOList){
            //查询由当前批次创建的任务
            List<TaskMainPO> tps = baseMapper.queryTaskMainsByCalBatch(p.getId());
            //组合任务编号
            String sb = tps.stream().map(TaskMainPO::getTaskNo).collect(Collectors.joining(","));
            p.setTaskNos(sb);

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

            Map<String, Object> params = new HashMap<>();
            params.put("batchId",requestForEmployees.getCalculationBatchId());
            params.put("employeeNo",requestForEmployees.getEmployeeNo());
            params.put("employeeName",requestForEmployees.getEmployeeName());
            params.put("idType",requestForEmployees.getIdType());
            params.put("idNo",requestForEmployees.getIdNo());

            Page<CalculationBatchDetailBO> page = new Page<CalculationBatchDetailBO>(requestForEmployees.getCurrentNum(), requestForEmployees.getPageSize());

            List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryCalculationBatchDetails(page,params);

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
                        taskSubDeclarePO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_DECLARE));
                        taskSubDeclarePO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                        taskSubDeclarePO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubDeclarePO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubDeclarePO.setManagerName(p.getManagerName());//管理方名称
                        taskSubDeclarePO.setStatus("00");//草稿
                        taskSubDeclarePO.setAccountType(cbd.getAccountType());//账户类型
                        taskSubDeclarePO.setAreaType(cbd.getAreaType());//区域类型
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
                        taskSubMoneyPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_TRANSFER));
                        taskSubMoneyPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                        taskSubMoneyPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubMoneyPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubMoneyPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubMoneyPO.setStatus("00");//草稿
                        taskSubMoneyPO.setAreaType(cbd.getAreaType());//区域类型
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
                        taskSubPaymentPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_PAYMENT));
                        taskSubPaymentPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                        taskSubPaymentPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubPaymentPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubPaymentPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubPaymentPO.setStatus("00");//草稿
                        taskSubPaymentPO.setAreaType(cbd.getAreaType());//区域类型
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
                        taskSubSupplierPO.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_SUB_SUPPLIER));
                        taskSubSupplierPO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                        taskSubSupplierPO.setPeriod(cbd.getPeriod());//个税期间
                        taskSubSupplierPO.setManagerNo(p.getManagerNo());//管理方编号
                        taskSubSupplierPO.setManagerName(p.getManagerName());//管理方名称
                        taskSubSupplierPO.setStatus("00");//草稿
                        taskSubSupplierPO.setAccountType(cbd.getAccountType());//账户类型
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
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, CollectorsUtil.summingBigDecimal(TaskSubDeclareDetailPO::getTaxReal)));

                //总人数
                Map<Long, Long> mapheadcount = declareTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = declareTaskDetail.stream().filter(x -> "1".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubDeclareDetailPO::getTaskSubDeclareId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubDeclareId : mapTaxAmount.keySet()) {

                    TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
                    taskSubDeclarePO.setId(taskSubDeclareId);
                    taskSubDeclarePO.setTaxAmount(mapTaxAmount.get(taskSubDeclareId));//个税总金额
                    taskSubDeclarePO.setHeadcount(mapheadcount.get(taskSubDeclareId)!=null?mapheadcount.get(taskSubDeclareId).intValue():0);//总人数
                    taskSubDeclarePO.setChineseNum(mapChineseNum.get(taskSubDeclareId)!=null?mapChineseNum.get(taskSubDeclareId).intValue():0);//中方总人数
                    taskSubDeclarePO.setForeignerNum(taskSubDeclarePO.getHeadcount() - taskSubDeclarePO.getChineseNum());//外方总人数
                    taskSubDeclareService.updateById(taskSubDeclarePO);
                }


            }
            //批量新增划款子任务明细
            if (moneyTaskDetail != null && moneyTaskDetail.size() > 0) {
                taskSubMoneyDetailService.insertBatch(moneyTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = moneyTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, CollectorsUtil.summingBigDecimal(TaskSubMoneyDetailPO::getTaxReal)));

                //总人数
                Map<Long, Long> mapheadcount = moneyTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = moneyTaskDetail.stream().filter(x -> "1".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubMoneyDetailPO::getTaskSubMoneyId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubMoneyId : mapTaxAmount.keySet()) {

                    TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
                    taskSubMoneyPO.setId(taskSubMoneyId);
                    taskSubMoneyPO.setTaxAmount(mapTaxAmount.get(taskSubMoneyId));//个税总金额
                    taskSubMoneyPO.setHeadcount(mapheadcount.get(taskSubMoneyId)!=null?mapheadcount.get(taskSubMoneyId).intValue():0);//总人数
                    taskSubMoneyPO.setChineseNum(mapChineseNum.get(taskSubMoneyId)!=null?mapChineseNum.get(taskSubMoneyId).intValue():0);//中方总人数
                    taskSubMoneyPO.setForeignerNum(taskSubMoneyPO.getHeadcount() - taskSubMoneyPO.getChineseNum());//外方总人数
                    taskSubMoneyService.updateById(taskSubMoneyPO);
                }
            }
            //批量新增缴纳子任务明细
            if (paymentTaskDetail != null && paymentTaskDetail.size() > 0) {
                taskSubPaymentDetailService.insertBatch(paymentTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = paymentTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, CollectorsUtil.summingBigDecimal(TaskSubPaymentDetailPO::getTaxReal)));

                //总人数
                Map<Long, Long> mapheadcount = paymentTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = paymentTaskDetail.stream().filter(x -> "1".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubPaymentDetailPO::getTaskSubPaymentId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubPaymentId : mapTaxAmount.keySet()) {

                    TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
                    taskSubPaymentPO.setId(taskSubPaymentId);
                    taskSubPaymentPO.setTaxAmount(mapTaxAmount.get(taskSubPaymentId));//个税总金额
                    taskSubPaymentPO.setHeadcount(mapheadcount.get(taskSubPaymentId)!=null?mapheadcount.get(taskSubPaymentId).intValue():0);//总人数
                    taskSubPaymentPO.setChineseNum(mapChineseNum.get(taskSubPaymentId)!=null?mapChineseNum.get(taskSubPaymentId).intValue():0);//中方总人数
                    taskSubPaymentPO.setForeignerNum(taskSubPaymentPO.getHeadcount() - taskSubPaymentPO.getChineseNum());//外方总人数
                    taskSubPaymentService.updateById(taskSubPaymentPO);
                }
            }
            //批量新增供应商处理子任务明细
            if (supportTaskDetail != null && supportTaskDetail.size() > 0) {
                taskSubSupplierDetailService.insertBatch(supportTaskDetail);

                //子任务个税总金额
                Map<Long, BigDecimal> mapTaxAmount = supportTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, CollectorsUtil.summingBigDecimal(TaskSubSupplierDetailPO::getTaxReal)));

                //总人数
                Map<Long, Long> mapheadcount = supportTaskDetail.stream()
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, Collectors.counting()));

                //中方总人数
                Map<Long, Long> mapChineseNum = supportTaskDetail.stream().filter(x -> "1".equals(x.getIdType()))
                        .collect(Collectors.groupingBy(TaskSubSupplierDetailPO::getTaskSubSupplierId, Collectors.counting()));

                //更新子任务：个税总金额、总人数、中方人数、外放人数
                for (Long taskSubSupplierId : mapTaxAmount.keySet()) {

                    TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
                    taskSubSupplierPO.setId(taskSubSupplierId);
                    taskSubSupplierPO.setTaxAmount(mapTaxAmount.get(taskSubSupplierId));//个税总金额
                    taskSubSupplierPO.setHeadcount(mapheadcount.get(taskSubSupplierId)!=null?mapheadcount.get(taskSubSupplierId).intValue():0);//总人数
                    taskSubSupplierPO.setChineseNum(mapChineseNum.get(taskSubSupplierId)!=null?mapChineseNum.get(taskSubSupplierId).intValue():0);//中方总人数
                    taskSubSupplierPO.setForeignerNum(taskSubSupplierPO.getHeadcount() - taskSubSupplierPO.getChineseNum());//外方总人数
                    taskSubSupplierService.updateById(taskSubSupplierPO);
                }
            }

            //启动工作流
//            this.workflowService.startProcess(p.getId().toString(),WorkflowService.Process.fc_tax_main_task_audit,null);
    }


    /**
     * 创建主任务(主任务、明细)
     */
    @Transactional(rollbackFor = Exception.class)
    public TaskMainPO createTaskMain(Map<String,String> mapParams, String[] batchIds,String[] batchNos) {

        //新增主任务
        TaskMainPO p = new TaskMainPO();
        p.setTaskNo(taskNoService.getTaskNo(TaskNoService.TASK_MAIN));
        p.setManagerName(mapParams.get("ManagerName"));
        p.setManagerNo(mapParams.get("ManagerNo"));
        p.setStatus("00");//草稿
        taskMainService.insert(p);

        int i = 0;
        for(String batchId : batchIds){

            CalculationBatchPO calculationBatchPO = baseMapper.selectById(new Long(batchId));

            CalculationBatchTaskMainPO calculationBatchTaskMainPO = new CalculationBatchTaskMainPO();
            calculationBatchTaskMainPO.setTaskMainId(p.getId());//主任务id
            calculationBatchTaskMainPO.setCalBatchId(new Long(batchId));//计算批次id
            calculationBatchTaskMainPO.setBatchNo(batchNos[i]);//计算批次号
            calculationBatchTaskMainPO.setVersionNo(calculationBatchPO.getVersionNo());//批次版本号
            //新增批次和主任务的关联记录
            calculationBatchTaskMainService.insert(calculationBatchTaskMainPO);
            i++;
        }

        //组装查询条件
        Long[] cbatchIds = (Long[]) ConvertUtils.convert(batchIds,Long.class);

        List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryCalculationBatchDetailsByBatchIds(cbatchIds);
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

            //税种为正常薪资才合并
            if((entry.getKey().endsWith(IncomeSubject.NORMALSALARY.getCode()) || entry.getKey().endsWith(IncomeSubject.FOREIGNNORMALSALARY.getCode()))
                    && entry.getValue().size()>1){

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
                taskMainDetailPO.setDeduction(entry.getValue().get(0).getDeduction());//免税额
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
                BigDecimal donation = new BigDecimal(0);//准予扣除的捐赠额
                BigDecimal deductTakeoff = new BigDecimal(0);//允许扣除的税费
                BigDecimal otherDeductions = new BigDecimal(0);//其它扣除
                BigDecimal businessHealthInsurance = new BigDecimal(0);//商业保险
                BigDecimal deductRetirementInsurance=new BigDecimal(0);//基本养老保险费（税前扣除项目）
                BigDecimal deductMedicalInsurance=new BigDecimal(0);//基本医疗保险费（税前扣除项目）
                BigDecimal deductDlenessInsurance=new BigDecimal(0);//失业保险费（税前扣除项目）
                BigDecimal deductHouseFund=new BigDecimal(0);//住房公积金（税前扣除项目）
                BigDecimal dutyFreeAllowance = new BigDecimal(0);//免税津贴
                BigDecimal annuity = new BigDecimal(0);//企业年金个人部分
                BigDecimal incomeDutyfree = new BigDecimal(0);//免税所得
                BigDecimal taxReal=new BigDecimal(0);//税金
                BigDecimal incomeTotal=new BigDecimal(0);//收入额
                BigDecimal preTaxAggregate=new BigDecimal(0);//税前合计

                List<TaskMainDetailPO> tps = entry.getValue();
                //计算合并后的各项值
                for(TaskMainDetailPO tp : tps){

                    taskMainDetailIds.add(tp.getId());

                    //参与倒推计算的各项合计
                    donation = donation.add(tp.getDonation()==null?BigDecimal.ZERO:tp.getDonation());//准予扣除的捐赠额合计
                    deductTakeoff = deductTakeoff.add(tp.getDeductTakeoff()==null?BigDecimal.ZERO:tp.getDeductTakeoff());//允许扣除的税费合计
                    otherDeductions = otherDeductions.add(tp.getDeductOther()==null?BigDecimal.ZERO:tp.getDeductOther());//其它扣除合计
                    businessHealthInsurance = businessHealthInsurance.add(tp.getBusinessHealthInsurance()==null?BigDecimal.ZERO:tp.getBusinessHealthInsurance());//商业保险合计
                    deductRetirementInsurance = deductRetirementInsurance.add(tp.getDeductRetirementInsurance()==null?BigDecimal.ZERO:tp.getDeductRetirementInsurance());//基本养老保险费（税前扣除项目）合计
                    deductMedicalInsurance = deductMedicalInsurance.add(tp.getDeductMedicalInsurance()==null?BigDecimal.ZERO:tp.getDeductMedicalInsurance());//基本医疗保险费（税前扣除项目）合计
                    deductDlenessInsurance = deductDlenessInsurance.add(tp.getDeductDlenessInsurance()==null?BigDecimal.ZERO:tp.getDeductDlenessInsurance());//失业保险费（税前扣除项目）合计
                    deductHouseFund = deductHouseFund.add(tp.getDeductHouseFund()==null?BigDecimal.ZERO:tp.getDeductHouseFund());//住房公积金（税前扣除项目）合计
                    dutyFreeAllowance = dutyFreeAllowance.add(tp.getDutyFreeAllowance()==null?BigDecimal.ZERO:tp.getDutyFreeAllowance());//免税津贴合计
                    annuity = annuity.add(tp.getAnnuity()==null?BigDecimal.ZERO:tp.getAnnuity());//企业年金个人部分合计
                    incomeDutyfree = incomeDutyfree.add(tp.getIncomeDutyfree()==null?BigDecimal.ZERO:tp.getIncomeDutyfree());//免税所得合计
                    taxReal = taxReal.add(tp.getTaxReal()==null?BigDecimal.ZERO:tp.getTaxReal());//税金合计
                    incomeTotal = incomeTotal.add(tp.getIncomeTotal()==null?BigDecimal.ZERO:tp.getIncomeTotal());//收入额合计
                    preTaxAggregate = preTaxAggregate.add(tp.getPreTaxAggregate()==null?BigDecimal.ZERO:tp.getPreTaxAggregate());//税前合计
                }

                //计算收入额
                HashMap<String,BigDecimal> m = new HashMap<>();
                m.put("taxReal",taxReal);//税金
                m.put("preTaxAggregate",preTaxAggregate);//税前合计
                m.put("deduction",taskMainDetailPO.getDeduction());//免抵税额
                m.put("donation",donation);//准予扣除的捐赠额
                m.put("deductTakeoff",deductTakeoff);//允许扣除的税费
                m.put("otherDeductions",otherDeductions);//其它扣除
                m.put("businessHealthInsurance",businessHealthInsurance);//商业保险
                m.put("deductHouseFund",deductHouseFund);//住房公积金
                m.put("deductDlenessInsurance",deductDlenessInsurance);//失业保险费
                m.put("deductMedicalInsurance",deductMedicalInsurance);//基本医疗保险费
                m.put("deductRetirementInsurance",deductRetirementInsurance);//基本养老保险费
                m.put("dutyFreeAllowance",dutyFreeAllowance);//免税津贴
                m.put("annuity",annuity);//企业年金个人部分
                m.put("incomeDutyfree",incomeDutyfree);//免税所得
                incomeTotal = droolsService.incomeTotal(m);//收入额

                taskMainDetailPO.setIncomeTotal(incomeTotal);
                taskMainDetailPO.setDonation(donation);
                taskMainDetailPO.setDeductTakeoff(deductTakeoff);
                taskMainDetailPO.setDeductOther(otherDeductions);
                taskMainDetailPO.setBusinessHealthInsurance(businessHealthInsurance);
                taskMainDetailPO.setDeductRetirementInsurance(deductRetirementInsurance);
                taskMainDetailPO.setDeductMedicalInsurance(deductMedicalInsurance);
                taskMainDetailPO.setDeductDlenessInsurance(deductDlenessInsurance);
                taskMainDetailPO.setDeductHouseFund(deductHouseFund);
                taskMainDetailPO.setDutyFreeAllowance(dutyFreeAllowance);
                taskMainDetailPO.setAnnuity(annuity);
                taskMainDetailPO.setIncomeDutyfree(incomeDutyfree);
                taskMainDetailPO.setTaxReal(taxReal);
                taskMainDetailPO.setPreTaxAggregate(preTaxAggregate);
                //更新合并后数据值
                this.taskMainDetailService.updateById(taskMainDetailPO);

                EntityWrapper wrapper = new EntityWrapper();
                wrapper.in("id",taskMainDetailIds);
                TaskMainDetailPO tpo = new TaskMainDetailPO();
                tpo.setTaskMainDetailId(taskMainDetailPO.getId());
                    this.taskMainDetailService.update(tpo,wrapper);//更新合并的明细

                TaskMainPO tmp = new TaskMainPO();
                tmp.setId(taskMainId);
                tmp.setHasCombined(true);
                this.taskMainMapper.updateById(tmp);//更新主任务信息，标记任务存在合并的明细

            }
        }
    }
}
