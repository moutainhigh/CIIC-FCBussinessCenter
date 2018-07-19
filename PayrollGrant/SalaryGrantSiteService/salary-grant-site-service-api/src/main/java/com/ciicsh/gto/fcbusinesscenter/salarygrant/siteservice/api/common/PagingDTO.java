package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.common;


import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class PagingDTO {
    @NotNull(message = "当前页不能为空")
    @Min(value = 1, message = "当前页不能小于1")
    private Integer current = 1;

    @NotNull(message = "每页显示数量不能为空")
    @Max(value = 50, message = "每页显示数量最多为50")
    private Integer size = 10;

}
