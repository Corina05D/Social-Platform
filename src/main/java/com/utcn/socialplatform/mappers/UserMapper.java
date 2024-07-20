package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.UserDTO;
import com.utcn.socialplatform.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDTO toUserDTO(User user) {
        return UserDTO.builder()
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .build();
    }

    public static List<UserDTO> toUsersDTOList(List<User> userDTOs) {
        return userDTOs.stream().map(UserMapper::toUserDTO).collect(Collectors.toList());
    }
}
