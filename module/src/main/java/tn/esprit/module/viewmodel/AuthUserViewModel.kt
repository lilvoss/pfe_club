package tn.esprit.module.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.AuthUserResponse
import tn.esprit.module.repository.AuthUserRepository

class AuthUserViewModel(private val repository: AuthUserRepository) : ViewModel() {
    private val _authResponse = MutableLiveData<AuthUserResponse?>()
    val authResponse: LiveData<AuthUserResponse?> get() = _authResponse

    fun authUser(
        phone: Int,
        clientPhoneId: String,
        clientPhoneOs: Int,
        tarifId: Int?,
        deviceId: String,
        lang: String
    ) {
        repository.authUser(phone, clientPhoneId, clientPhoneOs, tarifId, deviceId, lang) { response ->
            _authResponse.postValue(response)
        }
    }

}
