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
    private val subscriptionRepository = SubscriptionRepository(sharedPreferences)

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
        shouldSubscribe: Boolean,
        callback: (SignUpResponse?, String?) -> Unit
    ) {
        val phone = sharedPreferences.getString("USER_PHONE", "")

        if (phone.isNullOrEmpty()) {
            Log.e("SignUpRepository", "⚠️ ERROR: Phone number is empty!")
            callback(null, "Phone number is empty!")
            return
        } else {
            Log.d("SignUpRepository", "📱 Phone number saved: $phone")
        }

        val call = apiService.signup(
            clientPrenom, clientNom, phone.toInt(), clientGender,
            source, clientAge, countryId, clientPhoneId, clientPhoneOs,
            deviceId, lang
        )

        call.enqueue(object : Callback<SignUpResponse> {
            override fun onResponse(call: Call<SignUpResponse>, response: Response<SignUpResponse>) {
                if (response.isSuccessful) {
                    val signUpResponse = response.body()

                    signUpResponse?.data?.token?.access_token?.let {
                        saveToken(it)
                    }


                    if (shouldSubscribe) {
                        requestSubscription(tarifId)
                    }

                    callback(signUpResponse, null)
                } else {
                    callback(null, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<SignUpResponse>, t: Throwable) {
                callback(null, t.message)
            }
        })
    }

    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
        Log.d("SignUpRepository", "✅ Token saved successfully: $token")
    }

    private fun requestSubscription(tarifId: Int) {
        subscriptionRepository.requestSubscribe(tarifId, "fr") { response, error ->
            if (response != null) {
                Log.d("SignUpRepository", "🎉 Abonnement réussi: ${response.message}")
            } else {
                Log.e("SignUpRepository", "❌ Échec de l'abonnement: $error")
            }
        }
    }
}
