package com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.dao;

import com.ciicsh.gto.fcsupportcenter.calculatewebcommandservice.entity.po.PayrollGroupPO;
import org.springframework.stereotype.Repository;

/**
 * Created by shenjian on 2017/11/28.
 */
@Repository
public interface ProductGroupMapper {

    PayrollGroupPO getById(String id);

}
