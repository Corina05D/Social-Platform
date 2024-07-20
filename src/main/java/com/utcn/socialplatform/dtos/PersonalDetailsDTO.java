package com.utcn.socialplatform.dtos;

import com.utcn.socialplatform.model.Photo;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class PersonalDetailsDTO {
    private String email;
    private String bio;
    private String home;
    private String birthplace;
    private String study;
    private String work;
    private PhotoDTO profilePicture;
}