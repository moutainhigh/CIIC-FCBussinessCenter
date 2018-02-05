package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.TaskSubDeclareDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.TaskSubDeclareService;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.declare.RequestForTaskSubDeclare;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.declare.ResponseForTaskSubDeclare;
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
public class TaskSubDeclareController extends BaseController{

    private static final Logger logger = LoggerFactory.getLogger(TaskSubDeclareController.class);

    @Autowired
    public TaskSubDeclareService taskSubDeclareService;

    /**
     * 查询申报子任务列表
     * @param taskSubDeclareDTO
     * @return
     */
    @RequestMapping(value = "/queryTaskSubDeclares")
    public JsonResult queryTaskSubDeclares(@RequestBody TaskSubDeclareDTO taskSubDeclareDTO) {

        JsonResult jr = new JsonResult();

        try {
            RequestForTaskSubDeclare requestForTaskSubDeclare = new RequestForTaskSubDeclare();
            BeanUtils.copyProperties(taskSubDeclareDTO, requestForTaskSubDeclare);
            ResponseForTaskSubDeclare responseForTaskSubDeclare = taskSubDeclareService.queryTaskSubDeclares(requestForTaskSubDeclare);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForTaskSubDeclare);

        } catch (Exception e) {
            e.printStackTrace();
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }

        return jr;
    }


}
