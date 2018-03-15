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
     */
    public static enum ReturnCode{

        FAIL("异常"),

        TM_ER01("任务内有未确认的合并明细，不能提交任务");

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
    public boolean isSuccess(){

        return this.code.equals("0");
    }
    /**
     * 错误
     * @return
     */
    public void error(){
        this.fill(ReturnCode.FAIL);
    }

}
