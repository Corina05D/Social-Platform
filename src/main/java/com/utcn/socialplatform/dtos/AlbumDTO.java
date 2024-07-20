package com.utcn.socialplatform.dtos;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AlbumDTO {
    private Long id;
    private String name;
    private String idUser;
    private List<PhotoDTO> photos;
}
