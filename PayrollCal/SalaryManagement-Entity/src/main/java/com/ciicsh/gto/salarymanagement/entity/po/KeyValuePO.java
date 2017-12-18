package com.ciicsh.gto.salarymanagement.entity.po;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Created by houwanhua on 2017/12/7.
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class KeyValuePO {
    private String key;
    private String value;
}
