package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;


import com.baomidou.mybatisplus.service.IService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;

import java.io.InputStream;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
public interface SalaryGrantReprieveEmployeeImportService extends IService<SalaryGrantReprieveEmployeeImportPO> {

    /**
     * @description 导入暂缓名单
     * @author chenpb
     * @since 2018-05-16
     * @param inputStream
     */
    void importReprieveList(InputStream inputStream);

}
