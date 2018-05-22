package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.mongodb.BasicDBObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.excel.poi.PoiItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.io.PushbackInputStream;
import java.util.List;
import java.util.Map;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRExcelColumnsReader {

    private final static Logger logger = LoggerFactory.getLogger(PRExcelColumnsReader.class);

    @Autowired
    private PRExcelColumnsMapper mapper;

    public PoiItemReader<List<String>> getReader(InputStream stream) throws  Exception{
        PoiItemReader<List<String>> itemReader = new PoiItemReader<>();
        itemReader.setLinesToSkip(1);  //First line is column names
        PushbackInputStream columnStream = new PushbackInputStream(stream);
        itemReader.setResource(new InputStreamResource(columnStream));
        itemReader.setRowMapper(mapper);
        itemReader.setSkippedRowsCallback( rowSet ->{
            logger.info(rowSet.getCurrentRow().toString());
        });
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
