package com.utcn.socialplatform.mappers;

import com.utcn.socialplatform.dtos.FriendPostDTO;
import com.utcn.socialplatform.dtos.FriendshipProposalDTO;
import com.utcn.socialplatform.dtos.GetPostDTO;
import com.utcn.socialplatform.model.PersonalDetails;
import com.utcn.socialplatform.model.Post;
import com.utcn.socialplatform.repository.PersonalDetailsRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class FriendPostMapper {
    private final PersonalDetailsRepository personalDetailsRepository;

    public FriendPostMapper(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public FriendPostDTO toDTO(Post post) {
        FriendPostDTO dto = new FriendPostDTO();
        FriendshipProposalDTO friendshipProposalDTO = new FriendshipProposalDTO();
        friendshipProposalDTO.setEmail(post.getUser().getEmail());
        friendshipProposalDTO.setFirstName(post.getUser().getFirstName());
        friendshipProposalDTO.setLastName(post.getUser().getLastName());
        Optional<PersonalDetails> personalDetails = personalDetailsRepository.findById(post.getUser().getEmail());
        if (personalDetails.isPresent()) {
            if(personalDetails.get().getProfilePicture() != null) {
                friendshipProposalDTO.setProfilePictureName(personalDetails.get().getProfilePicture().getName());
            } else {
                friendshipProposalDTO.setProfilePictureName(null);
            }
        }
        dto.setFriend(friendshipProposalDTO);
        dto.setId(post.getId());
        dto.setDescription(post.getDescription());
        dto.setBlocked(post.getBlocked());
        if (post.getPhoto() != null) {
            dto.setPhotoName(post.getPhoto().getName());
        } else {
            dto.setPhotoName(null);
        }
        dto.setTimestamp(post.getTimestamp());
        return dto;
    }
}