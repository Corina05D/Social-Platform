package com.utcn.socialplatform.dtos;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class GetPostDTO {
    private Long id;
    private String description;
    private String photoName;
    private LocalDateTime timestamp;
    private Boolean blocked;
}