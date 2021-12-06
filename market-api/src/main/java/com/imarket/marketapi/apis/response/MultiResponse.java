package com.imarket.marketapi.apis.response;

import lombok.Getter;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;

import java.util.List;

@Getter
public class MultiResponse<T> extends CommonResponse {
    private List<T> data;
    private PageInfo page;
    public MultiResponse(HttpStatus status, List<T> data, Page page) {
        super(status);
        this.data = data;
        this.page = new PageInfo(page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
    }
}
