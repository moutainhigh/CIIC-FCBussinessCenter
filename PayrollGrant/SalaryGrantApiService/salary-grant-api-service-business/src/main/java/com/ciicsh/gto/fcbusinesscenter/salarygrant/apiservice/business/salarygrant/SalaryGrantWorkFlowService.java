package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.entity.bo.SalaryGrantTaskBO;

import java.util.List;

/**
 * <p>
 * 薪资发放工作流调用 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-08-10
 */
public interface SalaryGrantWorkFlowService {
    /**
     *  任务单驳回处理
     * @author gaoyang
     * @date 2018-08-10
     * @param codeList
     * @return Boolean
     */
    Boolean rejectTask(List<SalaryGrantTaskBO> codeList);
}
