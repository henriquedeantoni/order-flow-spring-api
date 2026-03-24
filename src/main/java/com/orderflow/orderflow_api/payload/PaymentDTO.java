package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.PaymentMethod;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long paymentId;
    private PaymentMethod paymentMethod;
    private String pagPaymentId;
    private String pagStatus;
    private String pagStatusMessage;
    private String pagName;
}
