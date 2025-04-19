package tn.esprit.module.repository

import android.content.SharedPreferences
import retrofit2.Call
import tn.esprit.module.model.CartridgeResponse
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.PartnerResponse
import tn.esprit.module.request.Home_api

class CartridgeRepository(private val sharedPreferences: SharedPreferences) {

    private val api: Home_api  = RetrofitModule.getHomeApiService(sharedPreferences)

    fun fetchCartridges(
        type: String,
        position: String?,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        lang: String
    ): Call<CartridgeResponse> {
        return api.getCartridgesBanner(type, position, location, latitude, longitude, lang)

    }

    fun fetchPartners(
        type: String,
        position: String?,
        location: String?,
        latitude: Double?,
        longitude: Double?,
        lang: String
    ): Call<PartnerResponse> {
        return api.getCartridgesPartner(type, position, location, latitude, longitude, lang)
    }

}
