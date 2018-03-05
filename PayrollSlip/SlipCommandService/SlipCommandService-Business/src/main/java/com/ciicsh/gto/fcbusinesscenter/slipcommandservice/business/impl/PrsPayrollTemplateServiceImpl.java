package com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.impl;

import com.baomidou.mybatisplus.plugins.Page;
import org.apache.poi.hwpf.HWPFDocumentCore;
import org.apache.poi.hwpf.converter.WordToHtmlConverter;
import org.apache.poi.hwpf.converter.WordToHtmlUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.dao.PrsPayrollTemplateMapper;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.entity.po.PrsPayrollTemplatePO;
import com.ciicsh.gto.fcbusinesscenter.slipcommandservice.business.PrsPayrollTemplateService;
import org.w3c.dom.Document;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Date;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

/**
 * 工资单模板 服务实现类
 *
 * @author taka
 * @since 2018-01-29
 */
@Service
@Transactional
@SuppressWarnings("all")
public class PrsPayrollTemplateServiceImpl implements PrsPayrollTemplateService {

    @Autowired
    private PrsPayrollTemplateMapper prsPayrollTemplateMapper;

    @Override
    public List<PrsPayrollTemplatePO> listPrsPayrollTemplates(Map<String, Object> params) {

        List<PrsPayrollTemplatePO> records = prsPayrollTemplateMapper.list(params);

        return records;
    }

    @Override
    public Page<PrsPayrollTemplatePO> pagePrsPayrollTemplates(Map<String, Object> params) {
        int limit = 20;
        int offset = 0;

        int currentPage = params.get("currentPage") == null ? 1 : (int) params.get("currentPage");

        if (params.get("pageSize") != null) {
            limit =  (int) params.get("pageSize");
        }

        if (currentPage > 1) {
            offset = (currentPage - 1) * limit;
        }

        params.put("limit", limit);
        params.put("offset", offset);

        int total = prsPayrollTemplateMapper.total(params);

        List<PrsPayrollTemplatePO> records = prsPayrollTemplateMapper.list(params);
        Page<PrsPayrollTemplatePO> page = new Page<>();
        page.setRecords(records);
        page.setCurrent(currentPage);
        page.setTotal(total);
        page.setSize(limit);

        return page;
    }

    @Override
    public PrsPayrollTemplatePO getPrsPayrollTemplate(Map<String, Object> params) {
        return prsPayrollTemplateMapper.get(params);
    }

    @Override
    public Boolean addPrsPayrollTemplate(Map<String, Object> params) {
      if (params.get("createdBy") == null) {
        params.put("createdBy", '1');
      }

      if (params.get("modifiedBy") == null) {
        params.put("modifiedBy", '1');
      }

        if (params.get("effectiveTime") != null) {
            if (params.get("effectiveTime").equals("")) {
                params.put("effectiveTime", null);
            } else {
                params.put("effectiveTime", new Date((long) params.get("effectiveTime")));
            }
        }

        if (params.get("invalidTime") != null) {
            if (params.get("invalidTime").equals("")) {
                params.put("invalidTime", null);
            } else {
                params.put("invalidTime", new Date((long) params.get("invalidTime")));
            }
        }

        String templateFileName = (String)params.get("templateFileName");

        if (templateFileName != null && templateFileName.contains(".doc")) {
            String templateFileUrl = (String)params.get("templateFileUrl");
            try {
                HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(new URL(templateFileUrl).openStream());
                WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                        DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                wordToHtmlConverter.processDocument(wordDocument);
                Document htmlDocument = wordToHtmlConverter.getDocument();
                DOMSource domSource = new DOMSource(htmlDocument);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                StreamResult streamResult = new StreamResult(out);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
                out.close();

                params.put("html", out.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }

        prsPayrollTemplateMapper.insert(params);

        return true;
    }

    @Override
    public Boolean updatePrsPayrollTemplate(Map<String, Object> params) {
      if (params.get("modifiedBy") == null) {
        params.put("modifiedBy", '1');
      }

        if (params.get("effectiveTime") != null) {
            if (params.get("effectiveTime").equals("")) {
                params.put("effectiveTime", null);
            } else {
                params.put("effectiveTime", new Date((long) params.get("effectiveTime")));
            }
        }

        if (params.get("invalidTime") != null) {
            if (params.get("invalidTime").equals("")) {
                params.put("invalidTime", null);
            } else {
                params.put("invalidTime", new Date((long) params.get("invalidTime")));
            }
        }

        String templateFileName = (String)params.get("templateFileName");

        if (templateFileName != null && templateFileName.contains(".doc")) {
            String templateFileUrl = (String)params.get("templateFileUrl");
            try {
                HWPFDocumentCore wordDocument = WordToHtmlUtils.loadDoc(new URL(templateFileUrl).openStream());
                WordToHtmlConverter wordToHtmlConverter = new WordToHtmlConverter(
                        DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument());
                wordToHtmlConverter.processDocument(wordDocument);
                Document htmlDocument = wordToHtmlConverter.getDocument();
                DOMSource domSource = new DOMSource(htmlDocument);
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                StreamResult streamResult = new StreamResult(out);

                TransformerFactory tf = TransformerFactory.newInstance();
                Transformer serializer = tf.newTransformer();
                serializer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                serializer.setOutputProperty(OutputKeys.INDENT, "yes");
                serializer.setOutputProperty(OutputKeys.METHOD, "html");
                serializer.transform(domSource, streamResult);
                out.close();

                params.put("html", out.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TransformerConfigurationException e) {
                e.printStackTrace();
            } catch (TransformerException e) {
                e.printStackTrace();
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            }

        }

        prsPayrollTemplateMapper.update(params);

        return true;
    }
}
