package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.GetMessageDTO;
import com.utcn.socialplatform.dtos.MessageDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.mappers.MessageMapper;
import com.utcn.socialplatform.model.Message;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.MessageRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
@Service
public class MessageServiceImpl implements MessageService {
    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    public MessageServiceImpl(MessageRepository messageRepository, UserRepository userRepository) {
        this.messageRepository = messageRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<GetMessageDTO> getMessages(String idSender, String idReceiver) {
        Optional<User> sender = userRepository.findById(idSender);
        Optional<User> receiver = userRepository.findById(idReceiver);
        if(sender.isPresent() && receiver.isPresent()){
            List<Message> messages = messageRepository.findBySenderAndReceiver(sender.get(), receiver.get());
            messages.addAll(messageRepository.findBySenderAndReceiver(receiver.get(), sender.get()));
            return MessageMapper.toDTOList(messages);
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public void createMessage(MessageDTO messageDTO) throws ApiExceptionResponse {
        Optional<User> sender = userRepository.findById(messageDTO.getIdSender());
        Optional<User> receiver = userRepository.findById(messageDTO.getIdReceiver());
        if (sender.isPresent() && receiver.isPresent()) {
            Message message = new Message();
            message.setId(null);
            message.setSender(sender.get());
            message.setReceiver(receiver.get());
            message.setMessage(messageDTO.getMessage());
            message.setTimestamp((new Date()).toString());
            messageRepository.save(message);
        } else {
            throw ApiExceptionResponse.builder().errors(Collections.singletonList("Sender or receiver does not exist"))
                    .message("Sender or receiver does not exist").status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
