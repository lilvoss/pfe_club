package tn.esprit.module.request

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.Query
import tn.esprit.module.model.CartridgeResponse

interface Home_api {

        @GET("V2.0/cartridges")
        fun getCartridges(
            @Query("type") type: String,
            @Query("position") position: String?,
            @Query("location") location: String?,
            @Query("latitude") latitude: Double?,
            @Query("longitude") longitude: Double?,
            @Header("lang") lang: String
        ): Call<CartridgeResponse>

}