package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantReprieveEmployeeImportService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantReprieveEmployeeImportMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantReprieveEmployeeImportBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantReprieveEmployeeImportServiceImpl extends ServiceImpl<SalaryGrantReprieveEmployeeImportMapper, SalaryGrantReprieveEmployeeImportPO> implements SalaryGrantReprieveEmployeeImportService {
    @Autowired
    SalaryGrantReprieveEmployeeImportMapper salaryGrantReprieveEmployeeImportMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    /**
     * 根据任务单编号，任务单类型，雇员编号，发放状态暂缓雇员
     * @author chenpb
     * @since 2018-05-23
     * @param bos
     * @param taskCode
     * @param taskType
     * @param userId
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<SalaryGrantEmployeeBO> deferEmployee(List<SalaryGrantEmployeeBO> bos, String taskCode, Integer taskType, String userId) {
        List<SalaryGrantEmployeeBO> failList = new ArrayList<>();
        bos.stream().forEach(x -> {
            x.setTaskCode(taskCode);
            x.setTaskType(taskType);
            x.setModifiedBy(userId);
            Integer row = salaryGrantEmployeeMapper.deferEmployee(x);
            if (row==0) {
                failList.add(x);
            }
        });
        if (!failList.isEmpty()) {
            List<SalaryGrantReprieveEmployeeImportBO> lists = convertToEntities(failList, SalaryGrantReprieveEmployeeImportBO.class);
            salaryGrantReprieveEmployeeImportMapper.deleteReprieveEmp(taskCode, taskType);
            salaryGrantReprieveEmployeeImportMapper.insertBatch(lists);
        }
        return failList;
    }

    private static <E, T> List<E> convertToEntities(Collection<T> list, Class<E> e) {
        if (list == null || list.size() <= 0) {
            return Collections.emptyList();
        }
        return list.stream().map(t -> convertToEntity(t, e)).collect(Collectors.toList());
    }

    private static <E, T> E convertToEntity(T t, Class<E> e) {
        E entity = BeanUtils.instantiate(e);
        BeanUtils.copyProperties(t, entity);
        return entity;
    }
}
