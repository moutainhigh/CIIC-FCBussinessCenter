package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.impl;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.ciicsh.gt1.FileHandler;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.business.FileService;
import com.ciicsh.gto.fcbusinesscenter.tax.commandservice.dao.FileMapper;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.po.FilePO;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.request.file.RequestForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.entity.response.file.ResponseForFile;
import com.ciicsh.gto.fcbusinesscenter.tax.util.support.StrKit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author yuantongqing
 * on create 2018/1/5
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileMapper, FilePO> implements FileService, Serializable {

    private static final Logger logger = LoggerFactory.getLogger(FileServiceImpl.class);

    /**
     * 条件查询文件
     *
     * @param requestForFile
     * @return
     */
    @Override
    public ResponseForFile queryTaxFile(RequestForFile requestForFile) {
        ResponseForFile responseForFile = new ResponseForFile();
        List<FilePO> filePOList = new ArrayList<>();
        EntityWrapper wrapper = new EntityWrapper();
        wrapper.setEntity(new FilePO());
        //判断是否包含主键ID条件
        if (null != requestForFile.getId()) {
            wrapper.andNew("id = {0}", requestForFile.getId());
        }
        //判断是否包含业务ID
        if (null != requestForFile.getBusinessId()) {
            wrapper.andNew("business_id = {0}", requestForFile.getBusinessId());
        }
        //判断是否包含业务类型
        if (StrKit.notBlank(requestForFile.getBusinessType())) {
            wrapper.andNew("business_type = {0}", requestForFile.getBusinessType());
        }
        wrapper.andNew("is_active = {0} ", true);
        wrapper.orderBy("created_time", false);

        //判断是否分页
        if (null != requestForFile.getPageSize() && null != requestForFile.getCurrentNum()) {
            Page<FilePO> pageInfo = new Page<>(requestForFile.getCurrentNum(), requestForFile.getPageSize());
            filePOList = baseMapper.selectPage(pageInfo, wrapper);
            //获取查询总数目
            int total = baseMapper.selectCount(wrapper);
            responseForFile.setRowList(filePOList);
            responseForFile.setTotalNum(total);
            responseForFile.setCurrentNum(requestForFile.getCurrentNum());
            responseForFile.setPageSize(requestForFile.getPageSize());
        } else {
            filePOList = baseMapper.selectList(wrapper);
            responseForFile.setRowList(filePOList);
        }
        return responseForFile;
    }

    /**
     * 删除文件
     *
     * @param requestForFile
     * @return
     */
    @Transactional
    @Override
    public Boolean deleteTaxFile(RequestForFile requestForFile) {
        Boolean flag = true;
        try {
            if (requestForFile.getId() != null) {
                FilePO filePO = new FilePO();
                filePO.setId(requestForFile.getId());
                filePO.setActive(false);
                filePO.setModifiedBy("adminDel");
                filePO.setModifiedTime(new Date());
                //删除文件:即修改数据为不可用
                flag = super.insertOrUpdate(filePO);
            }
        } catch (Exception e) {
            logger.error("deleteTaxFile error " + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            flag = false;
        }
        return flag;
    }

    @Transactional
    @Override
    public Boolean uploadFileByBusinessIdAndType(RequestForFile requestForFile, MultipartFile file) {
        Boolean flag = true;
        try {
            String fileName = file.getOriginalFilename();
            String url = FileHandler.uploadFile(file.getInputStream());
            FilePO filePO = new FilePO();
            filePO.setBusinessId(requestForFile.getBusinessId());
            filePO.setBusinessType(requestForFile.getBusinessType());
            filePO.setFilePath(url);
            filePO.setFilenameSource(fileName);
            filePO.setCreatedBy("admin");
            filePO.setModifiedBy("admin");
            super.insertOrUpdate(filePO);
        } catch (Exception e) {
            logger.error("uploadFileByBusinessIdAndType error " + e.toString());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            flag = false;
        }
        return flag;
    }


}