package com.ciicsh.gto.salarymanagement.entity.utils;

import com.ciicsh.gto.salarymanagement.entity.enums.ValuedEnum;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 此帮助类严格限定为有value和label的枚举类，如定义枚举类型为 ADMIN(value, label)这种
 *
 * @author   NeoJiang
 * @version  V1.0
 */
public class EnumHelpUtil {

    /**
     * 获取Enum的label值
     * @param clazz
     * @param value
     * @param <T>
     * @return
     */
    public static <T extends ValuedEnum> String getLabelByValue(Class<T> clazz, int value) {
        T[] arr = clazz.getEnumConstants();
        if (arr == null) {
            return null;
        }
        for (T e : arr) {
            if (value == e.getValue()) {
                return e.getLabel();
            }
        }
        return null;
    }

    /**
     * 获取Enum的label list
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends ValuedEnum> List<String> getEnumLabels(Class<T> clazz) {
        return Arrays.stream(clazz.getEnumConstants())
                .map(T::getLabel)
                .collect(Collectors.toList());
    }

    /**
     * 获取Enum的自定义map
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T extends ValuedEnum> List<HashMap<String, Object>> getLabelValueList(Class<T> clazz) {
        return Arrays.stream(clazz.getEnumConstants())
                .map(i -> {
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("label", i.getLabel());
                    map.put("value", i.getValue());
                    return map;
                })
                .collect(Collectors.toList());
    }
}