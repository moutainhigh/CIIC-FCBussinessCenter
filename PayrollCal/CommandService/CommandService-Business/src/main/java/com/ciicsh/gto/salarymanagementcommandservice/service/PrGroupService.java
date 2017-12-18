package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/10/24.
 * @author jiangtianning
 */
public interface PrGroupService {

    /**
     * 获取薪资组列表
     * @param paramItem 查询参数
     * @return 薪资组列表
     */
    PageInfo<PrPayrollGroupPO> getList(PrPayrollGroupPO param, Integer pageNum, Integer pageSize);

    /**
     * 获取一个薪资组 By Id
     * @param id 查询参数
     * @return 薪资组实体
     */
    PrPayrollGroupPO getItemById(String id);

    /**
     * 更新一个薪资组
     * @param paramItem 更新参数
     * @return 更新条数
     */
    int updateItemById(PrPayrollGroupPO paramItem);

    /**
     * 插入一个薪资组
     * @param paramItem 待插入薪资组实体
     * @return 薪资组code
     */
    int addItem(PrPayrollGroupPO paramItem);

    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<String> getNameList(String managementId);

    /**
     * 从薪资组里删除薪资项
     * @param prItemId 薪资项id
     * @param prGroupId 薪资组id
     * @return 若删除成功返回(SUCCESS, 删除条数),否则返回(FAILURE, 导致删除失败的薪资项名称列表String List)
     */
    Map<String, Object> deletePrItemFromGroup(String prItemName, String prItemId, String prGroupId);

    /**
     * 导入薪资组
     * @param from
     * @param to
     * @return 导入结果
     */
    boolean importPrGroup(String from, String to);

    /**
     * 删除薪资组
     * @param prGroupId
     * @return
     */
    int deleteByIds(List<String> prGroupId);

    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<KeyValuePO> getPayrollGroupNames(String managementId);
}
