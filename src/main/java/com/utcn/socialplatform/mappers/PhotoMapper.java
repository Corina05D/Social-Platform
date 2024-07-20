package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.PhotoDTO;
import com.utcn.socialplatform.model.Photo;

import java.util.List;
import java.util.stream.Collectors;

public class PhotoMapper {

    public static PhotoDTO toDTO(Photo photo) {
        return new PhotoDTO(photo.getId(), photo.getName(), photo.getUser().getEmail());
    }

    public static List<PhotoDTO> toDTOList(List<Photo> photos) {
        return photos.stream().map(PhotoMapper::toDTO).collect(Collectors.toList());
    }
}