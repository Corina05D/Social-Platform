package com.utcn.socialplatform.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RegisterDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthDate;
}