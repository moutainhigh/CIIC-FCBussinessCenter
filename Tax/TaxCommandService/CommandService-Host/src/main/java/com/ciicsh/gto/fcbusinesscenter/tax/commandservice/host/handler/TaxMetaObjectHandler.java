package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.handler;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import java.util.Date;

/**
 * @author wuhua
 */
public class TaxMetaObjectHandler extends MetaObjectHandler {

    public void insertFill(MetaObject metaObject) {

    }

    /**
     * 更新填充
     * @param metaObject
     */
    @Override
    public void updateFill(MetaObject metaObject) {
        //修改时间
        setFieldValByName("modifiedTime", new Date(), metaObject);
    }
}
