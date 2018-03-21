package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import java.io.File;
import java.io.FileInputStream;

public class BaseService {

    /**
     * 模板文件目录
     */
    public static final String TEMPLATE_FILE_PATH =
            Thread.currentThread().getContextClassLoader()
                    .getResource("template/file/")
                    .getPath().substring(1);

    /**
     * 通过文件名获取POIFSFileSystem对象
     *
     * @param fileName
     * @return
     */
    protected POIFSFileSystem getFSFileSystem(String fileName) {
        POIFSFileSystem fs = null;
        //excel全路径
        try {
            String excel = TEMPLATE_FILE_PATH + fileName;
            File file = new File(excel);
            fs = new POIFSFileSystem(new FileInputStream(file));
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
        }
        return fs;
    }

    /**
     * 通过POIFSFileSystem对象获取wb
     *
     * @param fs
     * @return
     */
    protected HSSFWorkbook getHSSFWorkbook(POIFSFileSystem fs) {
        HSSFWorkbook wb = null;
        //excel全路径
        try {
            // 读取excel模板
            wb = new HSSFWorkbook(fs);
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
        }
        return wb;
    }
}
