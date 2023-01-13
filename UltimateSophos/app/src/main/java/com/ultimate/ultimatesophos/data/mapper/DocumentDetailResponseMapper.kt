package com.ultimate.ultimatesophos.data.mapper

import com.ultimate.ultimatesophos.data.models.response.DocumentDetailModel
import com.ultimate.ultimatesophos.domain.models.DocumentDetailDto
import com.ultimate.ultimatesophos.domain.models.DocumentDetailedInfoDto
import com.ultimate.ultimatesophos.data.models.response.DocumentDetailResponseModel

class DocumentDetailResponseMapper {
    fun transform(value: DocumentDetailResponseModel): DocumentDetailDto {
        return DocumentDetailDto(
            itemList = transformDocumentsModelList(value.items),
            count = value.count ?: 0,
            scannedCount = value.scannedCount ?: 0
        )
    }

    private fun transformDocumentsModelList(documentsList: List<DocumentDetailModel>): List<DocumentDetailedInfoDto> {
        return documentsList.map { transformDocumentsModel(it) }
    }

    private fun transformDocumentsModel(documentDetail: DocumentDetailModel): DocumentDetailedInfoDto {
        return DocumentDetailedInfoDto(
            id = documentDetail.id,
            date = documentDetail.date,
            idType = documentDetail.idType,
            identification = documentDetail.identification,
            name = documentDetail.name,
            lastName = documentDetail.lastName,
            city = documentDetail.city,
            email = documentDetail.email,
            attachmentType = documentDetail.attachmentType,
            attachment = documentDetail.attachment
        )
    }
}
