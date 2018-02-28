package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollExpressPO;

/**
 * 工资单邮寄快递信息 服务类
 *
 * @author taka
 * @since 2018-02-24
 */
public interface PrsPayrollExpressService {
    List<PrsPayrollExpressPO> listPrsPayrollExpresss(Map<String, Object> params);

    Page<PrsPayrollExpressPO> pagePrsPayrollExpresss(Map<String, Object> params);

    PrsPayrollExpressPO getPrsPayrollExpress(Map<String, Object> params);

    Boolean addPrsPayrollExpress(Map<String, Object> params);

    Boolean updatePrsPayrollExpress(Map<String, Object> params);
}
