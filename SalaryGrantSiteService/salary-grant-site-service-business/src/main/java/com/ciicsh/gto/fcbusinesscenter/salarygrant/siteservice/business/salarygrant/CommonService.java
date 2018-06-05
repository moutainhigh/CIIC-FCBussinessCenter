package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryProxyDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeGroupInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.SalaryBatchDTO;

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

    /**
     * 调用账单中心接口查询雇员薪酬服务费
     *
     * @param salaryGrantSubTaskCode
     * @param employeePOList
     * @return
     */
    SalaryProxyDTO selectEmployeeServiceFeeAmount(String salaryGrantSubTaskCode, List<SalaryGrantEmployeePO> employeePOList);

    /**
     * 调用结算中心发放工资清单接口
     *
     * @param taskPaymentBO
     * @param employeePaymentBOList
     * @return
     */
    SalaryBatchDTO saveSalaryBatchData(SalaryGrantTaskPaymentBO taskPaymentBO, List<SalaryGrantEmployeePaymentBO> employeePaymentBOList);

    /**
     * 调用客服中心暂缓池操作接口
     *
     * @param salaryGrantTaskBO
     * @param employeeList
     * @return
     */
    Boolean addDeferredPool(SalaryGrantTaskBO salaryGrantTaskBO, List<SalaryGrantEmployeePO> employeeList);
}
