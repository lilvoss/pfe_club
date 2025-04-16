package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.SubscribeResponse
import tn.esprit.module.request.AuthApiService

class SubscriptionRepository(private val sharedPreferences: SharedPreferences) {
    private var apiService: AuthApiService = RetrofitModule.getApiService(sharedPreferences)


    fun updateApiService(newApiService: AuthApiService) {
        this.apiService = newApiService
    }

    fun requestSubscribe(
        tarifId: Int,
        lang: String = "fr",
        callback: (SubscribeResponse?, String?) -> Unit
    ) {
        Log.d("SubscriptionRepo", "Début d'abonnement pour tarifId: $tarifId")

        apiService.subscribe(tarifId, lang).enqueue(object : Callback<SubscribeResponse> {
            override fun onResponse(call: Call<SubscribeResponse>, response: Response<SubscribeResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("SubscriptionRepo", "Réponse d'abonnement: ${it.message}")

                        // Mettre à jour l'URL de base si nécessaire
                        it.base_url?.takeIf { newUrl ->
                            newUrl.isNotEmpty() && newUrl != sharedPreferences.getString("BASE_URL", null)
                        }?.let { newUrl ->
                            sharedPreferences.edit().putString("BASE_URL", newUrl).apply()
                            Log.d("SubscriptionRepo", "Nouvelle URL de base: $newUrl")
                        }

                        callback(it, null)
                    } ?: run {
                        val error = "Réponse vide du serveur"
                        Log.e("SubscriptionRepo", error)
                        callback(null, error)
                    }
                } else {
                    val error = response.errorBody()?.string() ?: "Erreur inconnue"
                    Log.e("SubscriptionRepo", "Erreur API: $error")
                    callback(null, error)
                }
            }

            override fun onFailure(call: Call<SubscribeResponse>, t: Throwable) {
                val error = t.message ?: "Échec réseau"
                Log.e("SubscriptionRepo", "Échec de la requête: $error", t)
                callback(null, error)
            }
        })
    }
}