package Bookington2.demo.controller;

import Bookington2.demo.dto.auth.LoginRequest;
import Bookington2.demo.dto.request.APIResponse;
import Bookington2.demo.dto.request.UserCreationRequest;
import Bookington2.demo.dto.request.UserUpdateRequest;
import Bookington2.demo.entity.User;
import Bookington2.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/auth")
    ResponseEntity<?> login(@RequestBody LoginRequest request) {
        return userService.authenticateUser(request);

    }

    @PostMapping("/register/players")
    APIResponse<User> createPlayer(@RequestBody @Valid UserCreationRequest request) {

        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.createUser(request, "PLAYER"));
        return apiResponse;
    }

    @PostMapping("/register/owners")
    APIResponse<User> createOwner(@RequestBody @Valid UserCreationRequest request) {

        APIResponse<User> apiResponse = new APIResponse<>();
        apiResponse.setResult(userService.createUser(request, "OWNER"));
        return apiResponse;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") Integer userId) {
        return userService.getUsers(userId);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/users/me")
    User getMyProfile() {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication();
        var userDetails = (Bookington2.demo.service.UserDetailsImpl) authentication.getPrincipal();
        return userService.getUsers(userDetails.getId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("{userId}")
    User updateUser(@PathVariable Integer userId, @RequestBody UserUpdateRequest request) {
        return userService.updateUser(userId, request);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable Integer userId) {
        userService.deleteUser(userId);
        return "User has been deleted";
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/users/me")
    User updateMyProfile(@RequestBody UserUpdateRequest request) {
        var authentication = org.springframework.security.core.context.SecurityContextHolder.getContext()
                .getAuthentication();
        var userDetails = (Bookington2.demo.service.UserDetailsImpl) authentication.getPrincipal();
        return userService.updateUser(userDetails.getId(), request);
    }
}