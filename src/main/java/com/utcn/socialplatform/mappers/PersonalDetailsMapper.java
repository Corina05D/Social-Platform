package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.PersonalDetailsDTO;
import com.utcn.socialplatform.model.PersonalDetails;

public class PersonalDetailsMapper {

    public static PersonalDetailsDTO toDTO(PersonalDetails personalDetails) {
        PersonalDetailsDTO dto = new PersonalDetailsDTO();
        dto.setEmail(personalDetails.getEmail());
        dto.setBio(personalDetails.getBio());
        dto.setHome(personalDetails.getHome());
        dto.setBirthplace(personalDetails.getBirthplace());
        dto.setStudy(personalDetails.getStudy());
        dto.setWork(personalDetails.getWork());
        if (personalDetails.getProfilePicture() != null) {
            dto.setProfilePicture(PhotoMapper.toDTO(personalDetails.getProfilePicture()));
        } else {
            dto.setProfilePicture(null);
        }
        return dto;
    }

}