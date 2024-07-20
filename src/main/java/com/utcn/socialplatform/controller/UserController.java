package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.UserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.utcn.socialplatform.model.Constants.API_V1;
import static com.utcn.socialplatform.model.Constants.USERS;

@RestController
@RequestMapping(API_V1+USERS)
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("")
    public ResponseEntity<List<UserDTO>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers());
    }

    @DeleteMapping("/{email}")
    public ResponseEntity<String> deleteUser(@PathVariable String email) {
        try {
            userService.deleteUser(email);
            return ResponseEntity.ok("User with email " + email + " has been deleted successfully.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}
