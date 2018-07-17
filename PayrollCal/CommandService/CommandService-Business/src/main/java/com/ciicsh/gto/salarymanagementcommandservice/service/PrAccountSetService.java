package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by jiangtianning on 2017/11/1.
 */
public interface PrAccountSetService {


    /**
     * 新增薪资账套
     * @param payrollAccountSetPO 新增薪资实体
     * @return 是否新增成功
     */
    Boolean addAccountSet(PrPayrollAccountSetPO payrollAccountSetPO);


    /**
     * 编辑薪资账套
     * @param payrollAccountSetPO 新增薪资实体
     * @return 是否编辑成功
     */
    Boolean editAccountSet(PrPayrollAccountSetPO payrollAccountSetPO);


    /**
     * 编辑薪资账套是否禁用/启用
     * @param payrollAccountSetPO 新增薪资实体
     * @return 是否编辑成功
     */
    Integer editIsActive(PrPayrollAccountSetPO payrollAccountSetPO);


    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<KeyValuePO> getPayrollAccountSetNames(String managementId);


    /**
     * 获取薪资账套列表
     * @param payrollAccountSetPO 薪资账套实体
     * @param pageNum 页码
     * @param pageSize 每页数据记录
     * @return 薪资账套列表
     */
    PageInfo<PrPayrollAccountSetPO> getPayrollAccountSets(PrPayrollAccountSetPO payrollAccountSetPO, Integer pageNum, Integer pageSize);


    /**
     * 获取薪资账套扩展列表
     * @param extensionPO 薪资账套扩展实体
     * @param pageNum 页码
     * @param pageSize 每页数据记录
     * @return 薪资账套扩展列表
     */
    PageInfo<PrPayrollAccountSetExtensionPO> getPayrollAccountSetExts(PrPayrollAccountSetExtensionPO extensionPO, Integer pageNum, Integer pageSize);

    /*根据CODE 获取薪资帐套信息*/
    PrPayrollAccountSetPO getAccountSetInfo(@Param("accSetCode") String accSetCode);

    /**
     * 是否已经存在薪资账套
     * @param optPO
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistPayrollAccountSet(PrAccountSetOptPO optPO);


    /**
     * 根据薪资账套Code获取薪资账套扩展数据
     * @param accountSetCode 薪资账套Code
     * @return 薪资账套扩展数据
     */
    PrPayrollAccountSetExtensionPO getPayrollAccountSetExtByCode(String accountSetCode);

    /**
     * 获取薪资帐套列表带薪资项列表 by 管理方ID
     * @param managementId
     * @return
     */
    List<PrAccountSetOptPO> getAccountSetWithItemsByManagementId(String managementId);

    int countByEmpGroupCodeList(@Param("empGroupCodes") List<String> empGroupCodes);

}
