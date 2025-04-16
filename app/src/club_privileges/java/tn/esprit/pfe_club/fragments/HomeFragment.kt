package tn.esprit.pfe_club.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.adapters.BannerAdapter
import tn.esprit.module.viewmodel.CartridgeViewModel
import tn.esprit.pfe_club.adapters.CartridgeViewModelFactory
import androidx.recyclerview.widget.PagerSnapHelper


class HomeFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var dotsContainer: LinearLayout
    private lateinit var cartridgeViewModel: CartridgeViewModel
    private lateinit var bannerAdapter: BannerAdapter

    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        dotsContainer = view.findViewById(R.id.dotsContainer)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.layoutManager = layoutManager
        val snapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(recyclerView)

        bannerAdapter = BannerAdapter(emptyList())
        recyclerView.adapter = bannerAdapter

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val factory = CartridgeViewModelFactory(sharedPreferences)
        cartridgeViewModel = ViewModelProvider(this, factory).get(CartridgeViewModel::class.java)

        cartridgeViewModel.cartridgeResponse.observe(viewLifecycleOwner, { response ->
            response?.let {
                val allCartridgeItems = it.data.flatMap { cartridge -> cartridge.cartridge_items }
                bannerAdapter = BannerAdapter(allCartridgeItems)
                recyclerView.adapter = bannerAdapter
                bannerAdapter.notifyDataSetChanged()

                setupDots(allCartridgeItems.size)

                // Mise à jour des dots selon le scroll
                recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)
                        val position = layoutManager.findFirstVisibleItemPosition()
                        setCurrentDot(position)
                    }
                })
            }
        })

        cartridgeViewModel.error.observe(viewLifecycleOwner, { error ->
            Log.e(TAG, "Error: $error")
        })

        val type = "banner"
        val position: String? = null
        val location: String? = null
        val latitude: Double? = null
        val longitude: Double? = null
        val lang = "fr"

        Log.d(TAG, "Calling fetchCartridges with:")
        Log.d(TAG, "  type     = $type")
        Log.d(TAG, "  position = $position")
        Log.d(TAG, "  location = $location")
        Log.d(TAG, "  latitude = $latitude")
        Log.d(TAG, "  longitude= $longitude")
        Log.d(TAG, "  lang     = $lang")

        cartridgeViewModel.loadCartridges(type, position, location, latitude, longitude, lang)
    }

    private fun setupDots(count: Int) {
        dotsContainer.removeAllViews()
        for (i in 0 until count) {
            val dot = ImageView(requireContext()).apply {
                val params = LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
                params.setMargins(8, 0, 8, 0)
                layoutParams = params
                setImageResource(if (i == 0) R.drawable.dot_active else R.drawable.dot_inactive)
            }
            dotsContainer.addView(dot)
        }
    }

    private fun setCurrentDot(index: Int) {
        for (i in 0 until dotsContainer.childCount) {
            val dot = dotsContainer.getChildAt(i) as ImageView
            dot.setImageResource(if (i == index) R.drawable.dot_active else R.drawable.dot_inactive)
        }
    }
}
