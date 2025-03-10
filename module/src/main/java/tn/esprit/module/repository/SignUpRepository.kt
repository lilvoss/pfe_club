package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.SignUpResponse
import tn.esprit.module.request.AuthApiService

class SignUpRepository(private val sharedPreferences: SharedPreferences) {

    private val apiService: AuthApiService = RetrofitModule.getApiService(sharedPreferences)

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
        callback: (SignUpResponse?, String?) -> Unit
    ) {

        val phone = sharedPreferences.getString("USER_PHONE", "")

        if (phone.isNullOrEmpty()) {
            Log.e("VerifyLoginRepository", "⚠️ ERROR: Phone number is empty!")
            // Appel du callback avec les deux paramètres attendus
            callback(null, "Phone number is empty!")
            return
        } else {
            Log.d("VerifyLoginRepository", "📱 Phone number saved: $phone")
        }

        val call = apiService.signup(
            clientPrenom, clientNom, phone.toInt(), clientGender,
            source, clientAge, countryId, clientPhoneId, clientPhoneOs,
            deviceId, lang
        )

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    callback(response.body(), null)
                } else {
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }
}
