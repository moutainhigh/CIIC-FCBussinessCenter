package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author baofeng@ciicsh.com
 * @createTime 2018-07-11 13:55
 * @description 客户服务_配置变更任务记录DTO
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class CmyFcConfigureTaskDTO implements Serializable {

    private static final long serialVersionUID = 3186480775397480660L;

    // 变更任务单编号：SP+GL+YY+5位数字(36进制)
    private String cmyFcConfigureTaskId;

    // 管理方id
    private String managementId;

    // 任务单状态 1 - 发送成功；2 -发送失败
    private int status;

    // 任务单类型 1 - 薪资组实例变更；2 - 雇员扩展字段变更
    private int type;

    // 需要变更薪资组id或雇员扩展字段模板id
    private String targetId;

    // 上传附件地址
    private String uploadUrl;

    // 变更说明
    private String remark;

    // 是否有效
    private Boolean isActive;

    // 数据创建时间
    private LocalDateTime createdTime;

    // 最后修改时间
    private LocalDateTime modifiedTime;

    // 创建人
    private String createdBy;

    // 最后修改人
    private String modifiedBy;

    // 薪资组编码
    private String groupCode;

    // 薪资组名称
    private String groupName;

}
