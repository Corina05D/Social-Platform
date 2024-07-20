package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.CreatePhotoDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.PhotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.utcn.socialplatform.model.Constants.API_V1;
import static com.utcn.socialplatform.model.Constants.PHOTO;


@RestController
@RequestMapping(API_V1 + PHOTO)
public class PhotoController {
    private final PhotoService photoService;

    public PhotoController(PhotoService photoService) {
        this.photoService = photoService;
    }

    @PostMapping("")
    public ResponseEntity<String> uploadPhotoRequest(@RequestBody CreatePhotoDTO photoDTO) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.createPhoto(photoDTO));
    }

    @DeleteMapping("/{photo_id}")
    public ResponseEntity<String> deletePhoto(@PathVariable Long photo_id) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.deletePhoto(photo_id));
    }

    @GetMapping("/{email_address}")
    public ResponseEntity<?> getPhotos(@PathVariable String email_address) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(photoService.getPhotos(email_address));
    }
}