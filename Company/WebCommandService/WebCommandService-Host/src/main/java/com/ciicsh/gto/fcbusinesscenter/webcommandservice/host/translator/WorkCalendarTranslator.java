package com.ciicsh.gto.fcbusinesscenter.webcommandservice.host.translator;

import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.dto.WorkingCalendarPageDTO;
import com.ciicsh.gto.fcbusinesscenter.webcommandservice.entity.po.WorkingCalendarPO;
import org.springframework.beans.BeanUtils;

/**
 * @Author: guwei
 * @Description: 工作日历转换类
 * @Date: Created in 13:16 2017/12/28
 */
public class WorkCalendarTranslator {

    /**
     * workingCalendarPageDTO 2 WorkingCalendarPO
     * @param workingCalendarPageDTO
     * @return
     */
    public static WorkingCalendarPO convertToPO(WorkingCalendarPageDTO workingCalendarPageDTO){
        WorkingCalendarPO workingCalendarPO = new WorkingCalendarPO();
        BeanUtils.copyProperties(workingCalendarPageDTO,workingCalendarPO);
        return workingCalendarPO;
    }

    /**
     * workingCalendarPO 2 WorkingCalendarPageDTO
     * @param workingCalendarPO
     * @return
     */
    public static WorkingCalendarPageDTO convertToDTO(WorkingCalendarPO workingCalendarPO){
        WorkingCalendarPageDTO workingCalendarPageDTO = new WorkingCalendarPageDTO();
        BeanUtils.copyProperties(workingCalendarPO,workingCalendarPageDTO);
        return workingCalendarPageDTO;
    }
}
