package com.ciicsh.gto.fcbusinesscenter.tax.commandservice.api.json;

/**
 * Created by shil on 2017/9/20.
 */
public class JsonResult {
    public JsonResult(){}
    public JsonResult(String errorcode) {
        this.errorcode = errorcode;
    }

    public JsonResult(Object data){
        this.data = data;
    }

    public JsonResult(String errorcode, String errormsg) {
        this.errorcode = errorcode;
        this.errormsg = errormsg;
    }

    public JsonResult(String errorcode, Object data){
        this.errorcode = errorcode;
        this.data = data;
    }

    public JsonResult(String errorcode, String errormsg, Object data){
        this.errorcode = errorcode;
        this.errormsg = errormsg;
        this.data = data;
    }

    private String errorcode = "0";
    private String errormsg = "";
    private Object data = "";

    public String getErrorcode(){
        return errorcode;
    }

    public void setErrorcode(String errorcode){
        this.errorcode = errorcode;
    }

    public String getErrormsg() {
        return errormsg;
    }

    public void setErrormsg(String errormsg) {
        this.errormsg = errormsg;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }


    /**
     *  自定义成功编码
     * @param successCode
     * @param successMsg
     * @param data
     * @return
     */
    public JsonResult success(String successCode, String successMsg,Object data) {
        return new JsonResult(successCode, successMsg,data);
    }
    /**
     * 默认成功返回结果集
     * @param data
     * @return
     */
    public JsonResult success(Object data){
        return new JsonResult("0", "success",data);
    }

    /**
     * 默认成功结果为true
     * @return
     */
    public JsonResult success(){
        return new JsonResult("0", "success",true);
    }

    /**
     * 自定义错误编码
     * @param errorCode
     * @param errorMsg
     * @param data
     * @return
     */
    public JsonResult error(String errorCode, String errorMsg,Object data) {
        return new JsonResult(errorCode, errorMsg,data);
    }

    /**
     * 默认错误返回结果集
     * @param data
     * @return
     */
    public JsonResult error(Object data){
        return new JsonResult("1", "error",data);
    }

    /**
     * 默认错误信息返回false
     * @return
     */
    public JsonResult error(){
        return new JsonResult("1", "error",false);
    }
}
