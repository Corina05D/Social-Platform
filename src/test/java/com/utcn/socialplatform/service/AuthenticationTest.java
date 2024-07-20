package com.utcn.socialplatform.service;

import com.utcn.socialplatform.controller.AuthenticationController;
import com.utcn.socialplatform.dtos.LoginDTO;
import com.utcn.socialplatform.dtos.LoginResponseDTO;
import com.utcn.socialplatform.dtos.RegisterDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.verification.VerificationMode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AuthenticationTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private AuthenticationController authenticationController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testRegisterClient() throws ApiExceptionResponse {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();
        doNothing().when(userService).createClient(registerDTO);

        // When
        ResponseEntity<String> responseEntity = authenticationController.registerClient(registerDTO);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals("User has been successfully registered!", responseEntity.getBody());
        verify(userService, times(1)).createClient(registerDTO);
    }

    @Test
    void testRegisterClientFail() throws ApiExceptionResponse {
        // Given
        RegisterDTO registerDTO = new RegisterDTO();
        doThrow(ApiExceptionResponse.builder().errors(Collections.singletonList("User with email: " + registerDTO.getEmail() + " already exists"))
                .message("User with email: " + registerDTO.getEmail() + " already exists").status(HttpStatus.BAD_REQUEST).build()).when(userService).createClient(registerDTO);

        // When
        ApiExceptionResponse exception = org.junit.jupiter.api.Assertions.assertThrows(ApiExceptionResponse.class, () -> authenticationController.registerClient(registerDTO));

        // Then
        assertEquals("User with email: " + registerDTO.getEmail() + " already exists", exception.getMessage());
        verify(userService, times(1)).createClient(registerDTO);
    }

    @Test
    void testLoginSuccess() throws ApiExceptionResponse {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        LoginResponseDTO loginResponseDTO = new LoginResponseDTO();
        when(userService.login(loginDTO)).thenReturn(loginResponseDTO);

        // When
        ResponseEntity<LoginResponseDTO> responseEntity = authenticationController.login(loginDTO);

        // Then
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(loginResponseDTO, responseEntity.getBody());
        verify(userService, times(1)).login(loginDTO);
    }

    @Test
    void testLoginFailInvalidCredentials() throws ApiExceptionResponse {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        when(userService.login(loginDTO)).thenThrow(ApiExceptionResponse.builder().errors(Collections.singletonList("Invalid credentials"))
                .message("Invalid credentials").status(HttpStatus.BAD_REQUEST).build());

        // When
        ApiExceptionResponse exception = org.junit.jupiter.api.Assertions.assertThrows(ApiExceptionResponse.class, () -> authenticationController.login(loginDTO));

        // Then
        assertEquals("Invalid credentials", exception.getMessage());
        verify(userService, times(1)).login(loginDTO);
    }

    @Test
    void testLoginFailNotValidated() throws ApiExceptionResponse {
        // Given
        LoginDTO loginDTO = new LoginDTO();
        when(userService.login(loginDTO)).thenThrow(ApiExceptionResponse.builder().errors(Collections.singletonList("User is not validated"))
                .message("User is not validated").status(HttpStatus.BAD_REQUEST).build());

        // When
        ApiExceptionResponse exception = org.junit.jupiter.api.Assertions.assertThrows(ApiExceptionResponse.class, () -> authenticationController.login(loginDTO));

        // Then
        assertEquals("User is not validated", exception.getMessage());
        verify(userService, times(1)).login(loginDTO);
    }

}
