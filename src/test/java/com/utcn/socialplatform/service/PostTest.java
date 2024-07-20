package com.utcn.socialplatform.service;

import com.utcn.socialplatform.controller.PostController;
import com.utcn.socialplatform.dtos.CreatePostDTO;
import com.utcn.socialplatform.dtos.FriendPostDTO;
import com.utcn.socialplatform.dtos.GetPostDTO;
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
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class PostTest {

    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreatePost_Success() throws ApiExceptionResponse {
        // Arrange
        CreatePostDTO createPostDTO = new CreatePostDTO();
        doNothing().when(postService).createPost(createPostDTO);

        // Act
        ResponseEntity<?> response = postController.createPost(createPostDTO);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post created successfully", response.getBody());
    }

    @Test
    public void testCreatePost_Exception() throws ApiExceptionResponse {
        // Arrange
        CreatePostDTO createPostDTO = new CreatePostDTO();
        doThrow(ApiExceptionResponse.builder()
                .status(HttpStatus.BAD_REQUEST)
                .message("The user is not validated")
                .build()).when(postService).createPost(createPostDTO);

        // Act
        ResponseEntity<?> response = postController.createPost(createPostDTO);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("The user is not validated", response.getBody());
    }

    @Test
    public void testGetPosts() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        List<GetPostDTO> posts = Collections.emptyList();
        when(postService.getPosts(email)).thenReturn(posts);

        // Act
        ResponseEntity<List<GetPostDTO>> response = postController.getPosts(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }

    @Test
    public void testPatchPost_Success() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        Map<String, String> updateData = Map.of("description", "Updated description");
        doNothing().when(postService).modifyPost(postId, "Updated description");

        // Act
        ResponseEntity<?> response = postController.patchPost(postId, updateData);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post with id " + postId + " has been updated successfully.", response.getBody());
    }

    @Test
    public void testPatchPost_Exception() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        Map<String, String> updateData = Map.of("description", "Updated description");
        doThrow(ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Post with id: 1 does not exist")
                .build()).when(postService).modifyPost(postId, "Updated description");

        // Act
        ResponseEntity<?> response = postController.patchPost(postId, updateData);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Post with id: 1 does not exist", response.getBody());
    }

    @Test
    public void testDeletePost_Success() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        doNothing().when(postService).deletePost(postId);

        // Act
        ResponseEntity<String> response = postController.deletePost(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post with id " + postId + " has been deleted successfully.", response.getBody());
    }

    @Test
    public void testDeletePost_Exception() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        doThrow(ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Post with id: 1 not found")
                .build()).when(postService).deletePost(postId);

        // Act
        ResponseEntity<String> response = postController.deletePost(postId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Post with id: 1 not found", response.getBody());
    }

    @Test
    public void testGetPostsForAllUsers() {
        // Arrange
        List<GetPostDTO> posts = Collections.emptyList();
        when(postService.getAllPosts()).thenReturn(posts);

        // Act
        ResponseEntity<List<GetPostDTO>> response = postController.getPosts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }

    @Test
    public void testBlockPost_Success() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        doNothing().when(postService).blockPost(postId);

        // Act
        ResponseEntity<String> response = postController.blockPost(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Post with id " + postId + " has been blocked successfully.", response.getBody());
    }

    @Test
    public void testBlockPost_Exception() throws ApiExceptionResponse {
        // Arrange
        Long postId = 1L;
        doThrow(ApiExceptionResponse.builder()
                .status(HttpStatus.NOT_FOUND)
                .message("Post with id: 1 not found")
                .build()).when(postService).blockPost(postId);

        // Act
        ResponseEntity<String> response = postController.blockPost(postId);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Post with id: 1 not found", response.getBody());
    }

    @Test
    public void testGetFriendsPosts_Success() throws ApiExceptionResponse {
        // Arrange
        String email = "example@example.com";
        List<FriendPostDTO> posts = Collections.emptyList();
        when(postService.getFriendsPosts(email)).thenReturn(posts);

        // Act
        ResponseEntity<?> response = postController.getFriendsPosts(email);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(posts, response.getBody());
    }
}