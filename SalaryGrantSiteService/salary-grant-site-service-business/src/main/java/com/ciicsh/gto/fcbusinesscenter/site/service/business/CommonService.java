package com.ciicsh.gto.fcbusinesscenter.site.service.business;

import java.util.Map;

/**
 * <p>
 * 公共服务接口 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-30
 */
public interface CommonService {

    /**
     *  调用公共服务生成薪资发放任务单的entity_id
     * @param entityParam 定义薪资发放idCode
     * @return String ENTITY_ID
     */
    String getEntityIdForSalaryGrantTask(Map entityParam);
}
