package com.ciicsh.gto.fcbusinesscenter.tax.entity.bo;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import java.util.List;

/**
 * @author yuantongqing on 2018-05-22
 */
public class TemplateFileBO {

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 文件wb
     */
    private HSSFWorkbook wb;

    /**
     * 是否是文件
     */
    private Boolean type;

    /**
     * 文件夹下的子文件
     */
    private List<TemplateFileBO> templateFileBOList;

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public HSSFWorkbook getWb() {
        return wb;
    }

    public void setWb(HSSFWorkbook wb) {
        this.wb = wb;
    }

    public Boolean getType() {
        return type;
    }

    public void setType(Boolean type) {
        this.type = type;
    }

    public List<TemplateFileBO> getTemplateFileBOList() {
        return templateFileBOList;
    }

    public void setTemplateFileBOList(List<TemplateFileBO> templateFileBOList) {
        this.templateFileBOList = templateFileBOList;
    }

    @Override
    public String toString() {
        return "TemplateFileBO{" +
                "templateName='" + templateName + '\'' +
                ", wb=" + wb +
                ", type=" + type +
                ", templateFileBOList=" + templateFileBOList +
                '}';
    }
}
