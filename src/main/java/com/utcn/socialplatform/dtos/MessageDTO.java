package com.utcn.socialplatform.dtos;

import com.utcn.socialplatform.model.User;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class MessageDTO {
    private String idSender;
    private String idReceiver;
    private String message;
}
