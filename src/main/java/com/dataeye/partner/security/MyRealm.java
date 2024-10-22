package com.dataeye.partner.security;

import com.dataeye.partner.bean.PlayletPartnerUser;
import com.dataeye.partner.mapper.PlayletPartnerUserMapper;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author jaret
 * @date 2024/10/21 17:30
 * @description
 */
public class MyRealm extends AuthorizingRealm {

    public static final int HASH_ITERATIONS = 2;

    @Autowired
    private PlayletPartnerUserMapper playletPartnerUserMapper;

    /**
     * 身份认证
     * 主要作用是提供一个身份的鉴定功能，基本思路是，从数据库中查找用户身份信息，交给Shiro框架，shiro框架会自动与登录页传进来的账号信息进行对比是否匹配，如果匹配，则登录成功，否则登录失败
     */
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        String username = (String) token.getPrincipal();

        // 从数据库查询用户信息
        PlayletPartnerUser user = playletPartnerUserMapper.findByUsername(username);
        if (user == null) {
            throw new UnknownAccountException("用户不存在");
        }

        //查询用户的角色和权限存到SimpleAuthenticationInfo中，这样在其它地方 SecurityUtils.getSubject().getPrincipal() 就能拿出用户的所有信息，包括角色和权限
        return new SimpleAuthenticationInfo(user, user.getPassword(), ByteSource.Util.bytes(user.getSalt()), getName());
    }


    /**
     * 授权
     * 身份鉴定完毕后，把权限赋予给当前用户，以便后续在需要的地方根据权限细致控制
     */
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //null usernames are invalid
        if (principals == null) {
            throw new AuthorizationException("PrincipalCollection method argument cannot be null.");
        }
        return new SimpleAuthorizationInfo();
    }
}

