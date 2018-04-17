package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;


import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CommonListDTO {
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer currentPage = 1;

    @NotNull(message = "每页显示数量不能为空")
    @Max(value = 50, message = "每页显示数量最多为50")
    private Integer size = 10;

    public Integer getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(Integer currentPage) {
        this.currentPage = currentPage;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
