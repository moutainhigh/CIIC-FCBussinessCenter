package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * @author wuhua
 */
public interface ExportFileService {
    /**
     * 完税凭证模板处理(徐汇)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    void exportAboutXH(HSSFWorkbook wb,List<TaskSubProofDetailPO> taskSubProofDetailPOList);

    /**
     * 完税凭证模板处理(三分局)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    void exportAboutSFJ(HSSFWorkbook wb,List<TaskSubProofDetailPO> taskSubProofDetailPOList);

    /**
     * 完税凭证模板处理(浦东)
     * @param wb
     * @param taskSubProofDetailPOList
     */
    void exportAboutPD(HSSFWorkbook wb,List<TaskSubProofDetailPO> taskSubProofDetailPOList);
}
