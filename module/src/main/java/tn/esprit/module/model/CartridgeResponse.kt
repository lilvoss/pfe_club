package tn.esprit.module.model

data class CartridgeResponse(
    val base_url: String,
    val status: Boolean,
    val message: String,
    val data: List<Cartridge>
)

data class Cartridge(
    val cartridge_id: Int,
    val cartridge_title: String,
    val cartridge_type: String,
    val cartridge_position: String,
    val cartridge_location: String,
    val cartridge_active: Int,
    val cartridge_order: Int,
    val cartridge_stores: String,
    val cartridge_items: List<CartridgeItem>, // Représente les bannières
    val created_at: String,
    val updated_at: String,
    val entry_by: Int?,
    val cartridge_desc: String?,
    val cartridge_icon: String?
)

data class CartridgeItem(
    val id: Int,
    val title: String,
    val image: String, // URL de l'image
    val description: String?,
    val link_type: String?,
    val link: String?,
    val partner_id: Int,
    val is_active: Int
)
