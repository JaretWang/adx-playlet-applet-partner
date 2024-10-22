package com.dataeye.partner.controller;

import com.alibaba.fastjson.JSONObject;
import com.dataeye.partner.bean.PlayletPartnerUser;
import com.dataeye.partner.bean.params.UserChangePwd;
import com.dataeye.partner.bean.response.ApiResponse;
import com.dataeye.partner.bean.response.StatusCode;
import com.dataeye.partner.bean.vo.UserLoginVO;
import com.dataeye.partner.security.SecurityTool;
import com.dataeye.partner.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.crypto.CryptoException;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;

/**
 * @author jaret
 * @date 2024/10/21 17:21
 * @description
 */
@Slf4j
@Api(value = "用户相关", tags = {"用户相关"})
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @ApiOperation("用户登录")
    @PostMapping("/login")
    public ApiResponse<UserLoginVO> login(@ApiParam("用户名") @NotBlank(message = "用户名不能为空") @RequestParam String username,
                                         @ApiParam("密码(RSA非对称加密)") @NotBlank(message = "密码不能为空") @RequestParam String password,
                                         @ApiParam("用户名") @NotBlank(message = "记住我") @RequestParam(required = false, defaultValue = "false") Boolean rememberMe) {
        try {
            //解密
            String decryptPassword = SecurityTool.decrypt(password);
            UsernamePasswordToken token = new UsernamePasswordToken(username, decryptPassword);
            token.setRememberMe(rememberMe);

            Subject subject = SecurityUtils.getSubject();
            subject.login(token);
            PlayletPartnerUser principal = (PlayletPartnerUser) subject.getPrincipal();
            UserLoginVO result = new UserLoginVO(principal.getId(), principal.getUsername());
            return ApiResponse.success(result);
        } catch (UnknownAccountException uae) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "未知账户");
        } catch (IncorrectCredentialsException ice) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "密码不正确");
        } catch (LockedAccountException lae) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "账户已锁定");
        } catch (ExcessiveAttemptsException eae) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "用户名或密码错误次数过多,账户已锁定");
        } catch (DisabledAccountException sae) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "帐号已经禁止登录");
        } catch (AuthenticationException ae) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "用户名或密码不正确");
        } catch (CryptoException e) {
            return ApiResponse.error(StatusCode.FORBIDDEN, "密码不合法");
        } catch (Exception e) {
            log.error("登录异常", e);
            return ApiResponse.error(StatusCode.INTERNAL_SERVER_ERROR, "登录异常");
        }
    }

    @ApiOperation("用户登出")
    @GetMapping("/logout")
    public String logout() {
        Subject subject = SecurityUtils.getSubject();
        //清除当前登录用户的认证信息
        subject.logout();
        return "已登出";
    }

    @ApiOperation("修改密码")
    @GetMapping("/changePwd")
    public String changePwd(@Validated @RequestBody UserChangePwd changePwd) {
        int i = userService.changePwd(changePwd);

        Subject subject = SecurityUtils.getSubject();
        //清除当前登录用户的认证信息
        subject.logout();
        return "已登出";
    }


}
