package com.ciicsh.consignationcommandservice.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * Created by houwanhua on 2017/11/9.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SsEmpRefund extends BaseEntity {
    private Integer empRefundId;
    private String employeeTaskId;
    private String empArchiveId;
    private Double amount;
    private Date processTime;
    private Integer processWay;
}
