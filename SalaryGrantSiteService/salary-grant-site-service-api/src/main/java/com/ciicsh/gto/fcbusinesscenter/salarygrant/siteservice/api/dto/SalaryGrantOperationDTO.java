package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 日志信息
 * </p>
 *
 * @author chenpb
 * @since 2018-05-07
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantOperationDTO {
    /**
     * 任务单编号
     */
    private String taskCode;
    /**
     * 操作员
     */
    private String operateUser;
    /**
     * 最后修改时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date operateDate;
    /**
     * 操作
     */
    private String operateStep;
}
