package tn.esprit.pfe_club.activities

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import tn.esprit.module.model.VerifyCodeResponse
import tn.esprit.module.repository.VerifyLoginRepository
import tn.esprit.module.viewmodel.VerifyLoginViewModel
import tn.esprit.pfe_club.R

class CodeActivity : AppCompatActivity() {

    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var viewModel: VerifyLoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.code_actcivity)

        sharedPreferences = getSharedPreferences("AppPreferences", MODE_PRIVATE)
        viewModel = VerifyLoginViewModel(VerifyLoginRepository(sharedPreferences))

        val et1 = findViewById<EditText>(R.id.edit_text_1)
        val et2 = findViewById<EditText>(R.id.edit_text_2)
        val et3 = findViewById<EditText>(R.id.edit_text_3)
        val et4 = findViewById<EditText>(R.id.edit_text_4)
        val btn = findViewById<ImageButton>(R.id.button_flesh)

        // Auto‐move OTP
        listOf(et1 to et2, et2 to et3, et3 to et4).forEach { (cur, nxt) ->
            cur.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1) nxt.requestFocus()
                }
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            })
        }

        btn.setOnClickListener {
            val codeStr = et1.text.toString() + et2.text + et3.text + et4.text
            if (codeStr.length == 4 && codeStr.all { it.isDigit() }) {
                val code = codeStr.toInt()
                viewModel.verifyLogin(code, "device_id_1234", 1, "fr") { resp ->
                    runOnUiThread {
                        if (resp != null) {
                            Log.d("CodeActivity", """
                                🔍 API Response:
                                • base_url=${resp.base_url}
                                • status=${resp.status}
                                • success=${resp.success}
                                • message=${resp.message}
                                • client=${resp.data?.client}
                                • token=${resp.data?.token}
                            """.trimIndent())
                            // ← Ici on utilise `status` et non `success`
                            if (resp.status) {
                                Toast.makeText(this, "✅ ${resp.message}", Toast.LENGTH_LONG).show()
                                startActivity(Intent(this, HomeActivity::class.java))
                                finish()
                            } else {
                                Toast.makeText(this, "❌ ${resp.message}", Toast.LENGTH_LONG).show()
                            }
                        } else {
                            Toast.makeText(this, "❌ Erreur réseau ou réponse invalide", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "🔴 Code invalide", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
