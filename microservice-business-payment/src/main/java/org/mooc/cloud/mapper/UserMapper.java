package org.mooc.cloud.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.mooc.cloud.entity.User;

@Mapper
public interface UserMapper {

    User findById(Long id);
}
