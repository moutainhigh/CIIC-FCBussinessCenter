package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.bo.BatchCompareEmpBO;
import com.ciicsh.gto.salarymanagement.entity.dto.BatchCompareRequestDTO;
import com.ciicsh.gto.salarymanagement.entity.po.AdvanceBatchInfoPO;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by NeoJiang on 2018/5/2.
 */
public interface BatchService {

    /**
     * @param compareDTO 对比批次信息
     * @return 对比结果List
     */
    List<BatchCompareEmpBO> compareBatch(BatchCompareRequestDTO compareDTO);

    int updateAdvancedBatch(AdvanceBatchInfoPO advanceBatchInfoPO);

}
