package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.CreatePostDTO;
import com.utcn.socialplatform.dtos.FriendPostDTO;
import com.utcn.socialplatform.dtos.GetPostDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PostService {
    void createPost(CreatePostDTO createPostDTO) throws ApiExceptionResponse;
    List<GetPostDTO> getPosts(String idUser) throws ApiExceptionResponse;
    void modifyPost(Long postId, String newDescription) throws ApiExceptionResponse;
    void deletePost(Long postId) throws ApiExceptionResponse;
    void blockPost(Long postId) throws ApiExceptionResponse;
    List<GetPostDTO> getAllPosts();
    List<FriendPostDTO> getFriendsPosts(String email) throws ApiExceptionResponse;;
}