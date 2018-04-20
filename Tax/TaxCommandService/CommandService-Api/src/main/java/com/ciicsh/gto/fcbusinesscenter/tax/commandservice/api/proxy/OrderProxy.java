package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.OrderDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import java.util.ArrayList;
import java.util.Map;

/**
 * 任务单接口
 * @author wuhua
 */
@FeignClient(name="fcbusiness-center-tax-command-service")
@RequestMapping(value = "/tax")
public interface OrderProxy {

    /**
     * 根据批次号查询任务单
     *
     * @param orderDTO
     * @return
     */
    @PostMapping(value = "/queryTasksByBatchIds")
    JsonResult<Map<String,ArrayList<OrderDTO>>> queryTasksByBatchIds(@RequestBody OrderDTO orderDTO);
}
