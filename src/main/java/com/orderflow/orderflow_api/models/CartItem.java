package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@Table(name = "cart_items")
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "cart_id")
    private Cart cart;

    @Column(name = "added_at")
    private OffsetDateTime addDate = OffsetDateTime.now(ZoneOffset.UTC);

    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;

    private Integer quantity;
    private double discount;
    private double itemPrice;

    public CartItem(Integer quantity, double discount, double itemPrice) {
        this.quantity = quantity;
        this.discount = discount;
        this.itemPrice = itemPrice;
    }
}
