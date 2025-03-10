package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import tn.esprit.module.repository.VerifyLoginRepository
import tn.esprit.module.viewmodel.VerifyLoginViewModel
import tn.esprit.module.model.AuthUserResponse
import tn.esprit.module.model.VerifyCodeResponse
import tn.esprit.pfe_club.R

class CodeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var verifyLoginViewModel: VerifyLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_actcivity)

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Initialize ViewModel
        val repository = VerifyLoginRepository(sharedPreferences)
        verifyLoginViewModel = VerifyLoginViewModel(repository)

        val editText1 = findViewById<EditText>(R.id.edit_text_1)
        val editText2 = findViewById<EditText>(R.id.edit_text_2)
        val editText3 = findViewById<EditText>(R.id.edit_text_3)
        val editText4 = findViewById<EditText>(R.id.edit_text_4)
        val submitButton = findViewById<ImageButton>(R.id.button_flesh)

        // Listen for text changes to move focus automatically
        setupOtpAutoMove(editText1, editText2)
        setupOtpAutoMove(editText2, editText3)
        setupOtpAutoMove(editText3, editText4)

        submitButton.setOnClickListener {
            val otpCode = "${editText1.text}${editText2.text}${editText3.text}${editText4.text}"

            if (otpCode.length == 4) {
                verifyLogin(otpCode.toInt())
            }
        }

        // Observe ViewModel responses
        verifyLoginViewModel.loginResponse.observe(this, Observer { response ->
            handleLoginResponse(response, editText1, editText2, editText3, editText4)
        })
    }

    private fun verifyLogin(code: Int) {
        val phone = 123456789 // Replace with actual phone number
        val clientPhoneId = "device_id_1234" // Replace with actual device ID
        val clientPhoneOs = 1 // Example OS value
        val lang = "fr"

        verifyLoginViewModel.verifyLogin(code, clientPhoneId, clientPhoneOs, lang)
    }

    private fun handleLoginResponse(response: VerifyCodeResponse?, vararg editTexts: EditText) {
        if (response != null && response.status == true) {  // Vérification du status
            // Login réussi, navigation vers HomeActivity
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
            finish() // Optionnel, pour empêcher de revenir à cette activité
        } else {
            // Afficher un message d'erreur (code invalide)
            showErrorAnimation(*editTexts)
        }
    }



    private fun showErrorAnimation(vararg editTexts: EditText) {
        // Apply red border to indicate error
        for (editText in editTexts) {
            editText.setBackgroundResource(R.drawable.edittext_error_border)
        }

        // Apply shaking animation
        val shake = AnimationUtils.loadAnimation(this, R.anim.shake)
        for (editText in editTexts) {
            editText.startAnimation(shake)
        }
    }

    private fun setupOtpAutoMove(current: EditText, next: EditText) {
        current.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (s?.length == 1) {
                    next.requestFocus()
                }
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })
    }
}
