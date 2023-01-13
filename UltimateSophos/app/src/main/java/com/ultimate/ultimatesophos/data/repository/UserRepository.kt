package com.ultimate.ultimatesophos.data.repository

import com.ultimate.ultimatesophos.data.models.request.UserRequestEntity
import com.ultimate.ultimatesophos.data.models.response.UsersResponseModel
import com.ultimate.ultimatesophos.data.provider.UserProvider
import com.ultimate.ultimatesophos.data.repository.network.UsersRemote


class UserRepository {
    private val api = UsersRemote()

    suspend fun getUserByCredentials(params: UserRequestEntity): UsersResponseModel {
        val response = api.getUserInfo(params)
        UserProvider.users = response
        return response
    }
}
