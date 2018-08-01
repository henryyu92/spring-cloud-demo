package org.mooc.cloud.feign.impl;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.feign.UserRegistryClient;
import org.springframework.stereotype.Component;

@Component
public class UserRegistryFallback implements UserRegistryClient {
    @Override
    public User getUser(Long id) {
        return null;
    }
}
