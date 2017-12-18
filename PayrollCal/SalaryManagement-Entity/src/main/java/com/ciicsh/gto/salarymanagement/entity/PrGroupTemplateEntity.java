package com.ciicsh.gto.salarymanagement.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/2.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrGroupTemplateEntity extends PrBaseEntity{

    /**
     * 薪资组模板Id ，格式为PRGT+NNNNNN
     */
    private String entityId;

    /**
     * 薪资组模板编码，规则为：XZZMB-xxxxxx(六位数字序号)
     */
    private String code;

    /**
     * 薪资组模板名称
     */
    private String name;

    /**
     * 版本号
     */
    private String version;

    /**
     * 备注
     */
    private String remark;

    /**
     * 薪资项列表
     */
    List<PrItemEntity> prItemEntityList;
}
