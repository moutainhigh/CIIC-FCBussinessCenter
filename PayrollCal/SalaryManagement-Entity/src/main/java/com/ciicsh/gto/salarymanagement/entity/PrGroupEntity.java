package com.ciicsh.gto.salarymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.List;

/**
 * Created by jiangtianning on 2017/10/24.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PrGroupEntity extends PrBaseEntity{

    /**
     * entityId
     */
    private String entityId;

    /**
     * 父薪资组ID
     */
    private String parentId;

    /**
     * 薪资组编码
     */
    private String code;

    /**
     * 管理方ID
     */
    private String managementId;

    /**
     * 继承薪资组模板的id
     */
    private String groupTemplateId;

    /**
     * 薪资组名称
     */
    private String name;

    /**
     * 薪资组版本
     */
    private String version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 是否是模板
     */
    private Boolean isTemplate;

    /**
     * 薪资项列表
     */
    private List<PrItemEntity> prItemEntityList;
}
