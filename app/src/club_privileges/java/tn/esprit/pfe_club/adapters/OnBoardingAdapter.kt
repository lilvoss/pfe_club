package tn.esprit.pfe_club.adapters

import OnBoardingScreen
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pfe_club.R

class OnBoardingAdapter(private val screens: List<OnBoardingScreen>) :
    RecyclerView.Adapter<OnBoardingAdapter.ScreenViewHolder>() {

    class ScreenViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageIllustration: ImageView = view.findViewById(R.id.newboarding)
        val textDescription: TextView = view.findViewById(R.id.text1)
        val textDescription1: TextView = view.findViewById(R.id.text2)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ScreenViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_onboarding, parent, false)
        return ScreenViewHolder(view)
    }

    override fun onBindViewHolder(holder: ScreenViewHolder, position: Int) {
        val screen = screens[position]
        holder.textDescription.text = screen.description
        holder.textDescription1.text = screen.description2
        holder.imageIllustration.setImageResource(screen.imageRes)
    }

    override fun getItemCount(): Int = screens.size
}
