package com.ultimate.ultimatesophos.domain.usecase

import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import comu.ultimate.ultimatesophos.data.repository.DocumentsRepository

class UploadDocumentsUseCase {
    private val repository = DocumentsRepository()

    suspend operator fun invoke(params: PutDocumentsRequestEntity): DocumentsUploadResponseModel {
        return repository.putDocuments(params)
    }
}
