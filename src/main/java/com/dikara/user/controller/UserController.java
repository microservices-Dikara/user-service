package com.dikara.user.controller;

import com.dikara.user.constant.BaseResponse;
import com.dikara.user.dto.request.UserRequest;
import com.dikara.user.dto.response.PaginationResponse;
import com.dikara.user.dto.response.UserResponse;
import com.dikara.user.service.UserService;
import com.dikara.user.util.JwtPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

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

    @PostMapping
    public UserResponse create(@RequestBody UserRequest user){
        return userService.createUser(user);

    }

    @DeleteMapping ("/{id}")
    public Map<String, String> delete (@PathVariable String id){
        return userService.deleteUser(id);
    }

    @PutMapping("/{id}")
    public UserResponse update (@PathVariable String id,@RequestBody UserRequest user){
        return userService.updateUser(id, user);
    }
   // @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public PaginationResponse<UserResponse> findAllUsersWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return userService.findAll(page, size, keyword);
    }

    @GetMapping("/me")
    public UserResponse getCurrentUser(@AuthenticationPrincipal JwtPrincipal principal) {

        UserResponse result = userService.findById(principal.getUserId());
        return result;
    }
}
