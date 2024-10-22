package com.dataeye.partner.bean.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author jaret
 * @date 2024/10/21 17:45
 * @description
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse<T> {

    private int status = StatusCode.SUCCESS.getStatusCode();
    private String message = StatusCode.SUCCESS.getStatusCodeReason();
    private ApiPage page;
    private T data;

    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<>(StatusCode.SUCCESS.getStatusCode(), msg, null, null);
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(StatusCode.SUCCESS.getStatusCode(), StatusCode.SUCCESS.getStatusCodeReason(), null, data);
    }

    public static <T> ApiResponse<T> error(String message) {
        return new ApiResponse<>(StatusCode.INTERNAL_SERVER_ERROR.getStatusCode(), message, null, null);
    }

    public static <T> ApiResponse<T> error(StatusCode statusCode, String msg) {
        return new ApiResponse<>(statusCode.getStatusCode(), msg, null, null);
    }

    public ApiResponse<T> addPage(Integer page, Integer pageSize, Long totalRecords) {
        page = page == null ? 0 : page;
        pageSize = pageSize == null ? 0 : pageSize;
        totalRecords = totalRecords == null ? 0 : totalRecords;

        this.page = new ApiPage(page, pageSize, totalRecords);
        //算总页数
        if (totalRecords == 0 || page == 0 || pageSize == 0) {
            this.page.setTotalPage(0);
        } else {
            this.page.setTotalPage((int) (totalRecords - 1) / pageSize + 1);
        }
        return this;
    }

}
