package com.ultimate.ultimatesophos.data.provider

import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentDetailResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentsListResponseModel

class DocumentsProvider {
    companion object {
        var putOperationResult: DocumentsUploadResponseModel = DocumentsUploadResponseModel()
        var getByEmailOperationResult: DocumentsListResponseModel = DocumentsListResponseModel()
        var getByIdOperationResult: DocumentDetailResponseModel = DocumentDetailResponseModel()
    }
}
