package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.EmpExtendFieldTemplateProxy;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmpExtendFieldTemplateListDTO;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.EmployeeExtendFieldDTO;
import com.ciicsh.gto.fcoperationcenter.fcoperationcentercommandservice.api.dto.JsonResult;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;


/**
 * <p>Description: </p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/7/27 0027
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmpExtendFieldTemplateProxyTest {
    @Autowired
    private EmpExtendFieldTemplateProxy empExtendFieldTemplateProxy;

    /**
     * 列出指定管理方下有效的雇员扩展字段模板
     */
    @Test
    public void listTemplates() {
        JsonResult<List<EmpExtendFieldTemplateListDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplates("GL1800257");
        if (!ObjectUtils.isEmpty(listJsonResult)) {
            List<EmpExtendFieldTemplateListDTO> templateListDTOList = listJsonResult.getData();
            if (!CollectionUtils.isEmpty(templateListDTOList)) {
                System.out.println("templateListDTOList: " + JSONObject.toJSONString(templateListDTOList));
            } else {
                System.out.println("管理方下有效的雇员扩展字段模板 返回结果为空!");
            }
        } else {
            System.out.println("管理方下有效的雇员扩展字段模板 接口调用异常!");
        }
    }

    /**
     * 列出指定管理方下指定雇员扩展字段模板中参与薪资计算的扩展字段
     */
    @Test
    public void listTemplateFields() {
        JsonResult<List<EmployeeExtendFieldDTO>> listJsonResult = empExtendFieldTemplateProxy.listTemplateFields(50L);
        if (!ObjectUtils.isEmpty(listJsonResult)) {
            List<EmployeeExtendFieldDTO> extendFieldDTOList = listJsonResult.getData();
            if (!CollectionUtils.isEmpty(extendFieldDTOList)) {
                System.out.println("extendFieldDTOList: " + JSONObject.toJSONString(extendFieldDTOList));
            } else {
                System.out.println("列出指定管理方下指定雇员扩展字段模板中参与薪资计算的扩展字段 返回结果为空!");
            }
        } else {
            System.out.println("列出指定管理方下指定雇员扩展字段模板中参与薪资计算的扩展字段 接口调用异常!");
        }
    }
}
