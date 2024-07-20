package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class LoginResponseDTO {
    private String email;
    private String firstName;
    private String lastName;
    private String role;
    private String token;

}