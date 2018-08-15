package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import java.util.Map;

/**
 * <p>
 * 公共服务接口 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-07
 */
public interface CommonService {
    /**
     *  调用公共服务根据字典类型值和字典项值获取字典项内容
     * @param dicValue 字典类型值
     * @param dicItemValue 字典项值
     * @return String 字典项内容
     */
    String getNameByValue(String dicValue, String dicItemValue);

    /**
     *  调用公共服务生成薪资发放任务单的entity_id
     * @param entityParam 定义薪资发放idCode
     * @return String ENTITY_ID
     */
    String getEntityIdForSalaryGrantTask(Map entityParam);

    /**
     *  调用公共服务根据国家编码查询国家名称
     * @param countryCode 国家编码
     * @return String 国家名称
     */
    String getCountryName(String countryCode);
}
