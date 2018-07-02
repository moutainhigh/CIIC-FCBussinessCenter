package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.BizTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.*;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupTemplateHistoryMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupTemplateMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.ApprovalHistoryService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupTemplateService;
import com.ciicsh.gto.salarymanagementcommandservice.service.impl.PrItem.PrItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiangtianning on 2017/11/2.
 * @author jiangtianning
 */
@Service
public class PrGroupTemplateServiceImpl implements PrGroupTemplateService {


    @Autowired
    private PrPayrollGroupTemplateMapper prPayrollGroupTemplateMapper;

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private PrItemService prItemService;

    @Autowired
    private PrGroupService prGroupService;

    @Autowired
    private PrPayrollGroupTemplateHistoryMapper prPayrollGroupTemplateHistoryMapper;

    @Autowired
    private ApprovalHistoryService approvalHistoryService;

    private final static String PAY_ITEM_REGEX = "\\[([^\\[\\]]+)\\]";

    @Override
    public PageInfo<PrPayrollGroupTemplatePO> getListPage(PrPayrollGroupTemplatePO param, Integer pageNum, Integer pageSize) {
        List<PrPayrollGroupTemplatePO> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, pageSize);
        try {
            resultList = prPayrollGroupTemplateMapper.selectListByEntityUseLike(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrPayrollGroupTemplatePO> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public List<PrPayrollGroupTemplatePO> getList(PrPayrollGroupTemplatePO param) {
        return prPayrollGroupTemplateMapper.selectListByEntityUseLike(param);
    }

    @Override
    public PrPayrollGroupTemplatePO getItemByCode(String code) {
        PrPayrollGroupTemplatePO param = new PrPayrollGroupTemplatePO();
        param.setGroupTemplateCode(code);
        PrPayrollGroupTemplatePO result = prPayrollGroupTemplateMapper.selectOne(param);
        return result;
    }

    @Override
    public int newItem(PrPayrollGroupTemplatePO param) {
        //检查是否有重复薪资组模板名称
        int nameExistFlg = prPayrollGroupTemplateMapper.selectCountGroupTemplateByName(param.getGroupTemplateName());
        if (nameExistFlg > 0) {
            throw new BusinessException("重复的薪资组模板名称");
        }
        param.setApprovalStatus(ApprovalStatusEnum.APPROVE.getValue());
        int result = prPayrollGroupTemplateMapper.insert(param);
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int deleteByCodes(List<String> codes) {
        int result = prPayrollGroupTemplateMapper.deleteByCodes(codes);
        codes.forEach(i -> prPayrollItemMapper.deleteItemByGroupTemplateCode(i));
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateItemByCode(PrPayrollGroupTemplatePO param) {
        // 编辑时校验薪资组模板是否有同名
        EntityWrapper<PrPayrollGroupTemplatePO> wrapper = new EntityWrapper<>();
        wrapper.eq("group_template_name", param.getGroupTemplateName());
        List<PrPayrollGroupTemplatePO> templateList = prPayrollGroupTemplateMapper.selectList(wrapper);
        if (templateList != null) {
            templateList.removeIf(template -> template.getGroupTemplateCode().equals(param.getGroupTemplateCode()));
            if (templateList.size() > 0 ) {
                throw new BusinessException("重复的薪资组模板名称");
            }
        }
        return prPayrollGroupTemplateMapper.updateItemByCode(param);
    }

    @Override
    public List<HashMap<String, String>> getNameList() {
        List<HashMap<String, String>> resultList = prPayrollGroupTemplateMapper.selectNameList();
        return resultList;
    }

    @Override
    public Map<String, Object> deletePrItemFromGroupTemplate(String prGroupTemplateId, String prItemId, String prItemName) {
//        Map<String, Object> result = new HashMap<>();
//        int deleteRows = 0;
//        List<String> failList = new ArrayList<>();
//        //对薪资组模板本身的影响
//        PrGroupTemplateEntity prGroupTemplateEntity = prGroupTemplateMapper.selectItemById(prGroupTemplateId);
//        List<PrItemEntity> prItemEntityList = null;
//        try {
//            prItemEntityList = prGroupTemplateEntity.getPrItemEntityList();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        filterPrItem(prItemEntityList,prItemName);
//
//        //对继承该薪资组模板的薪资组的影响
//        List<String> ids = new ArrayList<>();
//        List<PrItemEntity> items = null;
//        try {
//            items = prItemService.getListByGroupTemplateId(prGroupTemplateId);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        filterPrItem(items,prItemName);
//
//        if (failList.size() == 0) {
//            try {
//                items.stream().forEach(i -> {
//                    if (i.getName().equals(prItemName)){
//                        ids.add(i.getEntityId());
//                    }
//                });
//                ids.stream().forEach(i->prItemService.deleteItemById(i));
//                deleteRows = ids.size();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            result.put("SUCCESS", deleteRows);
//        } else {
//            result.put("FAILURE", failList);
//        }
//        return result;
        return null;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean approvePrGroupTemplate(PrPayrollGroupTemplatePO paramItem){
        boolean result;
        if (paramItem.getApprovalStatus() == 2) {
            // 获取该薪资组模板中的薪资项数据
            List<PrPayrollItemPO> items = prItemService.getListByGroupTemplateCode(paramItem.getGroupTemplateCode());
            String itemsJsonStr = JSON.toJSONString(items);
            // 获取更新前的薪资组模板数据
            PrPayrollGroupTemplatePO prGroupTemplate = this.getItemByCode(paramItem.getGroupTemplateCode());
            String version = prGroupTemplate.getVersion();
            // 保存历史数据
            PrPayrollGroupTemplateHistoryPO prPayrollGroupTemplateHistoryPO = new PrPayrollGroupTemplateHistoryPO();
            prPayrollGroupTemplateHistoryPO.setPayrollGroupTemplateCode(paramItem.getGroupTemplateCode());
            prPayrollGroupTemplateHistoryPO.setVersion(version);
            prPayrollGroupTemplateHistoryPO.setPayrollGroupTemplateHistory(itemsJsonStr);
            prPayrollGroupTemplateHistoryPO.setCreatedBy(UserContext.getUserId());
            prPayrollGroupTemplateHistoryPO.setModifiedBy(UserContext.getUserId());
            prPayrollGroupTemplateHistoryMapper.insert(prPayrollGroupTemplateHistoryPO);
        }
        // 更新审批历史
        ApprovalHistoryPO approvalHistoryPO = new ApprovalHistoryPO();
        approvalHistoryPO.setBizCode(paramItem.getGroupTemplateCode());
        approvalHistoryPO.setBizType(BizTypeEnum.PR_GROUP_TEMPLATE.getValue());
        approvalHistoryPO.setApprovalResult(paramItem.getApprovalStatus());
        approvalHistoryPO.setComments(paramItem.getComments());
        approvalHistoryPO.setCreatedName(UserContext.getName());
        approvalHistoryPO.setCreatedBy(UserContext.getUserId());
        approvalHistoryService.addApprovalHistory(approvalHistoryPO);
        // 处理审批通过和审批拒绝的草稿版薪资项和正式版薪资项,approvalStatus：2,通过; 3,拒绝;
        prPayrollItemMapper.deleteApprovedItemByGroupTemplateCode(paramItem.getApprovalStatus(), paramItem.getGroupTemplateCode());
        prPayrollItemMapper.insertApprovedItemByGroupTemplateCode(paramItem.getApprovalStatus(), paramItem.getGroupTemplateCode());
        int updateResult = prPayrollGroupTemplateMapper.updateItemByCode(paramItem);
        result = updateResult == 1;
        return result;
    }

    @Override
    public PrPayrollGroupTemplateHistoryPO getLastVersion(String srcCode){
        return prPayrollGroupTemplateHistoryMapper.selectLastVersionByCode(srcCode);
    }


    //判断是否可以删项
    private Map<String, Object> filterPrItem(List<PrItemEntity> items, String prItemName){
        Map<String, Object> result = new HashMap<>();
        List<String> failList = new ArrayList<>();
        if (items == null || items.size()<1){
            result.put("FAILURE", failList);
        }
        if (items != null) {
            items.stream()
                    .filter(item -> item.getType().equals(ItemTypeEnum.CALC.getValue()))
                    .forEach(item ->{
                        List<String> itemNames = getPayItems(item.getFormula());
                        if (itemNames.stream()
                                .anyMatch(str -> str.trim().equals(prItemName))){
                            failList.add(item.getName());
                        }
                    });
        }
        return result;
    }

    @Override
    public List<HashMap<String, String>> getPrGroupTemplateNameList(String query) {
        List<HashMap<String, String>> result;
        result = prPayrollGroupTemplateMapper.selectGroupTemplateNameListByName(query);
        return result;
    }

    @Override
    public boolean publishPrGroupTemplate(String code) {

        //获取薪资组模板下的薪资项
        List<PrPayrollItemPO> prItemsInTemplate = prItemService.getListByGroupTemplateCode(code);

        //获取所有继承该模板的薪资组
        List<PrPayrollGroupPO> prGroups = prGroupService.getListByTemplateCode(code);

        //根据每个薪资组做处理
        prGroups.forEach(i -> {
            List<PrPayrollItemPO> itemsInGroup = prItemService.getListByGroupCode(i.getGroupCode());
            // 删除已经不存在在薪资组模板中的继承项
            List<String> needDeleteItemCodes = new LinkedList<>();
            itemsInGroup.forEach(j -> {
                if (!StringUtils.isEmpty(j.getParentItemCode())
                        && prItemsInTemplate.stream().noneMatch(k -> j.getParentItemCode().equals(k.getItemCode()))) {
                    needDeleteItemCodes.add(j.getItemCode());
                }
            });
            if (needDeleteItemCodes.size() > 0) {
                prItemService.deleteItemByCodes(needDeleteItemCodes);
            }
            // 更新，添加继承项
            prItemsInTemplate.forEach(j -> {
                Optional<PrPayrollItemPO> prItemInGroupOpt =
                        itemsInGroup.parallelStream()
                                .filter(k -> j.getItemCode().equals(k.getParentItemCode()))
                                .findFirst();

                if (prItemInGroupOpt.isPresent()) {
                    // 存在则更新
                    PrPayrollItemPO updateParam = j.deepClone();
                    updateParam.setId(null);
                    updateParam.setItemCode(prItemInGroupOpt.get().getItemCode());
                    updateParam.setPayrollGroupCode(i.getGroupCode());
                    updateParam.setParentItemCode(null);
                    updateParam.setDisplayPriority(null);
                    updateParam.setCalPriority(null);
                    updateParam.setModifiedBy(UserContext.getUserId());
                    prItemService.updateItem(updateParam);
                } else {
                    // 不存在则插入
                    PrPayrollItemPO insertParam = j.deepClone();
                    insertParam.setId(null);
                    insertParam.setManagementId(i.getManagementId());
                    insertParam.setPayrollGroupCode(i.getGroupCode());
                    insertParam.setParentItemCode(j.getItemCode());
                    insertParam.setModifiedBy(UserContext.getUserId());
                    insertParam.setCreatedBy(UserContext.getUserId());
                    prItemService.addItem(insertParam);
                }
            });
        });

        return true;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean importPrGroupTemplate(String from, String to, Boolean fromTemplate) {

        //获取from薪资组中的薪资项
        List<PrPayrollItemPO> items;
        if (fromTemplate) {
            items = prItemService.getListByGroupTemplateCode(from);
        } else {
            items = prItemService.getListByGroupCode(from);
        }

        if (items == null || items.size() == 0) {
            throw new BusinessException("导入薪资组中无薪资项");
        }
        items.forEach(i -> {
            i.setPayrollGroupTemplateCode(to);
            i.setParentItemCode(null);
            i.setCreatedBy(UserContext.getUserId());
            i.setModifiedBy(UserContext.getUserId());
            i.setCreatedTime(new Date());
            i.setModifiedTime(new Date());
        });
        //删除原来存在于该薪资组模板的薪资项
        try {
            prPayrollItemMapper.deleteItemByGroupTemplateCode(to);
        } catch (RuntimeException e) {
            throw new BusinessException("导入目标薪资组模板中薪资项删除失败");
        }
        int insertItemResult = prItemService.addList(items);
        if (insertItemResult == 0) {
            throw new BusinessException("导入薪资组模板中薪资项时失败");
        }
        return true;
    }

    // 从公式中获取薪资项名称列表
    private List<String> getPayItems(String formula){
        List<String> nameList = new LinkedList<>();
        Pattern p = Pattern.compile(PAY_ITEM_REGEX);
        Matcher m = p.matcher(formula);
        while(m.find()) {
            nameList.add(m.group(1));
        }
        return nameList;
    }
}
