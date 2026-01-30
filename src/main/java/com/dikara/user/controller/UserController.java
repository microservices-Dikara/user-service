package com.dikara.user.controller;

import com.dikara.user.constant.BaseResponse;
import com.dikara.user.dto.request.UserRequest;
import com.dikara.user.dto.response.PaginationResponse;
import com.dikara.user.dto.response.UserResponse;
import com.dikara.user.service.UserService;
import com.dikara.user.util.JwtPrincipal;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Tag(name = "User", description = "User management API")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @Operation(summary = "Get all users")
    @GetMapping("/getAll")
   // @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll(  @RequestHeader("X-User-Id") String userId,
                                        @RequestHeader("X-Username") String username){

        System.out.println(  "Hello " + username + " (" + userId + ")");
        return userService.findAll();
    }


    //@PreAuthorize("denyAll()")
    @GetMapping("/test-deny")
    public String test() {
        return "SHOULD NEVER SHOW";
    }

    @Operation(summary = "Create users")
    @PostMapping
    public UserResponse create(@RequestBody UserRequest user){
        return userService.createUser(user);

    }

    @Operation(summary = "Delete user by Id")
    @DeleteMapping ("/{id}")
    public Map<String, String> delete (@PathVariable String id){
        return userService.deleteUser(id);
    }

    @Operation(summary = "Update user by Id")
    @PutMapping("/{id}")
    public UserResponse update (@PathVariable String id,@RequestBody UserRequest user){
        return userService.updateUser(id, user);
    }
   // @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all users with pagination format")
    @GetMapping("/pagination")
    public PaginationResponse<UserResponse> findAllUsersWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return userService.findAll(page, size, keyword);
    }

    @Operation(summary = "Get all users with pagination format")
    @GetMapping("/me")
    public UserResponse getCurrentUser(@RequestHeader("X-User-Id") String userId) {

        UserResponse result = userService.findById(userId);
        return result;
    }
}
