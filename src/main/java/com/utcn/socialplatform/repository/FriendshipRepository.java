package com.utcn.socialplatform.repository;

import com.utcn.socialplatform.model.Friendship;
import com.utcn.socialplatform.model.FriendshipKey;
import org.springframework.data.repository.CrudRepository;

public interface FriendshipRepository extends CrudRepository<Friendship, FriendshipKey> {
    boolean existsByEmailSenderAndEmailReceiverAndIsAcceptedIsTrue(String emailSender, String emailReceiver);
    boolean existsByEmailSenderAndEmailReceiver(String emailSender, String emailReceiver);
    Friendship findByEmailSenderAndEmailReceiver(String emailSender, String emailReceiver);
    boolean existsByEmailSenderAndEmailReceiverAndIsAcceptedIsFalse(String emailSender, String emailReceiver);
}