package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business;

import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.file.RequestForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.file.ResponseForFile;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author yuantongqing
 * on create 2018/1/5
 */
public interface FileService {

    /**
     * 条件查询文件
     * @param requestForFile
     * @return
     */
    ResponseForFile queryTaxFile(RequestForFile  requestForFile);

    /**
     * 删除文件
     * @param requestForFile
     * @return
     */
    Boolean deleteTaxFile(RequestForFile  requestForFile);

    /**
     * 上传凭证，并保存数据库
     * @param requestForFile
     * @param file
     * @return
     */
    Boolean uploadFileByBusinessIdAndType(RequestForFile  requestForFile,MultipartFile file);
}
