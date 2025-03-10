package tn.esprit.pfe_club.activities

import OnBoardingScreen
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.adapters.OnBoardingAdapter

class MainActivity1 : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var btnNext: Button
    private lateinit var btnConnect: Button
    private lateinit var cardView: View
    private lateinit var dots: List<View>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        btnNext = findViewById(R.id.btn_next)
        btnConnect = findViewById(R.id.btn_connect)
        cardView = findViewById(R.id.cardView)
        dots = listOf(
            findViewById(R.id.dot1),
            findViewById(R.id.dot2),
            findViewById(R.id.dot3),
            findViewById(R.id.dot4)
        )

        val screens = listOf(
            OnBoardingScreen("Visitez un partenaire", "Découvrez vos privilèges chez plus de 600\n boutiques partenaires.",
                R.drawable.new_boarding
            ),
            OnBoardingScreen("Scannez le QR code","Scannez le QR code pour débloquer la remise\n exclusive instantanément.",
                R.drawable.new_boarding2
            ),
            OnBoardingScreen("Obtenez la remise","Économisez immédiatement avec Ooreedo\n Privilèges.",
                R.drawable.new_boarding3
            ),
            OnBoardingScreen("Profitez !","Activez le service gratuitement pendant 30 jours,\n puis 3 DT/mois.",
                R.drawable.new_boarding4
            )
        )

        viewPager.adapter = OnBoardingAdapter(screens)

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateDots(position)

                if (position == screens.size - 1) {
                    cardView.visibility = View.GONE
                    btnConnect.visibility = View.VISIBLE
                } else {
                    cardView.visibility = View.VISIBLE
                    btnConnect.visibility = View.GONE
                }

                btnNext.text = if (position == screens.size - 1) "Inscription" else "Suivant"
            }
        })

        btnNext.setOnClickListener {
            if (viewPager.currentItem < screens.size - 1) {
                viewPager.currentItem += 1
            }
        }

        // Navigate to SignUpActivity when "Se connecter" is clicked
        btnConnect.setOnClickListener {
            val intent = Intent(this, CodeVerifActivity::class.java)
            startActivity(intent)
        }
    }

    private fun updateDots(position: Int) {
        dots.forEachIndexed { index, dot ->
            dot.setBackgroundResource(if (index == position) R.drawable.dot_active else R.drawable.dot_inactive)
        }
    }
}
