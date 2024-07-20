package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.PersonalDetails;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface PersonalDetailsRepository extends CrudRepository<PersonalDetails, String> {
    Optional<PersonalDetails> findByProfilePictureId(Long id);
    PersonalDetails findByEmail(String email);
}