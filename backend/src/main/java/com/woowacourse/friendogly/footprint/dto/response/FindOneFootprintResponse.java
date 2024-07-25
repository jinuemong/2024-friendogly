package com.woowacourse.friendogly.footprint.dto.response;

import com.woowacourse.friendogly.footprint.domain.Footprint;
import com.woowacourse.friendogly.member.domain.Member;
import com.woowacourse.friendogly.pet.domain.Gender;
import com.woowacourse.friendogly.pet.domain.Pet;
import com.woowacourse.friendogly.pet.domain.SizeType;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record FindOneFootprintResponse(
        String memberName,
        String petName,
        String petDescription,
        LocalDate petBirthDate,
        SizeType petSizeType,
        Gender petGender,
        String footprintImageUrl,
        LocalDateTime createdAt,
        boolean isMine
) {

    public static FindOneFootprintResponse withMainPetImage(
            Member member,
            Pet mainPet,
            Footprint footprint,
            boolean isMine
    ) {
        return new FindOneFootprintResponse(
                member.getName().getValue(),
                mainPet.getName().getValue(),
                mainPet.getDescription().getValue(),
                mainPet.getBirthDate().getValue(),
                mainPet.getSizeType(),
                mainPet.getGender(),
                mainPet.getImageUrl(),
                footprint.getCreatedAt(),
                isMine
        );
    }

    public static FindOneFootprintResponse withFootprintImage(
            Member member,
            Pet mainPet,
            Footprint footprint,
            boolean isMine
    ) {
        return new FindOneFootprintResponse(
                member.getName().getValue(),
                mainPet.getName().getValue(),
                mainPet.getDescription().getValue(),
                mainPet.getBirthDate().getValue(),
                mainPet.getSizeType(),
                mainPet.getGender(),
                footprint.getImageUrl(),
                footprint.getCreatedAt(),
                isMine
        );
    }
}
