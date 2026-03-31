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
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supplies")
public class Supply {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplyId;

    private String supplyName;
    private String supplyReference;
    private String brandName;
    private String supplyDescription;
    private String supplyCode;
    private String supplyUnit;

    @ToString.Exclude
    @OneToOne(mappedBy = "supply", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private InventorySupply inventorySupply;

    @Column(name = "added_at")
    private OffsetDateTime addDate = OffsetDateTime.now(ZoneOffset.UTC);
}
