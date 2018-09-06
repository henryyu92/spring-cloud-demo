package org.mooc.cloud.feign.impl;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.feign.UserRegistryClient;
import org.springframework.stereotype.Component;

/**
 * Hystrix 熔断机制，当远程调用失败时会调用该类定义的相同的方法
 */
@Component
public class UserRegistryFallback implements UserRegistryClient {
    @Override
    public User getUser(Long id) {
        return null;
    }
}
