package com.happy.friendogly.club.dto.response;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.Status;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public record FindClubResponse(
        Long id,
        String title,
        String content,
        String ownerMemberName,
        AddressDetailResponse address,
        Status status,
        LocalDateTime createdAt,
        Set<Gender> allowedGender,
        Set<SizeType> allowedSize,
        int memberCapacity,
        int currentMemberCount,
        String imageUrl,
        String ownerImageUrl,
        boolean isMine,
        boolean alreadyParticipate,
        boolean canParticipate,
        boolean isMyPetsEmpty,
        List<ClubMemberDetailResponse> memberDetails,
        List<ClubPetDetailResponse> petDetails,
        Long chatRoomId
) {

    public FindClubResponse(Club club, Member member, List<Pet> myPets) {
        this(
                club.getId(),
                club.getTitle().getValue(),
                club.getContent().getValue(),
                club.findOwnerName().getValue(),
                new AddressDetailResponse(club.getAddress()),
                club.getStatus(),
                club.getCreatedAt(),
                club.getAllowedGenders(),
                club.getAllowedSizes(),
                club.getMemberCapacity().getValue(),
                club.countClubMember(),
                club.getImageUrl(),
                club.findOwnerImageUrl(),
                club.isOwner(member),
                club.isAlreadyJoined(member),
                club.isJoinable(member, myPets),
                myPets.isEmpty(),
                club.getClubMembers().stream()
                        .map(clubMember -> clubMember.getClubMemberId().getMember())
                        .map(ClubMemberDetailResponse::new)
                        .toList(),
                club.getClubPets().stream()
                        .map(clubPet -> clubPet.getClubPetId().getPet())
                        .map(pet -> new ClubPetDetailResponse(pet, pet.isOwner(member)))
                        .toList(),
                club.getChatRoom().getId()
        );
    }
}
