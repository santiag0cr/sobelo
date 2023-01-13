package com.ultimate.ultimatesophos.domain.usecase

import com.ultimate.ultimatesophos.data.mapper.UserResponseMapper
import com.ultimate.ultimatesophos.data.models.request.UserRequestEntity
import com.ultimate.ultimatesophos.data.repository.UserRepository
import com.ultimate.ultimatesophos.domain.models.UserResponseDto

class GetUsersByCredentialsUseCase {
    private val repository = UserRepository()
    private val mapper: UserResponseMapper = UserResponseMapper()

    suspend operator fun invoke(params: UserRequestEntity): UserResponseDto {
        return mapper.transform(
            repository.getUserByCredentials(params)
        )
    }
}
