package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business;

import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.FcPayrollCalcResultPO;
import com.mongodb.DBObject;
import org.bson.Document;

/**
 * 薪资计算批次结果表(雇员维度) 服务类
 *
 * @author taka
 * @since 2018-02-09
 */
public interface FcPayrollCalcResultService {
    List<Document> listFcPayrollCalcResults(String params);





}
