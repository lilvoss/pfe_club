package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import tn.esprit.pfe_club.R
import tn.esprit.module.Client.RetrofitModule
import tn.esprit.module.request.AuthApiService
import tn.esprit.module.viewmodel.SignUpViewModel
import tn.esprit.module.repository.SubscriptionRepository
import tn.esprit.module.model.SubscribeResponse
import tn.esprit.module.model.SignUpResponse
class SignUpActivity : AppCompatActivity() {

    private lateinit var prenomEditText: TextInputEditText
    private lateinit var nomEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var signupButton: Button
    private lateinit var subscribeCheckbox: CheckBox // Ajout du CheckBox

    private lateinit var signUpViewModel: SignUpViewModel
    private lateinit var subscriptionRepository: SubscriptionRepository
    private lateinit var sharedPreferences: SharedPreferences

    private val fixedTarifId: Int = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        initViews()
        initDependencies()
        setupButtons()
    }

    private fun initViews() {
        prenomEditText = findViewById(R.id.prenom)
        nomEditText = findViewById(R.id.nom)
        ageEditText = findViewById(R.id.Age)
        signupButton = findViewById(R.id.signup)
        subscribeCheckbox = findViewById(R.id.subscribe_checkbox) // Initialiser le CheckBox
    }

    private fun initDependencies() {
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        signUpViewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                    return SignUpViewModel(sharedPreferences) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }).get(SignUpViewModel::class.java)

        subscriptionRepository = SubscriptionRepository(sharedPreferences)
    }

    private fun setupButtons() {
        signupButton.setOnClickListener {
            if (validateSignUpInputs()) {
                // Déterminer si l'abonnement doit être effectué
                val shouldSubscribe = subscribeCheckbox.isChecked

                // Appeler la méthode d'inscription en fonction de l'état du CheckBox
                signUpViewModel.signup(
                    clientPrenom = prenomEditText.text.toString().trim(),
                    clientNom = nomEditText.text.toString().trim(),
                    clientGender = null,
                    source = "app",
                    clientAge = ageEditText.text.toString().toIntOrNull(),
                    countryId = null,
                    clientPhoneId = null,
                    clientPhoneOs = null,
                    deviceId = "android_device",
                    lang = "fr",
                    tarifId = fixedTarifId,
                    shouldSubscribe = shouldSubscribe // Passer la valeur du CheckBox
                ) { signUpResponse, error ->
                    if (signUpResponse != null) {
                        showToast("Inscription réussie!")
                        if (shouldSubscribe) {
                            showToast("Passage à l'abonnement...")
                           // executeSubscribe()
                        }
                    } else {
                        showToast("Échec de l'inscription : ${error ?: "Erreur inconnue"}")
                    }
                }
            }
        }
    }

    private fun validateSignUpInputs(): Boolean {
        val prenom = prenomEditText.text.toString().trim()
        val nom = nomEditText.text.toString().trim()
        val age = ageEditText.text.toString().toIntOrNull()

        if (prenom.isEmpty() || nom.isEmpty() || age == null) {
            showToast("Veuillez remplir tous les champs")
            return false
        }
        return true
    }

    private fun executeSubscribe() {
        val updatedRetrofit = RetrofitModule.getRetrofit(sharedPreferences)
        val updatedApiService = updatedRetrofit.create(AuthApiService::class.java)

        subscriptionRepository.updateApiService(updatedApiService)

        subscriptionRepository.requestSubscribe(fixedTarifId, "fr") { response, error ->
            if (response != null) {
                if (response.status) {
                    showToast("Abonnement réussi!")
                } else {
                    showToast(response.message ?: "Abonnement échoué")
                }
            } else {
                showToast("Erreur d'abonnement: ${error ?: "Inconnue"}")
            }
        }
    }

    private fun showToast(message: String) {
        runOnUiThread {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }




    private fun setFocusChangeListener(editText: TextInputEditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            val purpleColor = ContextCompat.getColor(this, R.color.purple)
            editText.setBackgroundColor(if (hasFocus) purpleColor else Color.TRANSPARENT)
        }
    }
}