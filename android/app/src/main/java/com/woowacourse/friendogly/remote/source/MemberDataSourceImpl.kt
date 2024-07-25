package com.woowacourse.friendogly.remote.source

import com.woowacourse.friendogly.data.model.MemberDto
import com.woowacourse.friendogly.data.source.MemberDataSource
import com.woowacourse.friendogly.remote.api.MemberService
import com.woowacourse.friendogly.remote.mapper.toData
import com.woowacourse.friendogly.remote.model.request.PostMembersRequest
import okhttp3.MultipartBody

class MemberDataSourceImpl(
    private val service: MemberService,
) : MemberDataSource {
    override suspend fun postMember(
        name: String,
        email: String,
        file: MultipartBody.Part?,
    ): Result<MemberDto> =
        runCatching {
            val body = PostMembersRequest(name = name, email = email)
            service.postMember(body = body, file = file).data.toData()
        }

    override suspend fun getMemberMine(): Result<MemberDto> =
        runCatching {
            service.getMemberMine().data.toData()
        }
}