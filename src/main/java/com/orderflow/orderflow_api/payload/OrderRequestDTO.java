package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class OrderRequestDTO {
    private Long localId;
    private PaymentMethod paymentMethod;
    private String pagName;
    private String pagPaymentId;
    private String pagStatus;
    private String pagResponseMessage;
}
