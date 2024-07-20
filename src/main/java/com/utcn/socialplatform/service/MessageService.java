package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.GetMessageDTO;
import com.utcn.socialplatform.dtos.MessageDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.model.Message;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface MessageService {
    List<GetMessageDTO> getMessages(String sender, String receiver);
    void createMessage(MessageDTO mesaj) throws ApiExceptionResponse;
}
