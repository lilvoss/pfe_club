package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.request.AuthApiService
import tn.esprit.module.request.AuthRequest
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.AuthStoreResponse

class AuthStoreRepository(private val sharedPreferences: SharedPreferences) {

    fun login(callback: (AuthStoreResponse?) -> Unit) {
        val apiService = RetrofitModule.getApiService(sharedPreferences)
        val authRequest = AuthRequest(STATIC_EMAIL, STATIC_PASSWORD)
        val call = apiService.login(authRequest)

        call.enqueue(object : Callback<AuthStoreResponse> {
            override fun onResponse(call: Call<AuthStoreResponse>, response: Response<AuthStoreResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        println("✅ API Response Received: $authResponse")

                        val token = authResponse.data?.token?.accessToken ?: ""

                        println("🔍 Extracted Token: $token")

                        if (token.isNotEmpty()) {
                            saveToken(token) // ✅ Save token in SharedPreferences
                        } else {
                            println("⚠️ Token is empty or missing!")
                        }
                    } else {
                        println("⚠️ API Response is null!")
                    }
                    callback(authResponse)
                } else {
                    println("❌ Authentication failed. Response code: ${response.code()}")
                    println("❌ Error body: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<AuthStoreResponse>, t: Throwable) {
                println("❌ Request failed: ${t.message}")
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

    companion object {
        private const val STATIC_EMAIL = "club@auth.com"
        private const val STATIC_PASSWORD = "auth_club"
    }
}


