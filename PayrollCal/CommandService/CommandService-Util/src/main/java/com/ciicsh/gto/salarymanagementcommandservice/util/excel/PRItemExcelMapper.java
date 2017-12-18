package com.ciicsh.gto.salarymanagementcommandservice.util.excel;

import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import com.ciicsh.gto.salarymanagement.entity.PrItemEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;

import java.util.List;

/**
 * Created by bill on 17/12/15.
 */
public class PRItemExcelMapper implements RowMapper<PrGroupEntity> {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelMapper.class);

    private PrGroupEntity groupEntity;

    public PrGroupEntity getGroupEntity() {
        return groupEntity;
    }

    public void setGroupEntity(PrGroupEntity groupEntity) {
        this.groupEntity = groupEntity;
    }

    @Override
    public PrGroupEntity mapRow(RowSet rs) throws Exception {

        int totalCount = rs.getMetaData().getColumnCount();
        List<PrItemEntity> itemEntities = this.groupEntity.getPrItemEntityList();
        if(totalCount != itemEntities.size()){
            logger.info("column count is not equal");
        }
        itemEntities.stream().forEach(item -> {
            String prName = item.getName();              // 薪资项名称
            Object val = rs.getProperties().get(prName);
            if(val == null){
                logger.info("不存在列名：" + prName);
            }else {
                item.setItemValue(val); //把EXCEL的值 赋予 该薪资项
            }

        });
        return this.groupEntity;
    }

}
