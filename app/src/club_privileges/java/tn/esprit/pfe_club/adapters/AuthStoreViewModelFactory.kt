package tn.esprit.pfe_club.adapters

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.esprit.module.repository.AuthStoreRepository
import tn.esprit.module.viewmodel.AuthStoreViewModel

class AuthStoreViewModelFactory(private val sharedPreferences: SharedPreferences) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthStoreViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthStoreViewModel(AuthStoreRepository(sharedPreferences)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
