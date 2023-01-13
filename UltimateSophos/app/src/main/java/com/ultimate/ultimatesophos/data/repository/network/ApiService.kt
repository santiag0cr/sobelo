package com.ultimate.ultimatesophos.data.repository.network

import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.data.models.response.OfficesResponseModel
import com.ultimate.ultimatesophos.data.models.response.UsersResponseModel
import com.ultimate.ultimatesophos.domain.definitions.Constants.DOCUMENTS
import com.ultimate.ultimatesophos.domain.definitions.Constants.OFFICES
import com.ultimate.ultimatesophos.domain.definitions.Constants.USERS
import com.ultimate.ultimatesophos.data.models.response.DocumentDetailResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentsListResponseModel
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query


interface ApiService {
    @GET(USERS)
    suspend fun getUserByCredentials(
        @Query("idUsuario") userId: String,
        @Query("clave") key: String
    ): Response<UsersResponseModel>

    @GET(OFFICES)
    suspend fun getOfficesList(): Response<OfficesResponseModel>

    @POST(DOCUMENTS)
    suspend fun postDocumentToRepository(
        @Body putDocumentsRequestEntity: PutDocumentsRequestEntity
    ): Response<DocumentsUploadResponseModel>

    @GET(DOCUMENTS)
    suspend fun getDocumentsByEmail(
        @Query("correo") email: String
    ): Response<DocumentsListResponseModel>

    @GET(DOCUMENTS)
    suspend fun getDocumentsByRegistry(
        @Query("idRegistro") id: String
    ): Response<DocumentDetailResponseModel>
}
