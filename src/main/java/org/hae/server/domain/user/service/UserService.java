package org.hae.server.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.user.dto.request.UserLoginRequest;
import org.hae.server.domain.user.dto.response.UserInfoResponse;
import org.hae.server.domain.user.mapper.UserMapper;
import org.hae.server.domain.user.model.User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    @Transactional
    public UserInfoResponse getLoginInfo(final UserLoginRequest request) {
        User user = userMapper.loginUser(request);
        return UserInfoResponse.of(user);
    }

}
