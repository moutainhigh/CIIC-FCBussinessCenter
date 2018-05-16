package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantReprieveEmployeeImportService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantEmployeeMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.dao.SalaryGrantReprieveEmployeeImportMapper;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.excel.ReprieveEmpImportDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantReprieveEmployeeImportPO;
import com.ciicsh.gto.fcbusinesscenter.util.poi.OfficeIoResult;
import com.ciicsh.gto.fcbusinesscenter.util.poi.OfficeIoUtils;
import com.ciicsh.gto.fcbusinesscenter.util.poi.model.CellSettings;
import com.ciicsh.gto.fcbusinesscenter.util.poi.model.SheetSettings;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * <p>
 * 薪资发放暂缓名单导入 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-28
 */
@Service
public class SalaryGrantReprieveEmployeeImportServiceImpl extends ServiceImpl<SalaryGrantReprieveEmployeeImportMapper, SalaryGrantReprieveEmployeeImportPO> implements SalaryGrantReprieveEmployeeImportService {
    @Autowired
    SalaryGrantReprieveEmployeeImportMapper salaryGrantReprieveEmployeeImportMapper;

    @Autowired
    SalaryGrantEmployeeMapper salaryGrantEmployeeMapper;

    /**
     * @description 导入暂缓名单
     * @author chenpb
     * @since 2018-05-16
     * @param inputStream
     */
    @Override
    public void importReprieveList(InputStream inputStream) throws IOException {
        OfficeIoResult officeIoResult = importXls(inputStream);
        List<ReprieveEmpImportDTO> list = officeIoResult.getImportList();
        List<SalaryGrantReprieveEmployeeImportPO> pos = new ArrayList<>();
        list.stream().forEach(x -> convertPos(x, pos));
        if (!pos.isEmpty()) {
            this.insertBatch(pos);
        }

        if (list != null && list.size() > 0) {
            exportXls(list);
        }

        if (!officeIoResult.isCompleted()) {
            officeIoResult.getErrors();
            OfficeIoResult result = OfficeIoUtils.exportErrorRecord(officeIoResult.getSheetSettings(), officeIoResult.getErrRecordRows());
            FileOutputStream out = new FileOutputStream("d:\\successError.xlsx");
            result.getResultWorkbook().write(out);
            out.close();
        }
    }

    /**
     * @description 导入暂缓名单
     * @author chenpb
     * @since 2018-05-16
     * @param dto
     * @param pos
     */
    private static void convertPos (ReprieveEmpImportDTO dto, List<SalaryGrantReprieveEmployeeImportPO> pos) {
        if (dto!=null) {
            SalaryGrantReprieveEmployeeImportPO po = BeanUtils.instantiate(SalaryGrantReprieveEmployeeImportPO.class);
            BeanUtils.copyProperties(dto, po);
            pos.add(po);
        }
    }

    private static OfficeIoResult importXls(InputStream inputStream) {
        SheetSettings sheet = new SheetSettings("sheet1", ReprieveEmpImportDTO.class);
        sheet = reprieveEmpCellSettings(sheet);

        OfficeIoResult officeIoResult = OfficeIoUtils.importXlsx(inputStream, new SheetSettings[]{sheet});
        return officeIoResult;
    }

    private static void exportXls(List list) throws IOException {
        SheetSettings sheet = new SheetSettings("导入成功的数据", ReprieveEmpImportDTO.class);
        sheet = reprieveEmpCellSettings(sheet);
        sheet.setExportData(list);

        FileOutputStream out = new FileOutputStream("d:\\successList.xlsx");
        OfficeIoResult result = OfficeIoUtils.exportXlsx(new SheetSettings[]{sheet});
        result.getResultWorkbook().write(out);
        out.close();
    }

    private static SheetSettings reprieveEmpCellSettings(SheetSettings sheetSettings) {
        sheetSettings.setCellSettings(new CellSettings[]{
                new CellSettings("salarygrantReprieveEmployeeImportId", "薪资发放暂缓名单编号"),
                new CellSettings("taskCode", "任务单编号"),
                new CellSettings("taskType", "任务单类型"),
                new CellSettings("employeeId", "雇员编号"),
                new CellSettings("employeeName", "雇员姓名"),
                new CellSettings("importTime", "导入时间"),
                new CellSettings("createdBy", "创建人"),
                new CellSettings("createdTime", "创建时间"),
                new CellSettings("modifiedBy", "最后修改人"),
                new CellSettings("modifiedTime", "最后修改时间")
        });
        return sheetSettings;
    }
}
