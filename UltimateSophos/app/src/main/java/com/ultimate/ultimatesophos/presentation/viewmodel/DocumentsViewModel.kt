package com.ultimate.ultimatesophos.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ultimate.ultimatesophos.domain.usecase.GetDocumentDetailUseCase
import com.ultimate.ultimatesophos.domain.usecase.GetDocumentsListUseCase
import com.ultimate.ultimatesophos.domain.usecase.UploadDocumentsUseCase
import com.ultimate.ultimatesophos.data.models.request.PutDocumentsRequestEntity
import com.ultimate.ultimatesophos.data.models.response.DocumentsUploadResponseModel
import com.ultimate.ultimatesophos.domain.models.DocumentDetailDto
import com.ultimate.ultimatesophos.domain.models.DocumentsListDto
import kotlinx.coroutines.launch

class DocumentsViewModel : ViewModel() {
    val putDocumentsModel = MutableLiveData<DocumentsUploadResponseModel>()
    val getDocumentsListModel = MutableLiveData<DocumentsListDto>()
    val getDocumentDetailModel = MutableLiveData<DocumentDetailDto>()
    val isLoading = MutableLiveData<Boolean>()
    private val uploadDocumentsUseCase = UploadDocumentsUseCase()
    private val getDocumentsListUseCase = GetDocumentsListUseCase()
    private val getDocumentDetailUseCase = GetDocumentDetailUseCase()

    fun putImageToRemote(params: PutDocumentsRequestEntity) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = uploadDocumentsUseCase.invoke(params)
            putDocumentsModel.postValue(result)
            isLoading.postValue(false)
        }
    }

    fun getUploadedImagesList(email: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getDocumentsListUseCase.invoke(email)
            getDocumentsListModel.postValue(result)
            isLoading.postValue(false)
        }
    }

    fun getImageDetail(id: String) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getDocumentDetailUseCase.invoke(id)
            getDocumentDetailModel.postValue(result)
            isLoading.postValue(false)
        }
    }
}
