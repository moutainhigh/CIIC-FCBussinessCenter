package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;

import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.dto.FileDTO;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json.JsonResult;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.common.log.LogTaskFactory;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl.FileServiceImpl;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.file.RequestForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.file.ResponseForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.util.enums.EnumUtil;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


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
    public JsonResult<ResponseForFile> queryTaxFile(@RequestBody FileDTO fileDTO) {
        JsonResult<ResponseForFile> jr = new JsonResult<>();

        RequestForFile requestForFile = new RequestForFile();
        BeanUtils.copyProperties(fileDTO, requestForFile);
        ResponseForFile responseForFile = fileService.queryTaxFile(requestForFile);
        jr.fill(responseForFile);

        return jr;
    }

    /**
     * 删除文件
     *
     * @param fileDTO
     * @return
     */
    @PostMapping(value = "deleteTaxFile")
    public JsonResult<Boolean> deleteTaxFile(@RequestBody FileDTO fileDTO) {
        JsonResult<Boolean> jr = new JsonResult<>();

        RequestForFile requestForFile = new RequestForFile();
        BeanUtils.copyProperties(fileDTO, requestForFile);
        Boolean flag = fileService.deleteTaxFile(requestForFile);
        jr.fill(flag);

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
    public JsonResult<Boolean> uploadFileByBusinessIdAndType(FileDTO fileDTO, MultipartFile file) {
        JsonResult<Boolean> jr = new JsonResult<>();
        try {
            RequestForFile requestForFile = new RequestForFile();
            BeanUtils.copyProperties(fileDTO, requestForFile);
            fileService.uploadFileByBusinessIdAndType(requestForFile, file);
            //jr.fill(true);
        } catch (Exception e) {
            Map<String, String> tags = new HashMap<>(16);
            tags.put("businessId", fileDTO.getBusinessId().toString());
            tags.put("businessType", fileDTO.getBusinessType());
            //日志工具类返回
            LogTaskFactory.getLogger().error(e, "TaskFileController.uploadFileByBusinessIdAndType", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, tags);
            jr.error();
        }
        return jr;
    }

    /**
     * 文件下载
     *
     * @param fileDTO
     * @return
     */
    @RequestMapping(value = "downloadTaxFile", method = RequestMethod.GET)
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
                Map<String, String> tags = new HashMap<>(16);
                tags.put("businessId", fileDTO.getBusinessId().toString());
                tags.put("businessType", fileDTO.getBusinessType());
                //日志工具类返回
                LogTaskFactory.getLogger().error(e, "TaskFileController.downloadTaxFile", EnumUtil.getMessage(EnumUtil.SOURCE_TYPE, "06"), LogType.APP, tags);
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        logger.error("close inputStream error ", e);
                    }
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        logger.error("close out error ", e);
                    }
                }

            }
        }
    }
}
