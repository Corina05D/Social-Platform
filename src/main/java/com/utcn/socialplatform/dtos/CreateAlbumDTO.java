package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class CreateAlbumDTO {
    private String name;
    private String idUser;
}
