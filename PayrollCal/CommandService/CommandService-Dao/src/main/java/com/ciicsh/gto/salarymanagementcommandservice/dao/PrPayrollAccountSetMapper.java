package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrEmpGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetExtensionPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountItemOptPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，薪酬账套信息 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrPayrollAccountSetMapper extends BaseMapper<PrPayrollAccountSetPO> {
    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<KeyValuePO> getPayrollAccountSetNames(@Param("managementId") String managementId);

    /**
     * 获取薪资账套列表
     * @param payrollAccountSetPO 薪资账套实体
     * @return 薪资账套列表
     */
    List<PrPayrollAccountSetPO> getPayrollAccountSets(PrPayrollAccountSetPO payrollAccountSetPO);

    /**
     * 获取薪资账套扩展列表
     * @param extensionPO 薪资账套扩展实体
     * @return 薪资账套扩展列表
     */
    List<PrPayrollAccountSetExtensionPO> getPayrollAccountSetExts(PrPayrollAccountSetExtensionPO extensionPO);

    /**
     * 是否已经存在薪资账套
     * @param optPO
     * @return 返回值大于0表示记录已经存在，返回小于或者等于0表示记录不存在
     */
    Integer isExistPayrollAccountSet(PrAccountItemOptPO optPO);

    /**
     * 根据薪资账套Code获取薪资账套扩展数据
     * @param accountSetCode 薪资账套Code
     * @return 薪资账套扩展数据
     */
    PrPayrollAccountSetExtensionPO getPayrollAccountSetExtByCode(@Param("accountSetCode") String accountSetCode);
}