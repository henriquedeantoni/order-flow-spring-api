package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @ManyToOne
    @JoinColumn(name = "supply_id")
    private Supply supply;

    private String codeBar;
    private String section;
    private String supplyReference;

    @Pattern(regexp = "STOCK_IN|STOCK_OUT", message = "Error: Status pattern not allowed")
    private String status;

    @Column(name = "validate_at")
    private LocalDate valDate;

    @Column(name = "added_at")
    private OffsetDateTime addDate = OffsetDateTime.now(ZoneOffset.UTC);

    private OffsetDateTime movementDate;

    public InventorySupply(String codeBar, String section, String supplyReference, String status, LocalDate valDate, OffsetDateTime addDate, OffsetDateTime movementDate) {
        this.codeBar = codeBar;
        this.section = section;
        this.supplyReference = supplyReference;
        this.status = status;
        this.valDate = valDate;
        this.addDate = addDate;
        this.movementDate = movementDate;
    }
}
