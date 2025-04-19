package tn.esprit.pfe_club.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import tn.esprit.module.model.PartnerItem
import tn.esprit.pfe_club.R

class TopThreeAdapter(private val partnerList: List<PartnerItem>) : RecyclerView.Adapter<TopThreeAdapter.TopThreeViewHolder>() {

    inner class TopThreeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.affiche)
        val profileImage: ImageView = itemView.findViewById(R.id.roundedImage)
        val title: TextView = itemView.findViewById(R.id.title)
        val category: TextView = itemView.findViewById(R.id.category)
        val discount: TextView = itemView.findViewById(R.id.discount) // Add id in XML if not there
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TopThreeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.single_popular_partner, parent, false) // Remplace par ton layout
        return TopThreeViewHolder(view)
    }

    override fun onBindViewHolder(holder: TopThreeAdapter.TopThreeViewHolder, position: Int) {
        val partner = partnerList[position]

        // Nom et catégorie
        holder.title.text = partner.partner_name
        holder.category.text = partner.partner_category_name
        holder.discount.text = "${partner.discount}% de remise"

        // Logo
        Glide.with(holder.itemView.context)
            .load(partner.partner_logo)
            .into(holder.profileImage)

        // Première image de localisation (si existe)
        if (!partner.partner_location_images.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(partner.partner_location_images[0])
                .into(holder.image)
        } else {
            holder.image.setImageResource(R.drawable.decathlon) // une image par défaut si vide
        }
    }

    override fun getItemCount(): Int = partnerList.size
}
