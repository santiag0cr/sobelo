package com.ultimate.ultimatesophos.domain.usecase

import com.ultimate.ultimatesophos.data.mapper.DocumentsListResponseMapper
import com.ultimate.ultimatesophos.domain.models.DocumentsListDto
import comu.ultimate.ultimatesophos.data.repository.DocumentsRepository

class GetDocumentsListUseCase {
    private val repository = DocumentsRepository()
    private val mapper: DocumentsListResponseMapper = DocumentsListResponseMapper()

    suspend operator fun invoke(email: String): DocumentsListDto {
        return mapper.transform(repository.getDocumentsByEmail(email))
    }
}
