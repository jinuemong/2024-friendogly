package com.woowacourse.friendogly.domain.usecase

import com.woowacourse.friendogly.domain.model.Gender
import com.woowacourse.friendogly.domain.model.Pet
import com.woowacourse.friendogly.domain.model.SizeType
import com.woowacourse.friendogly.domain.repository.PetRepository
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

class PostPetUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeType,
        gender: Gender,
        file: MultipartBody.Part?,
    ): Result<Pet> =
        repository.postPet(
            name = name,
            description = description,
            birthday = birthday,
            sizeType = sizeType,
            gender = gender,
            file = file,
        )
}