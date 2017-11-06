package com.ciicsh.zorro.leopardwebservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/25.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrEmployeeGroupEntity extends PrBaseEntity implements Serializable{

    /**
     * entityId
     */
    private String entityId;

    /**
     * 管理方Id
     */
    private String managementId;

    /**
     * 客户Id
     */
    private String companyId;

    /**
     * 雇员组编码
     */
    private String code;

    /**
     * 雇员组名
     */
    private String name;

    /**
     * 备注
     */
    private String remark;

    /**
     * 雇员列表
     */
    List<PrEmployeeEntity> employeeList;
}
