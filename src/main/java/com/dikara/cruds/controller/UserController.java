package com.dikara.cruds.controller;

import com.dikara.cruds.dto.request.UserRequest;
import com.dikara.cruds.dto.response.PaginationResponse;
import com.dikara.cruds.dto.response.UserResponse;
import com.dikara.cruds.entity.User;
import com.dikara.cruds.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserResponse> findAll(){
        return userService.findAll();
    }


    @PreAuthorize("denyAll()")
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
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/pagination")
    public PaginationResponse<UserResponse> findAllUsersWithPagination(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String keyword) {
        return userService.findAll(page, size, keyword);
    }
}
