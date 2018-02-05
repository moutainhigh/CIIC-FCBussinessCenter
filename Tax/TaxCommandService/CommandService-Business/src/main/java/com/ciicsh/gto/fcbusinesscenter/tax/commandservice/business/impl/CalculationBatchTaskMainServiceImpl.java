package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.CalculationBatchTaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.CalculationBatchTaskMainMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.CalculationBatchTaskMainPO;
import org.springframework.stereotype.Service;

import java.io.Serializable;

/**
 * @author wuhua
 */
@Service
public class CalculationBatchTaskMainServiceImpl extends ServiceImpl<CalculationBatchTaskMainMapper, CalculationBatchTaskMainPO> implements CalculationBatchTaskMainService, Serializable {}
