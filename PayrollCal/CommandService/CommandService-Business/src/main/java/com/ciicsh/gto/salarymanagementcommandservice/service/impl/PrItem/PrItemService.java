package com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem;

import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrItemInAccountSetPO;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;

/**
 * Created by jiangtianning on 2017/11/6.
 * @author jiangtianning
 */
public interface PrItemService {

    /**
     * 获取薪资项列表 from 薪资组 无草稿项
     * @param groupCode
     * @return 结果列表
     */
    List<PrPayrollItemPO> getListByGroupCode(String groupCode);

    /**
     * 获取薪资项列表 from 薪资组
     * @param groupCode
     * @param draftFlg
     * @return
     */
    List<PrPayrollItemPO> getListByGroupCode(String groupCode, boolean draftFlg);

    /**
     * 获取薪资项列表 from 薪资组 分页
     * @param code
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupCode(String code, Integer pageNum, Integer pageSize);

    /**
     * 获取薪资项列表 from 薪资组 分页 无草稿项
     * @param groupCode
     * @param pageNum
     * @param pageSize
     * @param draftFlg
     * @return
     */
    PageInfo<PrPayrollItemPO> getListByGroupCode(String groupCode, Integer pageNum, Integer pageSize, boolean draftFlg);

    /**
     * 获取薪资项列表 from 薪资组模板 无草稿项
     * @param groupTemplateCode
     * @return 结果列表
     */
    List<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode);

    /**
     * 获取薪资项列表 from 薪资组模板
     * @param groupTemplateCode
     * @param draftFlg
     * @return 结果列表
     */
    List<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode, boolean draftFlg);

    /**
     * 获取薪资项列表 from 薪资组模板 分页 无草稿项
     * @param groupTemplateCode
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode, Integer pageNum, Integer pageSize);

    /**
     * 获取薪资项列表 from 薪资组模板 分页
     * @param groupTemplateCode
     * @param pageNum
     * @param pageSize
     * @param draftFlg
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode, Integer pageNum, Integer pageSize, boolean draftFlg);

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
    int addList(List<PrPayrollItemPO> paramList);

    /**
     * 更新一个薪资项模板
     * @param param 更新薪资项实体
     * @return 更新条数
     */
    int updatePrItemById(PrPayrollItemPO param);

    /**
     * 获取薪资项类型列表
     * @return
     */
    List<HashMap<String, Object>> getTypeList();

    /**
     * 根据薪资项ID删除薪资项
     *
     * @param id    薪资项id
     * @return 删除条数
     */
    int deletePrItemById(Integer id);

    /**
     * 更新显示顺序
     * @param ids
     * @return 更新结果
     */
    boolean updateDisplayPriority(List<Integer> ids);

    /**
     * 更新计算顺序
     * @param ids
     * @return 更新结果
     */
    boolean updateCalPriority(List<Integer> ids);


    List<PrItemInAccountSetPO> selectItemNames(String batchCode);

    /**
     * 根据薪资项id查询薪资项明细
     * @param id 薪资项id
     * @return 薪资项明细
     */
    PrPayrollItemPO selectById(Integer id);

}
