package com.sendika.bookstore.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class LoginAttemptService {

    private final StringRedisTemplate redis;
    private final int maxFail;
    private final int lockMinutes;

    public LoginAttemptService(StringRedisTemplate redis,
                               @Value("${app.auth.max-fail}") int maxFail,
                               @Value("${app.auth.lock-minutes}") int lockMinutes) {
        this.redis = redis;
        this.maxFail = maxFail;
        this.lockMinutes = lockMinutes;
    }

    private String failKey(String username) { return "auth:fail:" + username; }
    private String lockKey(String username) { return "auth:lock:" + username; }

    public boolean isLocked(String username) {
        Boolean exists = redis.hasKey(lockKey(username));
        return Boolean.TRUE.equals(exists);
    }

    public long lockTtlSeconds(String username) {
        Long ttl = redis.getExpire(lockKey(username), TimeUnit.SECONDS);
        return ttl == null ? -1 : ttl;
    }

    public void onSuccess(String username) {
        redis.delete(failKey(username));
        redis.delete(lockKey(username));
    }

    public AttemptState onFailure(String username) {
        String fKey = failKey(username);
        Long count = redis.opsForValue().increment(fKey);
        if (count != null && count == 1L) {
            redis.expire(fKey, Duration.ofMinutes(lockMinutes));
        }
        if (count != null && count >= maxFail) {
            String lKey = lockKey(username);
            redis.opsForValue().set(lKey, "1", Duration.ofMinutes(lockMinutes));
            redis.delete(fKey);
            long ttl = Optional.ofNullable(redis.getExpire(lKey, TimeUnit.SECONDS))
                    .orElse((long) lockMinutes * 60);
            return new AttemptState(maxFail, true, ttl);
        }
        long fTtl = Optional.ofNullable(redis.getExpire(fKey, TimeUnit.SECONDS))
                .orElse((long) lockMinutes * 60);
        return new AttemptState(count == null ? 0 : count, false, fTtl);
    }

    public record AttemptState(long failCount, boolean locked, long ttlSeconds) { }
}
