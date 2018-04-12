package com.ciicsh.gto.fcoperationcenter.commandservice.api.dto.Custom;

import java.util.List;

/**
 * Created by NeoJiang on 2018/3/8.
 */
public class AccountSetWithItemsDTO {

    private String managementId;

    private String accountSetCode;

    private String accountSetName;

    private List<PrItemsInAccountSetDTO> accountItems;

    public String getManagementId() {
        return managementId;
    }

    public void setManagementId(String managementId) {
        this.managementId = managementId;
    }

    public String getAccountSetCode() {
        return accountSetCode;
    }

    public void setAccountSetCode(String accountSetCode) {
        this.accountSetCode = accountSetCode;
    }

    public String getAccountSetName() {
        return accountSetName;
    }

    public void setAccountSetName(String accountSetName) {
        this.accountSetName = accountSetName;
    }

    public List<PrItemsInAccountSetDTO> getAccountItems() {
        return accountItems;
    }

    public void setAccountItems(List<PrItemsInAccountSetDTO> accountItems) {
        this.accountItems = accountItems;
    }
}
