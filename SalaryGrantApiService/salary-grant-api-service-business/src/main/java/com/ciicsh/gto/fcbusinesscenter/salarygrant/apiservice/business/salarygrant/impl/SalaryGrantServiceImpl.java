package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.SalaryGrantService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.dao.SalaryGrantTaskMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.po.SalaryGrantTaskPO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 * 任务单 服务实现类
 * </p>
 *
 * @author chenpb
 * @since 2018-04-18
 */
@Service
public class SalaryGrantServiceImpl extends ServiceImpl<SalaryGrantTaskMapper, SalaryGrantTaskPO> implements SalaryGrantService {
    /**
     *  任务单
     */
    @Autowired
    SalaryGrantTaskMapper salaryGrantTaskMapper;

     /**
     * 根据批次号查相关任务单
     * @author chenpb
     * @date 2018-04-18
     * @param po
     * @return
     */
    @Override
    public List<SalaryGrantTaskBO> getTask(SalaryGrantTaskPO po) {
        return  salaryGrantTaskMapper.listTask(po);
    }

}
