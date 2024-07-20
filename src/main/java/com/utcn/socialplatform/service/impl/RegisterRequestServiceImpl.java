package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.RegisterRequestDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.MailService;
import com.utcn.socialplatform.service.RegisterRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RegisterRequestServiceImpl implements RegisterRequestService {
    private final UserRepository userRepository;
    private final MailService mailService;
    private final PersonalDetailsRepository personalDetailsRepository;

    @Autowired
    public RegisterRequestServiceImpl(UserRepository userRepository, MailService mailService, PersonalDetailsRepository personalDetailsRepository) {
        this.userRepository = userRepository;
        this.mailService = mailService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    @Override
    public void approveUser(String email) throws ApiExceptionResponse {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email " + email + " does not exist"))
                    .message("Email " + email + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else {
            try {
                User user = userOptional.get();
                user.setIsValidated(true);
                userRepository.save(user);
                mailService.sendEmail(user.getEmail(),"Request accepted","Your account has been validated.");
            } catch (Exception e) {
                throw ApiExceptionResponse.builder()
                        .errors(Collections.singletonList(e.getMessage()))
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
    }

    @Override
    public void denyUser(String email, String reason) throws ApiExceptionResponse {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email " + email + " does not exist"))
                    .message("Email " + email + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        } else if(userOptional.get().getIsValidated()==Boolean.TRUE){
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("The user is already validated"))
                    .message("The user is already validated")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }else{
            try {
                User user = userOptional.get();
                mailService.sendEmail(user.getEmail(),"Request deny","Cererea ta a fost respinsa din urmatorul motiv: " + reason);
                PersonalDetails personalDetails=personalDetailsRepository.findByEmail(user.getEmail());
                personalDetailsRepository.delete(personalDetails);
                userRepository.delete(user);
            } catch (Exception e) {
                throw ApiExceptionResponse.builder()
                        .errors(Collections.singletonList(e.getMessage()))
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
    }

    @Override
    public List<RegisterRequestDTO> getUnvalidatedUser(){
        List<User> unvalidatedUsers = userRepository.findByIsValidatedFalse();
        if (unvalidatedUsers.isEmpty()) {
            return Collections.emptyList();
        } else {
            List<RegisterRequestDTO> unvalidatedUserDTOs = unvalidatedUsers.stream()
                    .map(user -> new RegisterRequestDTO(user.getEmail(), user.getFirstName(), user.getLastName(), user.getRegistrationDate()))
                    .collect(Collectors.toList());
            return unvalidatedUserDTOs;
        }
    }

    @Override
    public void resetPassword(String email) throws ApiExceptionResponse {
        Optional<User> userOptional = userRepository.findById(email);
        if (userOptional.isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email " + email + " does not exist"))
                    .message("Email " + email + " does not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }else if(userOptional.get().getIsValidated()==Boolean.FALSE){
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("The user is not validated"))
                    .message("The user is not validated")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }else {
            try {
                User user = userOptional.get();
                String uuid = UUID.randomUUID().toString();
                String password=uuid.substring(0, 10);
                mailService.sendEmail(user.getEmail(),"New password","Your new password is: "+ password);
                user.setPassword(password);
                userRepository.save(user);
            } catch (Exception e) {
                throw ApiExceptionResponse.builder()
                        .errors(Collections.singletonList(e.getMessage()))
                        .message(e.getMessage())
                        .status(HttpStatus.BAD_REQUEST)
                        .build();
            }
        }
    }

}