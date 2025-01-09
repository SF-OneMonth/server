package org.hae.server.domain.user.dto.request;

public record UserLoginRequest(
        String userEmail,
        String userPwd
) {
}
