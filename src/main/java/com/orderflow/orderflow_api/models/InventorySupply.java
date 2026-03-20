package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "inventory_supplies")
public class InventorySupply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long inventorySupplyId;

    @OneToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    @Getter
    private Integer quantity=0;

    private String codeBar;
    private String section;

    @Column(name = "added_at")
    private OffsetDateTime addDate = OffsetDateTime.now(ZoneOffset.UTC);
}
