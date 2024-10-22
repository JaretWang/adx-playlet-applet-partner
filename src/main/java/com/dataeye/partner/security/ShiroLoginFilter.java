package com.dataeye.partner.security;

import com.alibaba.fastjson.JSON;
import com.dataeye.partner.bean.response.ApiResponse;
import com.dataeye.partner.bean.response.StatusCode;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.web.filter.authc.AuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author jaret
 * @date 2024/10/21 17:44
 * @description
 */
@Slf4j
public class ShiroLoginFilter extends AuthenticationFilter {

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) {
        PrintWriter out = null;
        HttpServletResponse res = (HttpServletResponse) response;
        try {
            res.setCharacterEncoding("UTF-8");
            res.setContentType("application/json");
            out = response.getWriter();

            ApiResponse<Object> error = ApiResponse.error(StatusCode.UNAUTHORIZED, "未登录，请重新登陆");
            out.println(JSON.toJSONString(error));
            out.flush();
        } catch (Exception e) {
            log.info("ShiroLoginFilter onAccessDenied error", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
        return false;
    }
}
