package com.ultimate.ultimatesophos.data.repository.network

import com.ultimate.ultimatesophos.core.RetrofitHelper
import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentDetailResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentsListResponseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class DocumentsRemote {
    private val retrofit = RetrofitHelper.getRetrofit()

    suspend fun putDocuments(params: PutDocumentsRequestEntity): DocumentsUploadResponseModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ApiService::class.java).postDocumentToRepository(params)
            response.body() ?: DocumentsUploadResponseModel()
        }
    }

    suspend fun getDocumentsByEmail(email: String): DocumentsListResponseModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ApiService::class.java).getDocumentsByEmail(email)
            response.body() ?: DocumentsListResponseModel()
        }
    }

    suspend fun getDocumentsByRegistry(id: String): DocumentDetailResponseModel {
        return withContext(Dispatchers.IO) {
            val response = retrofit.create(ApiService::class.java).getDocumentsByRegistry(id)
            response.body() ?: DocumentDetailResponseModel()
        }
    }
}

