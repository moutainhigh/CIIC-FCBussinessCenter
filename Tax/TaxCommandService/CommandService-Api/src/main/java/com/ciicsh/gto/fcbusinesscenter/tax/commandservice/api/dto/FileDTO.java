package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto;

/**
 * @author yuantongqing
 * on create 2018/1/5
 */
public class FileDTO {
    /**
     * 主键ID
     */
    private Long id;

    /**
     * 业务ID（申报子任务ID；缴纳子任务ID；完税凭证子任务ID）
     */
    private Long businessId;

    /**
     * 业务类型（01.申报回执单；02.缴纳凭证；03.完税凭证回执）
     */
    private String businessType;

    /**
     * 源文件名
     */
    private String filenameSource;

    /**
     * 路径
     */
    private String filePath;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getBusinessId() {
        return businessId;
    }

    public void setBusinessId(Long businessId) {
        this.businessId = businessId;
    }

    public String getBusinessType() {
        return businessType;
    }

    public void setBusinessType(String businessType) {
        this.businessType = businessType;
    }

    public String getFilenameSource() {
        return filenameSource;
    }

    public void setFilenameSource(String filenameSource) {
        this.filenameSource = filenameSource;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "FileDTO{" +
                "id=" + id +
                ", businessId=" + businessId +
                ", businessType='" + businessType + '\'' +
                ", filenameSource='" + filenameSource + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
