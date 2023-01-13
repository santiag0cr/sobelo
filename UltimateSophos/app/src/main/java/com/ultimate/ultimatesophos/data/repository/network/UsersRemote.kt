package com.ultimate.ultimatesophos.data.repository.network

import com.ultimate.ultimatesophos.core.RetrofitHelper
import com.ultimate.ultimatesophos.data.models.request.UserRequestEntity
import com.ultimate.ultimatesophos.data.models.response.UsersResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRemote {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getUserInfo(params: UserRequestEntity): UsersResponseModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ApiService::class.java).getUserByCredentials(params.userId, params.key)
            response.body() ?: UsersResponseModel()
        }
    }
}
