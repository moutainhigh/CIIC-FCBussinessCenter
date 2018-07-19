package com.ciicsh.gto.salarymanagement.entity.bo;


/**
 * Created by bill on 18/5/20.
 */
public class ExcelUploadStatistics {
    private int successCount;
    private int failedCount;
    private int total;

    private String[] failedContent;

    public int getSuccessCount() {
        return successCount;
    }

    public void setSuccessCount(int successCount) {
        this.successCount = successCount;
    }

    public int getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(int failedCount) {
        this.failedCount = failedCount;
    }

    public String[] getFailedContent() {
        return failedContent;
    }

    public void setFailedContent(String[] failedContent) {
        this.failedContent = failedContent;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
