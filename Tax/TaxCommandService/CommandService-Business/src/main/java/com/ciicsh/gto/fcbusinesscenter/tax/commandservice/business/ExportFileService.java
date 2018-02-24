package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;
import java.util.Map;

/**
 * @author wuhua
 */
public interface ExportFileService {
    /**
     * 完税凭证模板处理(徐汇)
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    void exportAboutXH(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList);

    /**
     * 完税凭证模板处理(三分局)
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    void exportAboutSFJ(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList);

    /**
     * 完税凭证模板处理(浦东)
     *
     * @param wb
     * @param map
     * @param taskSubProofDetailPOList
     */
    void exportAboutPD(HSSFWorkbook wb, Map<String, String> map, List<TaskSubProofDetailPO> taskSubProofDetailPOList);

    /**
     * 申报导出报税文件
     *
     * @param wb
     * @param map
     * @param taskSubDeclareDetailPOList
     */
    void exportAboutTax(HSSFWorkbook wb, Map<String, String> map, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList);

    /**
     * 申报导出报税文件(所得项目)
     * @param wb
     * @param taskSubDeclareDetailPOList
     */
    void exportAboutSubject(HSSFWorkbook wb, List<TaskSubDeclareDetailPO> taskSubDeclareDetailPOList);
}
