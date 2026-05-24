package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name="payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long paymentId;

    @OneToOne(mappedBy = "payment", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Order order;

    @NotBlank
    private PaymentMethod paymentMethod;

    private String pagPaymentId;
    private PaymentStatus pagStatus;
    private String pagResponseMessage;
    private String pagName;

    public Payment(PaymentMethod paymentMethod, String pagPaymentId, PaymentStatus pagStatus, String pagResponseMessage, String pagName) {
        this.paymentMethod = paymentMethod;
        this.pagPaymentId = pagPaymentId;
        this.pagStatus = pagStatus;
        this.pagResponseMessage = pagResponseMessage;
        this.pagName = pagName;
    }
}
