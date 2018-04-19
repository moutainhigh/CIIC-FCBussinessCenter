package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform;

import org.springframework.beans.BeanUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 通用转换器
 *
 * @author chenpb
 * @since 2018-04-19
 */
public class CommonTransform {
    /**
     * @param list 实体，即：bo或者po
     * @param t    dto
     * @return
     */
    public static <T, E> List<T> convertToDTOs(Collection<E> list, Class<T> t) {
        if (list == null || list.size() <= 0) {
            return Collections.emptyList();
        }
        return list.stream().map(e -> convertToDTO(e, t)).collect(Collectors.toList());
    }

    /**
     * @param e 实体，即：bo或者po
     * @param t dto
     * @return
     */
    public static <T, E> T convertToDTO(E e, Class<T> t) {
        T dto = BeanUtils.instantiate(t);
        if (e == null) {
            return null;
        }
        BeanUtils.copyProperties(e, dto);
        return dto;
    }

    public static <E, T> List<E> convertToEntities(Collection<T> list, Class<E> e) {
        if (list == null || list.size() <= 0) {
            return Collections.emptyList();
        }
        return list.stream().map(t -> convertToEntity(t, e)).collect(Collectors.toList());
    }

    public static <E, T> E convertToEntity(T t, Class<E> e) {
        E entity = BeanUtils.instantiate(e);
        BeanUtils.copyProperties(t, entity);
        return entity;
    }
}
