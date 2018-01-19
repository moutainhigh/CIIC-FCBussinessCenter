package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.FileDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.FileServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.file.RequestForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.file.ResponseForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author yuantongqing
 * on create 2018/1/4
 */
@RestController
public class TaskFileController extends BaseController {
    private static final Logger logger = LoggerFactory.getLogger(TaskFileController.class);

    @Autowired
    private FileServiceImpl fileService;

    /**
     * 条件查询文件
     *
     * @param fileDTO
     * @return
     */
    @PostMapping(value = "queryTaxFile")
    public JsonResult queryTaxFile(@RequestBody FileDTO fileDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForFile requestForFile = new RequestForFile();
            BeanUtils.copyProperties(fileDTO, requestForFile);
            ResponseForFile responseForFile = fileService.queryTaxFile(requestForFile);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(responseForFile);
        } catch (Exception e) {
            logger.error("queryTaxFile error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }

    /**
     * 删除文件
     *
     * @param fileDTO
     * @return
     */
    @PostMapping(value = "deleteTaxFile")
    public JsonResult deleteTaxFile(@RequestBody FileDTO fileDTO) {
        JsonResult jr = new JsonResult();
        try {
            RequestForFile requestForFile = new RequestForFile();
            BeanUtils.copyProperties(fileDTO, requestForFile);
            Boolean flag = fileService.deleteTaxFile(requestForFile);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            logger.error("deleteTaxFile error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }


    /**
     * 文件上传
     *
     * @param fileDTO
     * @param file
     * @return
     */
    @PostMapping(value = "uploadFileByBusinessIdAndType")
    public JsonResult uploadFileByBusinessIdAndType(FileDTO fileDTO, MultipartFile file) {
        JsonResult jr = new JsonResult();
        try {
            RequestForFile requestForFile = new RequestForFile();
            BeanUtils.copyProperties(fileDTO, requestForFile);
            Boolean flag = fileService.uploadFileByBusinessIdAndType(requestForFile, file);
            jr.setErrorcode("0");
            jr.setErrormsg("success");
            jr.setData(flag);
        } catch (Exception e) {
            logger.error("uploadFileByBusinessIdAndType error " + e.toString());
            jr.setErrorcode("1");
            jr.setErrormsg("error");
        }
        return jr;
    }

    /**
     * 文件下载
     *
     * @param fileDTO
     * @return
     */
    @RequestMapping(value = "downloadTaxFile")
    public void downloadTaxFile(FileDTO fileDTO, HttpServletResponse response) {
        InputStream inputStream = null;
        ByteArrayOutputStream out = null;
        if (!StrKit.isBlank(fileDTO.getFilenameSource()) && !StrKit.isBlank(fileDTO.getFilePath())) {
            try {
                //获取服务器输入流
                inputStream = FileHandler.getInputStream(fileDTO.getFilePath());
                out = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len;
                while ((len = inputStream.read(buffer)) > 0) {
                    out.write(buffer, 0, len);
                }
                byte[] bs = out.toByteArray();
                super.downloadFile(response, fileDTO.getFilenameSource(), bs);
            } catch (IOException e) {
                logger.error("download file error " + e.toString());
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error("close inputStream error " + e.toString());
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        logger.error("close out error " + e.toString());
                    }
                }

            }
        }
    }
}
