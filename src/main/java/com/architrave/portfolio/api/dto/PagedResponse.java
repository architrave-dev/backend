package com.architrave.portfolio.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PagedResponse<T>{

    private long totalElements;
    private int totalPages;

    private int page; // 1-based
    private int size;
    private List<T> content;

    public PagedResponse(Page<T> page) {
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.page = page.getNumber() + 1; // Convert to 1-based
        this.size = page.getSize();
        this.content = page.getContent();
    }
}
