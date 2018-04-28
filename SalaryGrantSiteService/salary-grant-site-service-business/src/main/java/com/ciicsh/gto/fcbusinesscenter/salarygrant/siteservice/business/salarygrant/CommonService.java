package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import java.util.List;
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

    /**
     *  调用公共服务根据字典类型值和字典项值获取字典项内容
     * @param dicValue 字典类型值
     * @param dicItemValue 字典项值
     * @return String 字典项内容
     */
    String getNameByValue(String dicValue, String dicItemValue);

    /**
     *  调用公共服务根据字典类型值获取字典项内容列表
     * @param dicValue 字典类型值
     * @return List 字典项内容列表
     */
    List getNameByValueForList(String dicValue);
}
