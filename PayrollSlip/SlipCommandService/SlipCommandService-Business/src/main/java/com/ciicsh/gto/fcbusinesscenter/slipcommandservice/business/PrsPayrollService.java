package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollPO;
import org.bson.Document;

/**
 * 工资单 服务类
 *
 * @author taka
 * @since 2018-02-09
 */
public interface PrsPayrollService {
    List<Document> listPubEmps(Map<String, Object> params);

    Page<Document> pagePubEmps(Map<String, Object> params);

    Boolean deletePubEmps(ArrayList<String> ids);

    PrsPayrollPO getPrsPayroll(Map<String, Object> params);

    Boolean addPrsPayroll(Map<String, Object> params);

    Boolean addPrsPayrolls(ArrayList<Map<String, Object>> objs);

    Boolean updatePrsPayroll(Map<String, Object> params);
}
