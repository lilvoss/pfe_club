package tn.esprit.module.viewmodel


import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.AuthStoreResponse
import tn.esprit.module.repository.AuthStoreRepository

class AuthStoreViewModel(private val repository: AuthStoreRepository) : ViewModel() {

    private val _authResponse = MutableLiveData<AuthStoreResponse?>()
    val authResponse: LiveData<AuthStoreResponse?> get() = _authResponse

    fun login(email: String, password: String) {
        repository.login(email, password) { response ->
            _authResponse.postValue(response)
        }
    }
}


