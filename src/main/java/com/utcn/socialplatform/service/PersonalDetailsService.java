package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.PersonalDetailsDTO;
import com.utcn.socialplatform.dtos.UpdatePersonalDetailsDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

@Component
public interface PersonalDetailsService {
    PersonalDetailsDTO getPersonalDetails(String email) throws ApiExceptionResponse;
    void updatePersonalDetails(String email, UpdatePersonalDetailsDTO updatePersonalDetailsDTO) throws ApiExceptionResponse;
    void updateProfilePicture(String email, String profilePictureName) throws ApiExceptionResponse;
}