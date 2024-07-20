package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface UserRepository extends CrudRepository<User, String> {
    List<User> findAll();
    List<User> findByIsValidatedFalse();
}