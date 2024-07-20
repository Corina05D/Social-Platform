package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.CreatePhotoDTO;
import com.utcn.socialplatform.dtos.PhotoDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.mappers.PhotoMapper;
import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.model.Post;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.PhotoRepository;
import com.utcn.socialplatform.repository.PostRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class PhotoServiceImpl implements PhotoService {
    private final PhotoRepository photoRepository;
    private final UserRepository userRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final PostRepository postRepository;

    public PhotoServiceImpl(PhotoRepository photoRepository, UserRepository userRepository, PersonalDetailsRepository personalDetailsRepository, PostRepository postRepository) {
        this.photoRepository = photoRepository;
        this.userRepository = userRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.postRepository = postRepository;
    }

    public String createPhoto(CreatePhotoDTO photoDTO) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(photoDTO.getIdUser());

        if(user.isPresent()) {
            Photo photo = new Photo(null, photoDTO.getName(), user.get());
            photoRepository.save(photo);
            return "Photo created successfully";
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + photoDTO.getIdUser() + " does not exist"))
                    .message("User with id: " + photoDTO.getIdUser() + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public String deletePhoto(Long id) throws ApiExceptionResponse {
        Optional<Photo> photo = photoRepository.findById(id);

        if(photo.isPresent()) {
            Photo getPhoto = photo.get();
            Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByProfilePictureId(getPhoto.getId());
            if(personalDetailsOptional.isPresent()) {
                PersonalDetails personalDetails = personalDetailsOptional.get();
                personalDetails.setProfilePicture(null);
                personalDetailsRepository.save(personalDetails);
            }
            Optional<Post> postOptional=postRepository.findByPhoto(photo.get());
            if(postOptional.isPresent()) {
                Post post = postOptional.get();
                post.setPhoto(null);
                postRepository.save(post);
            }
            photoRepository.deleteById(id);
            return "Photo deleted successfully";
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Photo with id: " + id + " does not exist"))
                    .message("Photo with id: " + id + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    public List<PhotoDTO> getPhotos(String idUser) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent()){
            return PhotoMapper.toDTOList(photoRepository.findAllByUser(user.get()));
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + idUser + " does not exist"))
                    .message("User with id: " + idUser + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}