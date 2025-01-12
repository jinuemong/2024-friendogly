package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.repository.PetRepository

class GetPetsMineUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(): DomainResult<List<Pet>, DataError.Network> = repository.getPetsMine()
}
