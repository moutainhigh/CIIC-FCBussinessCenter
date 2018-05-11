package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CountryDTO;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;

import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.util.exception.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
    @Autowired
    private DicItemServiceProxy dicItemServiceProxy;
    @Autowired
    private CountryServiceProxy countryServiceProxy;

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        // todo
        // 定义薪资发放code，在Confluence上EntityID编号规则中进行定义
        String idCode = (String)entityParam.get("idCode");
        // 获取公共服务生成返回的entity_id
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        if(dicValue == null || "".equals(dicValue)){
            return null;
        }else if(dicItemValue == null || "".equals(dicItemValue)){
            return null;
        }else{
            DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
            if(dicItemDTO == null || "".equals(dicItemDTO.getDicItemText())){
                return null;
            }else{
                return dicItemDTO.getDicItemText();
            }
        }
    }

    @Override
    public List getNameByValueForList(String dicValue) {
        if(dicValue == null || "".equals(dicValue)){
            return null;
        }else{
            List<DicItemDTO> dicItemDTOList = dicItemServiceProxy.listByDicValue(dicValue);
            if(dicItemDTOList.isEmpty()){
                return null;
            }else{
                List dicItemTextList = dicItemDTOList.stream().map(DicItemDTO::getDicItemText).collect(Collectors.toList());
                return dicItemTextList;
            }
        }
    }

    @Override
    public String getCountryName(String countryCode) {
        if(countryCode == null || "".equals(countryCode)){
            return null;
        }else{
            CountryDTO countryDTO = countryServiceProxy.selectByCountryCode(countryCode);
            if(countryDTO == null || "".equals(countryDTO.getCountryName())){
                return null;
            }else{
                return countryDTO.getCountryName();
            }
        }
    }
}
