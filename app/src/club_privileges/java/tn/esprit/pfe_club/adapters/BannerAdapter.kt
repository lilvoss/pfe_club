package tn.esprit.pfe_club.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.module.model.CartridgeItem
import tn.esprit.pfe_club.R

class BannerAdapter(private val items: List<CartridgeItem>) :
    RecyclerView.Adapter<BannerAdapter.BannerViewHolder>() {

    class BannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val image: ImageView = view.findViewById(R.id.banner)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BannerViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_banner, parent, false)
        return BannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerViewHolder, position: Int) {
        val cartridgeItem = items[position]

        if (!cartridgeItem.image.isNullOrEmpty()) {
            Glide.with(holder.image.context)
                .load(cartridgeItem.image) // Charge l'image depuis l'URL
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.decathlon) // Image par défaut si URL vide
        }
    }

    override fun getItemCount() = items.size
}
