package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.TaskWorkFlowRelationPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 任务单流程映射表
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-02
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TaskWorkFlowRelationBO extends TaskWorkFlowRelationPO implements Serializable {
    private static final long serialVersionUID = 1L;
}
