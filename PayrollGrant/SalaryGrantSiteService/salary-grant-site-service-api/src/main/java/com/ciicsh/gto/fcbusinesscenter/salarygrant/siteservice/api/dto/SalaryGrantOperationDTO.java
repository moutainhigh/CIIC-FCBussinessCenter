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
 * @since 2018-05-09
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantOperationDTO {
    /**
     * 处理人编号
     */
    private String operateUserId;
    /**
     * 处理人名称
     */
    private String operateUserName;
    /**
     * 处理时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8", locale = "zh")
    private Date operateTime;
    /**
     * 操作
     */
    private String operation;
    /**
     * 审批意见
     */
    private String opinion;
}
