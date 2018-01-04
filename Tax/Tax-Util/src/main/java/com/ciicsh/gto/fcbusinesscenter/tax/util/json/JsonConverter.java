package com.ciicsh.gto.fcbusinesscenter.tax.util.json;

import com.alibaba.fastjson.JSON;
import java.util.List;

public final class JsonConverter {
    private JsonConverter() { }

    public static <T> List<T> convertToEntityArr(Object jsonObj, Class<T> clazz) {
        return JSON.parseArray(JSON.toJSONString(jsonObj), clazz);
    }

    public static <T> T convertToEntity(Object jsonObj, Class<T> clazz) {
        return JSON.parseObject(JSON.toJSONString(jsonObj), clazz);

    }
}
