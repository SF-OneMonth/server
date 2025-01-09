package org.hae.server.domain.user.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.hae.server.domain.user.dto.request.UserLoginRequest;
import org.hae.server.domain.user.model.User;

@Mapper
public interface UserMapper {
    User loginUser(UserLoginRequest request);
}
