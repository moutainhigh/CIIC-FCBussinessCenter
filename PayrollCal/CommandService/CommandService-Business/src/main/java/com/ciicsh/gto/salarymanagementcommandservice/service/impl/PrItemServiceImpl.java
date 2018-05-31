package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrAccountSetOptPO;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrItemInAccountSetPO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupTemplateMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CommonServiceConst;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.util.TranslatorUtils;
import com.ciicsh.gto.salarymanagementcommandservice.util.constants.MessageConst;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by jiangtianning on 2017/11/6.
 * @author jiangtianning
 */
@Service
public class PrItemServiceImpl implements PrItemService {

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private PrPayrollGroupMapper prPayrollGroupMapper;

    @Autowired
    private PrPayrollGroupTemplateMapper prPayrollGroupTemplateMapper;

    @Autowired
    private CodeGenerator codeGenerator;

    private final static Pattern PAY_ITEM_REGEX = Pattern.compile("\\[([^\\[\\]]+)\\]");

    @Override
    public List<PrPayrollItemPO> getListByGroupCode(String groupCode) {
        return getListByGroupCode(groupCode, 0, 0).getList();
    }

    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupCode(String groupCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupCode(groupCode);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
//        ew.isNull("payroll_group_template_code");
        List<PrPayrollItemPO> resultList = prPayrollItemMapper.selectList(ew);
        PageInfo<PrPayrollItemPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public List<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode) {
        return getListByGroupTemplateCode(groupTemplateCode, 0, 0).getList();
    }

    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupTemplateCode(groupTemplateCode);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        ew.isNull("payroll_group_code").or().eq("payroll_group_code", "");
        List<PrPayrollItemPO> resultList = prPayrollItemMapper.selectList(ew);
        PageInfo<PrPayrollItemPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public PrPayrollItemPO getItemByCode(String code) {
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setItemCode(code);
        PrPayrollItemPO result = prPayrollItemMapper.selectOne(param);
        return result;
    }

    @Override
    @Transactional(
            rollbackFor = RuntimeException.class,
            isolation = Isolation.SERIALIZABLE
    )
    public int addItem(PrPayrollItemPO param) {
        // 将所在薪资组/模板的审核状态更新为草稿
        this.updateRelatedGroupStatus(param);
        // 插入薪资项
        param.setItemCode(codeGenerator.genPrItemCode(param.getManagementId()));
        param.setCalPriority(CommonServiceConst.DEFAULT_CAL_PRIORITY);
        param.setDisplayPriority(CommonServiceConst.DEFAULT_DIS_PRIORITY);
        param.setModifiedTime(new Date());
        param.setCreatedTime(new Date());
        if(param.getItemType() == ItemTypeEnum.CALC.getValue()) {
            String groupCode = param.getPayrollGroupCode() == null ? param.getPayrollGroupTemplateCode() : param.getPayrollGroupCode();
            param.setCalPriority(prPayrollItemMapper.selectMaxCalPriorityOfGroup(groupCode,
                    StringUtils.isEmpty(param.getPayrollGroupCode())) + 1);
        }
        this.replaceItemNameWithCode(param);
        int insertResult = prPayrollItemMapper.insert(param);
        return insertResult;
    }

    @Override
    public int addList(List<PrPayrollItemPO> paramList) {
        PrPayrollItemPO first = paramList.get(0);
        // 将所在薪资组的审核状态更新为草稿
        this.updateRelatedGroupStatus(first);
        //批量插入
        paramList.forEach(i -> {
            i.setItemCode(codeGenerator.genPrItemCode(i.getManagementId()));
            i.setModifiedTime(new Date());
            i.setCreatedTime(new Date());
        });
        int insertResult = prPayrollItemMapper.insertBatchItems(paramList);
        return insertResult;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateItem(PrPayrollItemPO param) {
        //先获取该薪资项当前详情用作check
        PrPayrollItemPO prPayrollItemPO = this.getItemByCode(param.getItemCode());

        // 如果该薪资项只存在于薪资组中，则修改无限制
        if (StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode())) {
//            this.realUpdateItem(param);
        }

        // 如果该薪资项由薪资组继承而产生，则需进行校验，不能修改薪资项名
        if (!StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode()) &&
                !StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupCode())) {
            if (param.getItemName() != null && !prPayrollItemPO.getItemName().equals(param.getItemName())) {
                throw new BusinessException(MessageConst.PAYROLL_ITEM_UPDATE_NAME_ERROR);
            }
//            this.realUpdateItem(param);
//            return updateResult;
        }
        // 如果该薪资项是薪资项模板中的项
        if (!StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode())) {
//            updateResult = prPayrollItemMapper.updateItemByCode(param);
//            return updateResult;
        }
