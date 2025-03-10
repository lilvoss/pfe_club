package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.textfield.TextInputEditText
import tn.esprit.pfe_club.R
import tn.esprit.module.viewmodel.SignUpViewModel

class SignUpActivity : AppCompatActivity() {

    private lateinit var prenomEditText: TextInputEditText
    private lateinit var nomEditText: TextInputEditText
    private lateinit var ageEditText: TextInputEditText
    private lateinit var signupButton: Button
    private lateinit var viewModel: SignUpViewModel
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_screen)

        // Récupération des vues et configuration des écouteurs de focus
        prenomEditText = findViewById<TextInputEditText>(R.id.prenom).apply {
            setFocusChangeListener(this)
        }
        nomEditText = findViewById<TextInputEditText>(R.id.nom).apply {
            setFocusChangeListener(this)
        }
        ageEditText = findViewById<TextInputEditText>(R.id.Age).apply {
            setFocusChangeListener(this)
        }
        signupButton = findViewById<Button>(R.id.signup)

        // Instanciation du ViewModel avec SharedPreferences
         sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        viewModel = ViewModelProvider(this, object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                    return SignUpViewModel(sharedPreferences) as T
                }
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        }).get(SignUpViewModel::class.java)

        // Observation des LiveData pour gérer la réponse ou les erreurs
        viewModel.signUpResponse.observe(this, Observer { response ->
            response?.let {
                Toast.makeText(this, "Inscription réussie : ${it.message}", Toast.LENGTH_LONG).show()

                // Rediriger vers HomeActivity après une inscription réussie
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
                finish()  // Cette ligne ferme SignUpActivity pour ne pas revenir en arrière
            }
        })


        viewModel.errorMessage.observe(this, Observer { error ->
            error?.let {
                Toast.makeText(this, "Erreur lors de l'inscription : $it", Toast.LENGTH_LONG).show()
            }
        })

        // Clic sur le bouton d'inscription
        signupButton.setOnClickListener {
            val prenom = prenomEditText.text.toString().trim()
            val nom = nomEditText.text.toString().trim()
            val age = ageEditText.text.toString().toIntOrNull()

            if (prenom.isEmpty() || nom.isEmpty() || age == null) {
                Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Valeurs par défaut pour les autres paramètres
            val clientGender: String? = null
            val source: String? = "app"
            val countryId: Int? = null
            val clientPhoneId: String? = null
            val clientPhoneOs: Int? = null
            val deviceId = "android_device_id" // à adapter selon vos besoins
            val lang = "fr"

            // Appel de l'API via le ViewModel
            viewModel.signup(
                clientPrenom = prenom,
                clientNom = nom,
                clientGender = clientGender,
                source = source,
                clientAge = age,
                countryId = countryId,
                clientPhoneId = clientPhoneId,
                clientPhoneOs = clientPhoneOs,
                deviceId = deviceId,
                lang = lang
            )
        }
    }

    // Fonction pour changer la couleur de fond d'un EditText lors de la sélection
    private fun setFocusChangeListener(editText: TextInputEditText) {
        editText.setOnFocusChangeListener { _, hasFocus ->
            val purpleColor = ContextCompat.getColor(this, R.color.purple)
            if (hasFocus) {
                editText.setBackgroundColor(purpleColor)
            } else {
                editText.setBackgroundColor(Color.TRANSPARENT)
            }
        }
    }
}
