package com.ultimate.ultimatesophos.domain.usecase

import com.ultimate.ultimatesophos.data.mapper.DocumentDetailResponseMapper
import com.ultimate.ultimatesophos.domain.models.DocumentDetailDto
import comu.ultimate.ultimatesophos.data.repository.DocumentsRepository

class GetDocumentDetailUseCase {
    private val repository = DocumentsRepository()
    private val mapper = DocumentDetailResponseMapper()

    suspend operator fun invoke(id: String): DocumentDetailDto {
        return mapper.transform(repository.getDocumentsById(id))
    }
}
