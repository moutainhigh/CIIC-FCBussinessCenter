package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 薪资发放任务单子表请求查询条件
 * </p>
 *
 * @author gaoyang
 * @since 2018-04-20
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RequestSubTaskDTO {
    /**
     * 任务单编号
     */
    private String taskCode;

}
