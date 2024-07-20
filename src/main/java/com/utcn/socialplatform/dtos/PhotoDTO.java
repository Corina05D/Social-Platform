package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class PhotoDTO {
    private Long id;
    private String name;
    private String idUser;
}