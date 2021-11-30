package com.imarket.marketapi.apis.response;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class PageInfo {
    private int pageNumber;
    private int pageSize;
    private long totalElements;
    private int totalPages;
}
