package com.ciicsh.caldispatchjob.compute.service;

import com.ciicsh.gto.fcbusinesscenter.util.mongo.NormalBatchMongoOpt;
import com.ciicsh.gto.fcbusinesscenter.util.mongo.TestBatchMongoOpt;
import com.ciicsh.gto.salarymanagement.entity.po.custom.PrCustBatchPO;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrNormalBatchMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollAccountItemRelationMapper;
import com.ciicsh.gto.salarymanagementcommandservice.dao.PrPayrollItemMapper;
import com.ciicsh.gto.salarymanagementcommandservice.service.common.CommonServiceImpl;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by bill on 17/12/19.
 */
@Service
public class NormalBatchServiceImpl {

    private final static Logger logger = LoggerFactory.getLogger(NormalBatchServiceImpl.class);

    @Autowired
    private CommonServiceImpl commonService;

    @Autowired
    private NormalBatchMongoOpt normalBatchMongoOpt;

    @Autowired
    private TestBatchMongoOpt testBatchMongoOpt;

    @Autowired
    private EmpGroupServiceImpl empGroupService;

    @Autowired
    private PrNormalBatchMapper normalBatchMapper;

    /**
     * 批量更新或者插入数据
     *
     * @param batchCode
     * @param batchType
     * @return
     */
    public int batchInsertOrUpdateNormalBatch(String batchCode, int batchType) {

        PrCustBatchPO initialPO = new PrCustBatchPO();
        initialPO.setCode(batchCode);
        List<PrCustBatchPO> custBatchPOList = normalBatchMapper.selectBatchListByUseLike(initialPO);
        if (custBatchPOList == null || custBatchPOList.size() == 0) {
            return -1;
        }

        PrCustBatchPO batchPO = custBatchPOList.get(0);
        String empGroupCode = batchPO.getEmpGroupCode();
        List<DBObject> employees = empGroupService.getEmployeesByEmpGroupCode(empGroupCode);

        return commonService.batchInsertOrUpdateNormalBatch(batchCode, batchType, employees);
    }

    /**
     * 删除批次
     *
     * @param batchCodes
     * @return
     */
    public int deleteNormalBatch(String batchCodes, int batchType) {
        String[] codes = batchCodes.split(",");
        if (codes.length > 0) {
            int rowAffected;
            if (batchType == 4) { // 测试批次
                rowAffected = normalBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
                logger.info("row affected : " + String.valueOf(rowAffected));
            } else { // 正常批次
                rowAffected = testBatchMongoOpt.delete(Criteria.where("batch_code").in(Arrays.asList(codes)));
                logger.info("row affected : " + String.valueOf(rowAffected));
            }
            return rowAffected;
        }
        return 0;
    }

}
