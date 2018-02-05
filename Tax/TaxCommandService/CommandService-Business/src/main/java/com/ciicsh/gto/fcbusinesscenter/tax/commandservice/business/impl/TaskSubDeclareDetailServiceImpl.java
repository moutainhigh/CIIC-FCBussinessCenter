package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareDetailService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.TaskSubDeclareDetailMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubDeclareDetailPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author wuhua
 */
@Service
public class TaskSubDeclareDetailServiceImpl extends ServiceImpl<TaskSubDeclareDetailMapper, TaskSubDeclareDetailPO> implements TaskSubDeclareDetailService, Serializable {}
