package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupHistoryPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.github.pagehelper.PageInfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/10/24.
 * @author jiangtianning
 */
public interface PrGroupService {

    /**
     * 获取薪资组列表
     * @param param 查询参数
     * @return 薪资组列表
     */
    PageInfo<PrPayrollGroupPO> getList(PrPayrollGroupPO param, Integer pageNum, Integer pageSize);

    /**
     * 根据薪资组模板获取薪资组列表
     * @param param 查询参数
     * @return 薪资组列表
     */
    List<PrPayrollGroupPO> getListByTemplateCode(String templateCode);

    /**
     * 获取一个薪资组 By code
     * @param code 查询参数
     * @return 薪资组实体
     */
    PrPayrollGroupPO getItemByCode(String code);

    /**
     * 更新一个薪资组
     * @param paramItem 更新参数
     * @return 更新条数
     */
    int updateItemByCode(PrPayrollGroupPO paramItem);

    /**
     * 插入一个薪资组
     * @param paramItem 待插入薪资组实体
     * @return 薪资组code
     */
    int addItem(PrPayrollGroupPO paramItem);

    /**
     * 导入薪资组
     * @param from
     * @param to
     * @return 导入结果
     */
    boolean importPrGroup(String from, String to, Boolean fromTemplate);

    /**
     * 删除薪资组
     * @param codes
     * @return
     */
    int deleteByCodes(List<String> codes);

    /**
     * 获取薪资组名称列表
     * @param managementId 管理方ID
     * @return 薪资组名称列表
     */
    List<KeyValuePO> getPayrollGroupNames(String managementId);

    /**
     * 复制薪资组
     * @param srcEntity
     * @param newEntity
     * @return
     */
    boolean copyPrGroup(PrPayrollGroupPO srcEntity, PrPayrollGroupPO newEntity);

    /**
     * 审核薪资租
     * @param paramItem
     * @return
     */
    boolean approvePrGroup(PrPayrollGroupPO paramItem);

    /**
     * 获取上个版本数据
     * @param srcCode
     * @return
     */
    PrPayrollGroupHistoryPO getLastVersion(String srcCode);

    /**
     * 获取薪资组名称列表
     * @return
     */
    List<HashMap<String, String>> getPrGroupNameList(String query, String managementId);
}
