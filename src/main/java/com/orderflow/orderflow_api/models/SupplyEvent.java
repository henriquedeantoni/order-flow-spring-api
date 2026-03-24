package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "supply_events")
public class SupplyEvent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long supplyEventId;

    private Long supplyId;

    @Pattern(regexp = "IN|OUT", message = "Error: The event type must be only IN or OUT")
    private String eventType;

    private Integer quantityMoved;

    private OffsetDateTime eventDate = OffsetDateTime.now(ZoneOffset.UTC);
}
