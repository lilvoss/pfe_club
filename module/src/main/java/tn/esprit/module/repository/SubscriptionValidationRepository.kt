package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.ConfirmSubscribeResponse  // Assurez-vous d'importer le bon modèle
import tn.esprit.module.request.AuthApiService

class SubscriptionValidationRepository(private val sharedPreferences: SharedPreferences) {

    private val apiService: AuthApiService = RetrofitModule.getApiService(sharedPreferences)

    // Retrieve the abonnementId from SharedPreferences
    fun getAbonnementId(): Int {
        return sharedPreferences.getInt("ABONNEMENT_ID", -1)
    }

    fun validateSubscription(
        abonnementId: Int,
        tarifId: Int,
        code: Int?,
        rechargeCard: Int?,
        lang: String,
        callback: (Boolean, String?) -> Unit
    ) {
        Log.d("SubscriptionValidationRepository", "Making API call to confirm subscription")
        Log.d("SubscriptionValidationRepository", "Calling confirmSubscribe with:")
        Log.d("SubscriptionValidationRepository", "abonnementId: $abonnementId")
        Log.d("SubscriptionValidationRepository", "tarifId: $tarifId")
        Log.d("SubscriptionValidationRepository", "code: $code")
        Log.d("SubscriptionValidationRepository", "rechargeCard: $rechargeCard")
        Log.d("SubscriptionValidationRepository", "lang: $lang")

        val call = apiService.confirmSubscribe(abonnementId, tarifId, code, rechargeCard, lang)

        call.enqueue(object : Callback<ConfirmSubscribeResponse> {
            override fun onResponse(
                call: Call<ConfirmSubscribeResponse>,
                response: Response<ConfirmSubscribeResponse>
            ) {
                if (response.isSuccessful) {
                    Log.d("SubscriptionValidationRepository", "API Response successful: ${response.body()}")
                    val subscriptionResponse = response.body()
                    if (subscriptionResponse?.status == true) {
                        callback(true, subscriptionResponse.message)
                    } else {
                        callback(false, subscriptionResponse?.message ?: "Unknown error")
                    }
                } else {
                    Log.e("SubscriptionValidationRepository", "API Response failed: ${response.errorBody()?.string()}")
                    callback(false, response.errorBody()?.string())
                }
            }

            override fun onFailure(call: Call<ConfirmSubscribeResponse>, t: Throwable) {
                Log.e("SubscriptionValidationRepository", "API call failed: ${t.message}")
                callback(false, t.message)
            }
        })
    }
}
