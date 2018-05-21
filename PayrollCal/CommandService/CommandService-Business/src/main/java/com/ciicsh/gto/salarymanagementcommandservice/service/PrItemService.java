package com.ciicsh.gto.salarymanagementcommandservice.service;

import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrItemInAccountSetPO;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.annotations.Param;
import scala.annotation.meta.param;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/11/6.
 * @author jiangtianning
 */
public interface PrItemService {

    /**
     * 获取薪资项列表 from 薪资组
     * @param code
     * @return 结果列表
     */
    List<PrPayrollItemPO> getListByGroupCode(String groupCode);

    /**
     * 获取薪资项列表 from 薪资组 分页
     * @param code
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupCode(String code, Integer pageNum, Integer pageSize);

    /**
     * 获取薪资项列表 from 薪资组模板
     * @param code
     * @return 结果列表
     */
    List<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode);

    /**
     * 获取薪资项列表 from 薪资组模板 分页
     * @param code
     * @param pageNum
     * @param pageSize
     * @return 结果列表
     */
    PageInfo<PrPayrollItemPO> getListByGroupTemplateCode(String code, Integer pageNum, Integer pageSize);

    /**
     * 获取一个薪资项
     * @param id 查询参数
     * @return 结果项
     */
    PrPayrollItemPO getItemByCode(String id);

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
    int updateItem(PrPayrollItemPO param);

    /**
     * 获取薪资项类型列表
     * @return
     */
    List<HashMap<String, Object>> getTypeList();

    /**
     * 通过id来删除薪资项
     * @param codes 薪资项codes
     * @return 删除条数
     */
    int deleteItemByCodes(List<String> codes);

    /**
     * 删除一个薪资组中的所有薪资项
     * @param prGroupId
     * @return
     */
    int deleteItemByPrGroupCode(String groupCode);


    /**
     * 获取薪资项列表
     * @param extPO
     * @return 返回薪资项列表
     */
    List<PrPayrollItemPO> getPayrollItems(PayrollGroupExtPO extPO);

    /**
     * 更新显示顺序
     * @param codes
     * @return
     */
    boolean updateDisplayPriority(List<String> codes);

    /**
     * 更新计算顺序
     * @param codes
     * @return
     */
    boolean updateCalPriority(List<String> codes);


    List<PrItemInAccountSetPO> selectItemNames(String batchCode);

}
