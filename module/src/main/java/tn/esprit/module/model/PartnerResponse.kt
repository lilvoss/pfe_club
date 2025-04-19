package tn.esprit.module.model

data class PartnerResponse(
    val base_url: String,
    val status: Boolean,
    val message: String,
    val data: List<PartnerCartridge>
)

data class PartnerCartridge(
    val cartridge_id: Int,
    val cartridge_title: String,
    val cartridge_type: String,
    val cartridge_position: String,
    val cartridge_location: String,
    val cartridge_active: Int,
    val cartridge_order: Int,
    val cartridge_stores: String,
    val cartridge_items: List<PartnerItem>,
    val created_at: String,
    val updated_at: String,
    val entry_by: Int?,
    val cartridge_desc: String?,
    val cartridge_icon: String?
)

data class PartnerItem(
    val partner_id: Int,
    val partner_logo: String,
    val partner_name: String,
    val partner_location_gps: String,
    val partner_location_images: List<String>,
    val partner_category_name: String,
    val discount: Int,
    val discount_type: Int
)
