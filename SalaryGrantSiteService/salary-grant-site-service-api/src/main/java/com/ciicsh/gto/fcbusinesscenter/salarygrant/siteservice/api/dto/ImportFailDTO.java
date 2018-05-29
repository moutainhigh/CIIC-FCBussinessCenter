package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto;

import lombok.*;

import java.util.List;

/**
 * <p>
 * 暂缓失败雇员信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-24
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
public class ImportFailDTO {
    /**
     * 暂缓雇员列表
     */
    private List<ReprieveEmployeeDTO> failList;
}
