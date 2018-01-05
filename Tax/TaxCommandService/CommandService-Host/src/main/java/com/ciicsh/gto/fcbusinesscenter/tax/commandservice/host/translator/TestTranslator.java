package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.translator;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TestDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.TestPO;
import org.springframework.beans.BeanUtils;

public class TestTranslator
{
    public static TestPO toTestPO(TestDTO testDTO){
        TestPO po = new TestPO();
        BeanUtils.copyProperties(testDTO,po);
        return po;
    }

}