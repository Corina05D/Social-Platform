package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.LoginDTO;
import com.utcn.socialplatform.dtos.LoginResponseDTO;
import com.utcn.socialplatform.dtos.UserDTO;
import com.utcn.socialplatform.mappers.UserMapper;
import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.Role;
import com.utcn.socialplatform.dtos.RegisterDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.security.JwtService;
import com.utcn.socialplatform.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final JwtService jwtService;

    public UserServiceImpl(UserRepository userRepository, PersonalDetailsRepository personalDetailsRepository, JwtService jwtService) {
        this.userRepository = userRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.jwtService = jwtService;
    }


    @Override
    public void createClient(RegisterDTO newUser) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(newUser.getEmail());

        if (user.isPresent()==Boolean.TRUE) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with email: " + newUser.getEmail() + " already exists"))
                    .message("User with email: " + newUser.getEmail() + " already exists").status(HttpStatus.BAD_REQUEST).build();
        } else {
            try{
                User actualUser = new User(
                        newUser.getEmail(),
                        newUser.getPassword(),
                        newUser.getFirstName(),
                        newUser.getLastName(),
                        Role.CLIENT,
                        Boolean.FALSE,
                        newUser.getBirthDate(),
                        new Date()
                );
                userRepository.save(actualUser);
                PersonalDetails newPersonalDetails = new PersonalDetails();
                newPersonalDetails.setEmail(actualUser.getEmail());
                newPersonalDetails.setUser(actualUser);
                personalDetailsRepository.save(newPersonalDetails);
            } catch (Exception e) {
                throw ApiExceptionResponse.builder().errors(Collections.singletonList(e.getMessage()))
                        .message(e.getMessage()).status(HttpStatus.BAD_REQUEST).build();
            }
        }

    }

    @Override
    public LoginResponseDTO login(LoginDTO loginDTO) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(loginDTO.getEmail());
        if (user.isPresent()==Boolean.FALSE) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Invalid credentials"))
                    .message("Invalid credentials").status(HttpStatus.BAD_REQUEST).build();
        } else {
            User actualUser = user.get();
            if(actualUser.getIsValidated()==Boolean.FALSE){
                throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with email: " + loginDTO.getEmail() + " is not validated"))
                        .message("User with email: " + loginDTO.getEmail() + " is not validated").status(HttpStatus.BAD_REQUEST).build();
            }

            if (actualUser.getPassword().equals(loginDTO.getPassword())) {
                return new LoginResponseDTO(actualUser.getEmail(), actualUser.getFirstName(), actualUser.getLastName(), actualUser.getRole().name(), jwtService.generateToken(actualUser));
            } else {
                throw ApiExceptionResponse.builder().errors(Collections.singletonList("Invalid credentials"))
                        .message("Invalid credentials").status(HttpStatus.BAD_REQUEST).build();
            }
        }
    }

    @Override
    public List<String> getIdUsers() {
        return userRepository.findAll().stream().map(User::getEmail).toList();
    }

    @Override
    public List<UserDTO> getUsers() {
        List<User> users = userRepository.findAll();
        for(User user: users){
            if(user.getRole() == Role.ADMINISTRATOR){
                users.remove(user);
            }
        }
        return UserMapper.toUsersDTOList(users);
    }

    @Override
    public void deleteUser(String email) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(email);
        if (user.isPresent()==Boolean.FALSE) {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with email: " + email + " does not exist"))
                    .message("User with email: " + email + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        } else {
            User actualUser = user.get();
            if(actualUser.getRole() == Role.ADMINISTRATOR){
                throw ApiExceptionResponse.builder().errors(Collections.singletonList("Cannot delete an administrator"))
                        .message("Cannot delete an administrator").status(HttpStatus.BAD_REQUEST).build();
            }
            userRepository.delete(actualUser);
        }
    }
}