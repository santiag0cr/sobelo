package com.ultimate.ultimatesophos.data.repository

import com.ultimate.ultimatesophos.data.models.response.OfficesResponseModel
import com.ultimate.ultimatesophos.data.provider.OfficesProvider
import com.ultimate.ultimatesophos.data.repository.network.OfficesRemote


class OfficesRepository {
    private val api = OfficesRemote()

    suspend fun getOfficesList(): OfficesResponseModel {
        val response = api.getOfficesInfo()
        OfficesProvider.offices = response
        return response
    }
}
