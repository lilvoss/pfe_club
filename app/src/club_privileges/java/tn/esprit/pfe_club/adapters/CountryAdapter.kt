package tn.esprit.pfe_club.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.model.Country

class CountryAdapter(context: Context, private val countryList: List<Country>) :
    ArrayAdapter<Country>(context, 0, countryList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return createView(position, convertView, parent)
    }

    private fun createView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.flags, parent, false)
        val imageView = view.findViewById<ImageView>(R.id.flag_image)
        val textView = view.findViewById<TextView>(R.id.country_code)

        val country = countryList[position]
        imageView.setImageResource(country.flagResId)
        textView.text = country.countryCode

        return view
    }
}
