package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;

import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.basicdataservice.api.CityServiceProxy;
import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.ProvinceServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CityDTO;
import com.ciicsh.gto.basicdataservice.api.dto.CountryDTO;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.basicdataservice.api.dto.ProvinceDTO;
import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeGroupInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.logservice.api.LogServiceProxy;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.BankFileProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileEmployeeProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileResultFileProxyDTO;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.BankFileResultProxyDTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * <p>
 * 公共服务接口 服务实现类
 * </p>
 *
 * @author gaoyang
 * @since 2018-03-30
 */
@Service
public class CommonServiceImpl implements CommonService {

    @Autowired
    private EntityIdServiceProxy entityIdServiceProxy;
    @Autowired
    private DicItemServiceProxy dicItemServiceProxy;
    @Autowired
    private CountryServiceProxy countryServiceProxy;
    @Autowired
    private CityServiceProxy cityServiceProxy;
    @Autowired
    private ProvinceServiceProxy provinceServiceProxy;
    @Autowired
    private BankFileProxy bankFileProxy;
    @Autowired
    private LogServiceProxy logService;

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        // todo
        // 定义薪资发放code，在Confluence上EntityID编号规则中进行定义
        String idCode = (String)entityParam.get("idCode");
        // 获取公共服务生成返回的entity_id
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        if(StringUtils.isEmpty(dicValue)){
            return null;
        }else if(StringUtils.isEmpty(dicItemValue)){
            return null;
        }else{
            DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
            if(dicItemDTO == null || "".equals(dicItemDTO.getDicItemText())){
                return null;
            }else{
                return dicItemDTO.getDicItemText();
            }
        }
    }

    @Override
    public List getNameByValueForList(String dicValue) {
        if(StringUtils.isEmpty(dicValue)){
            return null;
        }else{
            List<DicItemDTO> dicItemDTOList = dicItemServiceProxy.listByDicValue(dicValue);
            if(dicItemDTOList.isEmpty()){
                return null;
            }else{
                List dicItemTextList = dicItemDTOList.stream().map(DicItemDTO::getDicItemText).collect(Collectors.toList());
                return dicItemTextList;
            }
        }
    }

    @Override
    public String getCountryName(String countryCode) {
        if(StringUtils.isEmpty(countryCode)){
            return null;
        }else{
            CountryDTO countryDTO = countryServiceProxy.selectByCountryCode(countryCode);
            if(countryDTO == null || "".equals(countryDTO.getCountryName())){
                return null;
            }else{
                return countryDTO.getCountryName();
            }
        }
    }

    @Override
    public String getCityName(String cityCode) {
        if(StringUtils.isEmpty(cityCode)){
            return null;
        }else{
            CityDTO cityDTO = cityServiceProxy.selectByCityCode(cityCode);
            if(cityDTO == null || "".equals(cityDTO.getCityName())){
                return null;
            }else{
                return cityDTO.getCityName();
            }
        }

    }

    @Override
    public String getProvinceName(String provinceCode) {
        if(StringUtils.isEmpty(provinceCode)){
            return null;
        }else{
            ProvinceDTO provinceDTO = provinceServiceProxy.selectByProvinceCode(provinceCode);
            if(provinceDTO == null || "".equals(provinceDTO.getProvinceName())){
                return null;
            }else{
                return provinceDTO.getProvinceName();
            }
        }
    }

    @Override
    public List<OfferDocumentFilePO> generateOfferDocumentFile(SalaryGrantEmployeeGroupInfoBO groupInfoBO, OfferDocumentPO offerDocumentPO) {
        List<OfferDocumentFilePO> documentFilePOList = new ArrayList<>();
        OfferDocumentFilePO documentFilePO;

        BankFileProxyDTO bankFileProxyDTO = new BankFileProxyDTO();
        //银行代码：建行1，工行2，招商银行3，中国银行4，其他银行5
        bankFileProxyDTO.setBankCode(groupInfoBO.getBankcardType());
        List<BankFileEmployeeProxyDTO> list = new ArrayList<>();
        BankFileEmployeeProxyDTO employeeProxyDTO;

        List<SalaryGrantEmployeePO> employeePOList = groupInfoBO.getSalaryGrantEmployeePOList();

        if (!CollectionUtils.isEmpty(employeePOList)) {
            for (SalaryGrantEmployeePO employeePO : employeePOList) {
                employeeProxyDTO = new BankFileEmployeeProxyDTO();

                employeeProxyDTO.setEmployeeBankAccountName(employeePO.getEmployeeName());                       //雇员姓名
                employeeProxyDTO.setEmployeeBankAccount(employeePO.getCardNum());                                //雇员银行账号
                employeeProxyDTO.setEmployeeBankName(employeePO.getDepositBank());                               //银行名
                employeeProxyDTO.setEmployeeCityName(getCityName(employeePO.getBankcardCityCode()));             //城市名
                employeeProxyDTO.setPayAmount(employeePO.getPaymentAmount());                                    //付款金额
                employeeProxyDTO.setEmployeeProvinceName(getProvinceName(employeePO.getBankcardProvinceCode())); //省名称
                employeeProxyDTO.setEmployeeAreaCode(employeePO.getBankcardProvinceCode());                      //省代码
                employeeProxyDTO.setRemark("薪资发放");                                                           //薪资发放
                employeeProxyDTO.setPaymentBankAccountName(employeePO.getPaymentAccountName());                  //付款账户名
                employeeProxyDTO.setPaymentBankAccount(employeePO.getPaymentAccountCode());                      //付款账号
                employeeProxyDTO.setPaymentBankName(employeePO.getPaymentAccountBankName());                     //付款银行名

                list.add(employeeProxyDTO);
            }
        }

        bankFileProxyDTO.setList(list);

        JsonResult<BankFileResultProxyDTO> proxyDTOJsonResult = bankFileProxy.getPrivateFile(bankFileProxyDTO);
        if (!ObjectUtils.isEmpty(proxyDTOJsonResult) && "0".equals(proxyDTOJsonResult.getCode())) {
            BankFileResultProxyDTO fileResultProxyDTO = proxyDTOJsonResult.getData();

            if (!ObjectUtils.isEmpty(fileResultProxyDTO)) {
                List<BankFileResultFileProxyDTO> resultFileProxyDTOList = fileResultProxyDTO.getList();
                if (!CollectionUtils.isEmpty(resultFileProxyDTOList)) {
                    for (BankFileResultFileProxyDTO fileProxyDTO : resultFileProxyDTOList) {
                        documentFilePO = new OfferDocumentFilePO();
                        documentFilePO.setOfferDocumentId(offerDocumentPO.getOfferDocumentId()); //报盘信息ID
                        documentFilePO.setFileName(fileProxyDTO.getFileName());                  //报盘文件名称
                        documentFilePO.setFilePath(fileProxyDTO.getFilePath());                  //报盘文件地址
                        documentFilePO.setPaymentTotalSum(fileProxyDTO.getAmount());             //薪资发放总金额（RMB）
                        documentFilePO.setTotalPersonCount(fileProxyDTO.getCount());             //发薪人数
                        documentFilePO.setActive(true);
                        documentFilePO.setCreatedBy(offerDocumentPO.getCreatedBy());
                        documentFilePO.setCreatedTime(new Date());

                        documentFilePOList.add(documentFilePO);
                    }
                }
            }
        } else {
            logService.info(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("调用结算中心接口生成报盘文件").setContent("调用接口返回错误"));
        }

        return documentFilePOList;
    }
}
