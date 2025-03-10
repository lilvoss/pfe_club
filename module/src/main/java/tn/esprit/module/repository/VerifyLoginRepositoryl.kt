package tn.esprit.module.repository

import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.request.AuthApiService
import android.content.SharedPreferences
import android.util.Log
import tn.esprit.module.model.AuthUserResponse
import tn.esprit.module.model.VerifyCodeResponse

class VerifyLoginRepository(private val sharedPreferences: SharedPreferences) {

    fun verifyLogin(
        code: Int,
        clientPhoneId: String,
        clientPhoneOs: Int,
        lang: String,
        callback: (VerifyCodeResponse?) -> Unit
    ) {
        // Vérifier si le token est bien sauvegardé avant de le récupérer
        verifySavedToken()

        // Récupérer le numéro de téléphone depuis SharedPreferences
        val phone = sharedPreferences.getString("USER_PHONE", "")

        // Ajouter un log pour vérifier si le numéro est bien récupéré
        Log.d("VerifyLoginRepository", "🔍 Retrieved phone number: $phone")

        // Vérifier si le numéro de téléphone est vide ou nul
        if (phone.isNullOrEmpty()) {
            Log.e("VerifyLoginRepository", "⚠️ ERROR: Phone number is empty!")
            callback(null)
            return
        } else {
            Log.d("VerifyLoginRepository", "📱 Phone number saved: $phone")
        }

        // Récupérer le token depuis SharedPreferences
        val apiService = RetrofitModule.getApiService(sharedPreferences)
        val token = sharedPreferences.getString("AUTH_TOKEN", "") ?: ""

        if (token.isEmpty()) {
            Log.e("VerifyLoginRepository", "⚠️ ERROR: Token is empty!")
            callback(null)
            return
        }

        val bearerToken = "Bearer $token"

        // Afficher la requête complète dans les logs
        Log.d("VerifyLoginRepository", "🚀 Sending Request with Token: $bearerToken and Phone: $phone")
        Log.d("VerifyLoginRepository", "Requesting with code: $code, clientPhoneId: $clientPhoneId, clientPhoneOs: $clientPhoneOs, lang: $lang")

        // Faire la requête API
        apiService.verifyLogin(
            phone.toInt(), // Convertir en Int
            code,
            clientPhoneId,
            clientPhoneOs,
            lang
        ).enqueue(object : Callback<VerifyCodeResponse> {
            override fun onResponse(call: Call<VerifyCodeResponse>, response: Response<VerifyCodeResponse>) {
                if (response.isSuccessful) {
                    val codeResponse = response.body()
                    Log.d("VerifyLoginRepository", "✅ VerifyLogin API Success: $codeResponse")
                    // Continue processing...
                } else {
                    Log.e("VerifyLoginRepository", "❌ VerifyLogin API Failed. Response code: ${response.code()}")
                    Log.e("VerifyLoginRepository", "❌ Error body: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<VerifyCodeResponse>, t: Throwable) {
                Log.e("VerifyLoginRepository", "❌ VerifyLogin Request Failed: ${t.message}")
                callback(null)
            }
        })
    }



    // Méthode pour sauvegarder le token dans SharedPreferences
    private fun saveToken(token: String) {
        sharedPreferences.edit().putString("AUTH_TOKEN", token).apply()
        Log.d("AuthUserRepository", "🔒 Token saved: $token")
    }

    // Méthode pour vérifier le token sauvegardé dans SharedPreferences
    private fun verifySavedToken() {
        val savedToken = sharedPreferences.getString("AUTH_TOKEN", "")
        Log.d("AuthUserRepository", "🔍 Verified Saved Token: $savedToken")
    }
}
