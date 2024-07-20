package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CreatePostDTO {
    private String idUser;
    private String description;
    private String photoName;
}