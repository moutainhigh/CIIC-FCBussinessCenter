package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.OfferDocumentDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.OfferDocumentFileService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.List;

/**
 * <p>Description: 报盘文件 Controller</p>
 *
 * @author Rock Jiang
 * @version 1.0
 * @date 2018/4/24 0024
 */
@RestController
public class OfferFileController {
    @Autowired
    private SalaryGrantOfferDocumentTaskService offerDocumentTaskService;
    @Autowired
    private OfferDocumentFileService offerDocumentFileService;
    @Autowired
    private CommonService commonService;
    @Autowired
    LogServiceProxy logService;

    /**
     * 查询薪资发放报盘任务单列表
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/offerFile/DocumentTask")
    public Page<SalaryGrantTaskDTO> queryOfferDocumentTaskPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("查询薪资发放报盘任务单列表").setContent(JSON.toJSONString(salaryGrantTaskDTO)));

        Page<SalaryGrantTaskBO> page = new Page<>();
        page.setCurrent(salaryGrantTaskDTO.getCurrent());
        page.setSize(salaryGrantTaskDTO.getSize());
        SalaryGrantTaskBO salaryGrantTaskBO = new SalaryGrantTaskBO();
        BeanUtils.copyProperties(salaryGrantTaskDTO, salaryGrantTaskBO);
        //设置管理方信息
        salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
        page = offerDocumentTaskService.queryOfferDocumentTaskPage(page, salaryGrantTaskBO);

        //字段转换
        List<SalaryGrantTaskBO> grantTaskBOList = page.getRecords();
        if (!CollectionUtils.isEmpty(grantTaskBOList)) {
            int size = grantTaskBOList.size();
            SalaryGrantTaskBO grantTaskBO;
            for (int i = 0; i < size; i++) {
                grantTaskBO = grantTaskBOList.get(i);
                //发放类型不为空时设置发放类型名称
                if (!ObjectUtils.isEmpty(grantTaskBO.getGrantType())) {
                    grantTaskBO.setGrantTypeName(commonService.getNameByValue("sgmGrantType", String.valueOf(grantTaskBO.getGrantType())));
                }
            }
        }

        // BO PAGE 转换为 DTO PAGE
        String boJSONStr = JSONObject.toJSONString(page);
        Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);

        return taskDTOPage;
    }

    /**
     * 查询薪资发放报盘文件
     *
     * @return
     */
    @RequestMapping("/offerFile/queryOfferDocument/{taskCode}")
    public List<OfferDocumentDTO> queryOfferDocument(@PathVariable String taskCode){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("查询薪资发放报盘文件").setContent("taskCode: " + taskCode));

        List<OfferDocumentBO> documentBOList = offerDocumentTaskService.queryOfferDocument(taskCode);

        //字段转换
        if (!CollectionUtils.isEmpty(documentBOList)) {
            int size = documentBOList.size();
            OfferDocumentBO documentBO;
            for (int i = 0; i < size; i++) {
                documentBO = documentBOList.get(i);
                //银行卡种类不为空时设置银行卡种类名称
                if (!ObjectUtils.isEmpty(documentBO.getBankcardType())) {
                    documentBO.setBankcardName(commonService.getNameByValue("sgBankcardType", String.valueOf(documentBO.getBankcardType())));
                }
            }
        }

        // BO List 转换为 DTO List
        String boJSONStr = JSONObject.toJSONString(documentBOList);
        return JSONObject.parseArray(boJSONStr, OfferDocumentDTO.class);
    }

    /**
     * 下载薪资发放报盘文件
     *
     * @param response
     */
    @RequestMapping("/offerFile/download/{offerDocumentFileId}")
    public void download(HttpServletResponse response, @PathVariable Long offerDocumentFileId){
        logService.info(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("下载薪资发放报盘文件").setContent("offerDocumentFileId: " + offerDocumentFileId));

        OfferDocumentFilePO documentFilePO = offerDocumentFileService.selectById(offerDocumentFileId);
        try {
            //获取文件
            URL url = new URL(documentFilePO.getFilePath());
            URLConnection urlConnection = url.openConnection();
            InputStream inputStream = urlConnection.getInputStream();

            //定义类型
            //编码文件名
            String fileName = URLEncoder.encode(documentFilePO.getFileName(), "UTF-8");;
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("text/plain;charset=UTF-8");

            OutputStream outputStream = response.getOutputStream();

            byte[] buffer = new byte[1024];
            int len;
            while ((len = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, len);
            }

            outputStream.close();
            inputStream.close();
        } catch (IOException e) {
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("下载薪资发放报盘文件").setContent("异常"));
        }
    }
}
