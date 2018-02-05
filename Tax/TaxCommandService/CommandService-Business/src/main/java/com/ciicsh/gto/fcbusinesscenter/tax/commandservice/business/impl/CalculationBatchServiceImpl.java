package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.*;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForEmployees;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatch;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.DateTimeKit;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchServiceImpl extends ServiceImpl<CalculationBatchMapper, CalculationBatchPO> implements CalculationBatchService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(CalculationBatchServiceImpl.class);

    @Autowired
    private TaskMainServiceImpl taskMainService;

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

    @Override
    public ResponseForCalBatch queryCalculationBatchs(RequestForCalBatch requestForCalBatch) {

        List<CalculationBatchPO> calculationBatchPOList;
        ResponseForCalBatch responseForCalBatch = new ResponseForCalBatch();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchPO());
        //管理方名称
        if(StrKit.isNotEmpty(requestForCalBatch.getManagerName())){
            wrapper.like("manager_name",requestForCalBatch.getManagerName());
        }
        //薪酬计算批次号
        if(StrKit.isNotEmpty(requestForCalBatch.getBatchNo())){
            wrapper.andNew("batch_no={0}",requestForCalBatch.getBatchNo());
        }
        //wrapper.like("manager_name","恒大");
        wrapper.orderBy("created_time",false);
        Page<CalculationBatchPO> page = new Page<CalculationBatchPO>(requestForCalBatch.getCurrentNum(),requestForCalBatch.getPageSize());
        calculationBatchPOList = baseMapper.selectPage(page, wrapper);
        for(CalculationBatchPO p: calculationBatchPOList){
            p.setStatusName(EnumUtil.getMessage(EnumUtil.BATCH_NO_STATUS,p.getStatus()));
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

            for(CalculationBatchDetailBO p : calculationBatchDetailBOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));//证件类型中文显示
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));//个税所得项目中文显示
            }

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

        //新增主任务
        TaskMainPO p = new TaskMainPO();
        p.setTaskNo("SP" + DateTimeKit.format(new Date(),"yyyyMMddHHmmss"));
        p.setManagerName("中智上海");
        p.setManagerNo("CIICSH");
        taskMainService.insert(p);

        String[] batchIds = requestForMainTask.getBatchIds();
        String[] batchNos = requestForMainTask.getBatchNos();
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
        //Page<CalculationBatchDetailBO> page = new Page<CalculationBatchDetailBO>(1, 10000);
        Long[] cbatchIds = new Long[batchIds.length];
        int k = 0;
        for(String bid : batchIds){
            cbatchIds[k] = new Long(bid);
            k++;
        }
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.in("calculation_batch_id",batchIds);
        List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryCalculationBatchDetails(cbatchIds);

        //已创建的申报子任务(K:申报账户+个税期间，V:申报子任务id)
        HashMap<String,Long> declareTask = new HashMap<String,Long>();
        //申报子任务明细，用于批量新增
        List<TaskSubDeclareDetailPO> declareTaskDetail = new ArrayList<TaskSubDeclareDetailPO>();
        //已创建的划款子任务(K:缴纳账户+个税期间，V:划款子任务id)
        HashMap<String,Long> moneyTask = new HashMap<String,Long>();
        //划款子任务明细，用于批量新增
        List<TaskSubMoneyDetailPO> moneyTaskDetail = new ArrayList<TaskSubMoneyDetailPO>();
        //已创建的缴纳子任务(K:缴纳账户+个税期间，V:缴纳子任务id)
        HashMap<String,Long> paymentTask = new HashMap<String,Long>();
        //缴纳子任务明细，用于批量新增
        List<TaskSubPaymentDetailPO> paymentTaskDetail = new ArrayList<TaskSubPaymentDetailPO>();
        //已创建的供应商处理子任务(K:供应商+申报账户+个税期间，V:供应商处理子任务id)
        HashMap<String,Long> supportTask = new HashMap<String,Long>();
        //供应商处理子任务明细，用于批量新增
        List<TaskSubSupplierDetailPO> supportTaskDetail = new ArrayList<TaskSubSupplierDetailPO>();

        //新建子任务和子任务明细
        for(CalculationBatchDetailBO cbd :  calculationBatchDetailBOList){

            if(cbd.getDeclare()!=null && cbd.getDeclare()){

                if(logger.isDebugEnabled()){
                    logger.debug("...有申报服务");
                }
                //申报子任务关键字(申报账户，所得期间)
                String key = cbd.getDeclareAccount()+DateTimeKit.format(cbd.getPeriod(),"YYYY-MM");

                TaskSubDeclareDetailPO taskSubDeclareDetailPO = new TaskSubDeclareDetailPO();
                BeanUtils.copyProperties(cbd,taskSubDeclareDetailPO);//copy计算批次明细信息
                taskSubDeclareDetailPO.setCalculationBatchDetailId(cbd.getId());//计算批次明细id

                //申报子任务未创建
                if(declareTask.get(key)!=null){
                    taskSubDeclareDetailPO.setTaskSubDeclareId(declareTask.get(key));//申报子任务id
                //申报子任务已创建
                }else{
                    TaskSubDeclarePO taskSubDeclarePO = new TaskSubDeclarePO();
                    taskSubDeclarePO.setTaskMainId(p.getId());
                    taskSubDeclarePO.setTaskNo("SPSB" + DateTimeKit.format(new Date(),"yyyyMMddHHmmssSSS"));
                    taskSubDeclarePO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                    taskSubDeclarePO.setPeriod(cbd.getPeriod());//个税期间
                    taskSubDeclarePO.setManagerNo(p.getManagerNo());//管理方编号
                    taskSubDeclarePO.setManagerName(p.getManagerName());//管理方名称
                    //新增申报子任务
                    taskSubDeclareService.insert(taskSubDeclarePO);
                    //记录申报子任务
                    declareTask.put(key,taskSubDeclarePO.getId());
                    taskSubDeclareDetailPO.setTaskSubDeclareId(taskSubDeclarePO.getId());//申报子任务id
                }

                //待新增的申报子任务明细
                declareTaskDetail.add(taskSubDeclareDetailPO);

            }
            if(cbd.getTranfer()!=null && cbd.getTranfer()){

                if(logger.isDebugEnabled()){
                    logger.debug("...有划款服务");
                }
                //划款子任务关键字(缴纳账户，所得期间)
                String key = cbd.getPayAccount()+DateTimeKit.format(cbd.getPeriod(),"YYYY-MM");

                TaskSubMoneyDetailPO taskSubMoneyDetailPO = new TaskSubMoneyDetailPO();
                BeanUtils.copyProperties(cbd,taskSubMoneyDetailPO);//copy计算批次明细信息
                taskSubMoneyDetailPO.setCalculationBatchDetailId(cbd.getId());//计算批次明细id

                //划款子任务未创建
                if(moneyTask.get(key)!=null){
                    taskSubMoneyDetailPO.setTaskSubMoneyId(moneyTask.get(key));//划款子任务id
                //划款子任务已创建
                }else{
                    TaskSubMoneyPO taskSubMoneyPO = new TaskSubMoneyPO();
                    taskSubMoneyPO.setTaskMainId(p.getId());
                    taskSubMoneyPO.setTaskNo("SPHK" + DateTimeKit.format(new Date(),"yyyyMMddHHmmssSSS"));
                    taskSubMoneyPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                    taskSubMoneyPO.setPeriod(cbd.getPeriod());//个税期间
                    taskSubMoneyPO.setManagerNo(p.getManagerNo());//管理方编号
                    taskSubMoneyPO.setManagerName(p.getManagerName());//管理方名称
                    //新增划款子任务
                    taskSubMoneyService.insert(taskSubMoneyPO);
                    //记录划款子任务
                    moneyTask.put(key,taskSubMoneyPO.getId());
                    taskSubMoneyDetailPO.setTaskSubMoneyId(taskSubMoneyPO.getId());//划款子任务id
                }

                //待新增的划款子任务明细
                moneyTaskDetail.add(taskSubMoneyDetailPO);
            }
            if(cbd.getPay()!=null && cbd.getPay()){
                if(logger.isDebugEnabled()){
                    logger.debug("...有缴纳服务");
                }
                //缴纳子任务关键字(缴纳账户，所得期间)
                String key = cbd.getPayAccount()+DateTimeKit.format(cbd.getPeriod(),"YYYY-MM");

                TaskSubPaymentDetailPO taskSubPaymentDetailPO = new TaskSubPaymentDetailPO();
                BeanUtils.copyProperties(cbd,taskSubPaymentDetailPO);//copy计算批次明细信息
                taskSubPaymentDetailPO.setCalculationBatchDetailId(cbd.getId());//计算批次明细id

                //缴纳子任务未创建
                if(paymentTask.get(key)!=null){
                    taskSubPaymentDetailPO.setTaskSubPaymentId(paymentTask.get(key));//缴纳子任务id
                //缴纳子任务已创建
                }else{
                    TaskSubPaymentPO taskSubPaymentPO = new TaskSubPaymentPO();
                    taskSubPaymentPO.setTaskMainId(p.getId());
                    taskSubPaymentPO.setTaskNo("SPJN" + DateTimeKit.format(new Date(),"yyyyMMddHHmmssSSS"));
                    taskSubPaymentPO.setPaymentAccount(cbd.getPayAccount());//缴纳账户
                    taskSubPaymentPO.setPeriod(cbd.getPeriod());//个税期间
                    taskSubPaymentPO.setManagerNo(p.getManagerNo());//管理方编号
                    taskSubPaymentPO.setManagerName(p.getManagerName());//管理方名称
                    //新增划款子任务
                    taskSubPaymentService.insert(taskSubPaymentPO);
                    //记录缴纳子任务已创建
                    paymentTask.put(key,taskSubPaymentPO.getId());
                    taskSubPaymentDetailPO.setTaskSubPaymentId(taskSubPaymentPO.getId());//缴纳子任务id
                }

                //待新增的缴纳子任务明细
                paymentTaskDetail.add(taskSubPaymentDetailPO);
            }

            //供应商处理
            if(cbd.getSupport()!=null && cbd.getSupport()){

                if(logger.isDebugEnabled()){
                    logger.debug("...供应商处理");
                }
                //供应商处理子任务关键字(供应商，申报账户，所得期间)
                String key = cbd.getSupportName() + cbd.getDeclareAccount()+DateTimeKit.format(cbd.getPeriod(),"YYYY-MM");

                TaskSubSupplierDetailPO taskSubSupplierDetailPO = new TaskSubSupplierDetailPO();
                BeanUtils.copyProperties(cbd,taskSubSupplierDetailPO);//copy计算批次明细信息
                taskSubSupplierDetailPO.setCalculationBatchDetailId(cbd.getId());//计算批次明细id

                //供应商处理子任务未创建
                if(supportTask.get(key)!=null){
                    taskSubSupplierDetailPO.setTaskSubSupplierId(supportTask.get(key));//供应商处理子任务id
                //供应商处理子任务已创建
                }else{
                    TaskSubSupplierPO taskSubSupplierPO = new TaskSubSupplierPO();
                    taskSubSupplierPO.setTaskMainId(p.getId());
                    taskSubSupplierPO.setSupportName(cbd.getSupportName());//供应商名称
                    taskSubSupplierPO.setTaskNo("SPSU" + DateTimeKit.format(new Date(),"yyyyMMddHHmmssSSS"));
                    taskSubSupplierPO.setDeclareAccount(cbd.getDeclareAccount());//申报账户
                    taskSubSupplierPO.setPeriod(cbd.getPeriod());//个税期间
                    taskSubSupplierPO.setManagerNo(p.getManagerNo());//管理方编号
                    taskSubSupplierPO.setManagerName(p.getManagerName());//管理方名称
                    //新增供应商处理子任务
                    taskSubSupplierService.insert(taskSubSupplierPO);
                    //记录供应商处理子任务已创建
                    supportTask.put(key,taskSubSupplierPO.getId());
                    taskSubSupplierDetailPO.setTaskSubSupplierId(taskSubSupplierPO.getId());//供应商处理子任务id
                }

                //待新增的供应商处理子任务明细
                supportTaskDetail.add(taskSubSupplierDetailPO);
            }
        }

        //批量新增申报子任务明细
        if(declareTaskDetail!=null && declareTaskDetail.size()>0){
            taskSubDeclareDetailService.insertBatch(declareTaskDetail);
        }
        //批量新增划款子任务明细
        if(moneyTaskDetail!=null && moneyTaskDetail.size()>0){
            taskSubMoneyDetailService.insertBatch(moneyTaskDetail);
        }
        //批量新增缴纳子任务明细
        if(paymentTaskDetail!=null && paymentTaskDetail.size()>0){
            taskSubPaymentDetailService.insertBatch(paymentTaskDetail);
        }
        //批量新增供应商处理子任务明细
        if(supportTaskDetail!=null && supportTaskDetail.size()>0){
            taskSubSupplierDetailService.insertBatch(supportTaskDetail);
        }
    }
}
