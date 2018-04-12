package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPrintRecordPO;

/**
 * 工资单任务单打印记录 服务类
 *
 * @author taka
 * @since 2018-02-24
 */
public interface PrsPayrollPrintRecordService {
    List<PrsPayrollPrintRecordPO> listPrsPayrollPrintRecords(Map<String, Object> params);

    Page<PrsPayrollPrintRecordPO> pagePrsPayrollPrintRecords(Map<String, Object> params);

    PrsPayrollPrintRecordPO getPrsPayrollPrintRecord(Map<String, Object> params);

    Boolean addPrsPayrollPrintRecord(Map<String, Object> params);

    Boolean updatePrsPayrollPrintRecord(Map<String, Object> params);
}
