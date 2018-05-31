package com.ciicsh.gto.salarymanagementcommandservice.service.util.excel;

import com.mongodb.BasicDBObject;
import org.springframework.batch.item.excel.RowMapper;
import org.springframework.batch.item.excel.support.rowset.RowSet;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * Created by bill on 18/5/14.
 */
@Component
public class PRExcelColumnsMapper implements RowMapper<List<String>> {

    @Override
    public List<String> mapRow(RowSet rs) throws Exception {
        String[] colNames = rs.getMetaData().getColumnNames();

        if(colNames !=null && colNames.length > 0){
            return Arrays.asList(colNames);
        }
        return null;
    }
}
