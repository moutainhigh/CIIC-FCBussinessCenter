package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.OrderDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.proxy.OrderProxy;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.commons.collections.map.HashedMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * 任务单controller
 * @author wuhua
 */
@RestController
public class OrderController implements OrderProxy {

    /**
     * 根据批次号查询任务单
     *
     * @param orderDTO
     * @return
     */
    public JsonResult<Map<String,ArrayList<OrderDTO>>> queryTasksByBatchIds(@RequestBody OrderDTO orderDTO) {

        JsonResult<Map<String,ArrayList<OrderDTO>>> jr = new JsonResult<>();

        try {
            String batchIds = orderDTO.getBatchIds();
            Map<String,ArrayList<OrderDTO>> map = new HashedMap();
            if(StrKit.isNotEmpty(batchIds)){

                String[] batchidArray = batchIds.split(",");

                for(String bt : batchidArray){

                    ArrayList<OrderDTO> orders = new ArrayList<>();//主任务
                    map.put(bt,orders);
                    OrderDTO order1 = new OrderDTO();
                    order1.setTaskType("submitMainTask");
                    order1.setTaskId("00001");
                    order1.setTaskName("提交主任务");
                    orders.add(order1);
                    OrderDTO order2 = new OrderDTO();
                    order2.setTaskType("checkMainTask");
                    order2.setTaskId("00002");
                    order2.setTaskName("审批主任务");
                    orders.add(order2);
                    OrderDTO order3 = new OrderDTO();
                    order3.setTaskType("declareSubTask");
                    order3.setTaskId("00003");
                    order3.setTaskName("申报子任务");
                    orders.add(order3);
                    OrderDTO order4 = new OrderDTO();
                    order4.setTaskType("proofSubTask");
                    order4.setTaskId("00004");
                    order4.setTaskName("完税凭证子任务");
                    orders.add(order4);
                    OrderDTO order5 = new OrderDTO();
                    order5.setTaskType("moneySubTask");
                    order5.setTaskId("00005");
                    order5.setTaskName("划款子任务");
                    orders.add(order5);
                    OrderDTO order6 = new OrderDTO();
                    order6.setTaskType("paymentSubTask");
                    order6.setTaskId("00006");
                    order6.setTaskName("缴纳子任务");
                    orders.add(order6);
                    OrderDTO order7 = new OrderDTO();
                    order7.setTaskType("supplierSubTask");
                    order7.setTaskId("00007");
                    order7.setTaskName("供应商子任务");
                    orders.add(order7);

                }
            }

            jr.fill(map);
            LogTaskFactory.getLogger().info("", "OrderController.queryCalculationBatchs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "08"), LogType.APP, null);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("batchIds", orderDTO.getBatchIds());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "OrderController.queryCalculationBatchs", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "08"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

}
