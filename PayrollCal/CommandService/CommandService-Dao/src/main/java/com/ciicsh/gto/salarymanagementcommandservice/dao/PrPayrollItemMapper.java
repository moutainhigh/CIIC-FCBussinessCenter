package com.ciicsh.gto.salarymanagementcommandservice.dao;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrItemInAccountSetPO;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * <p>
  * 配置表，薪资项明细表 Mapper 接口
 * </p>
 *
 * @author Neo Jiang
 * @since 2017-12-05
 */
@Mapper
@Component
public interface PrPayrollItemMapper extends BaseMapper<PrPayrollItemPO> {

    /**
     * 删除薪资项by code
     * @param codes
     * @return
     */
    Integer deleteItemByCodes(@Param("codes") List<String> codes);

    /**
     * 获取薪资项列表
     * @param extPO
     * @return 返回薪资项列表
     */
    List<PrPayrollItemPO> getPayrollItems(PayrollGroupExtPO extPO);

     /**
      * 插入一组薪资项
     * @param items
     * @return
     */
    Integer insertBatchItems(@Param("items") List<PrPayrollItemPO> items);

    /**
     * 删除薪资项by group code
     * @param groupCode
     * @return
     */
    Integer deleteItemByGroupCode(@Param("groupCode") String groupCode);

    /**
     * 删除薪资项by groupTemplate code
     * @param groupTemplateCode
     * @return
     */
    Integer deleteItemByGroupTemplateCode(@Param("groupTemplateCode") String groupTemplateCode);

    /**
     * 更新薪资项 by code
     * @param prPayrollItemPO
     * @return
     */
    Integer updateItemByCode(PrPayrollItemPO prPayrollItemPO);

    /**
     * 获取当前薪资组中的最大计算顺序
     * @param groupCode
     * @return
     */
    Integer selectMaxCalPriorityOfGroup(@Param("groupCode")String groupCode, @Param("isTemplate")Boolean isTemplate);

    /**
     * 获取薪资项列表(如果有alias 优先显示)
     * @param batchCode
     * @return
     */
    List<PrItemInAccountSetPO> selectItemNames(@Param("batchCode")String batchCode);

    /**
     * 获取已审核的薪资项
     * @param param
     * @return
     */
    List<PrPayrollItemPO> selectApprovedGroupItems(PrPayrollItemPO param);

    /**
     * 获取已审核的薪资项
     * @param param
     * @return
     */
    List<PrPayrollItemPO> selectApprovedGroupTemplateItems(PrPayrollItemPO param);

    /**
     * 将草稿薪资项插入审批通过表
     * @param items
     * @return
     */
    Integer insertBatchApprovedItemsByGroup(@Param("payrollGroupCode") String prGroupCode,
                                            @Param("prGroupTemplateCode") String prGroupTemplateCode);


    /**
     * 审批通过时，先根据薪资组模板编码删除正式表的薪资项，在同步草稿表的薪资项到正式表
     * 审批拒绝时，先根据薪资组模板编码删除草稿表的薪资项，在同步正式表的薪资项到草稿表
     *
     * @param approvalStatus      审批状态
     * @param prGroupTemplateCode 薪资组模板编码
     */
    void deleteApprovedItemByGroupTemplateCode(@Param("approvalStatus") int approvalStatus, @Param("prGroupTemplateCode") String prGroupTemplateCode);

    /**
     * 审批通过时，先根据薪资组模板编码删除正式表的薪资项，在同步草稿表的薪资项到正式表
     * 审批拒绝时，先根据薪资组模板编码删除草稿表的薪资项，在同步正式表的薪资项到草稿表
     *
     * @param approvalStatus      审批状态
     * @param prGroupTemplateCode 薪资组模板编码
     */
    void insertApprovedItemByGroupTemplateCode(@Param("approvalStatus") int approvalStatus, @Param("prGroupTemplateCode") String prGroupTemplateCode);

}