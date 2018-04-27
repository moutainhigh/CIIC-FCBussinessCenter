package com.ciicsh.gto.fcbusinesscenter.util.common;

import com.ciicsh.gt1.common.auth.UserContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/4/27.
 */
public class CommonHelper {

    public static String getManagementIDs(){
        List<String> IDList = UserContext.getManagementInfoLists().stream().map(p-> p.getManagementId()).collect(Collectors.toList());
        if(IDList == null || IDList.size() ==0 ){
            return "";
        }
        return String.join(",",IDList);
    }
}
