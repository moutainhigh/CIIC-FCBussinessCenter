package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CountryDTO;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import org.apache.commons.lang.StringUtils;
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
    @Autowired
    private CountryServiceProxy countryServiceProxy;

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        if (StringUtils.isEmpty(dicValue)) {
            return null;
        } else if (StringUtils.isEmpty(dicItemValue)) {
            return null;
        } else {
            DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
            if (dicItemDTO == null || "".equals(dicItemDTO.getDicItemText())) {
                return null;
            } else {
                return dicItemDTO.getDicItemText();
            }
        }
    }

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        String idCode = (String) entityParam.get("idCode");
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }

    @Override
    public String getCountryName(String countryCode) {
        if (StringUtils.isEmpty(countryCode)) {
            return null;
        } else {
            CountryDTO countryDTO = countryServiceProxy.selectByCountryCode(countryCode);
            if (countryDTO == null || "".equals(countryDTO.getCountryName())) {
                return null;
            } else {
                return countryDTO.getCountryName();
            }
        }
    }
}
