package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.transform;

import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.salecenter.apiservice.api.dto.core.Pagination;

import java.util.List;

/**
 * 通用分页实体转换器
 *
 * @author 李理
 * @since 2017/12/27
 */
public class PageUtil {
    /**
     * 将分页数据内的实体转为DTO
     *
     * @param page
     * @param t    目标对象，即DTO
     * @param <T>  DTO
     * @param <E>  实体
     * @return
     */
    public static <T, E> Pagination<T> changeWapper(Page<E> page, Class<T> t) {
        Pagination<T> pagination = new Pagination<>(page.getCurrent(), page.getSize());
        pagination.setTotal(page.getTotal());
        List<T> list = CommonTransform.convertToDTOs(page.getRecords(), t);
        pagination.setRecords(list);
        return pagination;
    }
}
