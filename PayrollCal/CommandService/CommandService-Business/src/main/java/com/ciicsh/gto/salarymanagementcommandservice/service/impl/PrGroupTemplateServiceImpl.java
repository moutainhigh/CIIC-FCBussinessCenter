package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.po.KeyValuePO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupTemplatePO;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupTemplateMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrGroupTemplateService;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
    private PrItemService prItemService;

    private final static String PAY_ITEM_REGEX = "\\[([^\\[\\]]+)\\]";

    @Override
    public PageInfo<PrPayrollGroupTemplatePO> getList(PrPayrollGroupTemplatePO param, Integer pageNum, Integer pageSize) {
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
    public PrPayrollGroupTemplatePO getItemById(String id) {
        PrPayrollGroupTemplatePO result = prPayrollGroupTemplateMapper.selectById(id);
        return result;
    }

    @Override
    public int newItem(PrPayrollGroupTemplatePO param) {
        int result = prPayrollGroupTemplateMapper.insert(param);
        if (result >0) {
            return param.getId();
        }
        return result;
    }

    @Override
    public int deleteByIds(List<String> ids) {
        int result = prPayrollGroupTemplateMapper.deleteBatchIds(ids);
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int updateItemById(PrPayrollGroupTemplatePO param) {
        EntityWrapper<PrPayrollGroupTemplatePO> ew = new EntityWrapper<>();
        ew.setEntity(param);
        int result = prPayrollGroupTemplateMapper.updateById(param);
        return result;
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
    public List<KeyValuePO> getPayrollGroupTemplateNames() {
        return prPayrollGroupTemplateMapper.getPayrollGroupTemplateNames();
    }

    //判断是否可以删项
    private Map<String, Object> filterPrItem(List<PrItemEntity> items, String prItemName){
        Map<String, Object> result = new HashMap<>();
        List<String> failList = new ArrayList<>();
        if (items == null || items.size()<1){
            result.put("FAILURE", failList);
        }
        items.stream()
                .filter(item -> item.getType().equals(ItemTypeEnum.CALC.getValue()))
                .forEach(item ->{
                    List<String> itemNames = getPayItems(item.getFormula());
                    if (itemNames.stream()
                            .anyMatch(str -> str.trim().equals(prItemName))){
                        failList.add(item.getName());
                    }
                });
        return result;
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
