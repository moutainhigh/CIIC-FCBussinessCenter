package com.ciicsh.gto.fcbusinesscenter.util.common;

import com.ciicsh.gt1.common.auth.ManagementInfo;
import com.ciicsh.gt1.common.auth.UserContext;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by bill on 18/4/27.
 */
public class CommonHelper {

    /**
     * 获取当前用户管理方ID 列表 字符串 ， 格式："'GL001','GL002','GL003',... 用于 SQL IN 语句"
     * @return
     */
    public static String getManagementIDs() {
        List<ManagementInfo> list = UserContext.getManagementInfoLists();
        if(list == null || list.size() ==0 ){
            return "";
        }
        List<String> IDList = list.stream().map(p-> "'"+p.getManagementId()+"'").collect(Collectors.toList());
        return String.join(",",IDList);
    }
}
