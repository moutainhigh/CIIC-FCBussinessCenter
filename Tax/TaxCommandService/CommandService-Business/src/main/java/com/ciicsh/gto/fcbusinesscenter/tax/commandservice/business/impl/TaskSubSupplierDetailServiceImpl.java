package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubSupplierDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubSupplierDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubSupplierDetailPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author wuhua
 */
@Service
public class TaskSubSupplierDetailServiceImpl extends ServiceImpl<TaskSubSupplierDetailMapper, TaskSubSupplierDetailPO> implements TaskSubSupplierDetailService, Serializable {}
