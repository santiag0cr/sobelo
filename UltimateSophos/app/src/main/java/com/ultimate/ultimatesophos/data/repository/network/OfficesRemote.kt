package com.ultimate.ultimatesophos.data.repository.network

import com.ultimate.ultimatesophos.core.RetrofitHelper
import com.ultimate.ultimatesophos.data.models.response.OfficesResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class OfficesRemote {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun getOfficesInfo(): OfficesResponseModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ApiService::class.java).getOfficesList()
            response.body() ?: OfficesResponseModel()
        }
    }
}
