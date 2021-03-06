package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.EmpExtendFieldTemplateProxy;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmployeeExtendFieldDTO;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.dao.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CommonServiceConst;
import com.ciicsh.gto.salarymanagementcommandservice.util.DateUtil;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mongodb.BasicDBObject;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/24.
 *
 * @author jiangtianning
 */
@Service
public class PrGroupServiceImpl implements PrGroupService {

    @Autowired
    private PrPayrollGroupMapper prPayrollGroupMapper;

    @Autowired
    private PrPayrollBaseItemMapper prPayrollBaseItemMapper;

    @Autowired
    private PrItemService itemService;

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private PrPayrollGroupHistoryMapper prPayrollGroupHistoryMapper;

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    @Autowired
    private PrPayrollAccountItemRelationMapper prPayrollAccountItemRelationMapper;

    @Autowired
    private CodeGenerator codeGenerator;

    @Autowired
    private EmpExtendFieldTemplateProxy empExtendFieldTemplateProxy;

    private final static String PAY_ITEM_REGEX = "\\[([^\\[\\]]+)\\]";

    private final static String DEFAULT_VERSION = "1.0";

    @Override
    public PageInfo<PrPayrollGroupPO> getList(PrPayrollGroupPO param, Integer pageNum, Integer pageSize) {
        List<PrPayrollGroupPO> resultList;
        PageHelper.startPage(pageNum, pageSize);
        resultList = prPayrollGroupMapper.selectListByEntityUseLike(param);
        return new PageInfo<>(resultList);
    }

    @Override
    public List<PrPayrollGroupPO> getListByTemplateCode(String templateCode) {
        PrPayrollGroupPO param = new PrPayrollGroupPO();
        param.setGroupTemplateCode(templateCode);
        param.setIsActive(true);
        EntityWrapper<PrPayrollGroupPO> ew = new EntityWrapper<>(param);
        return prPayrollGroupMapper.selectList(ew);
    }

