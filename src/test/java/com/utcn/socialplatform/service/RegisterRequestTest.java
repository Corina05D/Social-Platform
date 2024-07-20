package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.RegisterRequestDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.impl.RegisterRequestServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RegisterRequestTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PersonalDetailsRepository personalDetailsRepository;

    @Mock
    private MailService mailService;

    @InjectMocks
    private RegisterRequestServiceImpl registerRequestService;

    @Test
    public void testApproveUserSuccess() throws ApiExceptionResponse {
        // Arrange
        String email = "corina.dragotoniu05@yahoo.com";
        User user = new User();
        user.setEmail(email);
        user.setIsValidated(false);

        // Mock repository behavior
        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act
        assertDoesNotThrow(() -> registerRequestService.approveUser(email));

        // Assert
        assertTrue(user.getIsValidated());
        verify(userRepository, times(1)).save(user);
        verify(mailService, times(1)).sendEmail(eq(email), eq("Request accepted"), eq("Your account has been validated."));
    }

    @Test
    public void testApproveUserUserNotFound() {
        // Arrange
        String email = "nonexistent@example.com";

        // Mock repository behavior
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        ApiExceptionResponse exception = assertThrows(ApiExceptionResponse.class, () -> registerRequestService.approveUser(email));
        assertEquals("Email " + email + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
    }

    @Test
    public void testDenyUserSuccess() throws ApiExceptionResponse {
        // Arrange
        String email = "test@yahoo.com";
        String reason = "Reason for denial";
        User user = new User();
        user.setEmail(email);
        user.setIsValidated(false);

        // Mock repository behavior
        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act
        assertDoesNotThrow(() -> registerRequestService.denyUser(email, reason));

        // Assert
        verify(mailService, times(1)).sendEmail(eq(email), eq("Request deny"), eq("Cererea ta a fost respinsa din urmatorul motiv: " + reason));
        verify(userRepository, times(1)).delete(user);
    }

    @Test
    public void testDenyUserUserNotFound() {
        // Arrange
        String email = "nonexistent@yahoo.com";
        String reason = "Reason for denial";

        // Mock repository behavior
        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        ApiExceptionResponse exception = assertThrows(ApiExceptionResponse.class, () -> registerRequestService.denyUser(email, reason));
        assertEquals("Email " + email + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verifyNoInteractions(mailService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testDenyUserUserAlreadyValidated() {
        // Arrange
        String email = "validated@example.com";
        String reason = "Reason for denial";
        User user = new User();
        user.setEmail(email);
        user.setIsValidated(true);

        // Mock repository behavior
        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act & Assert
        ApiExceptionResponse exception = assertThrows(ApiExceptionResponse.class, () -> registerRequestService.denyUser(email, reason));
        assertEquals("The user is already validated", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verifyNoInteractions(mailService);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testGetUnvalidatedUserNoUnvalidatedUsers() {
        // Arrange
        when(userRepository.findByIsValidatedFalse()).thenReturn(Collections.emptyList());

        // Act
        List<RegisterRequestDTO> result = registerRequestService.getUnvalidatedUser();

        // Assert
        assertTrue(result.isEmpty());
        verify(userRepository, times(1)).findByIsValidatedFalse();
    }

    @Test
    public void testGetUnvalidatedUserHasUnvalidatedUsers() {
        // Arrange
        User user1 = new User();
        user1.setEmail("email1@example.com");
        user1.setFirstName("John");
        user1.setLastName("Doe");
        user1.setRegistrationDate(new Date());
        user1.setIsValidated(false);

        User user2 = new User();
        user2.setEmail("email2@example.com");
        user2.setFirstName("Jane");
        user2.setLastName("Doe");
        user2.setRegistrationDate(new Date());
        user2.setIsValidated(false);

        List<User> unvalidatedUsers = Stream.of(user1, user2).collect(Collectors.toList());

        when(userRepository.findByIsValidatedFalse()).thenReturn(unvalidatedUsers);

        // Act
        List<RegisterRequestDTO> result = registerRequestService.getUnvalidatedUser();

        // Assert
        assertEquals(2, result.size());

        assertEquals("email1@example.com", result.get(0).getEmail());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Doe", result.get(0).getLastName());
        assertEquals(user1.getRegistrationDate(), result.get(0).getRegistrationDate());

        assertEquals("email2@example.com", result.get(1).getEmail());
        assertEquals("Jane", result.get(1).getFirstName());
        assertEquals("Doe", result.get(1).getLastName());
        assertEquals(user2.getRegistrationDate(), result.get(1).getRegistrationDate());

        verify(userRepository, times(1)).findByIsValidatedFalse();
    }

    @Test
    public void testResetPasswordUserExistsAndIsValidated() {
        // Arrange
        String email = "example@example.com";
        User user = new User();
        user.setEmail(email);
        user.setIsValidated(true);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act
        assertDoesNotThrow(() -> registerRequestService.resetPassword(email));

        // Assert
        verify(userRepository, times(1)).findById(email);
        verify(mailService, times(1)).sendEmail(eq(email), eq("New password"), anyString());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    public void testResetPasswordUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findById(email)).thenReturn(Optional.empty());

        // Act & Assert
        ApiExceptionResponse exception = assertThrows(ApiExceptionResponse.class, () -> registerRequestService.resetPassword(email));
        assertEquals("Email " + email + " does not exist", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verifyNoInteractions(mailService);
        verify(userRepository, times(1)).findById(email);
        verifyNoMoreInteractions(userRepository);
    }

    @Test
    public void testResetPasswordUserNotValidated() {
        // Arrange
        String email = "notvalidated@example.com";
        User user = new User();
        user.setEmail(email);
        user.setIsValidated(false);

        when(userRepository.findById(email)).thenReturn(Optional.of(user));

        // Act & Assert
        ApiExceptionResponse exception = assertThrows(ApiExceptionResponse.class, () -> registerRequestService.resetPassword(email));
        assertEquals("The user is not validated", exception.getMessage());
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        verifyNoInteractions(mailService);
        verify(userRepository, times(1)).findById(email);
        verifyNoMoreInteractions(userRepository);
    }
}