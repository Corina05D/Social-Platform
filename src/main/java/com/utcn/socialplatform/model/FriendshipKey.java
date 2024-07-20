package com.utcn.socialplatform.model;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class FriendshipKey implements Serializable {
    private String emailSender;
    private String emailReceiver;
}