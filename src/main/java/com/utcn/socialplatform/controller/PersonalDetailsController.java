package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.PersonalDetailsDTO;
import com.utcn.socialplatform.dtos.UpdatePersonalDetailsDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.PersonalDetailsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1)
public class PersonalDetailsController {
    private final PersonalDetailsService personalDetailsService;

    public PersonalDetailsController(PersonalDetailsService personalDetailsService) {
        this.personalDetailsService = personalDetailsService;
    }

    @GetMapping(PERSONAL_DETAILS + "/{email}")
    public ResponseEntity<?> getPersonalDetails(@PathVariable String email) {
        try {
            PersonalDetailsDTO personalDetailsDTO = personalDetailsService.getPersonalDetails(email);
            if (personalDetailsDTO != null) {
                return ResponseEntity.ok(personalDetailsDTO);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @PutMapping(PERSONAL_DETAILS + "/{email}")
    public ResponseEntity<?> updatePersonalDetails(@PathVariable String email, @RequestBody UpdatePersonalDetailsDTO personalDetailsDTO) {
        try {
            personalDetailsService.updatePersonalDetails(email, personalDetailsDTO);
            return ResponseEntity.ok("Personal details updated successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @PutMapping(PROFILE_PICTURE + "/{email}")
    public ResponseEntity<?> updateProfilePicture(@PathVariable String email, @RequestParam String profilePictureName) {
        try {
            personalDetailsService.updateProfilePicture(email, profilePictureName);
            return ResponseEntity.ok("Profile picture updated successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }
}