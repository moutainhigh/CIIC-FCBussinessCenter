package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.plugins.Page;
import com.ciicsh.gt1.common.auth.UserContext;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.Result;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.core.ResultGenerator;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.OfferDocumentDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.api.dto.SalaryGrantTaskDTO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.OfferDocumentFileService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.SalaryGrantOfferDocumentTaskService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.OfferDocumentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.host.util.CommonTransform;
import com.ciicsh.gto.fcbusinesscenter.util.common.CommonHelper;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.io.OutputStream;
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
    LogClientService logClientService;

    /**
     * 查询薪资发放报盘任务单列表
     *
     * @param salaryGrantTaskDTO
     * @return Page<SalaryGrantTaskDTO>
     */
    @RequestMapping("/offerFile/DocumentTask")
    public Result<Page<SalaryGrantTaskDTO>> queryOfferDocumentTaskPage(@RequestBody SalaryGrantTaskDTO salaryGrantTaskDTO) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("查询薪资发放报盘任务单列表").setContent(JSON.toJSONString(salaryGrantTaskDTO)));
            SalaryGrantTaskBO salaryGrantTaskBO = CommonTransform.convertToEntity(salaryGrantTaskDTO, SalaryGrantTaskBO.class);
            Page<SalaryGrantTaskBO> page = new Page<>(salaryGrantTaskDTO.getCurrent(), salaryGrantTaskDTO.getSize());
            //设置管理方信息
            salaryGrantTaskBO.setManagementIds(CommonHelper.getManagementIDs());
            page = offerDocumentTaskService.queryOfferDocumentTaskPage(page, salaryGrantTaskBO);
            // BO PAGE 转换为 DTO PAGE
            String boJSONStr = JSONObject.toJSONString(page);
            Page<SalaryGrantTaskDTO> taskDTOPage = JSONObject.parseObject(boJSONStr, Page.class);
            return ResultGenerator.genSuccessResult(taskDTOPage);
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("查询薪资发放报盘任务单列表异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常！");
        }

    }

    /**
     * 生成薪资发放报盘文件
     *
     * @return
     */
    @RequestMapping("/offerFile/generateOfferDocument/{taskCode}")
    public Result<List<OfferDocumentDTO>> generateOfferDocument(@PathVariable String taskCode) {
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件 -> 开始").setContent("taskCode： " + taskCode));
            List<OfferDocumentBO> documentBOList = offerDocumentTaskService.generateOfferDocument(taskCode, UserContext.getUserId());
            // BO List 转换为 DTO List
            String boJSONStr = JSONObject.toJSONString(documentBOList);
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("薪资发放报盘文件信息").setContent(boJSONStr));
            return ResultGenerator.genSuccessResult(JSONObject.parseArray(boJSONStr, OfferDocumentDTO.class));
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件异常").setContent(e.getMessage()));
            return ResultGenerator.genServerFailResult("系统异常！");
        }
    }

    /**
     * 下载薪资发放报盘文件
     *
     * @param response
     */
    @RequestMapping("/offerFile/download/{offerDocumentFileId}")
    public void download(HttpServletResponse response, @PathVariable Long offerDocumentFileId){
        try {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("下载薪资发放报盘文件").setContent("offerDocumentFileId：" + offerDocumentFileId));
            OfferDocumentFilePO documentFilePO = offerDocumentFileService.selectById(offerDocumentFileId);
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
        } catch (Exception e) {
            logClientService.errorAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("下载薪资发放报盘文件异常").setContent(e.getMessage()));
        }
    }
}
