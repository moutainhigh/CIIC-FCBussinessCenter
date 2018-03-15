package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskMainDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskMainDetailPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author wuhua
 */
@Service
public class TaskMainDetailServiceImpl extends ServiceImpl<TaskMainDetailMapper, TaskMainDetailPO> implements TaskMainDetailService, Serializable { }
