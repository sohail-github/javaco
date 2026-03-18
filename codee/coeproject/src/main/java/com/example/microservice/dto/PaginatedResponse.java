package com.example.microservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.List;

/**
 * Pagination response wrapper
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PaginatedResponse<T> {
    private List<T> content;
    private int page;
    private int pageSize;
    private long totalElements;
    private int totalPages;
    private boolean hasMore;
    private boolean isFirst;
    private boolean isLast;
}
