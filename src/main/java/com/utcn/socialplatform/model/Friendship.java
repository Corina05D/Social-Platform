package com.utcn.socialplatform.model;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@IdClass(FriendshipKey.class)
public class Friendship {
    @Id
    @Column(name = "emailSender")
    private String emailSender;

    @Id
    @Column(name = "emailReceiver")
    private String emailReceiver;

    private boolean isAccepted;
}