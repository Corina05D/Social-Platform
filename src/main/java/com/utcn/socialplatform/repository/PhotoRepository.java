package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface PhotoRepository extends CrudRepository<Photo, Long>{
    List<Photo> findAllByUser(User user);
    Photo findByNameAndUser(String name, User user);
}