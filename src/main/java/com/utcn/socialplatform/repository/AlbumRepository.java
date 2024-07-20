package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.Album;
import com.utcn.socialplatform.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface AlbumRepository extends CrudRepository<Album, Long>{
    List<Album> findByUser(User user);
}
