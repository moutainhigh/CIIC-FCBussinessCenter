package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.voucher.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForCalBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author yuantongqing on 2017/12/19
 */
@Service
public class CalculationBatchDetailServiceImpl extends ServiceImpl<CalculationBatchDetailMapper, CalculationBatchDetailPO> implements CalculationBatchDetailService, Serializable {

    /**
     * 查询申报记录
     * @param requestForProof
     * @return
     */
    @Override
    public ResponseForBatchDetail queryCalculationBatchDetail(RequestForProof requestForProof) {
        ResponseForBatchDetail responseForBatchDetail = new ResponseForBatchDetail();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new CalculationBatchDetailPO());
        //判断是否包含主键ID条件
        if (requestForProof.getId() != null && !"".equals(requestForProof.getId())) {
            wrapper.andNew("id = {0}", requestForProof.getId());
        }
        //判断是否包含证件类型
        if (requestForProof.getIdType() != null && !"".equals(requestForProof.getIdType())) {
            wrapper.andNew("id_type = {0}", requestForProof.getIdType());
        }
        //判断是否包含证件号
        if (requestForProof.getIdNo() != null && !"".equals(requestForProof.getIdNo())) {
            wrapper.andNew("id_no = {0}", requestForProof.getIdNo());
        }
        //判断是否包含所得项目
        if (requestForProof.getIncomeSubject() != null && !"".equals(requestForProof.getIncomeSubject())) {
            wrapper.andNew("income_subject = {0}", requestForProof.getIncomeSubject());
        }
        //判断是否包含个税期间条件
        if (requestForProof.getSubmitTimeStart() != null && !"".equals(requestForProof.getSubmitTimeStart())) {
            wrapper.andNew("period >= {0}", requestForProof.getSubmitTimeStart());
        }
        //判断是否包含个税期间条件
        if (requestForProof.getSubmitTimeEnd() != null && !"".equals(requestForProof.getSubmitTimeEnd())) {
            wrapper.andNew("period < {0} ", requestForProof.getSubmitTimeEnd());
        }
        //查询已申报的
        wrapper.andNew("is_declare = {0} ", 1);
        wrapper.orderBy("created_time", false);

        //判断是否是分页查询
        if (requestForProof.getCurrentNum() != null && requestForProof.getPageSize() != 0) {
            Page<CalculationBatchDetailPO> pageInfo = new Page<>(requestForProof.getCurrentNum(), requestForProof.getPageSize());
            List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取总数目
            int total = baseMapper.selectCount(wrapper);
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
            List<CalculationBatchDetailPO> calculationBatchDetailPOList = baseMapper.selectList(wrapper);
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
}
