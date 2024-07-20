package com.utcn.socialplatform.service;

import com.utcn.socialplatform.controller.PersonalDetailsController;
import com.utcn.socialplatform.dtos.PersonalDetailsDTO;
import com.utcn.socialplatform.dtos.UpdatePersonalDetailsDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PersonalDetailsTest {

    @Mock
    private PersonalDetailsService personalDetailsService;

    @InjectMocks
    private PersonalDetailsController personalDetailsController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetPersonalDetails_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        PersonalDetailsDTO personalDetailsDTO = new PersonalDetailsDTO(); // Mock personal details DTO
        when(personalDetailsService.getPersonalDetails(email)).thenReturn(personalDetailsDTO);

        // Act
        ResponseEntity<?> response = personalDetailsController.getPersonalDetails(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(personalDetailsDTO, response.getBody());
    }

    @Test
    public void testGetPersonalDetails_NotFound() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        when(personalDetailsService.getPersonalDetails(email)).thenReturn(null);

        // Act
        ResponseEntity<?> response = personalDetailsController.getPersonalDetails(email);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    public void testUpdatePersonalDetails_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        UpdatePersonalDetailsDTO personalDetailsDTO = new UpdatePersonalDetailsDTO(); // Mock update personal details DTO
        doNothing().when(personalDetailsService).updatePersonalDetails(email, personalDetailsDTO);

        // Act
        ResponseEntity<?> response = personalDetailsController.updatePersonalDetails(email, personalDetailsDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Personal details updated successfully", response.getBody());
    }

    @Test
    public void testUpdatePersonalDetails_Exception() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        UpdatePersonalDetailsDTO personalDetailsDTO = new UpdatePersonalDetailsDTO(); // Mock update personal details DTO
        ApiExceptionResponse apiExceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error updating personal details for " + email)
                .build();
        doThrow(apiExceptionResponse).when(personalDetailsService).updatePersonalDetails(email, personalDetailsDTO);

        // Act
        ResponseEntity<?> response = personalDetailsController.updatePersonalDetails(email, personalDetailsDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error updating personal details for " + email, response.getBody());
    }

    @Test
    public void testUpdateProfilePicture_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        String profilePictureName = "newProfilePic.jpg";
        doNothing().when(personalDetailsService).updateProfilePicture(email, profilePictureName);

        // Act
        ResponseEntity<?> response = personalDetailsController.updateProfilePicture(email, profilePictureName);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Profile picture updated successfully", response.getBody());
    }

    @Test
    public void testUpdateProfilePicture_Exception() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        String profilePictureName = "newProfilePic.jpg";
        ApiExceptionResponse apiExceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error updating profile picture for " + email)
                .build();
        doThrow(apiExceptionResponse).when(personalDetailsService).updateProfilePicture(email, profilePictureName);

        // Act
        ResponseEntity<?> response = personalDetailsController.updateProfilePicture(email, profilePictureName);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error updating profile picture for " + email, response.getBody());
    }
}