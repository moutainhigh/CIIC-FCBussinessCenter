package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/6/8 0008
 */
public class SalaryGrantTaskBOTest {
    @Test
    public void test() {
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        salaryGrantTaskBO.setGrantDate("2018-04-05");

        List<PrNormalBatchDTO> sortBatchDTOList = new ArrayList<>();
        PrNormalBatchDTO prNormalBatchDTO;
        for (int i = 1; i < 9; i++) {
            prNormalBatchDTO = new PrNormalBatchDTO();
            prNormalBatchDTO.setAdvancePeriod("2018-04-0" + i);

            sortBatchDTOList.add(prNormalBatchDTO);
        }
        System.out.println("sortBatchDTOList: " + sortBatchDTOList);

        for (PrNormalBatchDTO batchDTO : sortBatchDTOList) {
            System.out.println("grantDate: " + salaryGrantTaskBO.getGrantDate() + " advancePeriod: " + batchDTO.getAdvancePeriod());
            //垫付逾期日期
            String advancePeriod = batchDTO.getAdvancePeriod();
            if (salaryGrantTaskBO.getGrantDate().compareToIgnoreCase(advancePeriod) > 0) {
                System.out.println("grantDate > advancePeriod");
            } else {
                System.out.println("grantDate >= advancePeriod");
            }
        }
    }
}
