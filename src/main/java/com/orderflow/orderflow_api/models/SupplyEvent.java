package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supply_events")
public class SupplyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplyEventId;

    private Long itemId;

    private String eventType;
    private Integer quantityMoved;
    private LocalDateTime eventDate;
}
