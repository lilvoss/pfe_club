package tn.esprit.module.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import tn.esprit.module.model.SubscribeResponse
import tn.esprit.module.repository.SubscriptionRepository

class SubscriptionViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val repository: SubscriptionRepository = SubscriptionRepository(sharedPreferences)

    fun requestSubscribe(
        tarifId: Int,
        lang: String = "fr",
        onSuccess: (response: SubscribeResponse) -> Unit,
        onError: (errorMessage: String) -> Unit
    ) {
        repository.requestSubscribe(tarifId, lang) { response, error ->
            if (response != null) {
                if (response.status) {
                    onSuccess(response)
                } else {
                    onError(response.message ?: "Subscription failed")
                }
            } else {
                onError(error ?: "Unknown error occurred")
            }
        }
    }
}