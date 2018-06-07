package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagementcommandservice.dao.*;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CommonServiceConst;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by jiangtianning on 2017/10/24.
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
    private CodeGenerator codeGenerator;

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
        if (paramItem.getApprovalStatus() == null) {
            paramItem.setApprovalStatus(0);
        }
        return prPayrollGroupMapper.updateItemByCode(paramItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int addItem(PrPayrollGroupPO paramItem) {
        //检查是否有重复薪资组模板名称
        if (this.nameExistCheck(paramItem.getGroupName(), paramItem)) {
            throw new BusinessException("重复的薪资组名称");
        }
        paramItem.setGroupCode(codeGenerator.genPrGroupCode(paramItem.getManagementId()));
        paramItem.setVersion(DEFAULT_VERSION);
        paramItem.setGroupName(paramItem.getManagementId() + "-" + paramItem.getGroupName());
        paramItem.setApprovalStatus(ApprovalStatusEnum.APPROVE.getValue());
        int result;
        if (StringUtils.isEmpty(paramItem.getGroupTemplateCode())) {
            List<PrPayrollBaseItemPO> paramList = prPayrollBaseItemMapper.selectList(
                    new EntityWrapper<>(new PrPayrollBaseItemPO()));
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
                        item.setDisplayPriority(CommonServiceConst.DEFAULT_DIS_PRIORITY);
                        item.setCalPriority(CommonServiceConst.DEFAULT_CAL_PRIORITY);
                        return item;
                    })
                    .collect(Collectors.toList());
            itemService.addList(itemList);
            result = prPayrollGroupMapper.insert(paramItem);
            return result;
        }

        List<PrPayrollItemPO> prItemListFromGroupTemplate =
                itemService.getListByGroupTemplateCode(paramItem.getGroupTemplateCode(),0,0).getList();
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
        itemService.addList(itemList);
        result = prPayrollGroupMapper.insert(paramItem);
        return result;
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

        //检查是否有重复薪资组模板名称
        if (this.nameExistCheck(newEntity.getGroupName(), newEntity)) {
            throw new BusinessException("重复的薪资组名称");
        }
        newEntity.setGroupCode(codeGenerator.genPrGroupCode(newEntity.getManagementId()));
        newEntity.setVersion(DEFAULT_VERSION);
        newEntity.setApprovalStatus(ApprovalStatusEnum.APPROVE.getValue());
        int prGroupAddResult = prPayrollGroupMapper.insert(newEntity);
        if (prGroupAddResult == 0) {
            throw new BusinessException("复制薪资组失败");
        }
        List<PrPayrollItemPO> itemList = itemService.getListByGroupCode(srcEntity.getGroupCode());
        itemList.forEach(i -> {
            i.setPayrollGroupCode(newEntity.getGroupCode());
        });
        int itemAddResult = itemService.addList(itemList);
        if (itemAddResult == 0) {
            throw new BusinessException("复制薪资项失败");
        }
        return true;
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
            prPayrollGroupHistoryPO.setVersion(version);
            prPayrollGroupHistoryPO.setPayrollGroupHistory(itemsJsonStr);
            prPayrollGroupHistoryPO.setCreatedBy(UserContext.getUserId());
            prPayrollGroupHistoryPO.setModifiedBy(UserContext.getUserId());
            prPayrollGroupHistoryMapper.insert(prPayrollGroupHistoryPO);
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
    public List<HashMap<String, String>> getPrGroupNameList(String query, String managementId) {
        List<HashMap<String, String>> result = new ArrayList<>(50);
        result = prPayrollGroupMapper.selectGroupNameListByName(query, managementId);
        return result;
    }

    // 从公式中获取薪资项名称列表
    private List<String> getPayItems(String formula){
        List<String> names = new LinkedList<>();
        Pattern p = Pattern.compile(PAY_ITEM_REGEX);
        Matcher m = p.matcher(formula);
        while(m.find()) {
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
            String name = param.getManagementId() + "-" + userInputName;
            nameExistFlg = prPayrollGroupMapper.selectCountGroupByNameAndManagement(name, param.getManagementId());
        }
        return nameExistFlg != 0;
    }
}
