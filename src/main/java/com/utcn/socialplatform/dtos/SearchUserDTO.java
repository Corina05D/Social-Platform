package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class SearchUserDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureName;
    private boolean areFriends;
}
