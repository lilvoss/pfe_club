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

class PartnerHomeAdapter(private val partnerList: List<PartnerItem>) : RecyclerView.Adapter<PartnerHomeAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageLogo: ImageView = itemView.findViewById(R.id.roundedImage)
        val textName: TextView = itemView.findViewById(R.id.text1)
        val textCategory: TextView = itemView.findViewById(R.id.text2)
        val textDiscount: TextView = itemView.findViewById(R.id.text3)
        val imageLocation: ImageView = itemView.findViewById(R.id.affiche)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_home_partner, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val partner = partnerList[position]

        // Nom et catégorie
        holder.textName.text = partner.partner_name
        holder.textCategory.text = partner.partner_category_name
        holder.textDiscount.text = "${partner.discount}% de remise"

        // Logo
        Glide.with(holder.itemView.context)
            .load(partner.partner_logo)
            .into(holder.imageLogo)

        // Première image de localisation (si existe)
        if (!partner.partner_location_images.isNullOrEmpty()) {
            Glide.with(holder.itemView.context)
                .load(partner.partner_location_images[0])
                .into(holder.imageLocation)
        } else {
            holder.imageLocation.setImageResource(R.drawable.decathlon) // une image par défaut si vide
        }
    }

    override fun getItemCount(): Int = partnerList.size
}
