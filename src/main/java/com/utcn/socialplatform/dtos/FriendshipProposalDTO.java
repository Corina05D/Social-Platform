package com.utcn.socialplatform.dtos;

import lombok.*;


@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class FriendshipProposalDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String profilePictureName;
}
