package com.ciicsh.gto.salarymanagement.entity.message;

import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by bill on 17/12/5.
 */
@Getter
@Setter
public class PayrollMsg {

    // 当前批次code
    private String batchCode;

    // 源批次code
    private String originBatchCode;

    // 操作类型: 1增加, 2更新, 3删除, 4查询; 5导入
    private int operateType;

    // 批次类型 1 正常批次, 2 调整批次, 3 回溯批次, 4 测试批次, 5 导入批次;
    private int batchType;

    public String toString() {
        return String.format("批次号：%s --批次类型：%d --操作类型：%d", batchCode, batchType, operateType);
    }

}
