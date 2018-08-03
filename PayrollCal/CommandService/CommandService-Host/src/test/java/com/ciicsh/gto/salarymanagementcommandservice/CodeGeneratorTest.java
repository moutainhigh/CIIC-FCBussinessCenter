package com.ciicsh.gto.salarymanagementcommandservice;

import com.ciicsh.gto.salarymanagementcommandservice.service.common.CodeGenerator;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/8/2 0002
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class CodeGeneratorTest {
    @Autowired
    private CodeGenerator codeGenerator;

    @Test
    public void genPrItemCode() {
        String code = codeGenerator.genPrItemCode("GL1800255");
        System.out.println("code: " + code);
    }
}
