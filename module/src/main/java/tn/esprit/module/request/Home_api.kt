package tn.esprit.module.request

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Query
import tn.esprit.module.model.CartridgeResponse
import tn.esprit.module.model.PartnerResponse

interface Home_api {

    @GET("V2.0/cartridges")
    fun getCartridgesBanner(
        @Query("type") type: String = "banner",
        @Query("position") position: String?,
        @Query("location") location: String?,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Header("lang") lang: String
    ): Call<CartridgeResponse>

    @GET("V2.0/cartridges")
    fun getCartridgesPartner(
        @Query("type") type: String = "partner",
        @Query("position") position: String?,
        @Query("location") location: String?,
        @Query("latitude") latitude: Double?,
        @Query("longitude") longitude: Double?,
        @Header("lang") lang: String
    ): Call<PartnerResponse>


}