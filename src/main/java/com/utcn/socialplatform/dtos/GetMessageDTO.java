package com.utcn.socialplatform.dtos;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class GetMessageDTO {
    private String idSender;
    private String idReceiver;
    private String message;
    private String timestamp;
    private String senderName;
}
