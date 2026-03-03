package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalDTO {
    private Long localId;
    private String streetName;
    private String buildingName;
    private String city;
    private String state;
    private String country;
    private String postalCode;
}
