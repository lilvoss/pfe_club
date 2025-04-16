package tn.esprit.module.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import tn.esprit.module.repository.SubscriptionValidationRepository

class SubscriptionValidationViewModel(private val sharedPreferences: SharedPreferences) : ViewModel() {

    private val subscriptionValidationRepository = SubscriptionValidationRepository(sharedPreferences)

    // Mise à jour des paramètres pour correspondre à la méthode confirmSubscribe
    fun validateSubscription(
        abonnementId: Int,
        tarifId: Int,
        code: Int?,
        rechargeCard: Int?,
        lang: String,
        callback: (Boolean, String?) -> Unit
    ) {

        subscriptionValidationRepository.validateSubscription(abonnementId, tarifId, code, rechargeCard, lang, callback)
    }
}
