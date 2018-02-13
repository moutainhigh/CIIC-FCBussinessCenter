package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.ciicsh.gto.salarymanagement.entity.enums.ApprovalStatusEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.po.PayrollGroupExtPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollGroupPO;
import com.ciicsh.gto.salarymanagement.entity.po.PrPayrollItemPO;
import com.ciicsh.gto.salarymanagement.entity.utils.EnumHelpUtil;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollGroupMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.util.CommonServiceConst;
import com.ciicsh.gto.salarymanagementcommandservice.service.PrItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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



    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupCode(String groupCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupCode(groupCode);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
        List<PrPayrollItemPO> resultList = prPayrollItemMapper.selectList(ew);
        PageInfo<PrPayrollItemPO> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    public PageInfo<PrPayrollItemPO> getListByGroupTemplateCode(String groupTemplateCode, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        PrPayrollItemPO param = new PrPayrollItemPO();
        param.setPayrollGroupTemplateCode(groupTemplateCode);
        EntityWrapper<PrPayrollItemPO> ew = new EntityWrapper<>(param);
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
    @Transactional(rollbackFor = RuntimeException.class)
    public int addItem(PrPayrollItemPO param) {
        // 将所在薪资组的审核状态更新为草稿
        PrPayrollGroupPO groupParam = new PrPayrollGroupPO();
        groupParam.setGroupCode(param.getPayrollGroupCode());
        groupParam.setApprovalStatus(ApprovalStatusEnum.DRAFT.getValue());
        prPayrollGroupMapper.updateItemByCode(groupParam);
        // 插入薪资项
        param.setCalPriority(CommonServiceConst.DEFAULT_CAL_PRIORITY);
        param.setDisplayPriority(CommonServiceConst.DEFAULT_DIS_PRIORITY);
        int insertResult = prPayrollItemMapper.insert(param);
        return insertResult;
    }

    @Override
    public int addList(List<PrPayrollItemPO> paramList) {
        int insertResult = prPayrollItemMapper.insertBatchItems(paramList);
        return insertResult;
    }

    @Override
    public Map<String, Object> updateItem(PrPayrollItemPO param) {
        Map<String, Object> result = new HashMap<>();
        int updateResult = prPayrollItemMapper.updateItemByCode(param);
        //TODO 继承check逻辑变更
//        List<String> failList = new ArrayList<>();
//        List<String> successList = new ArrayList<>();
//        // 更新条数
//        int i = 0;
//        //先获取该薪资项当前详情用作check
//        PrPayrollItemPO prPayrollItemPO = new PrPayrollItemPO();
//        try {
//            prPayrollItemPO = prPayrollItemMapper.selectById(param.getEntityId());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        if (prPayrollItemPO == null) {
//            result.put("FAILURE", failList);
//            return result;
//        }
//        // 如果该薪资项只存在于薪资组中，则修改无限制
//        if (StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode())) {
//            try {
//                i = prItemMapper.updateItemById(param);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            result.put("SUCCESS", i);
//            return result;
//        }
//        // 如果该薪资项由薪资组继承而产生，则需进行校验，不能修改薪资项名
//        if (!StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupTemplateCode()) &&
//                !StringUtils.isEmpty(prPayrollItemPO.getPayrollGroupCode())) {
//            if (param.getName() != null && !prPayrollItemPO.getItemName().equals(param.getName())) {
//                result.put("FAILURE", "不能修改继承薪资组模板产生的薪资项名称");
//                return result;
//            }
//            try {
//                i = prItemMapper.updateItemById(param);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//            result.put("SUCCESS", i);
//            return result;
//        }
//        // 如果该薪资项是薪资项模板中的项目，则需进行校验，需要更新继承该薪资组模板的薪资组的薪资项
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
        result.put("SUCCESS", updateResult);
        return result;
    }

    @Override
    public List<HashMap<String, Object>> getTypeList() {
        List<HashMap<String, Object>> resultList = EnumHelpUtil.getLabelValueList(ItemTypeEnum.class);
        return resultList;
    }

    @Override
    public int deleteItemByCodes(List<String> codes) {
        int result = prPayrollItemMapper.deleteItemByCodes(codes);
        return result;
    }

    @Override
    public int deleteItemByPrGroupId(String prGroupId) {
        return prPayrollItemMapper.deleteItemByGroupCode(prGroupId);
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
}
