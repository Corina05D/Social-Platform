package com.utcn.socialplatform.service;

import com.utcn.socialplatform.controller.FriendshipController;
import com.utcn.socialplatform.dtos.FriendshipProposalDTO;
import com.utcn.socialplatform.dtos.FriendshipRequestDTO;
import com.utcn.socialplatform.dtos.SearchUserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class FriendshipTest {

    @Mock
    private FriendshipService friendshipService;

    @InjectMocks
    private FriendshipController friendshipController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSearchUsers_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        List<SearchUserDTO> users = Collections.emptyList();
        when(friendshipService.searchUsers(email)).thenReturn(users);

        // Act
        ResponseEntity<?> response = friendshipController.searchUsers(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(users, response.getBody());
    }

    @Test
    public void testSearchUsers_Exception() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error searching users for " + email)
                .build();
        when(friendshipService.searchUsers(email)).thenThrow(exceptionResponse);

        // Act
        ResponseEntity<?> response = friendshipController.searchUsers(email);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error searching users for " + email, response.getBody());
    }

    @Test
    public void testAddFriendship_Success() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        doNothing().when(friendshipService).addFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.addFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Friendship created successfully", response.getBody());
    }

    @Test
    public void testAddFriendship_Exception() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error creating friendship")
                .build();
        doThrow(exceptionResponse).when(friendshipService).addFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.addFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error creating friendship", response.getBody());
    }

    @Test
    public void testAcceptFriendship_Success() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        doNothing().when(friendshipService).acceptFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.acceptFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Friendship request accepted successfully", response.getBody());
    }

    @Test
    public void testAcceptFriendship_Exception() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error accepting friendship request")
                .build();
        doThrow(exceptionResponse).when(friendshipService).acceptFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.acceptFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error accepting friendship request", response.getBody());
    }

    @Test
    public void testDenyFriendship_Success() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        doNothing().when(friendshipService).denyFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.denyFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Friendship request denied successfully", response.getBody());
    }

    @Test
    public void testDenyFriendship_Exception() throws ApiExceptionResponse {
        // Arrange
        FriendshipRequestDTO friendshipRequestDTO = new FriendshipRequestDTO("sender@example.com", "receiver@example.com");
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error denying friendship request")
                .build();
        doThrow(exceptionResponse).when(friendshipService).denyFriendship(friendshipRequestDTO);

        // Act
        ResponseEntity<?> response = friendshipController.denyFriendship(friendshipRequestDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error denying friendship request", response.getBody());
    }

    @Test
    public void testGetFriendshipRequests_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        List<FriendshipProposalDTO> requests = Collections.emptyList();
        when(friendshipService.searchFriendshipProposals(email)).thenReturn(requests);

        // Act
        ResponseEntity<?> response = friendshipController.getFriendshipRequests(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(requests, response.getBody());
    }

    @Test
    public void testGetFriendshipRequests_Exception() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error retrieving friendship requests")
                .build();
        when(friendshipService.searchFriendshipProposals(email)).thenThrow(exceptionResponse);

        // Act
        ResponseEntity<?> response = friendshipController.getFriendshipRequests(email);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error retrieving friendship requests", response.getBody());
    }

    @Test
    public void testGetFriends_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        List<FriendshipProposalDTO> friends = Collections.emptyList();
        when(friendshipService.searchFriends(email)).thenReturn(friends);

        // Act
        ResponseEntity<?> response = friendshipController.getFriends(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(friends, response.getBody());
    }

    @Test
    public void testGetFriends_Exception() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        ApiExceptionResponse exceptionResponse = ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("Error retrieving friends list")
                .build();
        when(friendshipService.searchFriends(email)).thenThrow(exceptionResponse);

        // Act
        ResponseEntity<?> response = friendshipController.getFriends(email);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Error retrieving friends list", response.getBody());
    }
}