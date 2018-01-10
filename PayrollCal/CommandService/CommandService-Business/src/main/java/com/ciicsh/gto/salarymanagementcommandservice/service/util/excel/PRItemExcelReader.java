package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRItemExcelReader {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelReader.class);

    @Autowired
    private PRItemExcelMapper mapper;

    public PoiItemReader<List<BasicDBObject>> getPrGroupReader(InputStream stream, int importType, List<List<BasicDBObject>> payItems) throws  Exception{
        PoiItemReader<List<BasicDBObject>> itemReader = new PoiItemReader<>();
        itemReader.setLinesToSkip(1);  //First line is column names
        PushbackInputStream stream1 = new PushbackInputStream(stream);
        itemReader.setResource(new InputStreamResource(stream1));
        mapper.setList(payItems); //多个雇员薪资项列表
        mapper.setImportType(importType);
        itemReader.setRowMapper(mapper);
        itemReader.setSkippedRowsCallback( rowSet ->{
            logger.info(rowSet.getCurrentRow().toString());
        });
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
