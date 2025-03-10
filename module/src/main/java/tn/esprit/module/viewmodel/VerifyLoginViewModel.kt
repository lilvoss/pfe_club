package tn.esprit.module.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.VerifyCodeResponse
import tn.esprit.module.repository.VerifyLoginRepository

class VerifyLoginViewModel(private val repository: VerifyLoginRepository) : ViewModel() {
    private val _loginResponse = MutableLiveData<VerifyCodeResponse?>()
    val loginResponse: LiveData<VerifyCodeResponse?> get() = _loginResponse

    fun verifyLogin(
        code: Int,
        clientPhoneId: String,
        clientPhoneOs: Int,
        lang: String
    ) {
        repository.verifyLogin(code, clientPhoneId, clientPhoneOs, lang) { response ->
            _loginResponse.postValue(response)
        }
    }
}
