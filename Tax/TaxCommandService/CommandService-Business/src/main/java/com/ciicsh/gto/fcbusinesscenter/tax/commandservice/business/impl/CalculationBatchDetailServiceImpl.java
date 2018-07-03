package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.BatchNoStatus;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yuantongqing on 2017/12/19
 */
@Service
public class CalculationBatchDetailServiceImpl extends ServiceImpl<CalculationBatchDetailMapper, CalculationBatchDetailPO> implements CalculationBatchDetailService, Serializable {

    @Autowired
    private CalculationBatchMapper calculationBatchMapper;

    /**
     * 查询申报记录
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForBatchDetail queryCalculationBatchDetail(RequestForProof requestForProof) {
        ResponseForBatchDetail responseForBatchDetail = new ResponseForBatchDetail();
        //判断是否是分页查询
        if (requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0) {
            Page<CalculationBatchDetailPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.queryTaxBatchDetailsListByRes(pageInfo,requestForProof);
            //获取总数目
            int total = calculationBatchDetailPOList.size();
            //获取证件类型中文和所得项目中文
            for(CalculationBatchDetailPO po: calculationBatchDetailPOList){
                po.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,po.getIdType()));
                po.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,po.getIncomeSubject()));
            }
            responseForBatchDetail.setCurrentNum(requestForProof.getCurrentNum());
            responseForBatchDetail.setPageSize(requestForProof.getPageSize());
            responseForBatchDetail.setTotalNum(total);
            responseForBatchDetail.setRowList(calculationBatchDetailPOList);
        } else {
            List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.queryTaxBatchDetailsListByRes(requestForProof);
            //获取证件类型中文和所得项目中文
            for(CalculationBatchDetailPO po: calculationBatchDetailPOList){
                po.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,po.getIdType()));
                po.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,po.getIncomeSubject()));
            }
            responseForBatchDetail.setRowList(calculationBatchDetailPOList);
        }
        return responseForBatchDetail;
    }

    /**
     * 查询批次明细
     * @param requestForEmployees
     * @return
     *//*
    @Override
    public ResponseForCalBatchDetail queryCalculationBatchDetails(RequestForEmployees requestForEmployees) {
        {
            ResponseForCalBatchDetail responseForCalBatchDetail = new ResponseForCalBatchDetail();
            List<CalculationBatchDetailPO> calculationBatchDetailPOList = new ArrayList<>();
            EntityWrapper wrapper = new EntityWrapper();
            wrapper.setEntity(new CalculationBatchDetailPO());
            //批次主表id
            if (requestForEmployees.getBatchid() != null) {
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
            wrapper.orderBy("id", true);

            Page<CalculationBatchDetailPO> page = new Page<CalculationBatchDetailPO>(0,10);
            calculationBatchDetailPOList = baseMapper.selectPage(page, wrapper);

            for(CalculationBatchDetailPO p : calculationBatchDetailPOList){
                p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));//证件类型中文显示
                p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));//个税所得项目中文显示
            }

            responseForCalBatchDetail.setRowList(calculationBatchDetailPOList);

            //判断是否是分页查询
            *//*if (requestForEmployees.getCurrentNum() != null && requestForEmployees.getPageSize() != 0) {
                Page<CalculationBatchDetailPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
                List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
                //获取总数目
                int total = baseMapper.selectCount(wrapper);
                for (CalculationBatchDetailPO calculationBatchDetailPO : calculationBatchDetailPOList) {
                    CalculationBatchDetailBO calculationBatchDetailBO = new CalculationBatchDetailBO();
                    BeanUtils.copyProperties(calculationBatchDetailPO, calculationBatchDetailBO);
                    calculationBatchDetailBOList.add(calculationBatchDetailBO);
                }
                responseForBatchDetail.setCurrentNum(requestForProof.getCurrentNum());
                responseForBatchDetail.setPageSize(requestForProof.getPageSize());
                responseForBatchDetail.setTotalNum(total);
                responseForBatchDetail.setRowList(calculationBatchDetailBOList);
            } else {
                List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.selectList(wrapper);
                for (CalculationBatchDetailPO calculationBatchDetailPO : calculationBatchDetailPOList) {
                    CalculationBatchDetailBO calculationBatchDetailBO = new CalculationBatchDetailBO();
                    BeanUtils.copyProperties(calculationBatchDetailPO, calculationBatchDetailBO);
                    calculationBatchDetailBOList.add(calculationBatchDetailBO);
                }
                responseForBatchDetail.setRowList(calculationBatchDetailBOList);
            }*//*
            return responseForCalBatchDetail;
        }
    }*/

