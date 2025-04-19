package tn.esprit.module.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.CartridgeResponse
import tn.esprit.module.model.PartnerResponse
import tn.esprit.module.repository.CartridgeRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartridgeViewModel(sharedPreferences: SharedPreferences) : ViewModel() {

    private val repository = CartridgeRepository(sharedPreferences)

    // LiveData pour gérer la réponse des bannières
    private val _cartridgeResponse = MutableLiveData<CartridgeResponse>()
    val cartridgeResponse: LiveData<CartridgeResponse> get() = _cartridgeResponse

    // LiveData pour gérer la réponse des partenaires
    private val _partnerResponse = MutableLiveData<PartnerResponse>()
    val partnerResponse: LiveData<PartnerResponse> get() = _partnerResponse

    // LiveData pour gérer les erreurs
    private val _error = MutableLiveData<String>()
    val error: LiveData<String> get() = _error

    // Méthode pour charger les bannières
    fun loadBanners(
        position: String?,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        lang: String
    ) {
        repository.fetchCartridges("banner", position, location, latitude, longitude, lang)
            .enqueue(object : Callback<CartridgeResponse> {
                override fun onResponse(
                    call: Call<CartridgeResponse>,
                    response: Response<CartridgeResponse>
                ) {
                    if (response.isSuccessful) {
                        _cartridgeResponse.postValue(response.body())
                    } else {
                        _error.postValue("Erreur: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<CartridgeResponse>, t: Throwable) {
                    _error.postValue("Erreur réseau: ${t.localizedMessage}")
                }
            })
    }

    // Méthode pour charger les partenaires
    fun loadPartners(
        position: String?,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        lang: String
    ) {
        repository.fetchPartners("partner", position, location, latitude, longitude, lang)
            .enqueue(object : Callback<PartnerResponse> {
                override fun onResponse(
                    call: Call<PartnerResponse>,
                    response: Response<PartnerResponse>
                ) {
                    if (response.isSuccessful) {
                        _partnerResponse.postValue(response.body())
                    } else {
                        _error.postValue("Erreur: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<PartnerResponse>, t: Throwable) {
                    _error.postValue("Erreur réseau: ${t.localizedMessage}")
                }
            })
    }
}
