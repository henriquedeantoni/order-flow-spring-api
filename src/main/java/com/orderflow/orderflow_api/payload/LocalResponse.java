package com.orderflow.orderflow_api.payload;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocalResponse {
    private List<LocalDTO> content;
    private Integer pageSize;
    private Integer pageNumber;
    private Long totalElements;
    private Integer totalPages;
    private boolean lastPage;
    private LocalDateTime timestamp;
}
