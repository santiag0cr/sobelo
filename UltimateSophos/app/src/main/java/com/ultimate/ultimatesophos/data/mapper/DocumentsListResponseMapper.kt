package com.ultimate.ultimatesophos.data.mapper

import com.ultimate.ultimatesophos.data.models.response.DocumentInfoModel
import com.ultimate.ultimatesophos.domain.models.DocumentGeneralInfoDto
import com.ultimate.ultimatesophos.domain.models.DocumentsListDto
import com.ultimate.ultimatesophos.data.models.response.DocumentsListResponseModel

class DocumentsListResponseMapper {
    fun transform(value: DocumentsListResponseModel): DocumentsListDto {
        return DocumentsListDto(
            itemList = transformDocumentsModelList(value.items),
            count = value.count ?: 0,
            scannedCount = value.scannedCount ?: 0
        )
    }

    private fun transformDocumentsModelList(documentsList: List<DocumentInfoModel>): List<DocumentGeneralInfoDto> {
        return documentsList.map { transformDocumentsModel(it) }
    }

    private fun transformDocumentsModel(documentDetail: DocumentInfoModel): DocumentGeneralInfoDto {
        return DocumentGeneralInfoDto(
            id = documentDetail.id,
            date = documentDetail.date,
            attachmentType = documentDetail.attachmentType,
            name = documentDetail.name,
            lastName = documentDetail.lastName
        )
    }
}
