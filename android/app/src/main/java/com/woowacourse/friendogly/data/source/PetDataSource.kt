package com.woowacourse.friendogly.data.source

import com.woowacourse.friendogly.data.model.GenderDto
import com.woowacourse.friendogly.data.model.PetDto
import com.woowacourse.friendogly.data.model.SizeTypeDto
import kotlinx.datetime.LocalDate
import okhttp3.MultipartBody

interface PetDataSource {
    suspend fun getPetsMine(): Result<List<PetDto>>

    suspend fun postPet(
        name: String,
        description: String,
        birthday: LocalDate,
        sizeType: SizeTypeDto,
        gender: GenderDto,
        file: MultipartBody.Part?,
    ): Result<PetDto>
}