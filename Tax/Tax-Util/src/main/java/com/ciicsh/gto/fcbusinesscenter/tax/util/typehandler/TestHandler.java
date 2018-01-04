package com.ciicsh.gto.fcbusinesscenter.tax.util.typehandler;

import com.alibaba.fastjson.JSON;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TestHandler extends BaseTypeHandler<Integer[]>{
    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, Integer[] parameter, JdbcType jdbcType) throws SQLException {
        ps.setString(i, Arrays.<Integer>asList(parameter).toString());
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, String columnName) throws SQLException {
        if (rs.getString(columnName) == null)
            return new Integer[0];
        List<Integer> intArr = JSON.parseArray(rs.getString(columnName), Integer.class);
        return intArr.toArray(new Integer[intArr.size()]);
    }

    @Override
    public Integer[] getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        if (rs.getString(columnIndex) == null)
            return new Integer[0];
        List<Integer> intArr = JSON.parseArray(rs.getString(columnIndex), Integer.class);
        return intArr.toArray(new Integer[intArr.size()]);
    }

    @Override
    public Integer[] getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        if (cs.getString(columnIndex) == null)
            return new Integer[0];
        List<Integer> intArr = JSON.parseArray(cs.getString(columnIndex), Integer.class);
        return intArr.toArray(new Integer[intArr.size()]);
    }
}
