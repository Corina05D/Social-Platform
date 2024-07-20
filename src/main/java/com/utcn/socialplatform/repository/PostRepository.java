package com.utcn.socialplatform.repository;


import com.utcn.socialplatform.model.Photo;
import com.utcn.socialplatform.model.Post;
import com.utcn.socialplatform.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;


public interface PostRepository extends CrudRepository<Post, Long> {
    List<Post> findAllByUser(User user);
    Optional<Post> findByPhoto(Photo photo);
    List<Post> findAll();
}