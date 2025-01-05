package org.hae.server.domain.user.controller;

import lombok.RequiredArgsConstructor;
import org.hae.server.domain.user.dto.request.UserLoginRequest;
import org.hae.server.domain.user.service.UserService;
import org.hae.server.global.common.response.SuccessType;
import org.hae.server.global.common.response.SFaaSResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/user/login")
    public ResponseEntity<SFaaSResponse<?>> getUserInfo(@RequestBody UserLoginRequest request) {
        return ResponseEntity.status(HttpStatus.OK).body(SFaaSResponse.success(SuccessType.OK, userService.getLoginInfo(request)));
    }

}