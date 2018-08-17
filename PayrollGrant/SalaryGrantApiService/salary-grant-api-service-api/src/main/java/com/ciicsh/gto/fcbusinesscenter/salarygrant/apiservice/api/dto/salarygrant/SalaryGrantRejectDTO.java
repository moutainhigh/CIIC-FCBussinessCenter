package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.api.dto.salarygrant;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * <p>
 * 驳回发放信息
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-10
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class SalaryGrantRejectDTO {
    private static final long serialVersionUID = 1L;
    /**
     * 驳回任务单编号
     */
    private List<SalaryGrantTaskDTO> codeList;
}
