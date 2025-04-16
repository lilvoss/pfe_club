package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.VerifyCodeResponse

class VerifyLoginRepository(private val sharedPreferences: SharedPreferences) {

    fun verifyLogin(
        code: Int?,
        clientPhoneId: String,
        clientPhoneOs: Int,
        lang: String,
        callback: (VerifyCodeResponse?) -> Unit
    ) {
        val savedToken = sharedPreferences.getString("AUTH_TOKEN", "")
        Log.d("VerifyLoginRepo", "🔐 Saved Token: $savedToken")

        val phone = sharedPreferences.getString("USER_PHONE", "")
        Log.d("VerifyLoginRepo", "📱 Phone: $phone")
        if (phone.isNullOrEmpty()) {
            callback(null)
            return
        }

        val apiService = RetrofitModule.getApiService(sharedPreferences)
        Log.d("VerifyLoginRepo", "🚀 Call verifyLogin(phone=$phone, code=$code, os=$clientPhoneOs, lang=$lang)")

        apiService.verifyLogin(
            phone.toInt(),
            code,
            clientPhoneId,
            clientPhoneOs,
            lang
        ).enqueue(object : Callback<VerifyCodeResponse> {
            override fun onResponse(
                call: Call<VerifyCodeResponse>,
                response: Response<VerifyCodeResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("VerifyLoginRepo", "✅ API Success: $body")

                    val token = body?.data?.token?.access_token
                    if (!token.isNullOrEmpty()) {
                        saveToken(token)
                    }



                    callback(body)
                } else {
                    Log.e("VerifyLoginRepo", "❌ API Error ${response.code()}: ${response.errorBody()?.string()}")
                    callback(null)
                }
            }

            override fun onFailure(call: Call<VerifyCodeResponse>, t: Throwable) {
                Log.e("VerifyLoginRepo", "❌ Network Failure: ${t.message}")
                callback(null)
            }
        })
    }

    private fun saveToken(token: String) {
        with(sharedPreferences.edit()) {
            putString("AUTH_TOKEN", token)
            apply()
        }
        Log.d("VerifyLoginRepo", "✅ Token saved: $token")
    }


}
