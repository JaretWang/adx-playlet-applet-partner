package com.dataeye.partner.security;

import org.apache.shiro.session.Session;
import org.apache.shiro.session.mgt.eis.AbstractSessionDAO;
import org.springframework.data.redis.core.RedisTemplate;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author jaret
 * @date 2024/10/21 17:31
 * @description 使用redis存储和管理会话
 */
public class RedisSessionDAO extends AbstractSessionDAO {

    private final RedisTemplate<String, Object> redisTemplate;

    private static final String SESSION_PREFIX = "dyzb:shiro:session:";

    public static final long EXPIRE_TIME = 60 * 60; // 1 hour

    public RedisSessionDAO(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    private String getKey(Serializable sessionId) {
        return SESSION_PREFIX + sessionId;
    }

    @Override
    protected Serializable doCreate(Session session) {
        Serializable sessionId = this.generateSessionId(session);
        assignSessionId(session, sessionId);

        String key = getKey(sessionId);
        redisTemplate.opsForValue().set(key, session, EXPIRE_TIME, TimeUnit.SECONDS);
        return sessionId;
    }

    @Override
    protected Session doReadSession(Serializable sessionId) {
        return (Session) redisTemplate.opsForValue().get(getKey(sessionId));
    }

    @Override
    public void update(Session session) {
        //会话滑动过期机制：每次会话更新（例如用户发起请求时），只要用户在1小时内持续进行操作，系统会一直延长会话的有效期，用户不会被自动登出。
        String key = getKey(session.getId());
        redisTemplate.opsForValue().set(key, session, EXPIRE_TIME, TimeUnit.SECONDS);
    }

    @Override
    public void delete(Session session) {
        String key = getKey(session.getId());
        redisTemplate.delete(key);
    }

    @Override
    public Collection<Session> getActiveSessions() {
        Set<String> keys = redisTemplate.keys(SESSION_PREFIX + "*");
        return Optional.ofNullable(keys).orElse(new HashSet<>())
                .stream()
                .map(key -> (Session) redisTemplate.opsForValue().get(key))
                .collect(Collectors.toSet());
    }
}
