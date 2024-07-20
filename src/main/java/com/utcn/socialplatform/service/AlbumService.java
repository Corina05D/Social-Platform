package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.AlbumDTO;
import com.utcn.socialplatform.dtos.AlbumIdDTO;
import com.utcn.socialplatform.dtos.CreateAlbumDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface AlbumService {
        List<AlbumDTO> getAlbums(String idUser) throws ApiExceptionResponse;
        void createAlbum(CreateAlbumDTO createAlbumDTO) throws ApiExceptionResponse;
        void deleteAlbum(Long albumId) throws ApiExceptionResponse;
        void addPhotoToAlbum(Long idAlbum, Long idPhoto) throws ApiExceptionResponse;
        void removePhotoFromAlbum(Long idAlbum, Long idPhoto) throws ApiExceptionResponse;
        void updateAlbumName(Long idAlbum, String albumName) throws ApiExceptionResponse;
        List<AlbumIdDTO> getAlbumsId(String idUser) throws ApiExceptionResponse;

}
