package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

/**
 * <p>
 * base控制器
 * </p>
 *
 * @author linhaihai
 * @since 2017-12-12
 */
@RequestMapping("/tax")
public class BaseController {

    @Autowired
    public LogService logService;

    private static final Logger logger = LoggerFactory.getLogger(BaseController.class);

    /**
     * 模板文件目录
     */
    public static final String TEMPLATE_FILE_PATH =
            Thread.currentThread().getContextClassLoader()
                    .getResource("template/file/")
                    .getPath().substring(1);

    /**
     * 下载文件
     *
     * @param response
     * @param fileName
     * @param content
     */
    protected void downloadFile(HttpServletResponse response, String fileName, byte[] content) {
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        BufferedInputStream bis = null;
        try {
            fileName = URLEncoder.encode(fileName, "UTF-8");
            response.reset();
            response.setContentType("application/octet-stream;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
            response.setContentLength(content.length);
            outputStream = response.getOutputStream();
            InputStream is = new ByteArrayInputStream(content);
            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(outputStream);

            byte[] buff = new byte[8192];
            int bytesRead;

            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {

                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "BaseController.downloadFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
        } finally {
            try {
                bis.close();
            } catch (Exception e) {
                logger.error("close bis error " + e.toString());
            }
            try {
                bos.close();
            } catch (Exception e) {
                logger.error("close bos error " + e.toString());
            }
            try {
                outputStream.flush();
            } catch (Exception e) {
                logger.error("flush outputStream error " + e.toString());
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                logger.error("close outputStream error " + e.toString());
            }
        }

    }

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

    /**
     * 导出成excel
     *
     * @param response
     * @param wb
     * @param fileName
     */
    protected void exportExcel(HttpServletResponse response, HSSFWorkbook wb, String fileName) {
        ByteArrayOutputStream os = null;
        InputStream is = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            os = new ByteArrayOutputStream();
            wb.write(os);
            byte[] content = os.toByteArray();
            is = new ByteArrayInputStream(content);
            // 设置response参数，可以打开下载页面
            response.reset();
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName).getBytes(), "iso-8859-1"));
            ServletOutputStream sout = response.getOutputStream();

            bis = new BufferedInputStream(is);
            bos = new BufferedOutputStream(sout);
            byte[] buff = new byte[2048];
            int bytesRead;
            while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
                bos.write(buff, 0, bytesRead);
            }
        } catch (Exception e) {
            LogTaskFactory.getLogger().error(e, "BaseController.exportExcel", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, null);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    logger.error("exportExcel os close error " + e.toString());
                }
            }
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    logger.error("exportExcel is close error " + e.toString());
                }
            }
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    logger.error("exportExcel bis close error " + e.toString());
                }
            }
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e) {
                    logger.error("exportExcel bos close error " + e.toString());
                }
            }
        }
    }

}

