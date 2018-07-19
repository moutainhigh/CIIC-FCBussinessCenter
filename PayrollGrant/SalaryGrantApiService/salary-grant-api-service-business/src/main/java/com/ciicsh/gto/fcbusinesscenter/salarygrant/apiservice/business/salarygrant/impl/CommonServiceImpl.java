package com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.impl;

import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.apiservice.business.salarygrant.CommonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
        return dicItemDTO.getDicItemText();
    }
}
