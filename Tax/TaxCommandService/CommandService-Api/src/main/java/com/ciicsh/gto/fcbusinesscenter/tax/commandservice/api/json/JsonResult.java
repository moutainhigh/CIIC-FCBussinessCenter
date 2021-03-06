package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json;

/**
 * yuantongqing
 */
public class JsonResult<T> {

    /**
     * TD_ 个税数据处理；
     * TM_ 个税任务；
     * DE_ 申报；
     * TR_ 划款；
     * PA_ 缴纳；
     * PR_ 完税凭证；
     * SU_ 供应商；
     */
    public static enum ReturnCode{

        SUCCESS(""),

        TOKEN_ERROR("登录异常"),

        FAIL("异常"),

        TM_ER01("任务内有未确认的合并明细，不能提交任务"),

        TM_ER02("请将所有子任务退回，否则无法提交"),

        TM_ER03("请将所有子任务退回，否则无法失效"),

        DE_ER01("任务内有未确认的合并明细，不能提交任务"),

        SU_ER01("任务内有未确认的合并明细，不能提交任务"),

        CONSTRAINTS_1("有批次已取消关账，不能创建任务"),

        CONSTRAINTS_2("相关批次已取消关账，不能提交"),

        CONSTRAINTS_3("当前任务相关批次数据非最新，不能提交"),

        RECOVERY_1("雇员所在任务已经变更为取消关账状态，不能恢复"),

        RECOVERY_2("雇员所在任务已经创建，不能恢复"),

        COMPLETE_ERROR("相关主任务下的子任务有退回的任务,不能提交"),

        REJECT_ERROR("相关主任务下的子任务有完成的任务,不能退回"),

        BILLCENTER_1("所选主任务划款子任务中滞纳金和罚金已经收款,不可退回"),

        BILLCENTER_2("所选任务划款子任务中滞纳金和罚金已经来款,不可退回"),

        SETTLEMENT_1("调用结算中心划款接口异常!"),

        SETTLEMENT_2("结算中心验证批次是否存在出错!"),

        SETTLEMENT_3("结算中心打卡出错!");


        private String  message;

        private ReturnCode(String message)
        {
            this.message = message;
        }

        public String getMessage() {
            return message;
        }
    }

    public JsonResult(){}

    public JsonResult(String code, String msg, T data){
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    private String code = "0";
    private String msg = "";
    private T data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    /**
     *  参数设置(成功)
     * @param data
     * @return
     */
    public void fill(T data) {
        this.data = data;
    }

    /**
     *  参数设置(失败)
     * @param rc
     * @return
     */
    public void fill(ReturnCode rc) {
        this.code = rc.toString();
        this.msg = rc.getMessage();
    }
    /**
     * 是否成功
     * @return
     */
    /*public boolean isSuccess(){

        return this.code.equals("0");
    }*/
    /**
     * 错误
     * @return
     */
    public void error(){
        this.fill(ReturnCode.FAIL);
    }

}
