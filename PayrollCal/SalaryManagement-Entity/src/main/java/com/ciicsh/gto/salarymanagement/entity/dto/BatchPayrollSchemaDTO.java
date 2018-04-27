package com.ciicsh.gto.salarymanagement.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * Created by NeoJiang on 2018/4/21.
 */
@Data
public class BatchPayrollSchemaDTO {

    private String batchCode;

    private List<PayItemCodeNameObj> itemList;

    @AllArgsConstructor
    @Data
    public class PayItemCodeNameObj {

        private String itemCode;

        private String itemName;

    }
}
