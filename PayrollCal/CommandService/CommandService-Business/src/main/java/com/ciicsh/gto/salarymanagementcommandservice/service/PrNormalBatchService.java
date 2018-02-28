package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.multipart.MultipartFile;

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

    List<PrCustSubBatchPO> selectSubBatchList(String code);

    Integer deleteBatchByCodes(List<String> codes);

    PrNormalBatchPO getBatchByCode(String code);

    /**
     * 上传雇员薪资项数据
     * @param batchCode      批次编码
     * @param empGroupCode   雇员组编码
     * @param importType     上传数据规则：（"覆盖导入",1),("修改导入",2),("追加导入",3),("差异导入",4),
     * @param file           EXCEL 文件
     * @return INT           上传成功行数
     */
    int uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, int batchType,int importType, MultipartFile file);

    /**
     * 审批意见
     * @param batchCode
     * @param comments
     * @param status
     * @param modifiedBy
     * @return
     */
    int auditBatch(String batchCode, String comments, int status, String modifiedBy, String result);

    /**
     * 更新批次是否来款
     * @param batchCode
     * @param hasAdvance
     * @param modifiedBy
     * @return
     */
    int updateHasAdvance(String batchCode, boolean hasAdvance, String modifiedBy);

    /**
     * 更新批次是否垫付
     * @param batchCode
     * @param hasMoney
     * @param modifiedBy
     * @return
     */
    int updateHasMoneny(String batchCode, boolean hasMoney, String modifiedBy);
}
