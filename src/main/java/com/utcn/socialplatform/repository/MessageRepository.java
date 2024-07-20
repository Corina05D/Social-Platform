package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.Message;
import com.utcn.socialplatform.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends CrudRepository<Message, Long> {
        List<Message> findBySenderAndReceiver(User sender, User receiver);
}
