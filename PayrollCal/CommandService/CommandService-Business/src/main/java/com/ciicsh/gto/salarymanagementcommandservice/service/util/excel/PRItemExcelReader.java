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
import java.util.Map;

/**
 * Created by bill on 17/12/15.
 */
@Component
public class PRItemExcelReader {

    private final static Logger logger = LoggerFactory.getLogger(PRItemExcelReader.class);

    @Autowired
    private PRItemExcelMapper mapper;

    public PoiItemReader<List<BasicDBObject>> readExcel(Map<String,Object> payItemMap,Map<String,Object> identityMap,String batchCode, int batchType, InputStream stream) throws  Exception{
        PoiItemReader<List<BasicDBObject>> itemReader = new PoiItemReader<>();
        itemReader.setLinesToSkip(1);  //First line is column names
        PushbackInputStream excelStream = new PushbackInputStream(stream);
        itemReader.setResource(new InputStreamResource(excelStream));
        mapper.setPayItemMap(payItemMap);
        mapper.setIdentityMap(identityMap);
        mapper.setBatchCode(batchCode);
        mapper.setBatchType(batchType);
        itemReader.setRowMapper(mapper);
        itemReader.setSkippedRowsCallback( rowSet ->{
            logger.info(rowSet.getCurrentRow().toString());
        });
        itemReader.afterPropertiesSet();
        return itemReader;
    }
}
