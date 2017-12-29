package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.controller;


import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.dto.WorkingCalendarPageDTO;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.po.WorkingCalendarPO;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.translator.WorkCalendarTranslator;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.business.WorkingCalendarService;
import com.ciicsh.gto.fcsupportcenter.util.page.PageUtil;
import com.ciicsh.gto.fcsupportcenter.util.result.JsonResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * <p>
 * 工作日历 前端控制器
 * </p>
 *
 * @author guwei
 * @since 2017-12-27
 */
@RestController
@RequestMapping("/api/")
public class WorkingCalendarController extends BaseController{

    @Autowired
    private WorkingCalendarService workingCalendarService;

    /**
     * 获取工作日历列表
     * @param managementId
     * @param pageNum
     * @param pageSize
     * @return
     */
    @GetMapping("/getWorkingCalendarPage")
    public JsonResult getWorkingCalendarPage(String managementId, Integer pageNum, Integer pageSize){
        WorkingCalendarPageDTO workingCalendarPageDTO = new WorkingCalendarPageDTO();
        workingCalendarPageDTO.setManagementId(managementId);
        WorkingCalendarPO workingCalendarPO = WorkCalendarTranslator.convertToPO(workingCalendarPageDTO);
        Page page = new Page(PageUtil.setPageNum(pageNum), PageUtil.setPageSize(pageSize));
        List<WorkingCalendarPO> workingCalendarList = workingCalendarService.getWorkingCalendarList(workingCalendarPO, page);
        page.setRecords(workingCalendarList.stream().map(WorkCalendarTranslator::convertToDTO).collect(Collectors.toList()));
        return JsonResult.success(page);
    }

    /**
     * 获取工作日历详情
     * @param cmyFcWorkingCalendarId
     * @return
     */
    @GetMapping("/getWorkingCalendarItem/{id}")
    public JsonResult getWorkingCalendarItem(@PathVariable("id") int cmyFcWorkingCalendarId) {
        WorkingCalendarPO workingCalendarPO = workingCalendarService.selectById(cmyFcWorkingCalendarId);
        return JsonResult.success(WorkCalendarTranslator.convertToDTO(workingCalendarPO));
    }

    /**
     * 更新工作日历
     * @param workingCalendarPageDTO
     * @return
     */
    @PutMapping("/updateWorkingCalendarItem")
    public JsonResult updateWorkingCalendarItem(@RequestBody WorkingCalendarPageDTO workingCalendarPageDTO){
        WorkingCalendarPO workingCalendarPO = WorkCalendarTranslator.convertToPO(workingCalendarPageDTO);
        return JsonResult.success(workingCalendarService.updateWorkingCalendar(workingCalendarPO));
    }

    /**
     * 添加工作日历
     * @param workingCalendarPageDTO
     * @return
     */
    @PostMapping("/addWorkingCalendarItem")
    public JsonResult addWorkingCalendarItem(@RequestBody WorkingCalendarPageDTO workingCalendarPageDTO){
        WorkingCalendarPO workingCalendarPO = WorkCalendarTranslator.convertToPO(workingCalendarPageDTO);

        //Todo
        workingCalendarPO.setActive(true);
        workingCalendarPO.setCreatedTime(new Date());
        workingCalendarPO.setCreatedBy("gu");
        workingCalendarPO.setModifiedTime(new Date());
        workingCalendarPO.setModifiedBy("gu");
        return JsonResult.success(workingCalendarService.addWorkingCalendar(workingCalendarPO));
    }

    /**
     * 删除工作日历
     * @param cmyFcWorkingCalendarId
     * @return
     */
    @DeleteMapping("/dwlWorkingCalendarItem/{id}")
    public JsonResult deleteWorkingCalendarItem(@PathVariable("id") int cmyFcWorkingCalendarId){
        return JsonResult.success(workingCalendarService.deleteWorkingCalendar(cmyFcWorkingCalendarId));
    }
}
