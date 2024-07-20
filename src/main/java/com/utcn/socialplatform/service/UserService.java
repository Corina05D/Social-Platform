package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.LoginDTO;
import com.utcn.socialplatform.dtos.LoginResponseDTO;
import com.utcn.socialplatform.dtos.RegisterDTO;
import com.utcn.socialplatform.dtos.UserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface UserService {
    void createClient(RegisterDTO user) throws ApiExceptionResponse;
    LoginResponseDTO login(LoginDTO loginDTO) throws ApiExceptionResponse;
    List<String> getIdUsers();
    List<UserDTO> getUsers();
    void deleteUser(String email) throws ApiExceptionResponse;
}