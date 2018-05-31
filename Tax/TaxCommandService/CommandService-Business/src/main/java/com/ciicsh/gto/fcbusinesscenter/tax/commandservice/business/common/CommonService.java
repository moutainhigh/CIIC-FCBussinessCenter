package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common;

import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 获取公共信息
 * @author wuhua
 */
@Service
public class CommonService {

    @Autowired
    private DicItemServiceProxy dicItemServiceProxy;

    public Map<String,String> getDicItemByDicValue(String dicValue){

        Map<String,String> map = new HashMap<>();

        try {
            List<DicItemDTO> dicItemList = dicItemServiceProxy.listByDicValue(dicValue);

            if(dicItemList !=null){
                map = dicItemList.stream().collect(Collectors.toMap(DicItemDTO::getDicItemValue, DicItemDTO::getDicItemText));
            }
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "CommonService.getDicItemByDicValue", null, LogType.APP,null);
        }

        return map;

    }


}
