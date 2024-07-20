package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.AlbumDTO;
import com.utcn.socialplatform.model.Album;

import java.util.List;
import java.util.stream.Collectors;

public class AlbumMapper {
    public static AlbumDTO toDTO(Album album) {
        AlbumDTO dto = new AlbumDTO();
        dto.setId(album.getId());
        dto.setName(album.getName());
        dto.setPhotos(PhotoMapper.toDTOList(album.getPhotos()));

        return dto;
    }

    public static List<AlbumDTO> toDTOList(List<Album> albums) {
        return albums.stream().map(AlbumMapper::toDTO).collect(Collectors.toList());
    }
}
