package com.ultimate.ultimatesophos.presentation.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ultimate.ultimatesophos.domain.usecase.GetUsersByCredentialsUseCase
import com.ultimate.ultimatesophos.data.models.request.UserRequestEntity
import com.ultimate.ultimatesophos.domain.models.UserResponseDto
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    val userModel = MutableLiveData<UserResponseDto>()
    val isLoading = MutableLiveData<Boolean>()
    private val getUsersByCredentialsUseCase = GetUsersByCredentialsUseCase()

    fun getUserByCredentials(params: UserRequestEntity) {
        viewModelScope.launch {
            isLoading.postValue(true)
            val result = getUsersByCredentialsUseCase.invoke(params)
            userModel.postValue(result)
            isLoading.postValue(false)
        }
    }
}
