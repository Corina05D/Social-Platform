package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.PersonalDetailsDTO;
import com.utcn.socialplatform.dtos.UpdatePersonalDetailsDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.mappers.PersonalDetailsMapper;
import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.PhotoRepository;
import com.utcn.socialplatform.service.PersonalDetailsService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Objects;

@Service
public class PersonalDetailsServiceImpl implements PersonalDetailsService {
    private final PersonalDetailsRepository personalDetailsRepository;
    private final PhotoRepository photoRepository;

    public PersonalDetailsServiceImpl(PersonalDetailsRepository personalDetailsRepository, PhotoRepository photoRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public PersonalDetailsDTO getPersonalDetails(String email) throws ApiExceptionResponse {
        PersonalDetails personalDetails = personalDetailsRepository.findById(email)
                .orElseThrow(() ->
                        ApiExceptionResponse.builder()
                                .errors(Collections.singletonList("UserDetails for " + email + " does not exist"))
                                .message("UserDetails for " + email + " does not exist")
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );

        PersonalDetailsDTO personalDetailsDTO = PersonalDetailsMapper.toDTO(personalDetails);

        if (personalDetails.getProfilePicture() == null) {
            personalDetailsDTO.setProfilePicture(null);
        }

        return personalDetailsDTO;
    }

    @Override
    public void updatePersonalDetails(String email, UpdatePersonalDetailsDTO personalDetailsDTO) throws ApiExceptionResponse{
        PersonalDetails personalDetails = personalDetailsRepository.findById(email)
                .orElseThrow(() ->
                        ApiExceptionResponse.builder()
                                .errors(Collections.singletonList("Personal details for " + email + " do not exist"))
                                .message("Personal details for " + email + " do not exist")
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
        if (!Objects.equals(personalDetails.getBio(), personalDetailsDTO.getBio())) {
            personalDetails.setBio(personalDetailsDTO.getBio());
        }
        if (!Objects.equals(personalDetails.getHome(), personalDetailsDTO.getHome())) {
            personalDetails.setHome(personalDetailsDTO.getHome());
        }
        if (!Objects.equals(personalDetails.getBirthplace(), personalDetailsDTO.getBirthplace())) {
            personalDetails.setBirthplace(personalDetailsDTO.getBirthplace());
        }
        if (!Objects.equals(personalDetails.getStudy(), personalDetailsDTO.getStudy())) {
            personalDetails.setStudy(personalDetailsDTO.getStudy());
        }
        if (!Objects.equals(personalDetails.getWork(), personalDetailsDTO.getWork())) {
            personalDetails.setWork(personalDetailsDTO.getWork());
        }

        personalDetailsRepository.save(personalDetails);
 }

    @Override
    public void updateProfilePicture(String email, String profilePictureName) throws ApiExceptionResponse {
        PersonalDetails personalDetails = personalDetailsRepository.findById(email)
                .orElseThrow(() ->
                        ApiExceptionResponse.builder()
                                .errors(Collections.singletonList("Personal details for " + email + " do not exist"))
                                .message("Personal details for " + email + " do not exist")
                                .status(HttpStatus.BAD_REQUEST)
                                .build()
                );
        if (profilePictureName != null && !profilePictureName.isEmpty()) {
            Photo photo = photoRepository.findByNameAndUser(profilePictureName, personalDetails.getUser());
            if (photo != null) {
                personalDetails.setProfilePicture(photo);
            } else {
                Photo newPhoto = new Photo();
                newPhoto.setName(profilePictureName);
                newPhoto.setUser(personalDetails.getUser());
                photoRepository.save(newPhoto);
                personalDetails.setProfilePicture(newPhoto);
            }
        } else {
            personalDetails.setProfilePicture(null);
        }
        personalDetailsRepository.save(personalDetails);
    }
}