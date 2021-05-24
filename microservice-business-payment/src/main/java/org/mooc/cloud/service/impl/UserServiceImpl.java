package org.mooc.cloud.service.impl;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.service.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Override
    public User getUser(Long id) {

        return new User();
    }
}
