package org.hae.server.domain.user.dto.response;

import org.hae.server.domain.user.model.User;

public record UserInfoResponse(
        Long userId,
        String userEmail
) {
    public static UserInfoResponse of(User user) {
        return new UserInfoResponse(
                user.getUserId(),
                user.getUserEmail()
        );
    }
}
