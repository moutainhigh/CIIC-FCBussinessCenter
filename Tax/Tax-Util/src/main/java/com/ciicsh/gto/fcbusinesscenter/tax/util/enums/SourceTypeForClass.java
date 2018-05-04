package com.ciicsh.gto.fcbusinesscenter.tax.util.enums;

/**
 * @author wuhua
 */
public enum SourceTypeForClass {

    CalculationBatchController("00"),
    CalculationBatchDetailController("00"),
    CommonController("08"),
    OrderController("08"),
    TaskFileController("06"),
    TaskMainController("01"),
    TaskMainProofController("05"),
    TaskSubDeclareController("02"),
    TaskSubDeclareDetailController("02"),
    TaskSubMoneyController("03"),
    TaskSubMoneyDetailController("03"),
    TaskSubPaymentController("04"),
    TaskSubPaymentDetailController("04"),
    TaskSubProofController("05"),
    TaskSubProofDetailController("05"),
    TaskSubSupplierController("07"),
    TaskSubSupplierDetailController("07");

    private String  message;

    private SourceTypeForClass(String message)
    {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
