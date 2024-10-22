package com.dataeye.partner.bean.response;

import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaret
 * @date 2024/10/21 17:45
 * @description
 */
@Data
@NoArgsConstructor
public class ApiPage {

    private Integer pageId;
    private Integer pageSize;
    private Long totalRecords;
    private Integer totalPage;

    public ApiPage(Integer pageId, Integer pageSize, Long totalRecords) {
        this.pageId = pageId;
        this.pageSize = pageSize;
        this.totalRecords = totalRecords;
    }
}
