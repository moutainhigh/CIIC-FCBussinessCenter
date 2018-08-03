package com.ciicsh.gto.salarymanagementcommandservice;

import com.alibaba.fastjson.JSONObject;
import com.ciicsh.common.entity.JsonResult;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeExtendFieldProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.EmployeeExtendFieldRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.EmployeeExtendFieldResponseDTO;
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
 * @date 2018/8/2 0002
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = App.class)
public class EmployeeExtendFieldProxyTest {
    @Autowired
    private EmployeeExtendFieldProxy employeeExtendFieldProxy;

    @Test
    public void getEmployeeExtendFieldDetails() {
        EmployeeExtendFieldRequestDTO extendFieldRequestDTO = new EmployeeExtendFieldRequestDTO();
        extendFieldRequestDTO.setCompanyId("KH18000371");
        extendFieldRequestDTO.setEmployeeId("18024365");
        extendFieldRequestDTO.setEmpExtendFieldTemplateId(50L);
        JsonResult<List<EmployeeExtendFieldResponseDTO>> listJsonResult = employeeExtendFieldProxy.getEmployeeExtendFieldDetails(extendFieldRequestDTO);
        if (!ObjectUtils.isEmpty(listJsonResult)) {
            List<EmployeeExtendFieldResponseDTO> extendFieldResponseDTOList = listJsonResult.getData();
            if (!CollectionUtils.isEmpty(extendFieldResponseDTOList)) {
                System.out.println("雇员扩展字段薪资计算接口调用 返回: " + JSONObject.toJSONString(extendFieldResponseDTOList));
            } else {
                System.out.println("雇员扩展字段薪资计算接口调用 返回为空");
            }
        } else {
            System.out.println("雇员扩展字段薪资计算接口调用 异常");
        }
    }
}
