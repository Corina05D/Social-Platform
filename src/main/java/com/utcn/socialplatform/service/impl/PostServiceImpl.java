package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.CreatePostDTO;
import com.utcn.socialplatform.dtos.FriendPostDTO;
import com.utcn.socialplatform.dtos.FriendshipProposalDTO;
import com.utcn.socialplatform.dtos.GetPostDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.mappers.FriendPostMapper;
import com.utcn.socialplatform.mappers.PostMapper;
import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.model.Post;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.PhotoRepository;
import com.utcn.socialplatform.repository.PostRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.FriendshipService;
import com.utcn.socialplatform.service.PhotoService;
import com.utcn.socialplatform.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;
    private final PhotoService photoService;
    private final FriendshipService friendshipService;
    private final FriendPostMapper friendPostMapper;

    public PostServiceImpl(PostRepository postRepository, UserRepository userRepository, PhotoRepository photoRepository, PhotoService photoService, FriendshipService friendshipService, FriendPostMapper friendPostMapper) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
        this.photoService = photoService;
        this.friendshipService = friendshipService;
        this.friendPostMapper = friendPostMapper;
    }

    @Override
    public void createPost(CreatePostDTO createPostDTO) throws ApiExceptionResponse {
        User user = userRepository.findById(createPostDTO.getIdUser())
                .orElseThrow(() -> ApiExceptionResponse.builder()
                        .errors(Collections.singletonList("The user is not validated"))
                        .message("The user is not validated")
                        .status(HttpStatus.BAD_REQUEST)
                        .build());

        Photo photo = null;
        if (createPostDTO.getPhotoName() != null && !createPostDTO.getPhotoName().isEmpty()) {
            photo = photoRepository.findByNameAndUser(createPostDTO.getPhotoName(), user);
            if (photo == null) {
                photo = new Photo();
                photo.setName(createPostDTO.getPhotoName());
                photo.setUser(user);
                photoRepository.save(photo);
            }
        }

        Post post = new Post();
        post.setUser(user);
        post.setPhoto(photo);
        post.setDescription(createPostDTO.getDescription());
        post.setTimestamp(LocalDateTime.now());
        post.setBlocked(Boolean.FALSE);

        postRepository.save(post);
    }

    @Override
    public List<GetPostDTO> getPosts(String idUser) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent()){
            return PostMapper.toDTOList(postRepository.findAllByUser(user.get()));
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + idUser + " does not exist"))
                    .message("User with id: " + idUser + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public void modifyPost(Long postId, String newDescription) throws ApiExceptionResponse {
        Optional<Post> optionalPost = postRepository.findById(postId);
        if (optionalPost.isPresent()) {
            Post post = optionalPost.get();
            post.setDescription(newDescription);
            postRepository.save(post);
        } else {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Post with id: " + postId + " does not exist"))
                    .message("Post with id: " + postId + " does not exist")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public void deletePost(Long postId) throws ApiExceptionResponse {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            if(postOptional.get().getPhoto()!=null) photoService.deletePhoto(postOptional.get().getPhoto().getId());
            postRepository.delete(postOptional.get());
        } else {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Post with id: " + postId + " not found"))
                    .message("Post with id: " + postId + " not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public void blockPost(Long postId) throws ApiExceptionResponse {
        Optional<Post> postOptional = postRepository.findById(postId);
        if (postOptional.isPresent()) {
            Post post = postOptional.get();
            post.setBlocked(Boolean.TRUE);
            postRepository.save(post);
        } else {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Post with id: " + postId + " not found"))
                    .message("Post with id: " + postId + " not found")
                    .status(HttpStatus.NOT_FOUND)
                    .build();
        }
    }

    @Override
    public List<GetPostDTO> getAllPosts() {
        return PostMapper.toDTOList(postRepository.findAll());
    }

    @Override
    public List<FriendPostDTO> getFriendsPosts(String email) throws ApiExceptionResponse {
        Optional<User> userOptional = userRepository.findById(email);
        if(userOptional.isPresent()){
            List<FriendPostDTO> friendsPosts = new ArrayList<>();
            List<FriendshipProposalDTO> friends = friendshipService.searchFriends(email);
            if(!friends.isEmpty()){
                for (FriendshipProposalDTO friend : friends){
                    List<Post> p =postRepository.findAllByUser(userRepository.findById(friend.getEmail()).get());
                    for(Post post: p){
                        if(!post.getBlocked()) {
                            FriendPostDTO f = friendPostMapper.toDTO(post);
                            friendsPosts.add(f);
                        }
                    }
                }
                friendsPosts.sort(Comparator.comparing(FriendPostDTO::getTimestamp).reversed());
            }
            return friendsPosts;
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + email + " does not exist"))
                    .message("User with id: " + email + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}