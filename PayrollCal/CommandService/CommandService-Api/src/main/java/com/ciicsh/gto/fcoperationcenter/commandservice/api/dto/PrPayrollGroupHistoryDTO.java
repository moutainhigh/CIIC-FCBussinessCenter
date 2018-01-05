package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * Created by jiangtianning on 2018/1/2.
 * @author jiangtianning
 */
@Data
public class PrPayrollGroupHistoryDTO {

    /**
     * id
     */
    private Integer id;
    /**
     * 薪资组code
     */
    private String payrollGroupCode;
    /**
     * 薪资组版本
     */
    private String version;
    /**
     * 该薪资组版本的详细数据
     */
    private String payrollGroupHistory;
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
