package org.mooc.cloud.service.impl;

import org.mooc.cloud.entity.User;
import org.mooc.cloud.mapper.UserMapper;
import org.mooc.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

//    @Autowired
//    private UserMapper userMapper;

    @Override
    public User getUser(Long id) {
//        return userMapper.findById(id);

        return new User();
    }
}
