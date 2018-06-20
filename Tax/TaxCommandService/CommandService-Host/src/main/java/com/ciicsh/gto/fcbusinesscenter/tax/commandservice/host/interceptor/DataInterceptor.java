package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.interceptor;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.CommonService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.commondata.BasicData;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 加载basic data
 */
public class DataInterceptor extends HandlerInterceptorAdapter {

    @Autowired
    private CommonService commonService;

    public DataInterceptor() {
    }

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        try {
            BasicData.getInstance().setCertType(commonService.getDicItemByDicValue("certType"));
            //获取报税证件类型
            BasicData.getInstance().setTaxCertType(commonService.getDicItemByDicValue("taxCertType"));
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "DataInterceptor.preHandle", null, LogType.APP,null);
        }

        return true;
    }
}
