package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeGroupInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;

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

    /**
     *  调用公共服务根据国家编码查询国家名称
     * @param countryCode 国家编码
     * @return String 国家名称
     */
    String getCountryName(String countryCode);

    /**
     *  调用公共服务根据城市编码查询城市名称
     * @param cityCode 城市编码
     * @return String 城市名称
     */
    String getCityName(String cityCode);

    /**
     *  调用公共服务根据省份编码查询省份名称
     * @param provinceCode 省份编码
     * @return String 省份名称
     */
    String getProvinceName(String provinceCode);

    /**
     * 调用结算中心接口生成报盘文件
     *
     * @return
     */
    List<OfferDocumentFilePO> generateOfferDocumentFile(SalaryGrantEmployeeGroupInfoBO groupInfoBO, OfferDocumentPO offerDocumentPO);

    /**
     *  调用公共服务根据用户id查询用户名称
     * @param userId 用户id
     * @return String 用户名称
     */
    String getUserNameById(String userId);
}
