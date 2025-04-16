package tn.esprit.module.model

data class SubscriptionCodeResponse(
    val base_url: String,
    val status: Boolean,
    val message: String,
    val data: SubscriptionData
)

data class SubscriptionData(
    val client: ClientData
)

data class ClientData(
    val client_id: Int,
    val client_prenom: String,
    val client_nom: String,
    val client_telephone: String,
    val client_gender: String,
    val client_age: Int,
    val client_active: Int,
    val client_phone_id: String,
    val client_phone_os: String,
    val country_id: Int,
    val client_store: Int,
    val source: String,
    val created_at: String,
    val updated_at: String,
    val entry_by: Int,
    val client_email: String,
    val client_city: Int,
    val sub_store: Int,
    val sub_store_end_date: String,
    val tpv_preferences: String,
    val auth_provider: String,
    val auth_provider_id: String,
    val client_subscribed: Int,
    val is_connect: Int
)
