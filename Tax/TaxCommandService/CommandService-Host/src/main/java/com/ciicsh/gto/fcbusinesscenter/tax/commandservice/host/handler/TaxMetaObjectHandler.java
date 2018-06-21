package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;
import org.apache.ibatis.reflection.MetaObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;

/**
 * 公共字段自动填充
 * @author wuhua
 */
public class TaxMetaObjectHandler extends MetaObjectHandler {

    private static final Logger logger = LoggerFactory.getLogger(TaxMetaObjectHandler.class);

    @Override
    public void insertFill(MetaObject metaObject) {

        LocalDateTime llt = LocalDateTime.now();

        try {
            //登录信息
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            if(userInfoResponseDTO!=null){
                //创建人
                setFieldValByName("createdBy", userInfoResponseDTO.getLoginName(), metaObject);
                //修改人
                setFieldValByName("modifiedBy", userInfoResponseDTO.getLoginName(), metaObject);
                //创建人displayname
                setFieldValByName("createdByDisplayName", userInfoResponseDTO.getDisplayName(), metaObject);
                //修改人displayname
                setFieldValByName("modifiedByDisplayName", userInfoResponseDTO.getDisplayName(), metaObject);
            }else{
                //创建人
                setFieldValByName("createdBy", "", metaObject);
                //修改人
                setFieldValByName("modifiedBy", "", metaObject);
                //创建人displayname
                setFieldValByName("createdByDisplayName", "", metaObject);
                //修改人displayname
                setFieldValByName("modifiedByDisplayName", "", metaObject);
            }
        } catch (Exception e) {
            logger.error("",e);
            setFieldValByName("createdBy", "", metaObject);
            setFieldValByName("modifiedBy", "", metaObject);
            setFieldValByName("createdByDisplayName", "", metaObject);
            setFieldValByName("modifiedByDisplayName", "", metaObject);
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
            UserInfoResponseDTO userInfoResponseDTO = UserContext.getUser();
            if(userInfoResponseDTO!=null){
                //修改人
                setFieldValByName("modifiedBy", userInfoResponseDTO.getLoginName(), metaObject);
                //修改人displayname
                setFieldValByName("modifiedByDisplayName", userInfoResponseDTO.getDisplayName(), metaObject);
            }else{
                //修改人
                setFieldValByName("modifiedBy", "", metaObject);
                //修改人displayname
                setFieldValByName("modifiedByDisplayName", "", metaObject);
            }

        } catch (Exception e) {
            logger.error("",e);
            setFieldValByName("modifiedBy", "", metaObject);
            setFieldValByName("modifiedByDisplayName", "", metaObject);
        }finally {
            //修改时间
            setFieldValByName("modifiedTime", llt, metaObject);
        }
    }
}
