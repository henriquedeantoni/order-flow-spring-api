package com.orderflow.orderflow_api.models;

public enum PaymentStatus {
    PAYMENT_PENDING(1),
    PAYMENT_APPROVED(2);

    private int code;

    private PaymentStatus(int code) {
        this.code = code;
    }
    public int getCode() {
        return code;
    }

    public static PaymentStatus valueOf(int code) {
        for (PaymentStatus value : PaymentStatus.values()) {
            if (value.getCode() == code) {
                return value;
            }
        }
        throw new IllegalArgumentException("No constant with code " + code + " found in inventory");
    }
}
