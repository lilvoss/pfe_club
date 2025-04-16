package tn.esprit.pfe_club.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import tn.esprit.module.repository.SubscriptionValidationRepository
import tn.esprit.module.viewmodel.SubscriptionValidationViewModel
import tn.esprit.pfe_club.R

class SubscriptionValidationActivity : AppCompatActivity() {

    private lateinit var viewModel: SubscriptionValidationViewModel
    private lateinit var subscriptionValidationRepository: SubscriptionValidationRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_subscription_activity)

        val sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)

        // Initialize repositories
        viewModel = SubscriptionValidationViewModel(sharedPreferences)
        subscriptionValidationRepository = SubscriptionValidationRepository(sharedPreferences)

        val edit1 = findViewById<EditText>(R.id.edit_text_1)
        val edit2 = findViewById<EditText>(R.id.edit_text_2)
        val edit3 = findViewById<EditText>(R.id.edit_text_3)
        val edit4 = findViewById<EditText>(R.id.edit_text_4)
        val buttonSubmit = findViewById<ImageButton>(R.id.button_flesh)


        edit1.addTextChangedListener { text ->
            if (text?.length == 1) {
                edit2.requestFocus()
            }
        }

        edit2.addTextChangedListener { text ->
            if (text?.length == 1) {
                edit3.requestFocus()
            }
        }

        edit3.addTextChangedListener { text ->
            if (text?.length == 1) {
                edit4.requestFocus()
            }
        }

        // To allow submission when the user presses 'Enter' after the last EditText
        edit4.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                buttonSubmit.performClick()
                true
            } else {
                false
            }
        }

        buttonSubmit.setOnClickListener {
            val codeStr = edit1.text.toString() + edit2.text.toString() +
                    edit3.text.toString() + edit4.text.toString()

            if (codeStr.length == 4 && codeStr.all { it.isDigit() }) {
                val code = codeStr.toIntOrNull()

                val abonnementId = subscriptionValidationRepository.getAbonnementId()
                val tarifId = intent.getIntExtra("TARIF_ID", -1)

                Log.d("SubscriptionValidation", "Entered code: $codeStr")
                Log.d("SubscriptionValidation", "Entered code: $code")
                Log.d("SubscriptionValidation", "Abonnement ID: $abonnementId")
                Log.d("SubscriptionValidation", "Tarif ID: $tarifId")

                if (abonnementId != -1 && tarifId != -1 && code != null) {
                    viewModel.validateSubscription(
                        abonnementId = abonnementId,
                        tarifId = tarifId,
                        code = code,
                        rechargeCard = null,
                        lang = "fr"
                    ) { success, message ->
                        runOnUiThread {
                            if (success) {
                                Toast.makeText(this, "✅ $message", Toast.LENGTH_LONG).show()
                                val intent = Intent(this, HomeActivity::class.java)
                                startActivity(intent)
                                finish() // pour fermer l'écran actuel
                            } else {
                                Toast.makeText(this, "❌ $message", Toast.LENGTH_LONG).show()
                            }
                        }
                    }
                } else {
                    Toast.makeText(this, "🔴 Veuillez vous assurer que l'abonnement et le tarif sont valides", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "⚠️ Veuillez saisir un code à 4 chiffres", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
