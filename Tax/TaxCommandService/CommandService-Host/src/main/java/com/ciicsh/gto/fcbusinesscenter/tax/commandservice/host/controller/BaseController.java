package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.controller;


import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URLEncoder;

/**
 * <p>
 * base控制器
 * </p>
 *
 * @author linhaihai
 * @since 2017-12-12
 */
public class BaseController {


    /**
     * 下载文件
     * @param response
     * @param fileName
     * @param content
     */
    protected void downloadFile(HttpServletResponse response,String fileName,byte[] content){
        BufferedOutputStream bos = null;
        ServletOutputStream outputStream = null;
        BufferedInputStream bis = null;
        try{
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
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            try {
                bis.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                bos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                outputStream.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

}

