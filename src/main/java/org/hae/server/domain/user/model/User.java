package org.hae.server.domain.user.model;

import lombok.Data;

@Data
public class User {
    private Long userId;
    private String userPwd;
    private String userEmail;
}
