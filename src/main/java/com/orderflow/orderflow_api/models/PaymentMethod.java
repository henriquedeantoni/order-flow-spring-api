package com.orderflow.orderflow_api.models;

public enum PaymentMethod {
    PAYMENT_CASH(1),
    PAYMENT_CREDIT(2),
    PAYMENT_DEBIT(3),
    PAYMENT_PIX(4);

    private int code;

    private PaymentMethod(int code){
        this.code = code;
    }

    public int getCode(){
        return code;
    }

    public static PaymentMethod valueOf(int code){
        for (PaymentMethod value : PaymentMethod.values()){
            if (value.getCode() == code){
                return value;
            }
        }
        throw new IllegalArgumentException("No constant with code " + code + " found in inventory");
    }
}
