package com.dikara.user.service.impl;

import com.dikara.user.constant.GlobalMessage;
import com.dikara.user.constant.Status;
import com.dikara.user.dto.request.UserRequest;
import com.dikara.user.dto.response.PaginationResponse;
import com.dikara.user.dto.response.UserResponse;
import com.dikara.user.entity.Role;
import com.dikara.user.entity.User;
import com.dikara.user.exception.BusinessException;
import com.dikara.user.repository.UserRepository;
import com.dikara.user.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class UserServiceImpl implements UserService {

private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> findAll() {
       return userRepository.findAll().stream().map(this::mapToResponse).toList();

    }

    @Override
    @Transactional
    public UserResponse createUser(UserRequest user) {

        String hashedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(hashedPassword);
        User userResult = userRepository.save(mapToEntity(user));
        return mapToResponse(userResult);
    }



    @Override
    public Map<String, String> deleteUser(String id) {
        User userResult = userRepository.findById(UUID.fromString(id)).orElse(null);
        if (userResult != null) {
            userRepository.deleteById(UUID.fromString(id));
            Map<String, String> response = new HashMap<>();
            response.put("status", "success");
            response.put("message", "User " + id + " has been deleted successfully");
            return response;
        } else {
            log.warn("User not found with code: {}", id);
            throw new BusinessException(
                    GlobalMessage.DATA_NOT_FOUND,
                    "user not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

    }

    @Override
    public UserResponse updateUser(String id, UserRequest user) {
        User userResult = userRepository.findById(UUID.fromString(id)).orElse(null);

        if (userResult == null){
            log.warn("User not found with code: {}", id);
            throw new BusinessException(
                    GlobalMessage.DATA_NOT_FOUND,
                    "user not found with id: " + id,
                    HttpStatus.NOT_FOUND
            );
        }

       updateMapToEntity(user, userResult);
        userResult =userRepository.save(userResult);
        return mapToResponse(userResult);
    }

    @Override
    public PaginationResponse<UserResponse> findAll(int page, int size, String keyword) {
        int zeroBasedPage = Math.max(0, page - 1);
        Pageable pageable = PageRequest.of(zeroBasedPage, size);

        Page<User> centerPage;
        if (keyword != null && !keyword.trim().isEmpty()) {
            centerPage= userRepository.findByKeywordOrderByCreatedDateDesc(keyword, pageable);
        }else{
            centerPage =    userRepository.findByIsDeletedFalseOrderByCreatedDateDesc(pageable);
        }
        Page<UserResponse> mappedPage = centerPage.map(this::mapToResponse);
        return new PaginationResponse<>(mappedPage);
    }

    @Override
    public UserResponse findById(String userId) {
        UUID id = UUID.fromString(userId);
        Optional<User> user =userRepository.findById(id);
        User userObject =  user.get();

        return mapToResponse(userObject);
    }

    private UserResponse mapToResponse (User user){
        return UserResponse.builder()
                .userStatus(user.getUserStatus())
                .id(user.getId())
                .name(user.getName())
                .phoneNumber(user.getPhoneNumber())
                .username(user.getUsername())
                .createdBy(user.getCreatedBy())
                .createdDate(user.getCreatedDate())
                .updatedBy(user.getUpdatedBy())
                .updatedDate(user.getUpdatedDate())
                .Role(user.getRole().toString())
                .build();

    }

    private User mapToEntity (UserRequest userRequest){
        LocalDateTime now = LocalDateTime.now();
        String status =String.valueOf(Status.userStatus.ACTIVE);


        return User.builder()
                .email(userRequest.getEmail())
                .password(userRequest.getPassword())
                .name(userRequest.getName())
                .phoneNumber(userRequest.getPhoneNumber())
                .username(userRequest.getUsername())
                .userStatus(status)
                .createdBy("Admin")
                .updatedBy("Admin")
                .createdDate(now)
                .updatedDate(now)
                .role(Role.USER)
                .build();
    }


    private void updateMapToEntity (UserRequest userRequest, User user){
        LocalDateTime now = LocalDateTime.now();

        user.setEmail(userRequest.getEmail());
        user.setUserStatus(userRequest.getStatus());
        user.setPassword(userRequest.getPassword());
        user.setName(userRequest.getName());
        user.setPhoneNumber(userRequest.getPhoneNumber());
        user.setUsername(userRequest.getUsername());
        user.setUpdatedBy("Admin");
        user.setUpdatedDate(now);
        user.setRole(Role.USER);




    }
}
