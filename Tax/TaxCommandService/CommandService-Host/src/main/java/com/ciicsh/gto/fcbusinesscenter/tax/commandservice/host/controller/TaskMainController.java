package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskMainDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskMainService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.data.RequestForTaskMain;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.data.ResponseForTaskMain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author wuhua
 */
@RestController
public class TaskMainController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TaskMainController.class);

    @Autowired
    public TaskMainService taskMainService;

    /**
     * 查询主任务列表
     * @param taskMainDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskMains")
    public JsonResult queryTaskMains(@RequestBody TaskMainDTO taskMainDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForTaskMain requestForTaskMain = new RequestForTaskMain();
            BeanUtils.copyProperties(taskMainDTO, requestForTaskMain);
            ResponseForTaskMain responseForTaskMain = taskMainService.queryTaskMains(requestForTaskMain);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForTaskMain);

        } catch (Exception e) {
            logger.error(e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }

        return jr;
    }

}
