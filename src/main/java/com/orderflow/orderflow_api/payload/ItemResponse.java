package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private List<Item> content;
}
