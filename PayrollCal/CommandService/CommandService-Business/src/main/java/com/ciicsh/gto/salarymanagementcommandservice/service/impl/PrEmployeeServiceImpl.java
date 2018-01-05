package com.ciicsh.gto.salarymanagementcommandservice.service.impl;

import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrEmployeeMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrFixedInputItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.IPrItemMapper;
import com.ciicsh.gto.salarymanagement.entity.dto.EmployeeSocialSecurityDTO;
import com.ciicsh.gto.salarymanagement.entity.PrEmployeeEntity;
import com.ciicsh.gto.salarymanagement.entity.PrFixedInputItemEntity;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import com.ciicsh.gto.salarymanagement.entity.enums.DataTypeEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.DefaultValueStyleEnum;
import com.ciicsh.gto.salarymanagement.entity.enums.ItemTypeEnum;
import com.ciicsh.gto.salarymanagementcommandservice.util.CommonUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by jiangtianning on 2017/10/31.
 */
@Service
public class PrEmployeeServiceImpl implements com.ciicsh.gto.salarymanagementcommandservice.service.PrEmployeeService {

    @Autowired
    private IPrEmployeeMapper prEmployeeMapper;

    @Autowired
    private IPrFixedInputItemMapper prFixedInputItemMapper;

    @Autowired
    private IPrItemMapper prItemMapper;

    final static int PAGE_SIZE = 10;

    @Override
    public PageInfo<PrEmployeeEntity> getEmployeeList(PrEmployeeEntity param, Integer pageNum) {

        List<PrEmployeeEntity> resultList = new ArrayList<>();
        PageHelper.startPage(pageNum, PAGE_SIZE);
        try {
            resultList = prEmployeeMapper.selectList(param);
        } catch (Exception e) {
            e.printStackTrace();
        }
        PageInfo<PrEmployeeEntity> pageInfo = new PageInfo<>(resultList);
        return pageInfo;
    }

    @Override
    @Transactional
    public List<PrEmployeeEntity> getImportedEmployeeList(String managementId) {

        // TODO 从open service获取数据
        List<EmployeeSocialSecurityDTO> testList = new LinkedList<>();
        testList.add(mockSocialSecurityDTO("公积金1", new BigDecimal(500)));
        testList.add(mockSocialSecurityDTO("测试", new BigDecimal(1000)));
        List<String> empIdList = new ArrayList<>();

        List<PrFixedInputItemEntity> listWaitForInsert = new ArrayList<>();
        testList.forEach(i -> {
            PrFixedInputItemEntity item = new PrFixedInputItemEntity();
            CommonUtils.copyNotNullProperties(i, item);
            listWaitForInsert.add(item);
        });
        testList.forEach(i -> empIdList.add(i.getEmployeeId()));
        try {
            prFixedInputItemMapper.deleteListByEmployeeId(empIdList);
            int result = prFixedInputItemMapper.insertList(listWaitForInsert);
            if (result != empIdList.size()) {
                throw new RuntimeException();
            }

            //对于每一个导入薪资项，若该管理方下的薪资组里不存在该薪资项则需要插入
            listWaitForInsert.forEach(i -> {
                List<String> prGroupIdList = getPrGroupIdsWithoutItem(i);

                if (prGroupIdList != null && prGroupIdList.size() > 0) {
                    //构筑待插入的薪资项列表
                    List<PrItemEntity> prItemEntities = new ArrayList<>();
                    prGroupIdList.forEach(id -> prItemEntities.add(initPrItemForFixItem(i, id)));
                    //插入薪资项
                    prItemMapper.insertList(prItemEntities);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException();
        }
        // TODO
        return null;
    }

    //对于每一个导入薪资项， 获取不存在该薪资项名的薪资组ID列表
    private List<String> getPrGroupIdsWithoutItem(PrFixedInputItemEntity item) throws RuntimeException {
        List<String> resultList = prItemMapper.selectGroupIdsWithoutItemName(item.getManagementId(), item.getPrItemName());
        return resultList;
    }

    //初始化薪资项from导入固定项
    private PrItemEntity initPrItemForFixItem(PrFixedInputItemEntity fixedItem, String groupId) {
        PrItemEntity prItem = new PrItemEntity();
        prItem.setCode("TEST-0000");
        prItem.setManagementId(fixedItem.getManagementId());
        prItem.setPrGroupId(groupId);
        prItem.setName(fixedItem.getPrItemName());
        prItem.setType(ItemTypeEnum.INPUT.getValue());
        prItem.setDataType(DataTypeEnum.NUM.getValue());
        prItem.setDefaultValueStyle(DefaultValueStyleEnum.USE_HISTORY_DATA.getValue());
        prItem.setIsHidden(false);
        prItem.setCreatedBy("Anonymous");
        prItem.setModifiedBy("Anonymous");
        return prItem;
    }

    // NEED DELETE
    private EmployeeSocialSecurityDTO mockSocialSecurityDTO(String name , BigDecimal value) {
        // TODO 从open service获取数据
        EmployeeSocialSecurityDTO dto = new EmployeeSocialSecurityDTO();
        dto.setIsActive(true);
        dto.setEmployeeId("001");
        dto.setManagementId("001");
        dto.setPrItemName(name);
        dto.setPrItemValue(value);
        return dto;
    }
}
