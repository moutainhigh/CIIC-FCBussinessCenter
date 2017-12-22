package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollBaseItemPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrGroupMapper;
import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollBaseItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrBaseItemService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupService;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CodeGenerator;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private IPrGroupMapper prGroupMapper;

    @Autowired
    private PrPayrollBaseItemMapper prPayrollBaseItemMapper;

    @Autowired
    private PrItemService itemService;

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    @Autowired
    private CodeGenerator codeGenerator;

    private final static String PAY_ITEM_REGEX = "\\[([^\\[\\]]+)\\]";

    @Override
    public PageInfo<PrPayrollGroupPO> getList(PrPayrollGroupPO param, Integer pageNum, Integer pageSize) {
        List<PrPayrollGroupPO> resultList;
        PageHelper.startPage(pageNum, pageSize);
        resultList = prPayrollGroupMapper.selectListByEntityUseLike(param);
        PageInfo<PrPayrollGroupPO> pageInfo = new PageInfo<>(resultList);
        return  pageInfo;
    }

    @Override
    public PrPayrollGroupPO getItemByCode(String code) {
        PrPayrollGroupPO param = new PrPayrollGroupPO();
        param.setGroupCode(code);
        PrPayrollGroupPO result = prPayrollGroupMapper.selectOne(param);
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int updateItemByCode(PrPayrollGroupPO paramItem) {

        return prPayrollGroupMapper.updateItemByCode(paramItem);
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public int addItem(PrPayrollGroupPO paramItem) {
        int result = prPayrollGroupMapper.insert(paramItem);
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
                    item.setPayrollGroupCode(paramItem.getGroupCode());
                    item.setItemCode(codeGenerator.genPrItemCode(paramItem.getManagementId()));
                    return item;
                })
                .collect(Collectors.toList());
        itemService.addList(itemList);
        return result;
    }

    @Override
    public List<String> getNameList(String managementId) {
        List<String> nameList = prGroupMapper.selectNameList(managementId);
        return nameList;
    }

    @Override
    public Map<String, Object> deletePrItemFromGroup(String prGroupId, String prItemId, String prItemName) {

        Map<String, Object> result = new HashMap<>();
        int deleteRows = 0;
        // 删除薪资项会影响的薪资项列表
        List<String> failList = new ArrayList<>();
        // 获取薪资组的参数
        PrGroupEntity param = new PrGroupEntity();
        param.setEntityId(prGroupId);
        List<PrItemEntity> prItemEntityList = new ArrayList<>();
        try {
//            prItemEntityList = this.getItem(param).getPrItemEntityList();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (prItemEntityList == null) {
            result.put("FAILURE", failList);
            return result;
        }
        prItemEntityList.stream()
                .filter(i -> i.getType().equals(ItemTypeEnum.CALC.getValue()))
                .forEach(i -> {
                    List<String> prItemNames = getPayItems(i.getFormula());
                    if (prItemNames.stream()
                            .anyMatch(str -> str.trim().equals(prItemName))) {
                        failList.add(i.getName());
                    }
                });
        if (failList.size() == 0) {
            try {
//                deleteRows = itemService.deleteItemById(prItemId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("SUCCESS", deleteRows);
        } else {
            result.put("FAILURE", failList);
        }
        return result;
    }

    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public boolean importPrGroup(String from, String to) {

        PrPayrollGroupPO toEntity = this.getItemByCode(to);

        //获取from薪资组中的薪资项
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupCode(from);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        List<PrPayrollItemPO> items = prPayrollItemMapper.selectList(ew);

        if (items == null || items.size() == 0) {
            throw new RuntimeException("导入薪资组中无薪资项");
        }
        items.forEach(i -> {
            i.setPayrollGroupCode(to);
            i.setItemCode(codeGenerator.genPrItemCode(toEntity.getManagementId()));
        });
        //删除原来存在于该薪资组的薪资项
        int deleteItemResult = prPayrollItemMapper.deleteItemByGroupCode(to);
        if (deleteItemResult == 0) {
            throw new RuntimeException("导入目标薪资组中薪资项删除失败");
        }
        // TODO 修改了插入批量薪资项API
        int insertItemResult = itemService.addList(items);
        if (insertItemResult == 0) {
            throw new RuntimeException("导入薪资组中薪资项时失败");
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

        boolean result = false;
        int prGroupAddResult = prPayrollGroupMapper.insert(newEntity);
        if (prGroupAddResult == 0) {
            throw new RuntimeException("复制薪资组失败");
        }
        List<PrPayrollItemPO> itemList = itemService.getListByGroupCode(srcEntity.getGroupCode(),
                0, 0).getList();
        itemList.forEach(i -> {
            i.setPayrollGroupCode(newEntity.getGroupCode());
            i.setItemCode(codeGenerator.genPrItemCode(srcEntity.getManagementId()));
        });
        int itemAddResult = itemService.addList(itemList);
        if (itemAddResult == 0) {
            throw new RuntimeException("复制薪资项失败");
        }
        result = true;
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
}