package com.ciicsh.gto.fcbusinesscenter.webqueryservice.dao;

import com.ciicsh.gto.fcsupportcenter.companyqueryservice.entity.po.PayrollGroupPO;
import org.springframework.stereotype.Repository;

/**
 * Created by shenjian on 2017/11/28.
 */
@Repository
public interface ProductGroupMapper {

    PayrollGroupPO getById(String id);

}
