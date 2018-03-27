package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;


import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.support.RequestForTaskSubSupplier;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.support.ResponseForTaskSubSupplier;

import java.util.List;

/**
 * @author wuhua
 */
public interface TaskSubSupplierService {
    /**
     * 查询供应商子任务
     *
     * @param requestForTaskSubSupplier
     * @return
     */
    ResponseForTaskSubSupplier queryTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier);

    /**
     * 根据供应商子任务ID查询供应商信息
     *
     * @param subSupplierId
     * @return
     */
    TaskSubSupplierPO querySupplierDetailsById(long subSupplierId);

    /**
     * 合并全国委托供应商任务
     *
     * @param requestForTaskSubSupplier
     */
    void mergeTaskSubSuppliers(RequestForTaskSubSupplier requestForTaskSubSupplier);

    /**
     * 拆分供应商子任务
     *
     * @param requestForTaskSubSupplier
     * @param type
     * @return
     */
    List<Long> splitSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier, String type);

    /**
     * 根据ID查询合并之前的供应商子任务
     *
     * @param mergeId
     * @return
     */
    List<TaskSubSupplierPO> queryTaskSubSupplierByMergeId(long mergeId);

    /**
     * 批量完成供应商任务
     *
     * @param requestForTaskSubSupplier
     */
    void completeTaskSubSupplier(RequestForTaskSubSupplier requestForTaskSubSupplier);

}

