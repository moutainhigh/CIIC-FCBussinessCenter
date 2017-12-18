package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrItemMapper;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by jiangtianning on 2017/11/6.
 */
@Service
public class PrItemServiceImpl implements PrItemService {

    @Autowired
    private IPrItemMapper prItemMapper;

    @Autowired
    private PrPayrollItemMapper prPayrollItemMapper;

    final static int PAGE_SIZE = 5;

    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupId(String groupId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setGroupId(Integer.parseInt(groupId));
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        List<PrPayrollItemPO> resultList = prPayrollItemMapper.selectList(ew);
        PageInfo<PrPayrollItemPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupTemplateId(String groupTemplateId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setGroupTemplateId(Integer.parseInt(groupTemplateId));
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        List<PrPayrollItemPO> resultList = prPayrollItemMapper.selectList(ew);
        PageInfo<PrPayrollItemPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public PageInfo<PrItemEntity> searchPrItemList(PrItemEntity paramItem, Integer pageNum) {
        List<PrItemEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prItemMapper.selectQueryList(paramItem);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrItemEntity> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public PrPayrollItemPO getItemById(String id) {
        PrPayrollItemPO result = prPayrollItemMapper.selectById(id);
        return result;
    }

    @Override
    public int addItem(PrPayrollItemPO param) {
        int insertResult = prPayrollItemMapper.insert(param);
        if (insertResult > 0) {
            return param.getId();
        } else {
            return 0;
        }
    }

    @Override
    public int addList(List<PrItemEntity> paramList) {
        return 0;
    }

    @Override
    public List<String> getNameList(String managementId) {
        List<String> resultList = prItemMapper.selectNameList(managementId);
        return resultList;
    }

    @Override
    public Map<String, Object> updateItem(PrItemEntity param) {
        Map<String, Object> result = new HashMap<>();
        List<String> failList = new ArrayList<>();
        List<String> successList = new ArrayList<>();
        // 更新条数
        int i = 0;
        //先获取该薪资项当前详情用作check
        PrItemEntity prItemEntity = new PrItemEntity();
        try {
            prItemEntity = prItemMapper.selectItemById(param.getEntityId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (prItemEntity == null) {
            result.put("FAILURE", failList);
            return result;
        }
        // 如果该薪资项只存在于薪资组中，则修改无限制
        if (StringUtils.isEmpty(prItemEntity.getPrGroupTemplateId())) {
            try {
                i = prItemMapper.updateItemById(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("SUCCESS", i);
            return result;
        }
        // 如果该薪资项由薪资组继承而产生，则需进行校验，不能修改薪资项名
        if (!StringUtils.isEmpty(prItemEntity.getPrGroupTemplateId()) && !StringUtils.isEmpty(prItemEntity.getPrGroupId())) {
            if (param.getName() != null && !prItemEntity.getName().equals(param.getName())) {
                result.put("FAILURE", "不能修改继承薪资组模板产生的薪资项名称");
                return result;
            }
            try {
                i = prItemMapper.updateItemById(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            result.put("SUCCESS", i);
            return result;
        }
        // 如果该薪资项是薪资项模板中的项目，则需进行校验，需要更新继承该薪资组模板的薪资组的薪资项
        if (!StringUtils.isEmpty(prItemEntity.getPrGroupTemplateId())) {
            List<PrItemEntity> prItemEntityList = new ArrayList<>();
            try {
                prItemMapper.updateItemById(param);
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                prItemEntityList = prItemMapper.selectListByGroupTemplateId(prItemEntity.getPrGroupTemplateId());
            } catch (Exception e) {
                e.printStackTrace();
            }
            Date templateItemLastModified = prItemEntity.getDataChangeLastTime();
            param.setEntityId(null);
            prItemEntityList.stream()
                    .filter(item -> !StringUtils.isEmpty(item.getPrGroupId()))
                    .forEach(item -> {
                        if (item.getDataChangeLastTime().after(templateItemLastModified)) {
                            failList.add(item.getName());
                        } else {
                            try {
                                CommonUtils.copyNotNullProperties(param, item);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            try {
                                int updateCount = prItemMapper.updateItemById(item);
                                if (updateCount != 0) {
                                    successList.add(item.getName());
                                } else {
                                    failList.add(item.getName());
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    });
            result.put("SUCCESS", successList);
            result.put("FAILURE", failList);
            return result;
        }
        return result;
    }

    @Override
    public List<Integer> getTypeList(String managementId) {
        List<Integer> resultList = prItemMapper.selectTypeList(managementId);
        return resultList;
    }

    public List<PrItemEntity> getListByGroupTemplateId(String prGroupTemplateId) {
        List<PrItemEntity> resultList = prItemMapper.selectListByGroupTemplateId(prGroupTemplateId);
        return resultList;
    }

    public List<PrItemEntity> getListByGroupId(String prGroupId) {
        List<PrItemEntity> resultList = prItemMapper.selectListByGroupId(prGroupId);
        return resultList;
    }


    @Override
    public int deleteItemByIds(List<String> ids) {
        int result = prPayrollItemMapper.deleteBatchIds(ids);
        return result;
    }

    @Override
    public int deleteItemByPrGroupId(String prGroupId) {
        return prItemMapper.deleteItemByPrGroupId(prGroupId);
    }
}
