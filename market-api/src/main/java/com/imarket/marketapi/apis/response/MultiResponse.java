package com.imarket.marketapi.apis.response;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

public class MultiResponse<T> extends CommonResponse {
    private List<T> data;
    private PageInfo pageInfo;
    public MultiResponse(HttpStatus status, Page page) {
        super(status);
        this.pageInfo = new PageInfo(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
