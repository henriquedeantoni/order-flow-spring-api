package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemImageDTO {
    private Long itemImageId;
    private String url;
    private String title;
    private String description;
}
