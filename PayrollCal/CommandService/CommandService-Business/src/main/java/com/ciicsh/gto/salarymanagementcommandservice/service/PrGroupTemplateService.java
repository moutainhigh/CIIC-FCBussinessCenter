package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrGroupTemplateEntity;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/11/2.
 * @author jiangtianning
 */
public interface PrGroupTemplateService {

    /**
     * 获取薪资项模板列表
     * @param param 查询参数
     * @param pageNum
     * @param pageSize
     * @return 薪资项模板列表
     */
    PageInfo<PrPayrollGroupTemplatePO> getList(PrPayrollGroupTemplatePO param, Integer pageNum, Integer pageSize);

    /**
     * 获取一个薪资项模板
     * @param entityId entityId
     * @return 薪资项模板
     */
    PrPayrollGroupTemplatePO getItemById(String entityId);

    /**
     * 插入一个薪资组模板
     * @param param 待插入薪资组模板entity
     * @return 插入条数
     */
    int newItem(PrPayrollGroupTemplatePO param);

    /**
     * 删除薪资组模板 BY entityId
     * @param ids entityId
     * @return 删除条数
     */
    int deleteByIds(List<String> ids);

    /**
     * 更新薪资组模板 BY entityId
     * @param param 更新参数
     * @return 更新条数
     */
    int updateItemById(PrPayrollGroupTemplatePO param);

    /**
     * 获取薪资组模板名称列表
     * @return 薪资组模板名称列表
     */
    List<HashMap<String, String>> getNameList();

    /**
     * 从薪资组模板里删除薪资项
     * @param prItemId 薪资项id
     * @param prGroupTemplateId 薪资组模板id
     * @return 若删除成功返回(SUCCESS, 删除条数),否则返回(FAILURE, 导致删除失败的薪资项名称列表String List)
     */
    Map<String, Object> deletePrItemFromGroupTemplate(String prGroupTemplateId, String prItemId, String prItemName);


    /**
     * 获取薪资组模板列表
     * @return 薪资组模板列表
     */
    List<KeyValuePO> getPayrollGroupTemplateNames();
}
