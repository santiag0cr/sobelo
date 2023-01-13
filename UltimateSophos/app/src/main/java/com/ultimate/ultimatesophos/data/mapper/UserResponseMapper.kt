package com.ultimate.ultimatesophos.data.mapper

import com.ultimate.ultimatesophos.data.models.response.UsersResponseModel
import com.ultimate.ultimatesophos.domain.models.UserResponseDto


class UserResponseMapper {
    fun transform(value: UsersResponseModel): UserResponseDto {
        return UserResponseDto(
            id = value.id,
            name = value.name,
            lastName = value.lastName,
            access = value.access,
            admin = value.admin
        )
    }
}
