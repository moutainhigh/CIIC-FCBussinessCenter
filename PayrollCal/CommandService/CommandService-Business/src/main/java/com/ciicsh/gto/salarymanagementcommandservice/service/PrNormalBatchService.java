package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.bo.ExcelUploadStatistics;
import com.ciicsh.gto.salarymanagement.entity.po.PrNormalBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustSubBatchPO;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
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

    PrCustBatchPO getCustBatchInfo(String batchCode);

    /**
     * 上传雇员薪资项数据
     * @param batchCode      批次编码
     * @param empGroupCode   雇员组编码
     * @param importType     上传数据规则：（"覆盖导入",1),("修改导入",2),("追加导入",3),("差异导入",4),
     * @param file           EXCEL 文件
     * @return INT           上传成功行数
     */
    ExcelUploadStatistics uploadEmpPRItemsByExcel(String batchCode, String empGroupCode, int batchType, int importType, MultipartFile file);

    /**
     * 审批意见
     * @param batchCode
     * @param comments
     * @param status
     * @param modifiedBy
     * @return
     */
    int auditBatch(String batchCode, String comments, int status, String modifiedBy, String advancePeriod, String result);

    /**
     * 更新批次是否来款
     * @param batchCodes
     * @param advance
     * @param modifiedBy
     * @return
     */
    int updateHasAdvance(List<String> batchCodes, int advance, String modifiedBy);

    /**
     * 更新批次是否垫付
     * @param hasMoney
     * @param modifiedBy
     * @return
     */
    int updateHasMoney(List<String> batchCodes, boolean hasMoney, String modifiedBy);


    /**
     * 根据管理方id获取批次id列表
     * @param managementId
     * @return
     */
    List<PrNormalBatchPO> getAllBatchesByManagementId(String managementId);

    /**
     * 获取管理方ID 列表
     * @param managementId
     * @return
     */
    List<String> getBatchIdListByManagementId(String managementId);

    /**
     * 获取历史批次列表
     * @param mgrIds
     * @return
     */
    List<PrNormalBatchPO> getHistoryBatchInfoList(List<String> mgrIds);

    /**
     * 通过管理放获取批次列表
     * @param managementId
     * @param batchCode
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<PrNormalBatchPO> getAllBatchesByManagementId(String managementId, String batchCode, Integer pageNum, Integer pageSize);

    /**
     * 获取对批次薪资结构
     * @param batchCode
     * @return
     */
    List<PrPayrollItemPO> getBatchPayrollSchema(String batchCode);


    String[] readExcelColumns(String batchCode, InputStream stream);

}
