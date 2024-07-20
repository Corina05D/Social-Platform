package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class FriendshipRequestDTO {
    private String emailSender;
    private String emailReceiver;
}