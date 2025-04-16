package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.InputFilter
import android.text.InputType
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.adapters.CountryAdapter
import tn.esprit.pfe_club.model.Country
import tn.esprit.module.viewmodel.AuthStoreViewModel
import tn.esprit.module.viewmodel.AuthUserViewModel
import tn.esprit.pfe_club.BuildConfig
import tn.esprit.pfe_club.adapters.AuthStoreViewModelFactory
import tn.esprit.pfe_club.adapters.AuthUserViewModelFactory

class CodeVerifActivity : AppCompatActivity() {

    private lateinit var baseUrls: List<String>
    private lateinit var authViewModel: AuthStoreViewModel
    private lateinit var authUserViewModel: AuthUserViewModel
    private var selectedBaseUrl: String? = null
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var phoneEditText: EditText
    private lateinit var spinner: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.country_layout)

        baseUrls = listOf(
            tn.esprit.pfe_club.BuildConfig.BASE_URL_1,
            tn.esprit.pfe_club.BuildConfig.BASE_URL_2,
            tn.esprit.pfe_club.BuildConfig.BASE_URL_3
        )

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        showSharedPreferencesContent()

        val authViewModelFactory = AuthStoreViewModelFactory(sharedPreferences)
        authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthStoreViewModel::class.java)

        val authUserViewModelFactory = AuthUserViewModelFactory(sharedPreferences)
        authUserViewModel = ViewModelProvider(this, authUserViewModelFactory).get(AuthUserViewModel::class.java)

        spinner = findViewById(R.id.spinner_countries)
        phoneEditText = findViewById(R.id.phone_number)

        phoneEditText.inputType = InputType.TYPE_CLASS_NUMBER
        phoneEditText.filters = arrayOf(InputFilter { source, _, _, _, _, _ ->
            if (source.matches(Regex("[0-9]+"))) source else ""
        })

        val countryCodes = resources.getStringArray(R.array.countries)
        val countries = listOf(
            Country("Tunisia", R.drawable.tn, countryCodes[0]),
            Country("Ivory Coast", R.drawable.civ, countryCodes[1]),
            Country("RDC", R.drawable.rdc, countryCodes[2])
        )

        val adapter = CountryAdapter(this, countries)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedBaseUrl = baseUrls.getOrElse(position) { baseUrls[0] }
                Toast.makeText(this@CodeVerifActivity, "Pays sélectionné: ${countries[position].name}", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {}
        }

        findViewById<ImageButton>(R.id.button_flesh).setOnClickListener {
            val phoneNumber = phoneEditText.text.toString().trim()
            if (selectedBaseUrl != null) {
                updateBaseUrl(selectedBaseUrl!!)

                // Récupérer les valeurs de BuildConfig pour l'email et le password
                val email = BuildConfig.STATIC_EMAIL
                val password = BuildConfig.STATIC_PASSWORD

                // Passer l'email et le mot de passe au login
                authViewModel.login(email, password)

                authViewModel.authResponse.observe(this, Observer { authResponse ->
                    if (authResponse != null) {
                        Toast.makeText(this, "Connexion réussie!", Toast.LENGTH_SHORT).show()
                        if (phoneNumber.isNotEmpty()) {
                            authenticateUser(phoneNumber)
                        } else {
                            Toast.makeText(this, "Veuillez entrer un numéro de téléphone", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this, "Échec de l'authentification", Toast.LENGTH_SHORT).show()
                    }
                })
            } else {
                Toast.makeText(this, "Veuillez sélectionner un pays", Toast.LENGTH_SHORT).show()
            }
        }

        findViewById<ImageButton>(R.id.back_button).setOnClickListener { finish() }
    }

    private fun authenticateUser(phone: String) {
        val clientPhoneId = "1111"
        val clientPhoneOs = 2
        val tarifId: Int? = null
        val deviceId = "1"
        val lang = "fr"

        authUserViewModel.authUser(phone.toInt(), clientPhoneId, clientPhoneOs, tarifId, deviceId, lang)

        authUserViewModel.authResponse.observe(this, Observer { response ->
            if (response != null) {
                sharedPreferences.edit().putString("USER_PHONE", phone).apply()

                when (response.data?.action) {
                    "verify-login" -> {
                        val intent = Intent(this@CodeVerifActivity, CodeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    "register" -> {
                        val intent = Intent(this@CodeVerifActivity, SignUpActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    else -> {
                        Toast.makeText(this, "Authentification réussie!", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                Toast.makeText(this, "Échec de l'authentification", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun updateBaseUrl(baseUrl: String) {
        with(sharedPreferences.edit()) {
            putString("BASE_URL", baseUrl)
            apply()
        }
        Toast.makeText(this, "Base URL mise à jour: $baseUrl", Toast.LENGTH_SHORT).show()
    }

    private fun showSharedPreferencesContent() {
        val allEntries = sharedPreferences.all
        val stringBuilder = StringBuilder("Contenu de SharedPreferences:\n")
        for ((key, value) in allEntries) {
            stringBuilder.append("$key: $value\n")
        }
        println(stringBuilder.toString())
        Toast.makeText(this, stringBuilder.toString(), Toast.LENGTH_LONG).show()
    }

    private fun recreateAuthViewModel() {
        val authViewModelFactory = AuthStoreViewModelFactory(sharedPreferences)
        authViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthStoreViewModel::class.java)
    }
}
