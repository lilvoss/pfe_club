package tn.esprit.module.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.SignUpResponse
import tn.esprit.module.repository.SignUpRepository

class SignUpViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val repository = SignUpRepository(sharedPreferences)

    private val _signUpResponse = MutableLiveData<SignUpResponse?>()
    val signUpResponse: LiveData<SignUpResponse?> get() = _signUpResponse

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    // Ajout du paramètre tarifId
    fun signup(
        clientPrenom: String,
        clientNom: String,
        clientGender: String?,
        source: String?,
        clientAge: Int?,
        countryId: Int?,
        clientPhoneId: String?,
        clientPhoneOs: Int?,
        deviceId: String,
        lang: String,
        tarifId: Int,
        shouldSubscribe: Boolean,// Ajout de tarifId
        callback: (Boolean, String?) -> Unit // Ajout du callback pour signaler la réussite ou l'erreur
    ) {
        repository.signup(
            clientPrenom, clientNom, clientGender, source,
            clientAge, countryId, clientPhoneId, clientPhoneOs, deviceId, lang,
            tarifId,
            shouldSubscribe
        ) { response, error ->
            if (response != null && response.status) {
                _signUpResponse.postValue(response)
                callback(true, null) // Succès
            } else {
                _errorMessage.postValue(error)
                callback(false, error) // Échec
            }
        }
    }
}
