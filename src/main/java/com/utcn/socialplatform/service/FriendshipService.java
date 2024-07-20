package com.utcn.socialplatform.service;

import com.utcn.socialplatform.dtos.FriendshipProposalDTO;
import com.utcn.socialplatform.dtos.FriendshipRequestDTO;
import com.utcn.socialplatform.dtos.SearchUserDTO;
import com.utcn.socialplatform.exceptions.ApiExceptionResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface FriendshipService {
    List<SearchUserDTO> searchUsers(String email) throws ApiExceptionResponse;
    void addFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse;
    void acceptFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse;
    void denyFriendship(FriendshipRequestDTO friendshipRequestDTO) throws ApiExceptionResponse;
    List<FriendshipProposalDTO> searchFriendshipProposals(String email) throws ApiExceptionResponse;
    List<FriendshipProposalDTO> searchFriends(String email) throws ApiExceptionResponse;
}