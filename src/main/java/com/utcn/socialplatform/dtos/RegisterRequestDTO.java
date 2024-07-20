package com.utcn.socialplatform.dtos;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class RegisterRequestDTO {
    private String email;
    private String firstName;
    private String lastName;
    private Date registrationDate;
}