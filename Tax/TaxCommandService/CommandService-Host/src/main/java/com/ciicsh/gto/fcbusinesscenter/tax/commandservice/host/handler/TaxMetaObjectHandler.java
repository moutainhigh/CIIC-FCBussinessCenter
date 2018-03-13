package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept.LoginInfoHolder;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * @author wuhua
 */
public class TaxMetaObjectHandler extends MetaObjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaxMetaObjectHandler.class);

    public void insertFill(MetaObject metaObject) {

        LocalDateTime llt = LocalDateTime.now();

        try {
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            //创建人
            setFieldValByName("createdBy", userInfoResponseDTO.getLoginName(), metaObject);
            //修改人
            setFieldValByName("modifiedBy", userInfoResponseDTO.getLoginName(), metaObject);
        } catch (Exception e) {
            logger.error("TaxMetaObjectHandler.insertFill "+e.getMessage());
            setFieldValByName("createdBy", "", metaObject);
            setFieldValByName("modifiedBy", "", metaObject);
        }finally {
            //创建时间
            setFieldValByName("createdTime",llt , metaObject);
            //修改时间
            setFieldValByName("modifiedTime", llt, metaObject);
        }
    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {

        LocalDateTime llt = LocalDateTime.now();

        try {
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = LoginInfoHolder.get().getResult().getObject();
            //修改人
            setFieldValByName("modifiedBy", userInfoResponseDTO.getLoginName(), metaObject);
        } catch (Exception e) {
            logger.error("TaxMetaObjectHandler.updateFill " + e.getMessage());
            setFieldValByName("modifiedBy", "", metaObject);
        }finally {
            //修改时间
            setFieldValByName("modifiedTime", llt, metaObject);
        }
    }
}
