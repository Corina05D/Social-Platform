package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.CreatePostDTO;
import com.utcn.socialplatform.dtos.GetPostDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.PostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1+POSTS)
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<?> createPost(@RequestBody CreatePostDTO createPostDTO) {
        try {
            postService.createPost(createPostDTO);
            return ResponseEntity.ok("Post created successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<GetPostDTO>> getPosts(@PathVariable String email) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getPosts(email));
    }

    @PatchMapping("/{post_id}")
    public ResponseEntity<?> patchPost(@PathVariable("post_id") Long postId, @RequestBody Map<String, String> updateData) {
        try {
            String newDescription = updateData.get("description");
            postService.modifyPost(postId, newDescription);
            return ResponseEntity.ok("Post with id " + postId + " has been updated successfully.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @DeleteMapping("/{post_id}")
    public ResponseEntity<String> deletePost(@PathVariable("post_id") Long postId) {
        try {
            postService.deletePost(postId);
            return ResponseEntity.ok("Post with id " + postId + " has been deleted successfully.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping("")
    public ResponseEntity<List<GetPostDTO>> getPosts() {
        return ResponseEntity.status(HttpStatus.OK).body(postService.getAllPosts());
    }

    @PutMapping("/{post_id}")
    public ResponseEntity<String> blockPost(@PathVariable() Long post_id) {
        try {
            postService.blockPost(post_id);
            return ResponseEntity.ok("Post with id " + post_id + " has been blocked successfully.");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @GetMapping(FRIENDS+"/{email}")
    public ResponseEntity<?> getFriendsPosts(@PathVariable("email") String email) throws ApiExceptionResponse{
        return ResponseEntity.status(HttpStatus.OK).body(postService.getFriendsPosts(email));
    }
}