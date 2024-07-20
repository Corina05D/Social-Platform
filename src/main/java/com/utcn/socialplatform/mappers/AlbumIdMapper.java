package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.AlbumIdDTO;
import com.utcn.socialplatform.model.Album;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumIdMapper {
    public static AlbumIdDTO toDTO(Album album) {
        AlbumIdDTO dto = new AlbumIdDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        return dto;
    }

    public static List<AlbumIdDTO> toDTOList(List<Album> albums) {
        return albums.stream().map(AlbumIdMapper::toDTO).collect(Collectors.toList());
    }
}
