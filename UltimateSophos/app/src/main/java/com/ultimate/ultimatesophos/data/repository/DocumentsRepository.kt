package comu.ultimate.ultimatesophos.data.repository

import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.data.provider.DocumentsProvider
import com.ultimate.ultimatesophos.data.repository.network.DocumentsRemote
import com.ultimate.ultimatesophos.data.models.response.DocumentDetailResponseModel
import com.ultimate.ultimatesophos.data.models.response.DocumentsListResponseModel


class DocumentsRepository {
    private val api = DocumentsRemote()

    suspend fun putDocuments(params: PutDocumentsRequestEntity): DocumentsUploadResponseModel {
        val response = api.putDocuments(params)
        DocumentsProvider.putOperationResult = response
        return response
    }

    suspend fun getDocumentsByEmail(email: String): DocumentsListResponseModel {
        val response = api.getDocumentsByEmail(email)
        DocumentsProvider.getByEmailOperationResult = response
        return response
    }

    suspend fun getDocumentsById(id: String): DocumentDetailResponseModel {
        val response = api.getDocumentsByRegistry(id)
        DocumentsProvider.getByIdOperationResult = response
        return response
    }
}
