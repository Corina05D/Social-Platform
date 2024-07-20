package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.AlbumDTO;
import com.utcn.socialplatform.dtos.AlbumIdDTO;
import com.utcn.socialplatform.dtos.CreateAlbumDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.utcn.socialplatform.model.Constants.API_V1;
import static com.utcn.socialplatform.model.Constants.ALBUM;

@RestController
@RequestMapping(API_V1 + ALBUM)
public class AlbumController {
    private final AlbumService albumService;

    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @GetMapping("/{email_address}")
    public ResponseEntity<List<AlbumDTO>> getAlbums(@PathVariable String email_address) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(albumService.getAlbums(email_address));
    }

    @PostMapping("")
    public ResponseEntity<String> createAlbum(@RequestBody CreateAlbumDTO createAlbumDTO) throws ApiExceptionResponse {
        albumService.createAlbum(createAlbumDTO);
        return ResponseEntity.status(HttpStatus.OK).body("Album has been successfully created!");
    }

    @PatchMapping("add_photo/{album_id}/{photo_id}")
    public ResponseEntity<String> addPhotoToAlbum(@PathVariable Long album_id, @PathVariable Long photo_id) throws ApiExceptionResponse {
        albumService.addPhotoToAlbum(album_id, photo_id);
        return ResponseEntity.status(HttpStatus.OK).body("Photo has been successfully added to album!");
    }

    @PatchMapping("remove_photo/{album_id}/{photo_id}")
    public ResponseEntity<String> removePhotoFromAlbum(@PathVariable Long album_id, @PathVariable Long photo_id) throws ApiExceptionResponse {
        albumService.removePhotoFromAlbum(album_id, photo_id);
        return ResponseEntity.status(HttpStatus.OK).body("Photo has been successfully removed from album!");
    }

    @PatchMapping("{album_id}/{new_name}")
    public ResponseEntity<String> renameAlbum(@PathVariable Long album_id, @PathVariable String new_name) throws ApiExceptionResponse {
        albumService.updateAlbumName(album_id, new_name);
        return ResponseEntity.status(HttpStatus.OK).body("Album has been successfully renamed!");
    }

    @DeleteMapping("/{album_id}")
    public ResponseEntity<String> deleteAlbum(@PathVariable Long album_id) throws ApiExceptionResponse {
        albumService.deleteAlbum(album_id);
        return ResponseEntity.status(HttpStatus.OK).body("Album has been successfully deleted!");
    }

    @GetMapping("/names/{email_address}")
    public ResponseEntity<List<AlbumIdDTO>> getAlbumNames(@PathVariable String email_address) throws ApiExceptionResponse {
        return ResponseEntity.status(HttpStatus.OK).body(albumService.getAlbumsId(email_address));
    }
}
