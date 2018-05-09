package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 工作流任务日志表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-09
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WorkFlowTaskInfoBO extends WorkFlowTaskInfoPO implements Serializable {
    private static final long serialVersionUID = 1L;
}
