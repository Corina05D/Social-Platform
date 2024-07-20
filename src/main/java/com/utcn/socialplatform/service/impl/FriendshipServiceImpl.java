package com.utcn.socialplatform.service.impl;

import com.utcn.socialplatform.dtos.FriendshipProposalDTO;

import com.utcn.socialplatform.dtos.FriendshipRequestDTO;
import com.utcn.socialplatform.dtos.SearchUserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import com.utcn.socialplatform.model.Friendship;

import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.Role;
import com.utcn.socialplatform.model.User;
import com.utcn.socialplatform.repository.FriendshipRepository;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import com.utcn.socialplatform.repository.UserRepository;
import com.utcn.socialplatform.service.FriendshipService;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipServiceImpl implements FriendshipService {
    private final UserRepository userRepository;
    private final FriendshipRepository friendshipRepository;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final SimpMessagingTemplate simpMessagingTemplate;

    public FriendshipServiceImpl(UserRepository userRepository, FriendshipRepository friendshipRepository, PersonalDetailsRepository personalDetailsRepository, SimpMessagingTemplate simpMessagingTemplate) {
        this.userRepository = userRepository;
        this.friendshipRepository = friendshipRepository;
        this.personalDetailsRepository = personalDetailsRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @Override
    public List<SearchUserDTO> searchUsers(String email) throws ApiExceptionResponse {
        if (userRepository.findById(email).isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email invalid"))
                    .message("Email invalid")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No users found"))
                    .message("No users found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        List<SearchUserDTO> searchUserDTOList = new ArrayList<>();
        for (User user : users){
            if (!user.getEmail().equals(email) && user.getRole()== Role.CLIENT){
                boolean areFriends = friendshipRepository.existsByEmailSenderAndEmailReceiver(email, user.getEmail())
                        || friendshipRepository.existsByEmailSenderAndEmailReceiver(user.getEmail(), email);
                SearchUserDTO searchUserDTO = new SearchUserDTO();
                searchUserDTO.setEmail(user.getEmail());
                searchUserDTO.setFirstName(user.getFirstName());
                searchUserDTO.setLastName(user.getLastName());
                PersonalDetails personalDetails = personalDetailsRepository.findByEmail(user.getEmail());
                if(personalDetails == null || personalDetails.getProfilePicture() == null) {
                    searchUserDTO.setProfilePictureName(null);
                } else {
                    searchUserDTO.setProfilePictureName(personalDetails.getProfilePicture().getName());
                }
                searchUserDTO.setAreFriends(areFriends);
                searchUserDTOList.add(searchUserDTO);
            }
        }
        return searchUserDTOList;
    }

    @Override
    public void addFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse {
        Optional<User> sender = userRepository.findById(friendshipRequestDTO.getEmailSender());
        Optional<User> receiver = userRepository.findById(friendshipRequestDTO.getEmailReceiver());
        if (sender.isEmpty() || receiver.isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("One or both users do not exist"))
                    .message("One or both users do not exist")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        boolean friendshipExists = friendshipRepository.existsByEmailSenderAndEmailReceiver(friendshipRequestDTO.getEmailSender(), friendshipRequestDTO.getEmailReceiver()) ||
                friendshipRepository.existsByEmailSenderAndEmailReceiver(friendshipRequestDTO.getEmailReceiver(), friendshipRequestDTO.getEmailSender());
        if (friendshipExists) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Friendship request already exists"))
                    .message("Friendship request already exists")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Friendship friendship = new Friendship();
        friendship.setEmailSender(friendshipRequestDTO.getEmailSender());
        friendship.setEmailReceiver(friendshipRequestDTO.getEmailReceiver());
        friendship.setAccepted(false);
        friendshipRepository.save(friendship);
        simpMessagingTemplate.convertAndSend("/topic/friend_requests/" + friendshipRequestDTO.getEmailReceiver(), "You have a new friend request from "+ sender.get().getFirstName() +" "+ sender.get().getLastName()+" !");
    }

    @Override
    public void acceptFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse {
        Friendship friendship = friendshipRepository.findByEmailSenderAndEmailReceiver(friendshipRequestDTO.getEmailSender(), friendshipRequestDTO.getEmailReceiver());
        if (friendship == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Friendship request not found"))
                    .message("Friendship request not found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (friendship.isAccepted()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Friendship request is already accepted"))
                    .message("Friendship request is already accepted")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        friendship.setAccepted(true);
        friendshipRepository.save(friendship);
    }

    @Override
    public void denyFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse {
        Friendship friendship = friendshipRepository.findByEmailSenderAndEmailReceiver(friendshipRequestDTO.getEmailSender(), friendshipRequestDTO.getEmailReceiver());
        if (friendship == null) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Friendship request not found"))
                    .message("Friendship request not found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        if (friendship.isAccepted()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Friendship request is already accepted and cannot be denied"))
                    .message("Friendship request is already accepted and cannot be denied")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        friendshipRepository.delete(friendship);
    }

    @Override
    public List<FriendshipProposalDTO> searchFriendshipProposals(String email) throws ApiExceptionResponse {
        if (userRepository.findById(email).isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email invalid"))
                    .message("Email invalid")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No users found"))
                    .message("No users found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        List<FriendshipProposalDTO> friendshipProposals = new ArrayList<>();
        for (User user : users){
            if (!user.getEmail().equals(email) && user.getRole() == Role.CLIENT){
                boolean receivedRequest = friendshipRepository.existsByEmailSenderAndEmailReceiverAndIsAcceptedIsFalse(user.getEmail(), email);
                if(receivedRequest){
                    FriendshipProposalDTO friendshipProposalDTO = new FriendshipProposalDTO();
                    friendshipProposalDTO.setEmail(user.getEmail());
                    friendshipProposalDTO.setFirstName(user.getFirstName());
                    friendshipProposalDTO.setLastName(user.getLastName());
                    PersonalDetails personalDetails = personalDetailsRepository.findByEmail(user.getEmail());
                    if(personalDetails == null || personalDetails.getProfilePicture() == null) {
                        friendshipProposalDTO.setProfilePictureName(null);
                    } else {
                        friendshipProposalDTO.setProfilePictureName(personalDetails.getProfilePicture().getName());
                    }
                    friendshipProposals.add(friendshipProposalDTO);
                }
            }
        }
        return friendshipProposals;
    }

    @Override
    public List<FriendshipProposalDTO> searchFriends(String email) throws ApiExceptionResponse {
        if (userRepository.findById(email).isEmpty()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("Email invalid"))
                    .message("Email invalid")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        Iterable<User> users = userRepository.findAll();
        if (!users.iterator().hasNext()) {
            throw ApiExceptionResponse.builder()
                    .errors(Collections.singletonList("No users found"))
                    .message("No users found")
                    .status(HttpStatus.BAD_REQUEST)
                    .build();
        }
        List<FriendshipProposalDTO> friends = new ArrayList<>();
        for (User user : users){
            if (!user.getEmail().equals(email) && user.getRole() == Role.CLIENT){
                boolean friendship = friendshipRepository.existsByEmailSenderAndEmailReceiverAndIsAcceptedIsTrue(user.getEmail(), email)
                        || friendshipRepository.existsByEmailSenderAndEmailReceiverAndIsAcceptedIsTrue(email, user.getEmail());
                if (friendship) {
                    FriendshipProposalDTO friend = new FriendshipProposalDTO();
                    friend.setEmail(user.getEmail());
                    friend.setFirstName(user.getFirstName());
                    friend.setLastName(user.getLastName());
                    PersonalDetails personalDetails = personalDetailsRepository.findByEmail(user.getEmail());
                    if(personalDetails == null || personalDetails.getProfilePicture() == null) {
                        friend.setProfilePictureName(null);
                    } else {
                        friend.setProfilePictureName(personalDetails.getProfilePicture().getName());
                    }
                    friends.add(friend);
                }
            }
        }
        return friends;
    }
}