package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.SalManagementPO;

/**
 * 管理方 服务类
 *
 * @author taka
 * @since 2018-02-05
 */
public interface SalManagementService {
    List<SalManagementPO> listSalManagements(Map<String, Object> params);

    Page<SalManagementPO> pageSalManagements(Map<String, Object> params);

    SalManagementPO getSalManagement(Map<String, Object> params);

    Boolean addSalManagement(Map<String, Object> params);

    Boolean updateSalManagement(Map<String, Object> params);
}
