package tn.esprit.module.request


import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

import tn.esprit.module.model.AuthStoreResponse
import tn.esprit.module.model.AuthUserResponse
import tn.esprit.module.model.SignUpResponse
import tn.esprit.module.model.SubscribeResponse
import tn.esprit.module.model.VerifyCodeResponse
import tn.esprit.module.request.AuthRequest

interface AuthApiService {
    @POST("V2.0/store/login")
    fun login(@Body request: AuthRequest): Call<AuthStoreResponse>

    @GET("dgv/cellular/check-identification")
    fun checkIdentification(): Call<ResponseBody>

    @POST("V3.0/auth-user")
    fun authUser(
        @Query("phone") phone: Int,
        @Query("client_phone_id") clientPhoneId: String,
        @Query("client_phone_os") clientPhoneOs: Int,
        @Query("tarif_id") tarifId: Int?,
        @Header("device-id") deviceId: String,
        @Header("lang") lang: String,
    ): Call<AuthUserResponse>


    @POST("V3.0/verify-login")
    fun verifyLogin(
        @Query("phone") phone: Int,
        @Query("code") code: Int,
        @Query("client_phone_id") clientPhoneId: String,
        @Query("client_phone_os") clientPhoneOs: Int,
        @Header("lang") lang: String
    ): Call<VerifyCodeResponse>

    @POST("V2.0/signup")
    fun signup(
        @Query("client_prenom") clientPrenom: String,
        @Query("client_nom") clientNom: String,
        @Query("client_telephone") clientTelephone: Int,
        @Query("client_gender") clientGender: String?,
        @Query("source") source: String?,
        @Query("client_age") clientAge: Int?,
        @Query("country_id") countryId: Int?,
        @Query("client_phone_id") clientPhoneId: String?,
        @Query("client_phone_os") clientPhoneOs: Int?,
        @Header("device-id") deviceId: String,
        @Header("lang") lang: String
    ): Call<SignUpResponse>


    @POST("V2.0/request-subscribe")
    fun subscribe(
        @Query("tarif_id") tarifId: Int,
        @Header("lang") lang: String = "fr" // Langue par défaut
    ): Call<SubscribeResponse>



}

