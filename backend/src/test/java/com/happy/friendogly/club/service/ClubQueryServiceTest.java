package com.happy.friendogly.club.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.happy.friendogly.club.domain.Club;
import com.happy.friendogly.club.domain.FilterCondition;
import com.happy.friendogly.club.dto.request.FindClubByFilterRequest;
import com.happy.friendogly.club.dto.response.FindClubByFilterResponse;
import com.happy.friendogly.member.domain.Member;
import com.happy.friendogly.pet.domain.Gender;
import com.happy.friendogly.pet.domain.Pet;
import com.happy.friendogly.pet.domain.SizeType;
import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;


class ClubQueryServiceTest extends ClubServiceTest {

    @Autowired
    private ClubQueryService clubQueryService;

    private Member savedMember;
    private Pet savedPet;

    @BeforeEach
    void setMemberAndPet() {
        savedMember = createSavedMember();
        savedPet = createSavedPet(savedMember);
    }

    @DisplayName("필터링된 모임을 정보를 조회한다.")
    @Test
    void findSearching() {
        Club club = createSavedClub(
                savedMember,
                savedPet,
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        FindClubByFilterRequest request = new FindClubByFilterRequest(
                FilterCondition.ALL.name(),
                province,
                city,
                village,
                Set.of(Gender.FEMALE.name()),
                Set.of(SizeType.SMALL.name())
        );

        List<FindClubByFilterResponse> responses = clubQueryService.findFindByFilter(savedMember.getId(), request);
        List<FindClubByFilterResponse> expectedResponses = List.of(
                new FindClubByFilterResponse(club, List.of(petImageUrl))
        );

        FindClubByFilterResponse actual = responses.get(0);
        FindClubByFilterResponse expected = expectedResponses.get(0);

        assertAll(
                () -> assertThat(actual.id()).isEqualTo(expected.id()),
                () -> assertThat(actual.title()).isEqualTo(expected.title()),
                () -> assertThat(actual.content()).isEqualTo(expected.content()),
                () -> assertThat(actual.address()).isEqualTo(expected.address()),
                () -> assertThat(actual.ownerMemberName()).isEqualTo(expected.ownerMemberName()),
                () -> assertThat(actual.status()).isEqualTo(expected.status()),
                () -> assertThat(actual.allowedSize()).containsExactlyInAnyOrderElementsOf(expected.allowedSize()),
                () -> assertThat(actual.allowedGender()).containsExactlyInAnyOrderElementsOf(expected.allowedGender()),
                () -> assertThat(actual.memberCapacity()).isEqualTo(expected.memberCapacity()),
                () -> assertThat(actual.currentMemberCount()).isEqualTo(expected.currentMemberCount()),
                () -> assertThat(actual.petImageUrls()).containsExactlyInAnyOrderElementsOf(expected.petImageUrls())
        );
    }

    @DisplayName("필터링된 모임을 정보가 없으면 빈 리스트를 반환한다.")
    @Test
    void findSearching_Nothing() {
        Club club = createSavedClub(
                savedMember,
                savedPet,
                Set.of(Gender.FEMALE, Gender.FEMALE_NEUTERED),
                Set.of(SizeType.SMALL)
        );

        FindClubByFilterRequest request = new FindClubByFilterRequest(
                FilterCondition.ALL.name(),
                province,
                city,
                village,
                Set.of(Gender.MALE.name()),
                Set.of(SizeType.SMALL.name())
        );

        List<FindClubByFilterResponse> responses = clubQueryService.findFindByFilter(savedMember.getId(), request);

        assertThat(responses.isEmpty()).isTrue();
    }
}