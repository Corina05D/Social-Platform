package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.LoginDTO;
import com.utcn.socialplatform.dtos.LoginResponseDTO;
import com.utcn.socialplatform.dtos.RegisterDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1 + AUTHENTICATION)
public class AuthenticationController {

    private final UserService userService;

    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }


    @PostMapping(REGISTER)
    public ResponseEntity<String> registerClient(@RequestBody RegisterDTO registerDTO) throws ApiExceptionResponse {
        userService.createClient(registerDTO);
        return ResponseEntity.status(HttpStatus.OK).body("User has been successfully registered!");
    }

    @PostMapping(LOGIN)
    public ResponseEntity<LoginResponseDTO> login(@RequestBody LoginDTO loginDTO) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(userService.login(loginDTO));
    }
}