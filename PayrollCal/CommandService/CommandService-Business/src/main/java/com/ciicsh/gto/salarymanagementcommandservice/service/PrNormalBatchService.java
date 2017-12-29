package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by bill on 17/12/7.
 */
public interface PrNormalBatchService {

    int insert(PrNormalBatchPO normalBatchPO);

    /**
     * 获取批次列表列表
     * @param param 查询参数
     * @param pageNum
     * @param pageSize
     * @return 薪资项模板列表
     */
    PageInfo<PrCustBatchPO> getList(PrCustBatchPO param, Integer pageNum, Integer pageSize);

    int update(PrNormalBatchPO normalBatchPO);

    List<PrCustSubBatchPO> selectSubBatchList(String code, Integer status);

    Integer deleteBatchByCodes(List<String> codes);

    PrNormalBatchPO getBatchByCode(String code);

    /**
     * 批量插入MONGODB数据：雇员数据，雇员薪资组数据
     * @param batchCode
     * @param empGroupCode
     * @param PrGroupCode
     * @param PrTempGroupCode
     * @return
     */
    int batchInsertMongoForEmpGroudCodeAndPRGroupCode(String batchCode, String empGroupCode, String PrGroupCode, String PrTempGroupCode);
}
