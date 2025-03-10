package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.AuthUserResponse

class AuthUserRepository(private val sharedPreferences: SharedPreferences) {

    fun authUser(
        phone: Int,
        clientPhoneId: String,
        clientPhoneOs: Int,
        tarifId: Int?,
        deviceId: String,
        lang: String,
        callback: (AuthUserResponse?) -> Unit
    ) {
        val apiService = RetrofitModule.getApiService(sharedPreferences)

        val token = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""
        if (token.isEmpty()) {
            Log.e("AuthUserRepository", "⚠️ ERROR: Token is empty!")
            callback(null)
            return
        }

        val bearerToken = "Bearer $token"
        Log.d("AuthUserRepository", "🚀 Sending Request with Token: $bearerToken")

        apiService.authUser(
            phone,
            clientPhoneId,
            clientPhoneOs,
            tarifId,
            deviceId,
            lang
        ).enqueue(object : Callback<AuthUserResponse> {
            override fun onResponse(call: Call<AuthUserResponse>, response: Response<AuthUserResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    Log.d("AuthUserRepository", "✅ AuthUser API Success: $authResponse")

                    authResponse?.data?.token?.access_token?.let {
                        saveToken(it)
                        verifySavedToken()
                    }
                    savePhone(phone.toString())

                    callback(authResponse)
                } else {
                    Log.e("AuthUserRepository", "❌ AuthUser API Failed. Response code: ${response.code()}")
                    Log.e("AuthUserRepository", "❌ Error body: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AuthUserResponse>, t: Throwable) {
                Log.e("AuthUserRepository", "❌ AuthUser Request Failed: ${t.message}")
                callback(null)
            }
        })
    }

    private fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("AUTH_TOKEN", token)
            apply()
        }
        println("✅ Token saved in SharedPreferences: $token") // Debugging log
    }

    private fun savePhone(phone: String) {
        sharedPreferences.edit().putString("USER_PHONE", phone).apply()
        Log.d("AuthUserRepository", "📞 Phone number saved: $phone")
    }

    private fun verifySavedToken() {
        val savedToken = sharedPreferences.getString("AUTH_TOKEN", "")
        Log.d("AuthUserRepository", "🔍 Verified Saved Token: $savedToken")
    }
}
