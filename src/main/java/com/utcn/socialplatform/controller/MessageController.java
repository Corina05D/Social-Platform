package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.GetMessageDTO;
import com.utcn.socialplatform.dtos.MessageDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.MessageService;
import com.utcn.socialplatform.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.List;

import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1 + MESSAGE)
public class MessageController {
    private final MessageService messageService;
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final UserService userService;

    public MessageController(MessageService messageService, SimpMessagingTemplate simpMessagingTemplate, UserService userService) {
        this.messageService = messageService;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.userService = userService;
    }

    @PostMapping("/sendMessage")
    public ResponseEntity sendMessage(@RequestBody MessageDTO message) throws ApiExceptionResponse {

        simpMessagingTemplate.convertAndSend("/topic/messages/" + message.getIdReceiver() + "/" + message.getIdSender(), message);
        messageService.createMessage(message);
        return ResponseEntity.ok("Message sent!");
    }

    @GetMapping("/getMessages/{sender}/{receiver}")
    public ResponseEntity getMessages(@PathVariable String sender, @PathVariable String receiver) {
        return ResponseEntity.ok(messageService.getMessages(sender, receiver).stream()
                .sorted(Comparator.comparing(GetMessageDTO::getTimestamp))
                .collect(java.util.stream.Collectors.toList()));
    }

    @GetMapping("/getUsers/{email}")
    public ResponseEntity<List<String>> getUsers(@PathVariable String email) {
        return ResponseEntity.ok(userService.getIdUsers().stream()
                .filter(user -> !user.equals(email))
                .collect(java.util.stream.Collectors.toList()));
    }
}
