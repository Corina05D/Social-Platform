package com.utcn.socialplatform.controller;

import com.utcn.socialplatform.dtos.FriendshipProposalDTO;
import com.utcn.socialplatform.dtos.FriendshipRequestDTO;

import com.utcn.socialplatform.dtos.SearchUserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.utcn.socialplatform.model.Constants.*;

@RestController
@RequestMapping(API_V1)
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService, SimpMessagingTemplate simpMessagingTemplate) {
        this.friendshipService = friendshipService;
    }

    @GetMapping(SEARCH + "/{email}")
    public ResponseEntity<?> searchUsers(@PathVariable String email) {
        try {
            List<SearchUserDTO> users = friendshipService.searchUsers(email);
            return ResponseEntity.ok(users);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @PostMapping(FRIENDS+ADD)
    public ResponseEntity<?> addFriendship(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        try {
            friendshipService.addFriendship(friendshipRequestDTO);
            return ResponseEntity.ok("Friendship created successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @PatchMapping(FRIENDS + ACCEPT)
    public ResponseEntity<?> acceptFriendship(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        try {
            friendshipService.acceptFriendship(friendshipRequestDTO);
            return ResponseEntity.ok("Friendship request accepted successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @DeleteMapping(FRIENDS + DENY)
    public ResponseEntity<?> denyFriendship(@RequestBody FriendshipRequestDTO friendshipRequestDTO) {
        try {
            friendshipService.denyFriendship(friendshipRequestDTO);
            return ResponseEntity.ok("Friendship request denied successfully");
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus())
                    .body(e.getMessage());
        }
    }

    @GetMapping(FRIENDS + REQUEST + "/{email_address_receiver}")
    public ResponseEntity<?> getFriendshipRequests(@PathVariable("email_address_receiver") String emailReceiver) {
        try {
            List<FriendshipProposalDTO> requests = friendshipService.searchFriendshipProposals(emailReceiver);
            return ResponseEntity.ok(requests);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }

    @GetMapping(FRIENDS + "/{email}")
    public ResponseEntity<?> getFriends(@PathVariable String email) {
        try {
            List<FriendshipProposalDTO> friends = friendshipService.searchFriends(email);
            return ResponseEntity.ok(friends);
        } catch (ApiExceptionResponse e) {
            return ResponseEntity.status(e.getStatus()).body(e.getMessage());
        }
    }
}

