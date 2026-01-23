package com.dikara.user.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class JwtPrincipal {

    private String userId;
    private String username;
    private List<String> roles;
}
