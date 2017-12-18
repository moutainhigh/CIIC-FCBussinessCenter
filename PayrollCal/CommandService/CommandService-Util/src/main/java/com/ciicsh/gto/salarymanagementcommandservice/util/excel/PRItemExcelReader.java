package com.ciicsh.gto.salarymanagementcommandservice.util.excel;

import com.ciicsh.gto.salarymanagement.entity.PrGroupEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.core.io.InputStreamResource;

import java.io.InputStream;
import java.io.PushbackInputStream;

/**
 * Created by bill on 17/12/15.
 */
public class PRItemExcelReader {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelReader.class);
    public static PoiItemReader<PrGroupEntity> getPrGroupReader(InputStream stream, PrGroupEntity entity) throws  Exception{
        PoiItemReader<PrGroupEntity> itemReader = new PoiItemReader<>();
        itemReader.setLinesToSkip(1);  //First line is column names
        PushbackInputStream stream1 = new PushbackInputStream(stream);
        itemReader.setResource(new InputStreamResource(stream1));
        PRItemExcelMapper mapper = new PRItemExcelMapper();
        mapper.setGroupEntity(entity);
        itemReader.setRowMapper(mapper);
        itemReader.setSkippedRowsCallback( rowSet ->{
            logger.info(rowSet.getCurrentRow().toString());
        });
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
