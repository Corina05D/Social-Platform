package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.CreatePhotoDTO;
import com.utcn.socialplatform.dtos.PhotoDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface PhotoService {
    String createPhoto(CreatePhotoDTO photoDTO) throws ApiExceptionResponse;
    String deletePhoto(Long id) throws ApiExceptionResponse;
    List<PhotoDTO> getPhotos(String idUser) throws ApiExceptionResponse;
}