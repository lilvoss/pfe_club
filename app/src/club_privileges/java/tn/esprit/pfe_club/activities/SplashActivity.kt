package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.module.viewmodel.AuthStoreViewModel
import tn.esprit.pfe_club.BuildConfig
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.adapters.AuthStoreViewModelFactory  // Assure-toi que cette importation est présente
import tn.esprit.pfe_club.activities.MainActivity1

class SplashScreenActivity : AppCompatActivity() {
    private lateinit var authViewModel: AuthStoreViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_layout)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        saveBaseUrlInPreferences()

        val factory = AuthStoreViewModelFactory(sharedPreferences)
        authViewModel = ViewModelProvider(this, factory).get(AuthStoreViewModel::class.java)

        val email = BuildConfig.STATIC_EMAIL  // Récupère l'email depuis BuildConfig
        val password = BuildConfig.STATIC_PASSWORD

        // Observer l'authentification
        authViewModel.authResponse.observe(this, Observer { authResponse ->
            if (authResponse != null) {
                goToMainScreen()
            } else {
                runOnUiThread {
                    Toast.makeText(this, "Échec de l'authentification", Toast.LENGTH_SHORT).show()
                }
                Handler().postDelayed({
                    authViewModel.login(email, password) // 🔥 Passer email et password
                }, 3000)
            }
        })

        // Lancer le login avec email et password
        authViewModel.login(email, password)
    }


    private fun saveBaseUrlInPreferences() {
        val baseUrl = BuildConfig.BASE_URL_1

        with(sharedPreferences.edit()) {
            putString("BASE_URL", baseUrl)  // Enregistrer la base URL
            apply()
        }
    }

    private fun goToMainScreen() {
        startActivity(Intent(this, MainActivity1::class.java))
        finish()
    }
}
