package org.mooc.cloud.mapper;

import org.mooc.cloud.entity.User;

/**
 * 默认内存作为存储
 */
public class DefaultMapperImpl implements UserMapper {
    @Override
    public User findById(Long id) {
        return null;
    }
}
