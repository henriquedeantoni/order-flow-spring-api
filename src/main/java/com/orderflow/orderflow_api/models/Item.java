package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name="items")
public class Item {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="item_id")
    private Long itemId;

    @NotBlank
    @Size(max=25)
    @Column(name = "name")
    private String itemName;

    @NotBlank
    @Size(max=1024)
    @Column(name = "description")
    private String description;

    @NotBlank
    private int quantity;

    @NotBlank
    @Column(name = "price")
    private double price;

    @NotBlank
    @Size(max=100)
    @Column(name = "discount")
    private int discount;

    private int timePrepareMinutes;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
