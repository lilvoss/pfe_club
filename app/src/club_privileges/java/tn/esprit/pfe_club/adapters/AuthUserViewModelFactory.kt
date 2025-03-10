package tn.esprit.pfe_club.adapters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import tn.esprit.module.repository.AuthUserRepository
import tn.esprit.module.viewmodel.AuthUserViewModel
import android.content.SharedPreferences

class AuthUserViewModelFactory(
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AuthUserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AuthUserViewModel(AuthUserRepository(sharedPreferences)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
