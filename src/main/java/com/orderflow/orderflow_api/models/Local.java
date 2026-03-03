package com.orderflow.orderflow_api.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "locals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Local {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long localId;

    @NotBlank
    @Size(min = 3, max = 50, message="Street name must be at least 3 and maximum 50 character")
    private String streetName;

    @NotBlank
    @Size(min = 3, max = 50, message="Building name must be at least 3 and maximum 50 character")
    private String buildingName;

    @NotBlank
    @Size(min = 3, max = 30, message="City name must be at least 3 and maximum 30 character")
    private String city;

    @NotBlank
    @Size(min = 2, max = 30, message="State name must be at least 2 and maximum 30 character")
    private String state;

    @NotBlank
    @Size(min = 2, max = 30, message="Country name must be at least 2 and maximum 30 character")
    private String country;

    @NotBlank
    @Size(min = 5, max = 30, message="Pin code must be at least 6 and maximum 30 character")
    private String postalCode;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Local(String streetName, String buildingName, String city, String state, String country, String postalCode) {
        this.streetName = streetName;
        this.buildingName = buildingName;
        this.city = city;
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
    }
}
