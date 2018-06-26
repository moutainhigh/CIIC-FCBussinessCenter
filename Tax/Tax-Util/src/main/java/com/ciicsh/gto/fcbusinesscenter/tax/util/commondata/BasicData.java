package com.ciicsh.gto.fcbusinesscenter.tax.util.commondata;

import java.util.HashMap;
import java.util.Map;

/**
 * basic data
 * @author wuhua
 */
public class BasicData {

    private Map<String, String> certType = new HashMap<String, String>();

    /**
     * 报税证件类型
     */

    private Map<String, String> taxCertType = new HashMap<String, String>();

    private BasicData() {
    }

    private static volatile BasicData instance = null;

    public static BasicData getInstance() {
        if (instance == null) {
            synchronized (BasicData.class) {
                if (instance == null) {
                    instance = new BasicData();
                }
            }
        }
        return instance;
    }

    public Map<String, String> getCertType() {
        return certType;
    }

    public void setCertType(Map<String, String> certType) {
        this.certType = certType;
    }

    public Map<String, String> getTaxCertType() {
        return taxCertType;
    }

    public void setTaxCertType(Map<String, String> taxCertType) {
        this.taxCertType = taxCertType;
    }
}
