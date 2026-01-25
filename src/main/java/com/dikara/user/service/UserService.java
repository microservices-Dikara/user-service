package com.dikara.user.service;

import com.dikara.user.dto.request.UserRequest;
import com.dikara.user.dto.response.PaginationResponse;
import com.dikara.user.dto.response.UserResponse;


import java.util.List;
import java.util.Map;


public interface UserService {

    List<UserResponse> findAll();
    UserResponse createUser(UserRequest user);
    Map<String, String> deleteUser(String id);
    UserResponse updateUser(String id, UserRequest user);
    PaginationResponse<UserResponse> findAll(int page, int size, String keyword);
    UserResponse findById(String userId);
}
