package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * <p>
 * 公共服务接口 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-05-07
 */
@Service
public class CommonServiceImpl implements CommonService {
    @Autowired
    private DicItemServiceProxy dicItemServiceProxy;
    @Autowired
    private EntityIdServiceProxy entityIdServiceProxy;

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
        return dicItemDTO.getDicItemText();
    }

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        // 定义薪资发放code，在Confluence上EntityID编号规则中进行定义
        String idCode = (String) entityParam.get("idCode");
        // 获取公共服务生成返回的entity_id
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }
}
