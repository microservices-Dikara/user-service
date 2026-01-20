package com.dikara.cruds.service;

import com.dikara.cruds.dto.request.UserRequest;
import com.dikara.cruds.dto.response.UserResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;


public interface UserService {

    List<UserResponse> findAll();
    UserResponse createUser(UserRequest user);
    Map<String, String> deleteUser(String id);
    UserResponse updateUser(String id, UserRequest user);

}
