package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.host.intercept;

import com.ciicsh.gto.commonservice.util.dto.Result;
import com.ciicsh.gto.identityservice.api.dto.response.UserInfoResponseDTO;

public class LoginInfoContext {

    private Result<UserInfoResponseDTO> result;

    public Result<UserInfoResponseDTO> getResult() {
        return result;
    }

    public void setResult(Result<UserInfoResponseDTO> result) {
        this.result = result;
    }
}
