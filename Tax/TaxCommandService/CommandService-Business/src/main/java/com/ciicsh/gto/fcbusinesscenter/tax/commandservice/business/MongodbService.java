package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.entity.CancelClosingMsg;
import com.ciicsh.gto.fcbusinesscenter.entity.ClosingMsg;

/**
 * 批次处理
 */
public interface MongodbService {

    //批次关账处理
    void acquireBatch(ClosingMsg closingMsg);

    //取消关账处理
    void cancelBatch(CancelClosingMsg cancelClosingMsg);
}
