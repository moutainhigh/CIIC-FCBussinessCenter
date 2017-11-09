package com.ciicsh.exportreporttemplateservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Created by houwanhua on 2017/11/9.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PrPayRoll extends BaseEntity{
    private long payRollId;
    private String managementId;
    private String employeeId;
    private String salaryPeriod;
    private String payRollBatch;
    private Integer channel;
    private String dataObject;
    private String remark;
}
