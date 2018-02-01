package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.Luncher;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TaskSubProofDetailPO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Luncher.class)
public class TaskSubProofServiceImplTest {

    private static final Logger logger = LoggerFactory.getLogger(TaskSubProofServiceImplTest.class);

    @Autowired
    private TaskSubProofService taskSubProofService;

    @Test
    public void querySubProofDetailList() throws Exception {
        long subProofId = 186;
        List<TaskSubProofDetailPO> taskSubProofDetailPOList = taskSubProofService.querySubProofDetailList(subProofId);
        logger.info("查询集合大小:"+taskSubProofDetailPOList.size());
    }

}
