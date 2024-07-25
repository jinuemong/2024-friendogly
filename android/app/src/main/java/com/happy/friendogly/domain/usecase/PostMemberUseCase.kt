package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.model.Member
import com.happy.friendogly.domain.repository.MemberRepository
import okhttp3.MultipartBody

class PostMemberUseCase(
    private val repository: MemberRepository,
) {
    suspend operator fun invoke(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<Member> = repository.postMember(name = name, email = email, file = file)
}