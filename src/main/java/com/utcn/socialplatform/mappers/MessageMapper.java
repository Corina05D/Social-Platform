package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.GetMessageDTO;
import com.utcn.socialplatform.dtos.MessageDTO;
import com.utcn.socialplatform.model.Message;

import java.util.List;
import java.util.stream.Collectors;

public class MessageMapper {

        public static GetMessageDTO toDTO(Message message) {
            GetMessageDTO dto = new GetMessageDTO();
            dto.setIdSender(message.getSender().getEmail());
            dto.setIdReceiver(message.getReceiver().getEmail());
            dto.setMessage(message.getMessage());
            dto.setTimestamp(message.getTimestamp());
            dto.setSenderName(message.getSender().getFirstName() + " " + message.getSender().getLastName());
            return dto;
        }

        public static List<GetMessageDTO> toDTOList(List<Message> messages) {
            return messages.stream().map(MessageMapper::toDTO).collect(Collectors.toList());
        }

}
