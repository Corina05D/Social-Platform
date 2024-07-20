package com.utcn.socialplatform.mappers;
import com.utcn.socialplatform.dtos.GetPostDTO;
import com.utcn.socialplatform.model.Post;

import java.util.List;
import java.util.stream.Collectors;

public class PostMapper {
    public static GetPostDTO toDTO(Post post) {
        GetPostDTO dto = new GetPostDTO();
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setBlocked(post.getBlocked());
        if (post.getPhoto() != null) {
            dto.setPhotoName(post.getPhoto().getName());
        } else {
            dto.setPhotoName(null);
        }
        dto.setTimestamp(post.getTimestamp());
        return dto;
    }

    public static List<GetPostDTO> toDTOList(List<Post> posts) {
        return posts.stream().map(PostMapper::toDTO).collect(Collectors.toList());
    }
}