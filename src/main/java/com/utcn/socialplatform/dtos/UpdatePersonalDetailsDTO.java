package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class UpdatePersonalDetailsDTO {
    private String email;
    private String bio;
    private String home;
    private String birthplace;
    private String study;
    private String work;
}