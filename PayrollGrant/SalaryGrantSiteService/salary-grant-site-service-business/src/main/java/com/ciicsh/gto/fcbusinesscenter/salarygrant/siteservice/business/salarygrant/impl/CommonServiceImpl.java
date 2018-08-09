package com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.impl;


import com.alibaba.fastjson.JSON;
import com.ciicsh.gto.afsystemmanagecenter.apiservice.api.SMUserInfoProxy;
import com.ciicsh.gto.afsystemmanagecenter.apiservice.api.dto.auth.SMUserInfoDTO;
import com.ciicsh.gto.basicdataservice.api.CityServiceProxy;
import com.ciicsh.gto.basicdataservice.api.CountryServiceProxy;
import com.ciicsh.gto.basicdataservice.api.DicItemServiceProxy;
import com.ciicsh.gto.basicdataservice.api.ProvinceServiceProxy;
import com.ciicsh.gto.basicdataservice.api.dto.CityDTO;
import com.ciicsh.gto.basicdataservice.api.dto.CountryDTO;
import com.ciicsh.gto.basicdataservice.api.dto.DicItemDTO;
import com.ciicsh.gto.basicdataservice.api.dto.ProvinceDTO;
import com.ciicsh.gto.billcenter.fcmodule.api.ISalaryEmployeeProxy;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryEmployeeProxyDTO;
import com.ciicsh.gto.billcenter.fcmodule.api.dto.SalaryProxyDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.DeferredPoolProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.EmployeeServiceProxy;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.FcEmpInfoRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.PaymentDeferredDelDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.request.PaymentDeferredRequestDTO;
import com.ciicsh.gto.companycenter.webcommandservice.api.dto.response.FcEmpInfoResponseDTO;
import com.ciicsh.gto.entityidservice.api.EntityIdServiceProxy;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.constant.SalaryGrantBizConsts;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.business.salarygrant.CommonService;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeeGroupInfoBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantEmployeePaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.bo.SalaryGrantTaskPaymentBO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentFilePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.OfferDocumentPO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantEmployeePO;
import com.ciicsh.gto.fcbusinesscenter.salarygrant.siteservice.entity.po.SalaryGrantMainTaskPO;
import com.ciicsh.gto.logservice.api.dto.LogDTO;
import com.ciicsh.gto.logservice.api.dto.LogType;
import com.ciicsh.gto.logservice.client.LogClientService;
import com.ciicsh.gto.salarymanagementcommandservice.api.BatchProxy;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.BatchAuditDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.dto.PrNormalBatchDTO;
import com.ciicsh.gto.salarymanagementcommandservice.api.page.Pagination;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.BankFileProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.SalaryServiceProxy;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.common.JsonResult;
import com.ciicsh.gto.settlementcenter.payment.cmdapi.dto.*;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
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
    private SMUserInfoProxy smUserInfoProxy;
    @Autowired
    private ISalaryEmployeeProxy salaryEmployeeProxy;
    @Autowired
    private SalaryServiceProxy salaryServiceProxy;
    @Autowired
    private DeferredPoolProxy deferredPoolProxy;
    @Autowired
    private BatchProxy batchProxy;
    @Autowired
    private EmployeeServiceProxy employeeServiceProxy;
    @Autowired
    LogClientService logClientService;

    @Override
    public String getEntityIdForSalaryGrantTask(Map entityParam) {
        // 定义薪资发放code，在Confluence上EntityID编号规则中进行定义
        String idCode = (String) entityParam.get("idCode");
        // 获取公共服务生成返回的entity_id
        String entityId = entityIdServiceProxy.getEntityId(idCode);
        return entityId;
    }

    @Override
    public String getNameByValue(String dicValue, String dicItemValue) {
        if (StringUtils.isEmpty(dicValue)) {
            return null;
        } else if (StringUtils.isEmpty(dicItemValue)) {
            return null;
        } else {
            DicItemDTO dicItemDTO = dicItemServiceProxy.selectByValue(dicValue, dicItemValue);
            if (dicItemDTO == null || "".equals(dicItemDTO.getDicItemText())) {
                return null;
            } else {
                return dicItemDTO.getDicItemText();
            }
        }
    }

    @Override
    public List getNameByValueForList(String dicValue) {
        if (StringUtils.isEmpty(dicValue)) {
            return null;
        } else {
            List<DicItemDTO> dicItemDTOList = dicItemServiceProxy.listByDicValue(dicValue);
            if (dicItemDTOList.isEmpty()) {
                return null;
            } else {
                List dicItemTextList = dicItemDTOList.stream().map(DicItemDTO::getDicItemText).collect(Collectors.toList());
                return dicItemTextList;
            }
        }
    }

    @Override
    public String getCountryName(String countryCode) {
        if (StringUtils.isEmpty(countryCode)) {
            return null;
        } else {
            CountryDTO countryDTO = countryServiceProxy.selectByCountryCode(countryCode);
            if (countryDTO == null || "".equals(countryDTO.getCountryName())) {
                return null;
            } else {
                return countryDTO.getCountryName();
            }
        }
    }

    @Override
    public String getCityName(String cityCode) {
        if (StringUtils.isEmpty(cityCode)) {
            return null;
        } else {
            CityDTO cityDTO = cityServiceProxy.selectByCityCode(cityCode);
            if (cityDTO == null || "".equals(cityDTO.getCityName())) {
                return null;
            } else {
                return cityDTO.getCityName();
            }
        }

    }

    @Override
    public String getProvinceName(String provinceCode) {
        if (StringUtils.isEmpty(provinceCode)) {
            return null;
        } else {
            ProvinceDTO provinceDTO = provinceServiceProxy.selectByProvinceCode(provinceCode);
            if (provinceDTO == null || "".equals(provinceDTO.getProvinceName())) {
                return null;
            } else {
                return provinceDTO.getProvinceName();
            }
        }
    }

    @Override
    public List<OfferDocumentFilePO> generateOfferDocumentFile(SalaryGrantEmployeeGroupInfoBO groupInfoBO, OfferDocumentPO offerDocumentPO) {
        List<OfferDocumentFilePO> documentFilePOList = new ArrayList<>();
        List<BankFileEmployeeProxyDTO> list = new ArrayList<>();
        BankFileProxyDTO bankFileProxyDTO = new BankFileProxyDTO();
        OfferDocumentFilePO documentFilePO;
        //银行代码：建行1，工行2，招商银行3，中国银行4，其他银行5
        if (groupInfoBO.getBankcardType().intValue()>4) {
            bankFileProxyDTO.setBankCode(Integer.valueOf(5));
        } else {
            bankFileProxyDTO.setBankCode(groupInfoBO.getBankcardType());
        }

        BankFileEmployeeProxyDTO employeeProxyDTO;

        List<SalaryGrantEmployeePO> employeePOList = groupInfoBO.getSalaryGrantEmployeePOList();

        if (!CollectionUtils.isEmpty(employeePOList)) {
            for (SalaryGrantEmployeePO employeePO : employeePOList) {
                employeeProxyDTO = new BankFileEmployeeProxyDTO();
                employeeProxyDTO.setEmployeeBankAccountName(employeePO.getEmployeeName());
                //雇员银行账号
                employeeProxyDTO.setEmployeeBankAccount(employeePO.getCardNum());
                //银行名称
                employeeProxyDTO.setEmployeeBankName(employeePO.getDepositBank());
                employeeProxyDTO.setEmployeeCityName(getCityName(employeePO.getBankcardCityCode()));
                //付款金额
                employeeProxyDTO.setPayAmount(employeePO.getPaymentAmount());
                employeeProxyDTO.setEmployeeProvinceName(getProvinceName(employeePO.getBankcardProvinceCode()));
                employeeProxyDTO.setEmployeeAreaCode(employeePO.getBankcardProvinceCode());
                employeeProxyDTO.setRemark("薪资发放");
                employeeProxyDTO.setPaymentBankAccountName(employeePO.getPaymentAccountName());
                employeeProxyDTO.setPaymentBankAccount(employeePO.getPaymentAccountCode());
                employeeProxyDTO.setPaymentBankName(employeePO.getPaymentAccountBankName());

                list.add(employeeProxyDTO);
            }
        }
        bankFileProxyDTO.setList(list);
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件 -> 调用结算中心接口").setContent(JSON.toJSONString(bankFileProxyDTO)));
        JsonResult<BankFileResultProxyDTO> proxyDTOJsonResult = bankFileProxy.getPrivateFile(bankFileProxyDTO);
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("生成薪资发放报盘文件 -> 结算中心处理结果").setContent(JSON.toJSONString(proxyDTOJsonResult)));
        if (!ObjectUtils.isEmpty(proxyDTOJsonResult) && "0".equals(proxyDTOJsonResult.getCode())) {
            BankFileResultProxyDTO fileResultProxyDTO = proxyDTOJsonResult.getData();
            if (!ObjectUtils.isEmpty(fileResultProxyDTO)) {
                List<BankFileResultFileProxyDTO> resultFileProxyDTOList = fileResultProxyDTO.getList();
                if (!CollectionUtils.isEmpty(resultFileProxyDTOList)) {
                    for (BankFileResultFileProxyDTO fileProxyDTO : resultFileProxyDTOList) {
                        documentFilePO = new OfferDocumentFilePO();
                        documentFilePO.setOfferDocumentId(offerDocumentPO.getOfferDocumentId());
                        documentFilePO.setFileName(fileProxyDTO.getFileName());
                        documentFilePO.setFilePath(fileProxyDTO.getFilePath());
                        documentFilePO.setPaymentTotalSum(fileProxyDTO.getAmount());
                        documentFilePO.setTotalPersonCount(fileProxyDTO.getCount());
                        documentFilePO.setActive(true);
                        documentFilePO.setCreatedBy(offerDocumentPO.getCreatedBy());
                        documentFilePO.setModifiedBy(offerDocumentPO.getCreatedBy());
                        documentFilePOList.add(documentFilePO);
                    }
                }
            }
        } else {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("报盘文件").setTitle("调用结算中心接口生成报盘文件").setContent("调用接口返回错误"));
        }
        return documentFilePOList;
    }

    @Override
    public String getUserNameById(String userId) {
        if (StringUtils.isEmpty(userId)) {
            return null;
        } else {
            com.ciicsh.gto.afsystemmanagecenter.apiservice.api.dto.JsonResult<List<SMUserInfoDTO>> userInfoDTOJsonResult = smUserInfoProxy.getUsersByUserId(userId);
            if (!ObjectUtils.isEmpty(userInfoDTOJsonResult) && userInfoDTOJsonResult.getCode() == 0) {
                List<SMUserInfoDTO> userInfoDTOList = userInfoDTOJsonResult.getData();
                if (!CollectionUtils.isEmpty(userInfoDTOList)) {
                    SMUserInfoDTO smUserInfoDTO = userInfoDTOList.get(0);
                    if (smUserInfoDTO == null || "".equals(smUserInfoDTO.getDisplayName())) {
                        return null;
                    } else {
                        return smUserInfoDTO.getDisplayName();
                    }
                } else {
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    @Override
    public SalaryProxyDTO selectEmployeeServiceFeeAmount(String salaryGrantSubTaskCode, List<SalaryGrantEmployeePO> employeePOList) {
        if (!CollectionUtils.isEmpty(employeePOList)) {
            //调用账单中心接口获取雇员薪酬服务费
            SalaryProxyDTO salaryProxyDTO = new SalaryProxyDTO();
            //批次编号
            salaryProxyDTO.setBatchCode(salaryGrantSubTaskCode);
            //公司雇员信息列表
            List<SalaryEmployeeProxyDTO> employeeList = new ArrayList<>();

            employeePOList.stream().forEach(employeePO -> {
                SalaryEmployeeProxyDTO employeeProxyDTO = new SalaryEmployeeProxyDTO();
                employeeProxyDTO.setCompanyId(employeePO.getCompanyId());
                employeeProxyDTO.setEmployeeId(employeePO.getEmployeeId());
                employeeList.add(employeeProxyDTO);
            });

            com.ciicsh.gto.billcenter.fcmodule.api.common.JsonResult<SalaryProxyDTO> proxyDTOJsonResult = salaryEmployeeProxy.getSalaryEmployeeServiceFee(salaryProxyDTO);
            if (!ObjectUtils.isEmpty(proxyDTOJsonResult)) {
                if (proxyDTOJsonResult.getCode().intValue() == 0) {
                    return proxyDTOJsonResult.getData();
                } else {
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("雇员服务").setTitle("调用账单中心接口查询雇员薪酬服务费").setContent("调用接口未成功返回数据"));
                }
            } else {
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("雇员服务").setTitle("调用账单中心接口查询雇员薪酬服务费").setContent("调用接口出现错误"));
            }
        }

        return null;
    }

    @Override
    public SalaryBatchDTO saveSalaryBatchData(SalaryGrantTaskPaymentBO taskPaymentBO, List<SalaryGrantEmployeePaymentBO> employeePaymentBOList) {
        SalaryBatchDTO salaryBatchDTO = new SalaryBatchDTO();

        if (!ObjectUtils.isEmpty(taskPaymentBO)) {
            salaryBatchDTO.setBatchCode(taskPaymentBO.getBatchCode()); //计算批次号
            salaryBatchDTO.setSequenceNo(taskPaymentBO.getTaskCode()); //流水号,发放批次号
            salaryBatchDTO.setManagementId(taskPaymentBO.getManagementId()); //管理方ID
            salaryBatchDTO.setManagementName(taskPaymentBO.getManagementName()); //管理方名称
            salaryBatchDTO.setPayMonth(taskPaymentBO.getGrantCycle()); //缴费月份
            salaryBatchDTO.setTotalAmount(taskPaymentBO.getPaymentTotalSum()); //工资清单总额（计算批次的总额）
            salaryBatchDTO.setTotalEmployeeCount(taskPaymentBO.getTotalPersonCount()); //雇员总数（计算批次的雇员数量）
            salaryBatchDTO.setPayAmount(taskPaymentBO.getPayAmount()); //本批次将付金额（计算批次里正常的雇员的实发工资之和）
            salaryBatchDTO.setPayEmployeeCount(taskPaymentBO.getPayEmployeeCount()); //本批次将付雇员数量（正常的雇员）
            salaryBatchDTO.setApplyer(getUserNameById(taskPaymentBO.getOperatorUserId())); //申请人
            salaryBatchDTO.setApplyDate(LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"))); //申请日期
            salaryBatchDTO.setAgentFeeAmount(new BigDecimal(0)); //财务代理费
            //is_advance，逻辑判断
            if (!ObjectUtils.isEmpty(taskPaymentBO.getBalanceGrant())) {
                if (taskPaymentBO.getBalanceGrant() == 0) {
                    //已来款balance_grant=0的就返回标记为0
                    salaryBatchDTO.setIsAdvance(0); //是否垫付(0:正常;1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付)信用期垫付就是周期垫付
                }

                if (taskPaymentBO.getBalanceGrant() == 1) {
                    if (!ObjectUtils.isEmpty(taskPaymentBO.getAdvanceType())) {
                        //垫付balance_grant=1，则返回对应的垫付类型1\2\3\4\5
                        salaryBatchDTO.setIsAdvance(taskPaymentBO.getAdvanceType()); //是否垫付(0:正常;1-信用期垫付;2-偶然垫付;3-水单垫付;4-AF垫付;5-预付款垫付)信用期垫付就是周期垫付
                    }
                }
            }
            salaryBatchDTO.setFinanceAccountId(2); //帐套ID(1:AF;2:FC;3:BPO)
        }

        List<SalaryEmployeeDTO> employeeList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(employeePaymentBOList)) {
            // -------------- 过滤出没有银行卡信息的雇员 待确认业务逻辑----------------
            //employeePaymentBOList = employeePaymentBOList.stream().filter(employeePaymentBO -> !StringUtils.isEmpty(employeePaymentBO.getCardNum())).collect(Collectors.toList());

            employeePaymentBOList.stream().forEach(employeePaymentBO -> {
                SalaryEmployeeDTO salaryEmployeeDTO = new SalaryEmployeeDTO();
                salaryEmployeeDTO.setCompanyId(employeePaymentBO.getCompanyId()); //公司ID
                salaryEmployeeDTO.setCompanyName(employeePaymentBO.getCompanyName()); //公司名称
                salaryEmployeeDTO.setEmployeeId(employeePaymentBO.getEmployeeId()); //雇员ID
                salaryEmployeeDTO.setEmployeeName(employeePaymentBO.getEmployeeName()); //雇员名称
                salaryEmployeeDTO.setBankId(ObjectUtils.isEmpty(employeePaymentBO.getBankcardType()) ? null : employeePaymentBO.getBankcardType()); //雇员银行卡所属银行编号
                salaryEmployeeDTO.setBankBranchName(employeePaymentBO.getDepositBank()); //支行名称
                salaryEmployeeDTO.setEmployeeBankAccountId(ObjectUtils.isEmpty(employeePaymentBO.getBankcardId()) ? null : employeePaymentBO.getBankcardId().intValue()); //雇员银行账号ID
                salaryEmployeeDTO.setEmployeeBankAccount(employeePaymentBO.getCardNum()); //雇员工资卡银行账号
                salaryEmployeeDTO.setProvinceName(getProvinceName(employeePaymentBO.getBankcardProvinceCode())); //省名称
                salaryEmployeeDTO.setCityName(getCityName(employeePaymentBO.getBankcardCityCode())); //城市名
                salaryEmployeeDTO.setAreaCode(employeePaymentBO.getBankcardProvinceCode()); //银行地区码
                salaryEmployeeDTO.setPaySocialinsuranceAmount(employeePaymentBO.getPersonalSocialSecurity()); //社保金额
                salaryEmployeeDTO.setPayHousefundAmount(employeePaymentBO.getIndividualProvidentFund()); //公积金金额
                if (!ObjectUtils.isEmpty(employeePaymentBO.getWelfareIncluded())) {
                    salaryEmployeeDTO.setIfWelfarePay(employeePaymentBO.getWelfareIncluded() ? 1 : 0); //是否要付社保/公积金(1:是；0:否)
                }
                salaryEmployeeDTO.setPayIncometaxAmount(employeePaymentBO.getPersonalIncomeTax()); //个税金额
                if (!ObjectUtils.isEmpty(employeePaymentBO.getGrantServiceType())) {
                    //发放服务标识：0-薪资发放
                    if (employeePaymentBO.getGrantServiceType() == SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT) {
                        salaryEmployeeDTO.setIfTaxPay(0); //是否要付个税(0否，1AF大库，2FC大库，3独立库)
                    } else {
                        if (!ObjectUtils.isEmpty(employeePaymentBO.getDeclarationAccountCategory())) {
                            //申报账户类别: 1-大库（FC目前服务协议只配置FC大库）
                            if (employeePaymentBO.getDeclarationAccountCategory() == SalaryGrantBizConsts.DECLARATION_ACCOUNT_CATEGORY_DEPOT_BIG) {
                                salaryEmployeeDTO.setIfTaxPay(2); //是否要付个税(0否，1AF大库，2FC大库，3独立库)
                            }

                            //申报账户类别: 2-独立库
                            if (employeePaymentBO.getDeclarationAccountCategory() == SalaryGrantBizConsts.DECLARATION_ACCOUNT_CATEGORY_DEPOT_INDEPENDENT) {
                                salaryEmployeeDTO.setIfTaxPay(3); //是否要付个税(0否，1AF大库，2FC大库，3独立库)
                            }
                        }
                    }
                }
                salaryEmployeeDTO.setSalaryAmount(employeePaymentBO.getWagePayable()); //应付工资
                if (!ObjectUtils.isEmpty(employeePaymentBO.getGrantServiceType())) {
                    //发放服务标识：1-个税
                    if (employeePaymentBO.getGrantServiceType() == SalaryGrantBizConsts.GRANT_SERVICE_TYPE_TAX) {
                        salaryEmployeeDTO.setIfSalaryPay(0); //是否要付工资(1:是；0:否)
                    }

                    //发放服务标识：0-薪资发放，2-薪资发放 + 个税
                    if (employeePaymentBO.getGrantServiceType() == SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT || employeePaymentBO.getGrantServiceType() == SalaryGrantBizConsts.GRANT_SERVICE_TYPE_GRANT_AND_TAX) {
                        salaryEmployeeDTO.setIfSalaryPay(1); //是否要付工资(1:是；0:否)
                    }
                }
                salaryEmployeeDTO.setPayAmount(employeePaymentBO.getPaymentAmount()); //应付金额 实发工资
                salaryEmployeeDTO.setSalaryMonth(employeePaymentBO.getGrantCycle()); //工资月份
                salaryEmployeeDTO.setTaxMonth(employeePaymentBO.getTaxCycle()); //个税月份
//                salaryEmployeeDTO.setFinanceAccountId(ObjectUtils.isEmpty(employeePaymentBO.getContractFirstParty()) ? null : Integer.parseInt(employeePaymentBO.getContractFirstParty())); //帐套ID
                salaryEmployeeDTO.setServiceFee(employeePaymentBO.getServiceFeeAmount()); //个人薪酬服务费
                //如果雇员是暂缓grant_status（1-手动、2-自动）不发放，则置为-1；否则赋值为对应grant_status的值
                if (!ObjectUtils.isEmpty(employeePaymentBO.getGrantStatus())) {
                    if (employeePaymentBO.getGrantStatus() == SalaryGrantBizConsts.GRANT_STATUS_MANUAL_REPRIEVE || employeePaymentBO.getGrantStatus() == SalaryGrantBizConsts.GRANT_STATUS_AUTO_REPRIEVE) {
                        salaryEmployeeDTO.setSalaryStatus(-1); //雇员薪资明细状态(0:正常;1:暂缓放开;2:退票完成;3:现金完成;4:调整完成;负值标识未完成/未放开)
                    } else {
                        salaryEmployeeDTO.setSalaryStatus(employeePaymentBO.getGrantStatus()); //雇员薪资明细状态(0:正常;1:暂缓放开;2:退票完成;3:现金完成;4:调整完成;负值标识未完成/未放开)
                    }
                }
                salaryEmployeeDTO.setTaxStatus(0); //雇员明细状态雇员个税状态(0:正常;1:暂缓放开;负值标识未完成/未放开)

                employeeList.add(salaryEmployeeDTO);
            });
        }

        salaryBatchDTO.setEmployeeList(employeeList);
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步薪资发放数据到结算中心 -> 同步薪资数据").setContent(JSON.toJSONString(salaryBatchDTO)));
        JsonResult<SalaryBatchDTO> salaryBatchDTOJsonResult = salaryServiceProxy.saveSalaryBatchData(salaryBatchDTO);
        logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放").setTitle("同步薪资发放数据到结算中心 -> 结算中心结果").setContent(JSON.toJSONString(salaryBatchDTOJsonResult)));
        if (!ObjectUtils.isEmpty(salaryBatchDTOJsonResult)) {
            if ("0".equals(salaryBatchDTOJsonResult.getCode())) {
                return salaryBatchDTOJsonResult.getData();
            } else {
                logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放定时任务").setTitle("调用结算中心发放工资清单接口").setContent("接口返回结果错误 消息：" + salaryBatchDTOJsonResult.getMsg()));
            }
        } else {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("薪资发放定时任务").setTitle("调用结算中心发放工资清单接口").setContent("调用接口异常"));
        }

        return null;
    }

    @Override
    public Boolean addDeferredPool(SalaryGrantTaskBO salaryGrantTaskBO, List<SalaryGrantEmployeePO> employeeList) {
        PaymentDeferredRequestDTO paymentDeferredRequestDTO = new PaymentDeferredRequestDTO();
        paymentDeferredRequestDTO.setTaskCode(salaryGrantTaskBO.getTaskCode());             //任务单编号
        paymentDeferredRequestDTO.setBatchCode(salaryGrantTaskBO.getBatchCode());           //薪酬计算批次号
        paymentDeferredRequestDTO.setManagementId(salaryGrantTaskBO.getManagementId());     //管理方编号
        paymentDeferredRequestDTO.setManagementName(salaryGrantTaskBO.getManagementName()); //管理方名称
        paymentDeferredRequestDTO.setGrantCycle(salaryGrantTaskBO.getGrantCycle());         //薪资周期

        List<PaymentDeferredRequestDTO.EmployeeInfo> employeeInfoList = new ArrayList<>();

        if (!CollectionUtils.isEmpty(employeeList)) {
            employeeList.stream().forEach(employeePO -> {
                PaymentDeferredRequestDTO.EmployeeInfo employeeInfo = new PaymentDeferredRequestDTO.EmployeeInfo();
                employeeInfo.setEmployeeId(employeePO.getEmployeeId());             //雇员编号
                employeeInfo.setEmployeeName(employeePO.getEmployeeName());         //雇员名称
                employeeInfo.setCompanyId(employeePO.getCompanyId());               //公司编号
                employeeInfo.setCompanyName(employeePO.getCompanyName());           //公司名称
                employeeInfo.setCardNum(employeePO.getCardNum());                   //收款人账号
                employeeInfo.setAccountName(employeePO.getAccountName());           //收款人姓名
                employeeInfo.setBankcode(employeePO.getBankCode());                 //收款行行号
                employeeInfo.setDepositeBank(employeePO.getDepositBank());          //收款行名称
                employeeInfo.setPaymentAmountRmb(employeePO.getPaymentAmountRMB()); //人民币金额
                employeeInfo.setPaymentAmount(employeePO.getPaymentAmount());       //发放金额
                employeeInfo.setCurrencyCode(employeePO.getCurrencyCode());         //发放币种
                employeeInfo.setGrantMode(employeePO.getGrantMode());               //发放方式
                employeeInfo.setGrantStatus(employeePO.getGrantStatus());           //发放状态
                employeeInfo.setReprieveType(employeePO.getReprieveType());         //暂缓类型
                employeeInfo.setCountryCode(employeePO.getCountryCode());           //国籍

                employeeInfoList.add(employeeInfo);
            });
        }

        paymentDeferredRequestDTO.setEmployeeInfos(employeeInfoList);

        com.ciicsh.common.entity.JsonResult jsonResult = deferredPoolProxy.addDeferredPool(paymentDeferredRequestDTO);
        if (!ObjectUtils.isEmpty(jsonResult)) {
            return jsonResult.isSuccess();
        } else {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("暂缓人员信息进入暂缓池").setTitle("调用客服中心暂缓池操作接口").setContent("调用接口异常"));
        }

        return false;
    }

    @Override
    public List<PrNormalBatchDTO> getBatchListByManagementId(String managementId) {
        try {
            Pagination<PrNormalBatchDTO> prNormalBatchDTOPagination = batchProxy.getBatchListByManagementId(managementId, null, 1, Integer.MAX_VALUE);
            return prNormalBatchDTOPagination.getList();
        } catch (Exception e) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("根据管理方ID获取批次列表").setTitle("根据管理方ID获取批次列表").setContent("调用接口异常"));
        }

        return null;
    }

    @Override
    public PrBatchDTO getBatchInfo(String batchCode, int batchType) {
        return batchProxy.getBatchInfo(batchCode, batchType);
    }

    @Override
    public int updateBatchStatus(SalaryGrantMainTaskPO salaryGrantMainTaskPO, PrNormalBatchDTO prNormalBatchDTO) {
        int result = 0;

        try {
            BatchAuditDTO batchAuditDTO = new BatchAuditDTO();
            //薪资发放日期
            String grantDate = salaryGrantMainTaskPO.getGrantDate();
            //增加天数
            int advanceDay = prNormalBatchDTO.getAdvanceDay();

            if (!StringUtils.isEmpty(grantDate)){
                grantDate = grantDate.replace("-", "");
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyyMMdd");
                LocalDate grantDateDate = LocalDate.parse(grantDate, dateTimeFormatter);
                grantDateDate = grantDateDate.plusDays(advanceDay);
                batchAuditDTO.setAdvancePeriod(grantDateDate.format(dateTimeFormatter));
                result = batchProxy.updateBatchStatus(batchAuditDTO);
            }
        } catch (Exception e) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("更新批次状态").setTitle("更新批次状态").setContent("调用接口异常"));
        }

        return result;
    }

    @Override
    public boolean delDeferredEmp(SalaryGrantMainTaskPO mainTaskPO) {
        boolean result = false;

        try {
            PaymentDeferredDelDTO paymentDeferredDelDTO = new PaymentDeferredDelDTO();
            paymentDeferredDelDTO.setBatchCode(mainTaskPO.getBatchCode());
            paymentDeferredDelDTO.setTaskCode(mainTaskPO.getSalaryGrantMainTaskCode());
            com.ciicsh.common.entity.JsonResult jsonResult = deferredPoolProxy.delDeferredEmp(paymentDeferredDelDTO);
            if (!ObjectUtils.isEmpty(jsonResult)) {
                result = jsonResult.isSuccess();
            }
        } catch (Exception e) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("暂缓池删除接口").setTitle("调用暂缓池删除接口").setContent("调用接口异常"));
        }

        return result;
    }

    @Override
    public SalaryGrantEmployeePO getEmployeeNewestInfo(SalaryGrantEmployeePO currentEmployeePO) {
        try {
            FcEmpInfoRequestDTO fcEmpInfoRequestDTO = new FcEmpInfoRequestDTO();
            fcEmpInfoRequestDTO.setCompanyId(currentEmployeePO.getCompanyId()); //公司编号
            fcEmpInfoRequestDTO.setEmployeeId(currentEmployeePO.getEmployeeId()); //雇员编号
            fcEmpInfoRequestDTO.setBankcardId(!ObjectUtils.isEmpty(currentEmployeePO.getBankcardId()) ? currentEmployeePO.getBankcardId().toString() : null); //银行卡编号
            fcEmpInfoRequestDTO.setRuleId(!ObjectUtils.isEmpty(currentEmployeePO.getCycleRuleId()) ? currentEmployeePO.getCycleRuleId().toString() : null); //薪资发放规则编号
            com.ciicsh.common.entity.JsonResult<FcEmpInfoResponseDTO> jsonResult = employeeServiceProxy.getFcEmpDetail(fcEmpInfoRequestDTO);
            if (!ObjectUtils.isEmpty(jsonResult)) {
                if (true == jsonResult.isSuccess()) {
                    FcEmpInfoResponseDTO retEmpInfoResponseDTO = jsonResult.getData();

                    return fcEmpInfoRequestDTO2SalaryGrantEmployeePO(retEmpInfoResponseDTO);
                } else {
                    logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("获取雇员最新信息").setTitle("获取雇员最新信息").setContent("获取雇员最新信息失败"));
                }
            }
        } catch (Exception e) {
            logClientService.infoAsync(LogDTO.of().setLogType(LogType.APP).setSource("获取雇员最新信息").setTitle("获取雇员最新信息").setContent("获取雇员最新信息接口调用失败"));
        }

        return null;
    }

    /**
     * 接口返回信息转换为雇员信息
     *
     * @param fcEmpInfoResponseDTO
     * @return
     */
    public SalaryGrantEmployeePO fcEmpInfoRequestDTO2SalaryGrantEmployeePO(FcEmpInfoResponseDTO fcEmpInfoResponseDTO){
        SalaryGrantEmployeePO newestEmployeePO = new SalaryGrantEmployeePO();

        //雇员基本信息表
        if (!ObjectUtils.isEmpty(fcEmpInfoResponseDTO.getBaseInfo())) {
            newestEmployeePO.setCountryCode(fcEmpInfoResponseDTO.getBaseInfo().getCountryCode()); //国籍
        }

        //雇员银行卡信息
        if (!ObjectUtils.isEmpty(fcEmpInfoResponseDTO.getBankCardInfo())) {
            newestEmployeePO.setBankcardType(fcEmpInfoResponseDTO.getBankCardInfo().getBankcardType()); //银行卡种类
            newestEmployeePO.setDepositBank(fcEmpInfoResponseDTO.getBankCardInfo().getDepositBank()); //收款行名称
            newestEmployeePO.setBankCode(fcEmpInfoResponseDTO.getBankCardInfo().getBankCode()); //收款行行号
            newestEmployeePO.setAccountName(fcEmpInfoResponseDTO.getBankCardInfo().getAccountName()); //收款人姓名
            newestEmployeePO.setCardNum(fcEmpInfoResponseDTO.getBankCardInfo().getCardNum()); //收款人账号
            newestEmployeePO.setCurrencyCode(fcEmpInfoResponseDTO.getBankCardInfo().getCurrencyCode()); //银行卡币种
            newestEmployeePO.setExchange(fcEmpInfoResponseDTO.getBankCardInfo().getExchange()); //银行卡汇率
            newestEmployeePO.setSwiftCode(fcEmpInfoResponseDTO.getBankCardInfo().getSwiftCode()); //银行国际代码
            newestEmployeePO.setIban(fcEmpInfoResponseDTO.getBankCardInfo().getIban()); //国际银行账户号码
            newestEmployeePO.setDefaultCard(1 == fcEmpInfoResponseDTO.getBankCardInfo().getDefult()); //是否默认卡
        }

        //薪资发放规则信息
        if (!ObjectUtils.isEmpty(fcEmpInfoResponseDTO.getSalGrantRule())) {
            newestEmployeePO.setBankcardId(Long.parseLong(fcEmpInfoResponseDTO.getSalGrantRule().getBankcardId())); //银行卡编号
            newestEmployeePO.setCurrencyCode(fcEmpInfoResponseDTO.getSalGrantRule().getCurrency()); //发放币种
            newestEmployeePO.setRuleAmount(fcEmpInfoResponseDTO.getSalGrantRule().getAmount()); //规则金额
            newestEmployeePO.setRuleRatio(fcEmpInfoResponseDTO.getSalGrantRule().getPercent()); //规则比例
            newestEmployeePO.setRuleType(fcEmpInfoResponseDTO.getSalGrantRule().getRuleType()); //规则类型
        }

        //雇员服务协议
        if (!ObjectUtils.isEmpty(fcEmpInfoResponseDTO.getEmpServiceAgreement())) {
            newestEmployeePO.setGrantMode(Integer.parseInt(fcEmpInfoResponseDTO.getEmpServiceAgreement().getSalaryGrantInfo())); //发放方式
        }

        //服务周期规则表
        if (!ObjectUtils.isEmpty(fcEmpInfoResponseDTO.getVariables())) {
            HashMap<String, Object> cycleRuleHashMap = fcEmpInfoResponseDTO.getVariables().get("cycleRule");
            if (!ObjectUtils.isEmpty(cycleRuleHashMap)) {
                newestEmployeePO.setGrantDate(cycleRuleHashMap.get("SalaryDayDate").toString()); //薪资发放日期
                newestEmployeePO.setGrantTime(Integer.parseInt(cycleRuleHashMap.get("SalaryDayTime").toString())); //薪资发放时段
            }
        }

        return newestEmployeePO;
    }
}
