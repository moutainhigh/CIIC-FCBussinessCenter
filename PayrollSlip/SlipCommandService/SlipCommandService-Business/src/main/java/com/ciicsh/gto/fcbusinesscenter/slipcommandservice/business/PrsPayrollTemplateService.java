package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollTemplatePO;

/**
 * 工资单模板 服务类
 *
 * @author taka
 * @since 2018-01-29
 */
public interface PrsPayrollTemplateService {
    List<PrsPayrollTemplatePO> listPrsPayrollTemplates(Map<String, Object> params);

    Page<PrsPayrollTemplatePO> pagePrsPayrollTemplates(Map<String, Object> params);

    PrsPayrollTemplatePO getPrsPayrollTemplate(Map<String, Object> params);

    Boolean addPrsPayrollTemplate(Map<String, Object> params);

    Boolean updatePrsPayrollTemplate(Map<String, Object> params);
}
