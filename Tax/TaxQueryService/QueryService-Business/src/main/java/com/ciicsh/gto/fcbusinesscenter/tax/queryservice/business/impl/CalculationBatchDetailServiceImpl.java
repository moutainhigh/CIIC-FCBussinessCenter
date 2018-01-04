package com.ciicsh.gto.fcbusinesscenter.tax.queryservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.bo.CalculationBatchDetailBO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.RequestForProof;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.voucher.ResponseForBatchDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.business.CalculationBatchDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.queryservice.dao.CalculationBatchDetailMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.io.Serializable;
import java.util.ArrayList;
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
        List<CalculationBatchDetailBO> calculationBatchDetailBOList = new ArrayList<>();
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
        }
        return responseForBatchDetail;
    }
}
