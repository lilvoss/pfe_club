package tn.esprit.module.Client

import android.content.SharedPreferences
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import tn.esprit.module.request.AuthApiService

object RetrofitModule {

    // Function to get the base URL from SharedPreferences
    fun getBaseUrl(sharedPreferences: SharedPreferences): String {
        return sharedPreferences.getString("BASE_URL", "https://api.clubprivileges.app/api/") ?: "https://api.clubprivileges.app/api/"
    }

    // Function to get the stored access token
    private fun getToken(sharedPreferences: SharedPreferences): String? {
        return sharedPreferences.getString("AUTH_TOKEN", null)
    }

    // Function to create an OkHttpClient that includes the Authorization header
    private fun getOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val token = getToken(sharedPreferences) // Get token
                val requestBuilder = chain.request().newBuilder()

                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token") // Attach token
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    // Function to get the Retrofit instance
    fun getRetrofit(sharedPreferences: SharedPreferences): Retrofit {
        val baseUrl = getBaseUrl(sharedPreferences) // Get base URL from SharedPreferences

        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(getOkHttpClient(sharedPreferences)) // Use the client with the token
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Function to get the API service instance
    fun getApiService(sharedPreferences: SharedPreferences): AuthApiService {
        return getRetrofit(sharedPreferences).create(AuthApiService::class.java)
    }
}
