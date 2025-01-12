package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.repository.TokenRepository

class DeleteTokenUseCase(
    private val repository: TokenRepository,
) {
    suspend operator fun invoke(): DomainResult<Unit, DataError.Local> = repository.deleteToken()
}
