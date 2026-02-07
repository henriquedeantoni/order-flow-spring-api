package com.orderflow.orderflow_api.payload;

import com.orderflow.orderflow_api.models.Item;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ItemResponse {
    private List<ItemDTO> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
    private LocalDateTime timestamp;
}