    @Override
    public PrPayrollGroupPO getItemByCode(String code) {
        PrPayrollGroupPO param = new PrPayrollGroupPO();
        param.setGroupCode(code);
        return prPayrollGroupMapper.selectOne(param);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateItemByCode(PrPayrollGroupPO paramItem) {
        // 验证同一管理方下是否有重复薪资组模板名称，如有，不允许编辑成功
        List<PrPayrollGroupPO> resultList = prPayrollGroupMapper.queryGroupListByNameAndManagementId(paramItem.getGroupName(), paramItem.getManagementId());
        resultList.removeIf(group -> group.getGroupCode().equals(paramItem.getGroupCode()));
        if (resultList != null && resultList.size() > 0) {
            throw new BusinessException("重复的薪资组名称");
        }
        return prPayrollGroupMapper.updateItemByCode(paramItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int addItem(PrPayrollGroupPO paramItem) {
        //检查是否有重复薪资组名称
        if (this.nameExistCheck(paramItem.getGroupName(), paramItem)) {
            throw new BusinessException("重复的薪资组名称");
        }
        paramItem.setGroupCode(codeGenerator.genPrGroupCode(paramItem.getManagementId()));
        paramItem.setVersion(DEFAULT_VERSION);
        paramItem.setGroupName(paramItem.getManagementId() + "-" + paramItem.getGroupName());
        paramItem.setApprovalStatus(ApprovalStatusEnum.APPROVE.getValue());
        int result;
        //继承薪资组模板ID不为空时
        if (StringUtils.isEmpty(paramItem.getGroupTemplateCode())) {
            List<PrPayrollBaseItemPO> paramList = prPayrollBaseItemMapper.selectList(
                    new EntityWrapper<>(new PrPayrollBaseItemPO()));
            // 初始化基本薪资项的显示顺序
            for (PrPayrollBaseItemPO p : paramList) {
                p.setDisplayPriority(paramList.indexOf(p));
            }
            List<PrPayrollItemPO> itemList = paramList.stream()
                    .map(i -> {
                        PrPayrollItemPO item = new PrPayrollItemPO();
                        BeanUtils.copyProperties(i, item);
                        item.setId(null);
                        item.setItemName(i.getBaseItemName());
                        item.setBaseItemCode(i.getBaseItemCode());
                        item.setItemType(i.getBaseItemType());
                        item.setCanLock(false);
                        item.setPayrollGroupCode(paramItem.getGroupCode());
                        item.setManagementId(paramItem.getManagementId());
                        item.setDisplayPriority(i.getDisplayPriority());
                        item.setCalPriority(CommonServiceConst.DEFAULT_CAL_PRIORITY);
                        item.setDefaultValue(i.getDataType() == DataTypeEnum.DATE.getValue() ? DateUtil.getNowDate(LocalDateTime.now(), DateUtil.YYYY_MM_DD) : i.getDefaultValue());
                        return item;
                    })
                    .collect(Collectors.toList());

            //添加雇员扩展字段薪资项
            List<PrPayrollItemPO> extendItemDTOList = getExtendItems(paramItem.getEmpExtendFieldTemplateId(), paramItem.getManagementId(), paramItem.getGroupCode());
            // 将扩展项List添加到返回结果中
            if (!CollectionUtils.isEmpty(extendItemDTOList)) {
                itemList.addAll(extendItemDTOList);
            }

            itemService.addList(itemList);
            result = prPayrollGroupMapper.insert(paramItem);
            // 1,新建薪资组,默认审核通过,同步草稿薪资项到审核通过表
            prPayrollItemMapper.insertBatchApprovedItemsByGroup(paramItem.getGroupCode(), null);
            // 2,审批通过,保存历史数据
            savePrPayrollGroupHistory(paramItem, itemList,null);
            return result;
        }

        //根据薪资组模版编码查询薪资项List
        List<PrPayrollItemPO> prItemListFromGroupTemplate =
                itemService.getListByGroupTemplateCode(paramItem.getGroupTemplateCode(), 0, 0).getList();
        List<PrPayrollItemPO> itemList = prItemListFromGroupTemplate.stream()
                .map(i -> {
                    PrPayrollItemPO item = new PrPayrollItemPO();
                    BeanUtils.copyProperties(i, item);
                    item.setId(null);
                    item.setManagementId(paramItem.getManagementId());
                    item.setParentItemCode(i.getItemCode());
                    item.setPayrollGroupCode(paramItem.getGroupCode());
                    return item;
                })
                .collect(Collectors.toList());

        //添加雇员扩展字段薪资项
        List<PrPayrollItemPO> extendItemDTOList = getExtendItems(paramItem.getEmpExtendFieldTemplateId(), paramItem.getManagementId(), paramItem.getGroupCode());
        // 将扩展项List添加到返回结果中
        if (!CollectionUtils.isEmpty(extendItemDTOList)) {
            itemList.addAll(extendItemDTOList);
        }

        itemService.addList(itemList);
        result = prPayrollGroupMapper.insert(paramItem);
        prPayrollItemMapper.insertBatchApprovedItemsByGroup(paramItem.getGroupCode(), null);
        return result;
    }

    /**
     * 根据模板ID获取扩展薪资项
     *
     * @param templateId
     * @return
     */
    private List<PrPayrollItemPO> getExtendItems(Long templateId, String managementId, String payrollGroupCode) {
        if (ObjectUtils.isEmpty(templateId)) {
            return null;
        }

        List<PrPayrollItemPO> extendItemDTOList = null;

        com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult<List<EmployeeExtendFieldDTO>> listJsonResult;
        try {
            listJsonResult = empExtendFieldTemplateProxy.listTemplateFields(templateId);

            if (!ObjectUtils.isEmpty(listJsonResult)) {
                extendItemDTOList = employeeExtendFieldList2PrPayrollItemList(listJsonResult.getData(), managementId, payrollGroupCode);
            }

        } catch (Exception e) {
            return new ArrayList<>();
        }

        return extendItemDTOList;
    }

    /**
     * 雇员扩展模板字段List转换为薪资项明细字段List
     *
     * @param extendFieldDTOList
     * @return
     */
    private List<PrPayrollItemPO> employeeExtendFieldList2PrPayrollItemList(List<EmployeeExtendFieldDTO> extendFieldDTOList, String managementId, String payrollGroupCode) {
        List<PrPayrollItemPO> payrollItemPOList = new ArrayList<>();

        if (CollectionUtils.isEmpty(extendFieldDTOList)) {
            return payrollItemPOList;
        }

        extendFieldDTOList.forEach(employeeExtendFieldDTO -> {
            PrPayrollItemPO payrollItemPO = new PrPayrollItemPO();
            payrollItemPO.setItemType(1); //薪资项类型：1 - 固定输入项
            payrollItemPO.setItemName(employeeExtendFieldDTO.getFieldName()); //薪资项名称
            payrollItemPO.setExtendFlag(1); //扩展标识：1-扩展字段；0-非扩展字段
            //薪资项编码, 此处无需设置
            payrollItemPO.setManagementId(managementId); //所属管理方ID
            payrollItemPO.setPayrollGroupCode(payrollGroupCode); //薪资组编码
            payrollItemPO.setCreatedTime(new Date());
            payrollItemPO.setModifiedTime(new Date());

            Integer fieldType = employeeExtendFieldDTO.getFieldType(); //接口中DTO 字段类型（1.数值 2.文本 3日期 4选项）
            if (!ObjectUtils.isEmpty(fieldType)) {
                switch (fieldType) {
                    case 1:
                        payrollItemPO.setDataType(2); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                        break;
                    case 2:
                    case 4:
                    case 3:
                        payrollItemPO.setDataType(1); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                        break;
                    default:
                        break;
                }
            }

            payrollItemPO.setDefaultValue(""); //默认数字
            payrollItemPO.setItemCondition(""); //薪资项取值范围的条件描述
            payrollItemPO.setFormulaContent(""); //公式内容
            payrollItemPO.setCanLock(false); //是否可以上锁
            payrollItemPO.setCalPrecision(0); //计算精度
            payrollItemPO.setCalPriority(0); //计算顺序
            payrollItemPO.setDefaultValueStyle(2); //默认值处理方式： 1 - 使用历史数据 2 - 使用基本值
            payrollItemPO.setDecimalProcessType(1); //小数处理方式： 1 - 四舍五入 2 - 简单去位
            payrollItemPO.setDisplayPriority(0); //显示顺序
            payrollItemPO.setActive(true); //是否有效

            payrollItemPOList.add(payrollItemPO);
        });

        return payrollItemPOList;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean importPrGroup(String from, String to, Boolean fromTemplate) {

        //获取from薪资组中的薪资项
        List<PrPayrollItemPO> items;
        if (fromTemplate) {
            items = itemService.getListByGroupTemplateCode(from);
        } else {
            items = itemService.getListByGroupCode(from);
        }

        if (items == null || items.size() == 0) {
            throw new BusinessException("导入薪资组中无薪资项");
        }
        items.forEach(i -> {
            i.setPayrollGroupCode(to);
            i.setParentItemCode(null);
            i.setCreatedBy(UserContext.getUserId());
            i.setModifiedBy(UserContext.getUserId());
            i.setCreatedTime(new Date());
            i.setModifiedTime(new Date());
        });
        //删除原来存在于该薪资组的薪资项
        try {
            prPayrollItemMapper.deleteItemByGroupCode(to);
        } catch (RuntimeException e) {
            throw new BusinessException("导入目标薪资组中薪资项删除失败");
        }
        int insertItemResult = itemService.addList(items);
        if (insertItemResult == 0) {
            throw new BusinessException("导入薪资组中薪资项时失败");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int deleteByCodes(List<String> codes) {
        int result = 0;
        result = prPayrollGroupMapper.deleteByCodes(codes);
        codes.forEach(i -> prPayrollItemMapper.deleteItemByGroupCode(i));
        return result;
    }

    @Override
    public List<KeyValuePO> getPayrollGroupNames(String managementId) {
        List<KeyValuePO> keyValues = new ArrayList<>();
        try {
            keyValues = prPayrollGroupMapper.getPayrollGroupNames(managementId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyValues;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean copyPrGroup(PrPayrollGroupPO srcEntity, PrPayrollGroupPO newEntity) {

        // 检查是否有重复薪资组名称
        if (this.nameExistCheck(newEntity.getGroupName(), newEntity)) {
            throw new BusinessException("重复的薪资组名称");
        }
        newEntity.setGroupCode(codeGenerator.genPrGroupCode(newEntity.getManagementId()));
        newEntity.setVersion(DEFAULT_VERSION);
        newEntity.setApprovalStatus(ApprovalStatusEnum.APPROVE.getValue());
        newEntity.setGroupName(builderStr(newEntity.getManagementId(), "-", newEntity.getGroupName()));
        int prGroupAddResult = prPayrollGroupMapper.insert(newEntity);
        if (prGroupAddResult == 0) {
            throw new BusinessException("复制薪资组失败");
        }
        List<PrPayrollItemPO> itemList = itemService.getListByGroupCode(srcEntity.getGroupCode());
        itemList.forEach(i -> {
            i.setPayrollGroupCode(newEntity.getGroupCode());
            i.setManagementId(newEntity.getManagementId());
            i.setOperateType(newEntity.getOperateType());
        });
        itemService.addList(itemList);

        //薪资项扩展字段处理
        List<PrPayrollItemPO> employeeExtendFieldsList = getTemplateEmployeeExtendFields(newEntity.getEmpExtendFieldTemplateId(),newEntity.getManagementId(),newEntity.getGroupCode());
        if (null != newEntity.getEmpExtendFieldTemplateId()) {
            int itemAddResult = itemService.addList(employeeExtendFieldsList);
            if (itemAddResult == 0) {
                throw new BusinessException("复制薪资项失败");
            }
        }
        // 1,复制薪资组,默认审核通过,同步草稿薪资项到审核通过表
        prPayrollItemMapper.insertBatchApprovedItemsByGroup(newEntity.getGroupCode(), null);
        // 2,审批通过,保存历史数据
        savePrPayrollGroupHistory(newEntity, itemList,employeeExtendFieldsList);

        return true;
    }

    private List<PrPayrollItemPO> getTemplateEmployeeExtendFields(Long templateID, String managementId,String groupCode) {
        List<PrPayrollItemPO> extendFieldsList = new ArrayList<>();

        if (org.springframework.util.ObjectUtils.isEmpty(templateID)) {
            return extendFieldsList;
        }
        try {
            JsonResult<List<EmployeeExtendFieldDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplateFields(templateID);
            if (!ObjectUtils.isEmpty(listJsonResult)) {
                List<EmployeeExtendFieldDTO> extendFieldDTOList = listJsonResult.getData();
                if (!CollectionUtils.isEmpty(extendFieldDTOList)) {
                    extendFieldDTOList.forEach(extendFieldDTO -> {
                        PrPayrollItemPO dbObject = new PrPayrollItemPO();
                        dbObject.setItemName(extendFieldDTO.getFieldName());
                        dbObject.setItemType(1); //薪资项类型：1-固定输入项;2-可变输入项;3-计算项;4-系统项

                        Integer fieldType = extendFieldDTO.getFieldType(); //接口中DTO 字段类型（1.数值 2.文本 3日期 4选项）
                        switch (fieldType) {
                            case 1:
                                dbObject.setDataType(2); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                                break;
                            case 2:
                            case 4:
                            case 3:
                                dbObject.setDataType(1); //数据格式: 1-文本,2-数字,3-日期,4-布尔
                                break;
                            default:
                                break;
                        }

                        dbObject.setManagementId(managementId);
                        dbObject.setPayrollGroupCode(groupCode);
                        dbObject.setItemCode(codeGenerator.genPrItemCode(managementId)); //薪资项编码
                        dbObject.setDefaultValue(""); //默认数字
                        dbObject.setItemCondition(""); //薪资项取值范围的条件描述
                        dbObject.setFormulaContent(""); //公式内容
                        dbObject.setCanLock(false); //是否可以上锁
                        dbObject.setFullFormula("");
                        dbObject.setCalPrecision(0); //计算精度
                        dbObject.setCalPriority(0); //计算顺序
                        dbObject.setDefaultValueStyle(2); //默认值处理方式： 1 - 使用历史数据 2 - 使用基本值
                        dbObject.setDecimalProcessType(1); //小数处理方式： 1 - 四舍五入 2 - 简单去位
                        dbObject.setDisplayPriority(0); //显示顺序
                        dbObject.setCreatedBy(UserContext.getUser().getDisplayName());
                        dbObject.setModifiedBy(UserContext.getUser().getDisplayName());
                        dbObject.setExtendFlag(1);

                        dbObject.setIsActive(true);
                        extendFieldsList.add(dbObject);
                    });
                }

            }

        } catch (Exception e) {
            return new ArrayList<>();
        }

        return extendFieldsList;
    }


    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean approvePrGroup(PrPayrollGroupPO paramItem) {
        boolean result;
        if (paramItem.getApprovalStatus() == 2) {
            // 获取该薪资组中的薪资项数据
            List<PrPayrollItemPO> items = this.getListByGroupCode(paramItem.getGroupCode());
            String itemsJsonStr = JSON.toJSONString(items);
            // 获取更新前的薪资组数据
            PrPayrollGroupPO prGroup = getItemByCode(paramItem.getGroupCode());
            String version = prGroup.getVersion();
//            String groupJsonStr = JSON.toJSONString(prGroup);
//            JSONObject jsonObject = new JSONObject();
//            jsonObject.put(PayrollGroupHistoryKey.PR_ITEMS, itemsJsonStr);
//            jsonObject.put(PayrollGroupHistoryKey.PR_GROUP, groupJsonStr);
//            String history = JSON.toJSONString(itemsJsonStr);
            // 保存历史数据
            PrPayrollGroupHistoryPO prPayrollGroupHistoryPO = new PrPayrollGroupHistoryPO();
            prPayrollGroupHistoryPO.setPayrollGroupCode(paramItem.getGroupCode());
            prPayrollGroupHistoryPO.setVersion(String.valueOf(Double.parseDouble(version) + 1));
            prPayrollGroupHistoryPO.setPayrollGroupHistory(itemsJsonStr);
            prPayrollGroupHistoryPO.setCreatedBy(UserContext.getUserId());
            prPayrollGroupHistoryPO.setModifiedBy(UserContext.getUserId());
            prPayrollGroupHistoryMapper.insert(prPayrollGroupHistoryPO);
            /**
             * 同步审批通过的薪资组的薪资项到薪资账套薪资项表pr_payroll_account_item_relation
             * 1,查出所有应用此薪资组的薪资账套code -> accountCodeList
             * 2,根据薪资组code和账套code删除账套薪资项表中的薪资项数据
             * 3,插入审批通过的薪资项到账套薪资项表
             */
            HashMap<String, Object> paramMap = new HashMap<>();
            paramMap.put("payrollGroupCode", paramItem.getGroupCode());
            List<PrPayrollAccountItemRelationPO> accountCodeList = prPayrollAccountItemRelationMapper.selectAccountCodeList(paramMap);
            accountCodeList.forEach(accountPO -> {
                HashMap<String, Object> delParamMap = new HashMap<>();
                delParamMap.put("account_set_code", accountPO.getAccountSetCode());
                delParamMap.put("payroll_group_code", paramItem.getGroupCode());
                prPayrollAccountItemRelationMapper.deleteByMap(delParamMap);
                List<PrPayrollAccountItemRelationPO> relations = items
                        .stream()
                        .map(item -> toAccountItemPOFromItemPO(item, accountPO.getAccountSetCode()))
                        .collect(Collectors.toList());
                if (relations != null && relations.size() > 0) {
                    prPayrollAccountItemRelationMapper.insertAccountItemRelationList(relations);
                }
            });
        }
        // 更新审批历史
        ApprovalHistoryPO approvalHistoryPO = new ApprovalHistoryPO();
        approvalHistoryPO.setBizCode(paramItem.getGroupCode());
        approvalHistoryPO.setBizType(BizTypeEnum.PR_GROUP.getValue());
        approvalHistoryPO.setCreatedBy(UserContext.getUserId());
        approvalHistoryPO.setApprovalResult(paramItem.getApprovalStatus());
        approvalHistoryPO.setComments(paramItem.getComments());
        approvalHistoryPO.setCreatedName(UserContext.getName());
        approvalHistoryService.addApprovalHistory(approvalHistoryPO);

        // 处理审批通过和审批拒绝的草稿版薪资项和正式版薪资项,approvalStatus：2,通过; 3,拒绝;
        prPayrollItemMapper.deleteApprovedItemByCode(paramItem.getApprovalStatus(), null, paramItem.getGroupCode());
        prPayrollItemMapper.insertApprovedItemByCode(paramItem.getApprovalStatus(), null, paramItem.getGroupCode());

        // 更新审批薪资组
        int updateResult = prPayrollGroupMapper.updateItemByCode(paramItem);
        result = updateResult == 1;
        return result;
    }

    @Override
    public PrPayrollGroupHistoryPO getLastVersion(String srcCode) {
        return prPayrollGroupHistoryMapper.selectLastVersionByCode(srcCode);
    }

    @Override
    public List<HashMap<String, String>> getPrGroupNameList(String name, String managementId) {
        List<HashMap<String, String>> result = new ArrayList<>(50);
        result = prPayrollGroupMapper.selectGroupNameListByName(name, managementId);
        return result;
    }

    // 从公式中获取薪资项名称列表
    private List<String> getPayItems(String formula) {
        List<String> names = new LinkedList<>();
        Pattern p = Pattern.compile(PAY_ITEM_REGEX);
        Matcher m = p.matcher(formula);
        while (m.find()) {
            names.add(m.group(1));
        }
        return names;
    }

    private List<PrPayrollItemPO> getListByGroupCode(String groupCode) {
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupCode(groupCode);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        return prPayrollItemMapper.selectList(ew);
    }

    private boolean nameExistCheck(String userInputName, PrPayrollGroupPO param) {
        int nameExistFlg;
        if (userInputName == null) {
            nameExistFlg = prPayrollGroupMapper.selectCountGroupByNameAndManagement(param.getGroupName(), param.getManagementId());
        } else {
            String name = builderStr(param.getManagementId(), "-", userInputName);
            nameExistFlg = prPayrollGroupMapper.selectCountGroupByNameAndManagement(name, param.getManagementId());
        }
        return nameExistFlg != 0;
    }

    private String builderStr(String... strs) {
        StringBuffer buffer = new StringBuffer();
        for (String s : strs) {
            buffer.append(s);
        }
        return buffer.toString();
    }

    private PrPayrollAccountItemRelationPO toAccountItemPOFromItemPO(PrPayrollItemPO payrollItemPO, String accountCode) {
        PrPayrollAccountItemRelationPO relationPO = new PrPayrollAccountItemRelationPO();
        relationPO.setAccountSetCode(accountCode);
        relationPO.setPayrollGroupCode(payrollItemPO.getPayrollGroupCode());
        relationPO.setPayrollItemCode(payrollItemPO.getItemCode());
        relationPO.setPayrollItemAlias(payrollItemPO.getItemName());
        relationPO.setActive(true);
        relationPO.setCreatedTime(new Date());
        relationPO.setCreatedBy(UserContext.getUserId());
        relationPO.setModifiedTime(new Date());
        relationPO.setModifiedBy(UserContext.getUserId());
        return relationPO;

    }

    /**
     * 薪资组审批通过,保存历史数据
     * @param prPayrollGroupPO 薪资组详情
     * @param itemList 薪资项列表
     */
    private void savePrPayrollGroupHistory(PrPayrollGroupPO prPayrollGroupPO, List<PrPayrollItemPO> itemList,List<PrPayrollItemPO> employeeExtendFieldsList) {
        if(!CollectionUtils.isEmpty(itemList) && !CollectionUtils.isEmpty(employeeExtendFieldsList)){
            itemList.addAll(employeeExtendFieldsList);
        }
        // 审批通过，保存历史数据
        if (ApprovalStatusEnum.APPROVE.getValue() == prPayrollGroupPO.getApprovalStatus()) {
            // 保存历史数据
            PrPayrollGroupHistoryPO prPayrollGroupHistoryPO = new PrPayrollGroupHistoryPO();
            prPayrollGroupHistoryPO.setPayrollGroupCode(prPayrollGroupPO.getGroupCode());
            prPayrollGroupHistoryPO.setVersion(DEFAULT_VERSION);
            prPayrollGroupHistoryPO.setPayrollGroupHistory(JSON.toJSONString(itemList));
            prPayrollGroupHistoryPO.setCreatedBy(UserContext.getUserId());
            prPayrollGroupHistoryPO.setModifiedBy(UserContext.getUserId());
            prPayrollGroupHistoryMapper.insert(prPayrollGroupHistoryPO);

        }
    }
}