//        if (!StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode())) {
//            List<PrItemEntity> prItemEntityList = new ArrayList<>();
//            try {
//                prItemMapper.updateItemById(param);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            try {
//                prItemEntityList = prItemMapper.selectListByGroupTemplateId(prPayrollItemPO.getPayrollGroupTemplateCode());
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            Date templateItemLastModified = prPayrollItemPO.getModifiedTime();
//            param.setEntityId(null);
//            prItemEntityList.stream()
//                    .filter(item -> !StringUtils.isEmpty(item.getPrGroupId()))
//                    .forEach(item -> {
//                        if (item.getDataChangeLastTime().after(templateItemLastModified)) {
//                            failList.add(item.getName());
//                        } else {
//                            try {
//                                CommonUtils.copyNotNullProperties(param, item);
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                            try {
//                                int updateCount = prItemMapper.updateItemById(item);
//                                if (updateCount != 0) {
//                                    successList.add(item.getName());
//                                } else {
//                                    failList.add(item.getName());
//                                }
//                            } catch (Exception e) {
//                                e.printStackTrace();
//                            }
//                        }
//                    });
//            result.put("SUCCESS", successList);
//            result.put("FAILURE", failList);
//            return result;
//        }
        TranslatorUtils.copyNotNullProperties(param, prPayrollItemPO);
        return this.realUpdateItem(prPayrollItemPO);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public int realUpdateItem(PrPayrollItemPO param) {
        // 将所在薪资组/模板的审核状态更新为草稿
        this.updateRelatedGroupStatus(param);
        if(param.getItemType() != ItemTypeEnum.CALC.getValue()){
            param.setCalPriority(CommonServiceConst.DEFAULT_CAL_PRIORITY);
        }
        param.setModifiedTime(new Date());
        this.replaceItemNameWithCode(param);
        return prPayrollItemMapper.updateItemByCode(param);
    }

    @Override
    public List<HashMap<String, Object>> getTypeList() {
        List<HashMap<String, Object>> resultList = EnumHelpUtil.getLabelValueList(ItemTypeEnum.class);
        return resultList;
    }

    @Override
    public int deleteItemByCodes(List<String> codes) {
        //先获取该薪资项当前详情用作check
        PrPayrollItemPO first = this.getItemByCode(codes.get(0));
        this.updateRelatedGroupStatus(first);
        return prPayrollItemMapper.deleteItemByCodes(codes);
    }

    @Override
    public int deleteItemByPrGroupCode(String groupCode) {
        return prPayrollItemMapper.deleteItemByGroupCode(groupCode);
    }

    @Override
    public List<PrPayrollItemPO> getPayrollItems(PayrollGroupExtPO extPO) {
        return prPayrollItemMapper.getPayrollItems(extPO);
    }

    @Override
    public boolean updateDisplayPriority(List<String> codes) {
        for(int i = 0; i < codes.size(); i++) {
            PrPayrollItemPO param = new PrPayrollItemPO();
            param.setItemCode(codes.get(i));
            param.setDisplayPriority(i);
            prPayrollItemMapper.updateItemByCode(param);
        }
        return true;
    }

    @Override
    public boolean updateCalPriority(List<String> codes) {
        for(int i = 0; i < codes.size(); i++) {
            PrPayrollItemPO param = new PrPayrollItemPO();
            param.setItemCode(codes.get(i));
            param.setCalPriority(i+1);
            prPayrollItemMapper.updateItemByCode(param);
        }
        return true;
    }

    @Override
    public List<PrItemInAccountSetPO> selectItemNames(String batchCode) {
        return prPayrollItemMapper.selectItemNames(batchCode);
    }

    private void updateRelatedGroupStatus(PrPayrollItemPO param) {
        // 将所在薪资组/模板的审核状态更新为草稿
        if (!StringUtils.isEmpty(param.getPayrollGroupTemplateCode())
                && StringUtils.isEmpty(param.getPayrollGroupCode())) {
            PrPayrollGroupTemplatePO groupTemplatePO = new PrPayrollGroupTemplatePO();
            groupTemplatePO.setGroupTemplateCode(param.getPayrollGroupTemplateCode());
            groupTemplatePO.setApprovalStatus(ApprovalStatusEnum.DRAFT.getValue());
            groupTemplatePO.setModifiedTime(new Date());
            groupTemplatePO.setModifiedBy(param.getModifiedBy());
            prPayrollGroupTemplateMapper.updateItemByCode(groupTemplatePO);
        } else {
            PrPayrollGroupPO groupPO = new PrPayrollGroupPO();
            groupPO.setGroupCode(param.getPayrollGroupCode());
            groupPO.setApprovalStatus(ApprovalStatusEnum.DRAFT.getValue());
            groupPO.setModifiedTime(new Date());
            groupPO.setModifiedBy(param.getModifiedBy());
            prPayrollGroupMapper.updateItemByCode(groupPO);
        }
    }

    private void replaceItemNameWithCode(PrPayrollItemPO param) {

        List<PrPayrollItemPO> itemPOList;
        if (!StringUtils.isEmpty(param.getPayrollGroupCode())) {
            itemPOList = this.getListByGroupCode(param.getPayrollGroupCode());
        } else {
            itemPOList = this.getListByGroupTemplateCode(param.getPayrollGroupTemplateCode());
        }

        if (!StringUtils.isEmpty(param.getFormulaContent())) {
            param.setFormulaContent(this.replacePayItem(param.getFormulaContent(), itemPOList, param.getCalPriority()));
        }

        if (!StringUtils.isEmpty(param.getItemCondition())) {
            param.setItemCondition(this.replacePayItem(param.getItemCondition(), itemPOList, param.getCalPriority()));
        }

    }

    private String replacePayItem(String content, List<PrPayrollItemPO> itemPOList, Integer thisCalPriority){

        // 薪资项名称-Code map
        Map<String, String> nameCodeMap = new HashMap<>();
        // 薪资项名称-计算顺序 map
        Map<String, Integer> nameCalPrioMap = new HashMap<>();
        itemPOList.forEach(i -> {
            nameCodeMap.put(i.getItemName(), i.getItemCode());
            nameCalPrioMap.put(i.getItemName(), i.getCalPriority());
        });

        Matcher m = PAY_ITEM_REGEX.matcher(content);
        while(m.find()) {
            String payItemName = m.group(1);
            if (StringUtils.isEmpty(nameCodeMap.get(payItemName))) {
                throw new BusinessException("计算公式中有不存在于该薪资组的薪资项: " + payItemName);
            }
            if (nameCalPrioMap.get(payItemName) > thisCalPriority) {
                throw new BusinessException("计算公式中依赖了计算优先级较低的薪资项: " + payItemName);
            }
            content = content.replace("[" + payItemName + "]", nameCodeMap.get(payItemName));
        }
        return content;
    }
}
