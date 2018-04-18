package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDateTime;

/**
 * base DTO
 * @author chenpb
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class BaseDTO {

    private Integer current = 1;

    private Integer pageSize = 10;

    /**
     * 创建人编号
     */
    private String createdBy;
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    /**
     * 修改人编号
     */
    private String modifiedBy;
    /**
     * 修改时间
     */
    private LocalDateTime modifiedTime;

}