    /**
     * 条件查询计算批次明细
     * @param requestForCalBatchDetail
     * @return
     */
    @Override
    public ResponseForCalBatchDetail queryTaxBatchDetailByRes(RequestForCalBatchDetail requestForCalBatchDetail) {
        ResponseForCalBatchDetail responseForCalBatchDetail = new ResponseForCalBatchDetail();
        CalculationBatchDetailBO calculationBatchDetailBO = new CalculationBatchDetailBO();
        BeanUtils.copyProperties(requestForCalBatchDetail,calculationBatchDetailBO);
        Page<CalculationBatchDetailBO> pageInfo = new Page<>(requestForCalBatchDetail.getCurrentNum(), requestForCalBatchDetail.getPageSize());
        List<CalculationBatchDetailBO> calculationBatchDetailBOList = baseMapper.queryTaxBatchDetailByRes(pageInfo, calculationBatchDetailBO);
        pageInfo.setRecords(calculationBatchDetailBOList);
        for(CalculationBatchDetailBO p : calculationBatchDetailBOList){
            //证件类型中文显示
            p.setIdTypeName(EnumUtil.getMessage(EnumUtil.IT_TYPE,p.getIdType()));
            //个税所得项目中文显示
            p.setIncomeSubjectName(EnumUtil.getMessage(EnumUtil.INCOME_SUBJECT,p.getIncomeSubject()));
        }
        responseForCalBatchDetail.setRowList(calculationBatchDetailBOList);
        responseForCalBatchDetail.setCurrentNum(requestForCalBatchDetail.getCurrentNum());
        responseForCalBatchDetail.setPageSize(requestForCalBatchDetail.getPageSize());
        responseForCalBatchDetail.setTotalNum(pageInfo.getTotal());
        return responseForCalBatchDetail;
    }

    /**
     * 批量恢复计算批次明细
     * @param ids
     */
    @Override
    public void queryCalculationBatchDetail(String[] ids) {
        if (ids != null && !"".equals(ids)) {
            CalculationBatchDetailPO calculationBatchDetailPO = new CalculationBatchDetailPO();
            //是否暂缓
            calculationBatchDetailPO.setDefer(false);
            //修改时间
//            calculationBatchDetailPO.setModifiedTime(LocalDateTime.now());
            EntityWrapper wrapper= new EntityWrapper();
            wrapper.setEntity(new CalculationBatchDetailPO());
            wrapper.in("id", ids);
            //恢复计算批次明细
            baseMapper.update(calculationBatchDetailPO, wrapper);
        }
    }

    /**
     * 批量暂缓计算批次明细
     * @param ids
     */
    @Override
    public void batchPostponeCalBatchDetail(String[] ids) {
        if (ids != null && !"".equals(ids)) {
            CalculationBatchDetailPO calculationBatchDetailPO = new CalculationBatchDetailPO();
            //是否暂缓
            calculationBatchDetailPO.setDefer(true);
            EntityWrapper wrapper= new EntityWrapper();
            wrapper.setEntity(new CalculationBatchDetailPO());
            wrapper.in("id", ids);
            //恢复计算批次明细
            baseMapper.update(calculationBatchDetailPO, wrapper);
        }
    }

    /**
     * 判断所选详情是否有取消关账状态的数据是否已经创建任务
     * @param ids
     * @return
     */
    @Override
    public int checkTaskByDetailIds(String[] ids) {
        int i = 0;
        //根据详情ID数组查询计算批次任务
        EntityWrapper calculationDetailWrapper = new EntityWrapper();
        calculationDetailWrapper.setEntity(new CalculationBatchDetailPO());
        calculationDetailWrapper.in("id",ids);
        List<CalculationBatchDetailPO> calculationBatchDetailPOS = baseMapper.selectList(calculationDetailWrapper);
        //获取计算批次任务ID集合
        List<Long> calculationIds = calculationBatchDetailPOS.stream().map(item -> item.getCalculationBatchId()).collect(Collectors.toList());
        EntityWrapper calculationWrapper = new EntityWrapper();
        calculationWrapper.setEntity(new CalculationBatchPO());
        calculationWrapper.in("id",calculationIds);
        calculationWrapper.and("is_active = {0} ", true);
        List<CalculationBatchPO> calculationBatchPOS = calculationBatchMapper.selectList(calculationWrapper);
        if(calculationBatchPOS.size() > 0){
            for(CalculationBatchPO calculationBatchPO : calculationBatchPOS){
                //如果状态为"01",则为"取消关账状态"
                if(BatchNoStatus.unclose.getCode().equals(calculationBatchPO.getStatus())){
                    i = 1;
                    break;
                }
                //查询由当前批次创建的任务
                List<TaskMainPO> taskMainPOList = calculationBatchMapper.queryTaskMainsByCalBatch(calculationBatchPO.getId());
                if(taskMainPOList.size() > 0){
                    i = 2;
                    break;
                }
            }
        }
        return i;
    }
}
