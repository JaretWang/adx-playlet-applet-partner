package com.dataeye.partner.security;


import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.crypto.hash.Sha256Hash;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.session.mgt.SessionManager;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

import javax.servlet.Filter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author jaret
 * @date 2024/10/21 17:30
 * @description
 */
@Configuration
public class ShiroConfig {

    @Bean
    public RedisSessionDAO redisSessionDAO(RedisTemplate<String, Object> redisTemplate) {
        return new RedisSessionDAO(redisTemplate);
    }

    /**
     * shiro的统一权限判定
     * 根据业务需要对权限进行拦截或放行, anon：所有请求可访问，  authc: 需要登录认证后才能访问
     */
    @Bean
    public ShiroFilterFactoryBean shiroFilter(SecurityManager securityManager,
                                              SessionManager sessionManager,
                                              RedisTemplate<String, Object> redisTemplate) {
        ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
        shiroFilterFactoryBean.setSecurityManager(securityManager);

        // 配置未登录成功的url
        shiroFilterFactoryBean.setLoginUrl("/dyzb/login");

        // 自定义过滤器
        Map<String, Filter> filters = new HashMap<>();
        filters.put("authc", new ShiroLoginFilter());
        shiroFilterFactoryBean.setFilters(filters);

        // 配置拦截路径
        Map<String, String> filterChainDefinitionMap = new LinkedHashMap<>();
        filterChainDefinitionMap.put("/user/login", "anon");
        filterChainDefinitionMap.put("/actuator/**", "anon");
        filterChainDefinitionMap.put("/doc.html", "anon");//接口文档
        filterChainDefinitionMap.put("/v2/api-docs/**", "anon");
        filterChainDefinitionMap.put("/swagger-resources/**", "anon");
        filterChainDefinitionMap.put("/webjars/**", "anon");
        filterChainDefinitionMap.put("/**", "authc"); // 添加自定义过滤器到过滤链
        shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
        return shiroFilterFactoryBean;
    }

    /**
     * 将自定义授权的 Realm 注入进SecurityManager
     */
    @Bean
    public MyRealm myRealm() {
        MyRealm myRealm = new MyRealm();
        myRealm.setCredentialsMatcher(hashedCredentialsMatcher());
        return myRealm;
    }

    /**
     * 凭证匹配器
     * 可以扩展凭证匹配器，实现 输入密码错误次数后锁定等功能
     */
    @Bean(name = "credentialsMatcher")
    public HashedCredentialsMatcher hashedCredentialsMatcher() {
        HashedCredentialsMatcher hashedCredentialsMatcher = new HashedCredentialsMatcher();
        //散列算法: sha256
        hashedCredentialsMatcher.setHashAlgorithmName(Sha256Hash.ALGORITHM_NAME);
        //散列的次数，比如散列两次，相当于 sha256(sha256(""));
        hashedCredentialsMatcher.setHashIterations(MyRealm.HASH_ITERATIONS);
        //storedCredentialsHexEncoded默认是true，此时用的是密码加密用的是Hex编码；false时用Base64编码
        hashedCredentialsMatcher.setStoredCredentialsHexEncoded(true);
        return hashedCredentialsMatcher;
    }


    /**
     * 将自定义 Realm 注入进SecurityManager
     * Shiro通过SecurityManager来管理内部组件实例，并通过它来提供安全管理的各种服务
     */
    @Bean("securityManager")
    public SecurityManager securityManager(MyRealm myRealm, SessionManager sessionManager) {
        DefaultWebSecurityManager securityManager = new DefaultWebSecurityManager();
        securityManager.setRealm(myRealm);
        securityManager.setSessionManager(sessionManager);
        return securityManager;
    }

    /**
     * 会话管理
     * 默认使用容器session，这里改为自定义session
     * session的全局超时时间默认是30分钟
     *
     * @return
     */
    @Bean
    public DefaultWebSessionManager sessionManager(SimpleCookie simpleCookie, RedisSessionDAO redisSessionDAO) {
        DefaultWebSessionManager sessionManager = new DefaultWebSessionManager();
        // 默认使用 MemerySessionDao，可以设置为 EnterpriseCacheSessionDAO 以配合ehcache实现分布式集群缓存支持
        sessionManager.setSessionDAO(redisSessionDAO);
        //sessionManager.setSessionDAO(new EnterpriseCacheSessionDAO());
        //sessionManager.setSessionListeners(Collections.singleton(mySessionListener));
        // 会话超时时间，单位：毫秒
        sessionManager.setGlobalSessionTimeout(60 * 60 * 1000);
        // 定时清理失效会话, 清理用户直接关闭浏览器造成的孤立会话
        sessionManager.setSessionValidationInterval(60 * 60 * 1000);
        // 是否开启定时清理失效会话
        sessionManager.setSessionValidationSchedulerEnabled(true);
        // 指定sessionid
        sessionManager.setSessionIdCookie(simpleCookie);
        // 是否允许将 sessionId 放到 cookie 中
        sessionManager.setSessionIdCookieEnabled(true);
        // 是否允许将 sessionId 放到 Url 地址拦中
        sessionManager.setSessionIdUrlRewritingEnabled(false);
        return sessionManager;
    }

    /**
     * 指定本系统sessionid, 问题: 与servlet容器名冲突, 如jetty, tomcat 等默认jsessionid,
     * 当跳出shiro servlet时如error-page容器会为jsessionid重新分配值导致登录会话丢失!
     *
     * @return
     */
    @Bean
    public SimpleCookie sessionIdCookie() {
        SimpleCookie simpleCookie = new SimpleCookie("JSESSIONID");
        // 防止xss攻击，窃取cookie内容
        simpleCookie.setHttpOnly(true);
//        simpleCookie.setMaxAge(60 * 60 * 24 * 7); // 7天, 单位：秒
        simpleCookie.setMaxAge(-1); //单位：秒
        return simpleCookie;
    }

}
