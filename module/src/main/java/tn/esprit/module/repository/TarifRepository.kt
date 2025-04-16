package tn.esprit.module.repository

import android.content.SharedPreferences
import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.model.TarifResponse
import tn.esprit.module.request.AuthApiService

class TarifRepository(private val sharedPreferences: SharedPreferences) {
    private val apiService: AuthApiService = RetrofitModule.getApiService(sharedPreferences)

    // Variable pour stocker le tarifId
    private var storedTarifId: Int? = null

    fun getTarifId(callback: (Int?, String?) -> Unit) {
        val call = apiService.getTarifId("day")

        call.enqueue(object : Callback<TarifResponse> {
            override fun onResponse(call: Call<TarifResponse>, response: Response<TarifResponse>) {
                Log.d("TarifRepository", "🔵 Réponse brute du serveur: ${response.raw()}")

                if (response.isSuccessful) {
                    val tarifResponse = response.body()
                    Log.d("TarifRepository", "✅ Réponse JSON: $tarifResponse")

                    val tarifId = tarifResponse?.data?.firstOrNull()?.tarifs?.firstOrNull()?.abonnementTarifsId

                    // Sauvegarder le tarifId dans la variable locale
                    if (tarifId != null) {
                        storedTarifId = tarifId  // Sauvegarde dans la variable locale
                        Log.d("TarifRepository", "✅ Tarif ID récupéré: $tarifId")
                        callback(tarifId, null)
                    } else {
                        Log.d("TarifRepository", "⚠️ Aucun tarif ID trouvé.")
                        callback(null, "Aucun tarif disponible")
                    }
                } else {
                    val errorBody: ResponseBody? = response.errorBody()
                    val errorMessage = errorBody?.string()
                    Log.e("TarifRepository", "❌ Erreur API: $errorMessage")
                    callback(null, "Erreur: $errorMessage")
                }
            }

            override fun onFailure(call: Call<TarifResponse>, t: Throwable) {
                Log.e("TarifRepository", "❌ Erreur de connexion: ${t.message}", t)
                callback(null, t.message)
            }
        })
    }

    // Accesseur pour obtenir le tarifId stocké
    fun getStoredTarifId(): Int? {
        return storedTarifId
    }
}
