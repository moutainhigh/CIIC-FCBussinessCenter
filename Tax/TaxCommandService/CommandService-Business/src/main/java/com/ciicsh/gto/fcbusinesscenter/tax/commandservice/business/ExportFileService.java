package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
public interface ExportFileService {
    /**
     * 申报导出(线下)
     *
     * @param subDeclareId
     * @return
     */
    Map<String, Object> exportForDeclareOffline(Long subDeclareId);

    /**
     * 申报导出(线上)
     *
     * @param subDeclareId
     * @return
     */
    HSSFWorkbook exportForDeclareOnline(Long subDeclareId);

    /**
     * 完税凭证导出
     *
     * @param subProofId
     * @return
     */
    HSSFWorkbook exportForProof(Long subProofId);

    /**
     * 申报导出报税文件(所得项目)
     *
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    void exportAboutSubject(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList);

    /**
     * 导出离职人员
     *
     * @param subDeclareId
     * @return
     */
    HSSFWorkbook exportQuitPerson(Long subDeclareId);

    /**
     * 获取压缩文件byte数组和文件名
     *
     * @param subDeclareId
     * @param tempFileName
     * @param dateStrFolder
     * @return
     * @throws Exception
     */
    Map<String, Object> getCompressedFileByte(Long subDeclareId, String tempFileName, String dateStrFolder) throws Exception;

    /**
     * 根据批次导出员工个税申报明细
     * @param taskNo
     * @param batchNos
     * @param operator
     * @return
     */
    Map<String,Object> exportTaxList(String taskNo,String[] batchNos,String operator);


    /**
     * 导出个税数据明细
     * @param taskMainId
     * @param employeeNo
     * @param employeeName
     * @return
     */
    Map<String,Object> exportTaskMainDetailData(long taskMainId,String employeeNo,String employeeName);

}
