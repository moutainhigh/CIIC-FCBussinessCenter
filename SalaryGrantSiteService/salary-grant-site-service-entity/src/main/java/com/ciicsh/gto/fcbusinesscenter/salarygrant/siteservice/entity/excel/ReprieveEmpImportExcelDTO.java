package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel;

import cn.afterturn.easypoi.excel.annotation.Excel;
import cn.afterturn.easypoi.excel.annotation.ExcelTarget;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import lombok.*;

import java.util.Date;

/**
 * <p>
 * 暂缓雇员导入信息
 * </p>
 *
 * @author chenpeb
 * @since 2018-05-16
 */
@Getter
@Setter
@ToString
@Data
@EqualsAndHashCode(callSuper = false)
@ExcelTarget("ReprieveEmpImportExcel")
public class ReprieveEmpImportExcelDTO {
//    @Excel(name = "暂缓编号", orderNum = "1")
//    private Long salarygrantReprieveEmployeeImportId;
    @Excel(name = "任务单编号", orderNum = "1")
    private String taskCode;
    @Excel(name = "任务单类型", orderNum = "2", replace = { "主表_0", "中智大库_1" , "中智代发（委托机构）_2" , "中智代发（客户账户）_3", "客户自行_4"})
    private Integer taskType;
    @Excel(name = "雇员编号", orderNum = "3")
    private String employeeId;
    @Excel(name = "雇员姓名", orderNum = "4")
    private String employeeName;
//    @Excel(name = "导入时间", orderNum = "5")
//    private Date importTime;
//    @Excel(name = "创建人", orderNum = "6")
//    private String createdBy;
//    @Excel(name = "创建时间", orderNum = "7")
//    private Date createdTime;
//    @Excel(name = "最后修改人", orderNum = "9")
//    private String modifiedBy;
//    @Excel(name = "最后修改时间", orderNum = "10")
//    private Date modifiedTime;
}
