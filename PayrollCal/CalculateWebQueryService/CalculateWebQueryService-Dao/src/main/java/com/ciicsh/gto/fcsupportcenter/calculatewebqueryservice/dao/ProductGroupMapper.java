package com.ciicsh.gto.fcsupportcenter.calculatewebqueryservice.dao;

import com.ciicsh.gto.fcsupportcenter.calculatewebqueryservice.entity.po.PayrollGroupPO;
import org.springframework.stereotype.Repository;

/**
 * Created by shenjian on 2017/11/28.
 */
@Repository
public interface ProductGroupMapper {

    PayrollGroupPO getById(String id);

}
