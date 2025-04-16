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

// AuthStoreRepository.kt

// AuthStoreRepository.kt

class AuthStoreRepository(private val sharedPreferences: SharedPreferences) {

    private val abonnementIdKey = "ABONNEMENT_ID"

    fun login(email: String, password: String, callback: (AuthStoreResponse?) -> Unit) {
        val apiService = RetrofitModule.getApiService(sharedPreferences)
        val authRequest = AuthRequest(email, password)
        val call = apiService.login(authRequest)

        call.enqueue(object : Callback<AuthStoreResponse> {
            override fun onResponse(call: Call<AuthStoreResponse>, response: Response<AuthStoreResponse>) {
                if (response.isSuccessful) {
                    val authResponse = response.body()
                    if (authResponse != null) {
                        println("✅ API Response Received: $authResponse")

                        // Extraire l'`abonnement_id` du premier abonnement dans la liste
                        val abonnements = authResponse.data?.subscriptions
                        if (!abonnements.isNullOrEmpty()) {
                            val abonnementId = abonnements[0].abonnementId // Extraire l'`abonnement_id`
                            saveAbonnementId(abonnementId) // Sauvegarder l'`abonnement_id` dans SharedPreferences
                            println("🔍 Extracted Abonnement ID: $abonnementId")
                        }

                        // Sauvegarde du token dans SharedPreferences (si nécessaire)
                        val token = authResponse.data?.token?.accessToken ?: ""
                        if (token.isNotEmpty()) {
                            saveToken(token)
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
        println("✅ Token saved in SharedPreferences: $token")
    }

    // Méthode pour sauvegarder l'`abonnementId` dans SharedPreferences
    private fun saveAbonnementId(abonnementId: Int) {
        with(sharedPreferences.edit()) {
            putInt(abonnementIdKey, abonnementId)
            apply()
        }
        println("✅ Abonnement ID saved in SharedPreferences: $abonnementId")
    }

    // Méthode pour récupérer l'`abonnementId` depuis SharedPreferences
    fun fetAbonnementId(): Int? {
        return sharedPreferences.getInt(abonnementIdKey, -1).takeIf { it != -1 }
    }
}
