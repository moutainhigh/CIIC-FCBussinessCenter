package com.ciicsh.gto.salarymanagementcommandservice.api.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by NeoJiang on 2018/1/4.
 * @author NeoJiang 
 */
@Data
public class PrPayrollGroupTemplateHistoryDTO {

    /**
     * id
     */
    private Integer id;
    /**
     * 薪资组模板code
     */
    private String payrollGroupTemplateCode;
    /**
     * 薪资组模板版本
     */
    private String version;
    /**
     * 该薪资组模板版本的详细数据
     */
    private String payrollGroupTemplateHistory;
    /**
     * 薪资项列表
     */
    private List<PrPayrollItemDTO> itemDTOList;
    /**
     * 数据创建时间
     */
    private Date createdTime;
    /**
     * 最后修改时间
     */
    private Date modifiedTime;
    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 最后修改人
     */
    private String modifiedBy;
}
