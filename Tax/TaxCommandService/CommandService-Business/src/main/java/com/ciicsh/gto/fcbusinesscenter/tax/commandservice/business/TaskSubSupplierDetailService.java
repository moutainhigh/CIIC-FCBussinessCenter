package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForSubSupplierDetail;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForSubSupplierDetail;

/**
 * @author wuhua
 */
public interface TaskSubSupplierDetailService {

    /**
     * 查询供应商申报明细
     *
     * @param requestForSubSupplierDetail
     * @return
     */
    ResponseForSubSupplierDetail querySubSupplierDetailsByParams(RequestForSubSupplierDetail requestForSubSupplierDetail);

    /**
     * 根据批量完成数组查询未确认的明细数目
     *
     * @param subSupplierIds
     * @return
     */
    int selectCount(String[] subSupplierIds);
}

