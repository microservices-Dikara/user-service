package com.dikara.user.dto.response;

import com.dikara.user.entity.User;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
@Getter
@Setter
public class UserResponse extends DefaultResponse {
    private UUID id;
    private String name;
    private String username;
    private String phoneNumber;
    private String userStatus;
    private String Role;




}
