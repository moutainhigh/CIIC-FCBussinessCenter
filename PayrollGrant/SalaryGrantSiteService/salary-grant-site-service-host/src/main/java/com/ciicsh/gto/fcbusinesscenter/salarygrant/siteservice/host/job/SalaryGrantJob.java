package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.job;


import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantJobService;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * <p>
 * 薪资发放定时任务
 * </p>
 *
 * @author chenpb
 * @since 2018-07-26
 */
@Component
public class SalaryGrantJob {
    @Autowired
    LogClientService logClientService;
    @Autowired
    SalaryGrantJobService salaryGrantJobService;

    /**
     * @description 薪资发放任务
     * 正式每天晚上20点执行
     *
     * @author chenpb
     * @since 2018-07-26
     * @param
     * @return
     */
    //@Scheduled(cron = "0 0 20 * * ?")
    @Scheduled(cron = "0 0/3 * * * ?")
    public void handle() {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步薪资发放数据到结算中心").setContent("开始"));
            salaryGrantJobService.handle();
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步薪资发放数据到结算中心").setContent("结束"));
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步薪资发放数据到结算中心").setContent("异常"));
        }
    }
}
