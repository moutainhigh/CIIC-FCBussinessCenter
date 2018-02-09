package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;

/**
 * @author wuhua
 */
public interface TaskSubSupplierService {
    /**
     * 查询供应商子任务
     * @param requestForTaskSubSupplier
     * @return
     */
    ResponseForTaskSubSupplier queryTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier);
}

