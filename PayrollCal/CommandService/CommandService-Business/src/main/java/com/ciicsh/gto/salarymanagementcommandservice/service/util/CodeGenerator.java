package com.ciicsh.gto.salarymanagementcommandservice.service.util;

import com.ciicsh.gto.salarymanagementcommandservice.service.PrCodeSeqService;
import org.apache.commons.lang.text.StrSubstitutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by jiangtianning on 2017/11/20.
 */
@Component
public class CodeGenerator {

    @Autowired
    private PrCodeSeqService prCodeSeqService;

    private static final DecimalFormat TEN_DIGITS = new DecimalFormat("0000000000");
    private static final DecimalFormat FIVE_DIGITS = new DecimalFormat("00000");

    private static final String PR_GROUP_SEQ = "pr_group_seq";
    private static final String PR_ITEM_SEQ = "pr_item_seq";
    private static final String PR_GROUP_TEMPLATE_SEQ = "pr_grouptemp_seq";
    private static final String PR_ACCOUNT_SET_SEQ = "pr_accountset_seq";
    private static final String PR_EMP_GROUP_SEQ = "pr_empgroup_seq";

    private static final String PR_NORMAL_BATCH_SEQ = "pr_normal_batch_seq"; //管理方ID-计算日期-10位序号

    private static final String PR_GROUP_PREFIX = "XZZ";
    private static final String PR_ITEM_PREFIX = "XZX";
    private static final String PR_GROUP_TEMPLATE_PREFIX = "XZZMB";
    private static final String PR_ACCOUNT_SET_PREFIX = "XZZT";
    private static final String PR_EMP_GROUP_PREFIX = "GYZ";

    private static final String CODE_TEMPLATE_NO_MANAGEMENT = "${prefix}_${seq}";
    private static final String CODE_TEMPLATE = "${prefix}_${managementId}_${seq}";
    private static final String PR_GROUP_TEMPLATE_CODE_TEMPLATE = "${prefix}_${seq}";

    public String genPrItemCode(String managementId) {
        long seq = prCodeSeqService.getCode(PR_ITEM_SEQ);
        String seqStr = FIVE_DIGITS.format(seq);
        return genCode(PR_ITEM_PREFIX, managementId, seqStr);
    }

    public String genPrGroupCode(String managementId) {
        long seq = prCodeSeqService.getCode(PR_GROUP_SEQ);
        String seqStr = FIVE_DIGITS.format(seq);
        return genCode(PR_GROUP_PREFIX, managementId, seqStr);
    }

    public String genPrAccountSetCode(String managementId) {
        long seq = prCodeSeqService.getCode(PR_ACCOUNT_SET_SEQ);
        String seqStr = FIVE_DIGITS.format(seq);
        return genCode(PR_ACCOUNT_SET_PREFIX, managementId, seqStr);
    }

    public String genEmpGroupCode(String managementId) {
        long seq = prCodeSeqService.getCode(PR_EMP_GROUP_SEQ);
        String seqStr = FIVE_DIGITS.format(seq);
        return genCode(PR_EMP_GROUP_PREFIX, managementId, seqStr);
    }

    public String genPrGroupTemplateCode() {
        long seq = prCodeSeqService.getCode(PR_GROUP_TEMPLATE_SEQ);
        String seqStr = FIVE_DIGITS.format(seq);
        return genCode(PR_GROUP_TEMPLATE_PREFIX, "", seqStr);
    }

    public String genPrNormalBatchCode(String managementId, String period){
        long seq = prCodeSeqService.getCode(PR_NORMAL_BATCH_SEQ);
        String seqStr = TEN_DIGITS.format(seq);
        return genCode(managementId, period, seqStr);
    }

    /**
     * 生成非批次的Code
     * @param prefix
     * @param managementId
     * @param seq
     * @return
     */
    private String genCode(String prefix, String managementId, String seq) {
        Map<String, String> queryMap = new HashMap<String,String>(){{
            put("prefix", prefix);
            put("managementId", managementId);
            put("seq", seq);
        }};
        StrSubstitutor sub = new StrSubstitutor(queryMap);
        if (PR_GROUP_TEMPLATE_PREFIX.equals(prefix)) {
            return sub.replace(PR_GROUP_TEMPLATE_CODE_TEMPLATE);
        }
        if (queryMap.get("managementId") == null) {
            return sub.replace(CODE_TEMPLATE_NO_MANAGEMENT);
        }
        return sub.replace(CODE_TEMPLATE);
    }

}