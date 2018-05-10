package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.WorkFlowTaskInfoPO;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

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
