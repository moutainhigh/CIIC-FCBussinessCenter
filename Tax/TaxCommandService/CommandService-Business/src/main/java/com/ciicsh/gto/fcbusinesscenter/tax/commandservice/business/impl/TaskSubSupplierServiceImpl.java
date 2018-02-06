package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author wuhua
 */
@Service
public class TaskSubSupplierServiceImpl extends ServiceImpl<TaskSubSupplierMapper, TaskSubSupplierPO> implements TaskSubSupplierService, Serializable {

}
