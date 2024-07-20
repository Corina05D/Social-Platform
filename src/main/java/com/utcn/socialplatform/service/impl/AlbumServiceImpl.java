package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.AlbumDTO;
import com.utcn.socialplatform.dtos.AlbumIdDTO;
import com.utcn.socialplatform.dtos.CreateAlbumDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.mappers.AlbumIdMapper;
import com.utcn.socialplatform.mappers.AlbumMapper;
import com.utcn.socialplatform.model.Album;
import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.AlbumRepository;
import com.utcn.socialplatform.repository.PhotoRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.AlbumService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class AlbumServiceImpl implements AlbumService {

    private final AlbumRepository albumRepository;
    private final UserRepository userRepository;
    private final PhotoRepository photoRepository;

    public AlbumServiceImpl(AlbumRepository albumRepository, UserRepository userRepository, PhotoRepository photoRepository) {
        this.albumRepository = albumRepository;
        this.userRepository = userRepository;
        this.photoRepository = photoRepository;
    }

    @Override
    public List<AlbumDTO> getAlbums(String idUser) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent()){
            return AlbumMapper.toDTOList(albumRepository.findByUser(user.get()));
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + idUser + " does not exist"))
                    .message("User with id: " + idUser + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public void createAlbum(CreateAlbumDTO createAlbumDTO) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(createAlbumDTO.getIdUser());
        if (user.isPresent()) {
            //checks if album with the same name already exists
            if (albumRepository.findByUser(user.get()).stream().anyMatch(album -> album.getName().equals(createAlbumDTO.getName()))){
                throw ApiExceptionResponse.builder().errors(Collections.singletonList("Album with name: " + createAlbumDTO.getName() + " already exists"))
                        .message("Album with name: " + createAlbumDTO.getName() + " already exists").status(HttpStatus.BAD_REQUEST).build();
            }

            Album album = new Album(null, createAlbumDTO.getName(), user.get(), new ArrayList<>());
            albumRepository.save(album);
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + createAlbumDTO.getIdUser() + " does not exist"))
                    .message("User with id: " + createAlbumDTO.getIdUser() + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public void deleteAlbum(Long albumId) throws ApiExceptionResponse {
        Optional<Album> album = albumRepository.findById(albumId);
        if(album.isPresent()){
            albumRepository.deleteById(albumId);
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Album with id: " + albumId + " does not exist"))
                    .message("Album with id: " + albumId + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public void addPhotoToAlbum(Long idAlbum, Long idPhoto) throws ApiExceptionResponse {
        Optional<Album> album = albumRepository.findById(idAlbum);
        Optional<Photo> photo = photoRepository.findById(idPhoto);

        if(album.isEmpty())
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Album with id: " + idAlbum + " does not exist"))
                    .message("Album with id: " + idAlbum + " does not exist").status(HttpStatus.BAD_REQUEST).build();

        if(photo.isEmpty())
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Photo with id: " + idPhoto + " does not exist"))
                    .message("Photo with id: " + idPhoto + " does not exist").status(HttpStatus.BAD_REQUEST).build();

        //check if photo is already in album
        if(album.get().getPhotos().stream().anyMatch(p -> p.getId().equals(idPhoto)))
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Photo with id: " + idPhoto + " is already in album with id: " + idAlbum))
                    .message("Photo with id: " + idPhoto + " is already in album with id: " + idAlbum).status(HttpStatus.BAD_REQUEST).build();

        Album getAlbum = album.get();
        getAlbum.getPhotos().add(photo.get());
        albumRepository.save(getAlbum);
    }

    @Override
    public void removePhotoFromAlbum(Long idAlbum, Long idPhoto) throws ApiExceptionResponse {
        Optional<Album> album = albumRepository.findById(idAlbum);
        Optional<Photo> photo = photoRepository.findById(idPhoto);
        if(album.isEmpty())
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Album with id: " + idAlbum + " does not exist"))
                    .message("Album with id: " + idAlbum + " does not exist").status(HttpStatus.BAD_REQUEST).build();

        if(photo.isEmpty())
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Photo with id: " + idPhoto + " does not exist"))
                    .message("Photo with id: " + idPhoto + " does not exist").status(HttpStatus.BAD_REQUEST).build();

        //check if photo is not in album
        if(album.get().getPhotos().stream().noneMatch(p -> p.getId().equals(idPhoto)))
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Photo with id: " + idPhoto + " is not in album with id: " + idAlbum))
                    .message("Photo with id: " + idPhoto + " is not in album with id: " + idAlbum).status(HttpStatus.BAD_REQUEST).build();

        Album getAlbum = album.get();
        getAlbum.getPhotos().remove(photo.get());
        albumRepository.save(getAlbum);
    }

    @Override
    public void updateAlbumName(Long idAlbum, String albumName) throws ApiExceptionResponse {
        Optional<Album> album = albumRepository.findById(idAlbum);
        if(album.isPresent()){
            Album getAlbum = album.get();
            getAlbum.setName(albumName);
            albumRepository.save(getAlbum);
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Album with id: " + idAlbum + " does not exist"))
                    .message("Album with id: " + idAlbum + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @Override
    public List<AlbumIdDTO> getAlbumsId(String idUser) throws ApiExceptionResponse {
        Optional<User> user = userRepository.findById(idUser);
        if(user.isPresent()){
            return AlbumIdMapper.toDTOList(albumRepository.findByUser(user.get()));
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("User with id: " + idUser + " does not exist"))
                    .message("User with id: " + idUser + " does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
