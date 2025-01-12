package com.happy.friendogly.domain.usecase

import com.happy.friendogly.domain.DomainResult
import com.happy.friendogly.domain.error.DataError
import com.happy.friendogly.domain.model.Pet
import com.happy.friendogly.domain.repository.PetRepository

class GetPetsUseCase(
    private val repository: PetRepository,
) {
    suspend operator fun invoke(id: Long): DomainResult<List<Pet>, DataError.Network> = repository.getPets(id = id)
}
