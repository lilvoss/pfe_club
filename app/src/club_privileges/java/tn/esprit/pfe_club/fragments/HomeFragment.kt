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
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import tn.esprit.pfe_club.R
import tn.esprit.pfe_club.adapters.BannerAdapter
import tn.esprit.pfe_club.adapters.PartnerHomeAdapter
import tn.esprit.pfe_club.adapters.TopThreeAdapter
import tn.esprit.module.viewmodel.CartridgeViewModel
import tn.esprit.pfe_club.adapters.CartridgeViewModelFactory
import tn.esprit.module.model.PartnerItem

class HomeFragment : Fragment() {

    private lateinit var recyclerViewBanner: RecyclerView
    private lateinit var recyclerViewTopThree: RecyclerView
    private lateinit var recyclerViewPartners: RecyclerView
    private lateinit var dotsContainer: LinearLayout
    private lateinit var cartridgeViewModel: CartridgeViewModel
    private lateinit var bannerAdapter: BannerAdapter
    private lateinit var partnerAdapter: PartnerHomeAdapter
    private lateinit var topThreeAdapter: TopThreeAdapter
    private lateinit var mainContent: View


    private val TAG = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerViewBanner = view.findViewById(R.id.recyclerView)
        recyclerViewTopThree = view.findViewById(R.id.recyclerView_3)
        recyclerViewPartners = view.findViewById(R.id.recyclerView_2)
        dotsContainer = view.findViewById(R.id.dotsContainer)
        mainContent = view.findViewById(R.id.scrollContent)


        // Config Banner RecyclerView
        val bannerLayoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewBanner.layoutManager = bannerLayoutManager
        PagerSnapHelper().attachToRecyclerView(recyclerViewBanner)
        bannerAdapter = BannerAdapter(emptyList())
        recyclerViewBanner.adapter = bannerAdapter

        // Config TopThree RecyclerView
        recyclerViewTopThree.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        topThreeAdapter = TopThreeAdapter(emptyList())
        recyclerViewTopThree.adapter = topThreeAdapter

        // Config Partner RecyclerView
        recyclerViewPartners.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        partnerAdapter = PartnerHomeAdapter(emptyList())
        recyclerViewPartners.adapter = partnerAdapter

        val sharedPreferences = requireContext().getSharedPreferences("AppPreferences", Context.MODE_PRIVATE)
        val factory = CartridgeViewModelFactory(sharedPreferences)
        cartridgeViewModel = ViewModelProvider(this, factory).get(CartridgeViewModel::class.java)

        // Observer pour les bannières
        cartridgeViewModel.cartridgeResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                val allItems = it.data.flatMap { cartridge -> cartridge.cartridge_items }
                bannerAdapter = BannerAdapter(allItems)
                recyclerViewBanner.adapter = bannerAdapter
                bannerAdapter.notifyDataSetChanged()
                setupDots(allItems.size)
                

                recyclerViewBanner.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                        val pos = bannerLayoutManager.findFirstVisibleItemPosition()
                        setCurrentDot(pos)
                    }
                })
            }
        }

        // Observer pour les partenaires
        cartridgeViewModel.partnerResponse.observe(viewLifecycleOwner) { response ->
            response?.let {
                val topThreeList = mutableListOf<PartnerItem>()
                val verticalList = mutableListOf<PartnerItem>()

                for (cartridge in it.data) {
                    when (cartridge.cartridge_position.lowercase()) {
                        "h" -> topThreeList.addAll(cartridge.cartridge_items)
                        "v" -> verticalList.addAll(cartridge.cartridge_items)
                    }
                }

                topThreeAdapter = TopThreeAdapter(topThreeList.take(3))
                recyclerViewTopThree.adapter = topThreeAdapter
                topThreeAdapter.notifyDataSetChanged()

                partnerAdapter = PartnerHomeAdapter(verticalList)
                recyclerViewPartners.adapter = partnerAdapter
                partnerAdapter.notifyDataSetChanged()
                mainContent.visibility = View.VISIBLE
            }
        }

        // Observer erreurs
        cartridgeViewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e(TAG, "Error: $error")
        }

        val position: String? = null
        val location: String? = null
        val latitude: Double? = null
        val longitude: Double? = null
        val lang = "fr"
        cartridgeViewModel.loadBanners(position, location, latitude, longitude, lang)
        cartridgeViewModel.loadPartners(position, location, latitude, longitude, lang)
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
