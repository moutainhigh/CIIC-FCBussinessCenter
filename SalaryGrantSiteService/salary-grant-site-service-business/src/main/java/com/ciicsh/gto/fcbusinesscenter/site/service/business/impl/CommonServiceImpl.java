package com.ciicsh.gto.fcbusinesscenter.site.service.business.impl;

import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.site.service.business.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 公共服务接口 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-30
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private EntityIdServiceProxy entityIdServiceProxy;

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        // todo
        // 定义薪资发放code，在Confluence上EntityID编号规则中进行定义
        String idCode = (String)entityParam.get("idCode");
        // 获取公共服务生成返回的entity_id
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }
}
