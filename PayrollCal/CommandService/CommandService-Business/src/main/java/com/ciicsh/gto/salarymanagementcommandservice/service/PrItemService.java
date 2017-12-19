package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/11/6.
 */
public interface PrItemService {

    /**
     * 获取薪资项列表 from 薪资组
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupId(String groupId, Integer pageNum, Integer pageSize);

    /**
     * 获取薪资项列表 from 薪资组模板
     * @param groupId
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupTemplateId(String groupId, Integer pageNum, Integer pageSize);

    /**
     * 查询薪资项列表
     * @param paramItem
     * @param pageNum
     * @return
     */
    PageInfo<PrItemEntity> searchPrItemList(PrItemEntity paramItem, Integer pageNum);

    /**
     * 获取一个薪资项
     * @param id 查询参数
     * @return 结果项
     */
    PrPayrollItemPO getItemById(String id);

    /**
     * 插入一个薪资项
     * @param param 待插入实体
     * @return 插入结果
     */
    int addItem(PrPayrollItemPO param);

    /**
     * 插入一组薪资项
     * @param paramList 待插入列表
     * @return 插入条数
     */
    int addList(List<PrItemEntity> paramList);

    /**
     * 获取薪资项名称列表
     * @param managementId 管理方ID
     * @return 名称列表
     */
    List<String> getNameList(@Param("managementId") String managementId);

    /**
     * 更新一个薪资项模板
     * @param param 更新薪资项实体
     * @return 更新条数
     */
    Map<String, Object> updateItem(PrItemEntity param);

    /**
     * 获取薪资项类型列表
     * @param managementId
     * @return
     */
    List<Integer> getTypeList(@Param("managementId") String managementId);

//    /**
//     * 根据薪资组模板id获取薪资项列表
//     * @param prGroupTemplateId
//     * @return
//     */
//    List<PrItemEntity> getListByGroupTemplateId(String prGroupTemplateId);
//
//    /**
//     * 根据薪资组id获取薪资项列表
//     * @param prGroupId
//     * @return
//     */
//    List<PrItemEntity> getListByGroupId(String prGroupId);

    /**
     * 通过id来删除薪资项
     * @param ids 薪资项id
     * @return 删除条数
     */
    int deleteItemByIds(List<String> ids);

    /**
     * 删除一个薪资组中的所有薪资项
     * @param prGroupId
     * @return
     */
    int deleteItemByPrGroupId(String prGroupId);


    /**
     * 获取薪资项列表
     * @param extPO
     * @return 返回薪资项列表
     */
    List<PrPayrollItemPO> getPayrollItems(PayrollGroupExtPO extPO);
}
