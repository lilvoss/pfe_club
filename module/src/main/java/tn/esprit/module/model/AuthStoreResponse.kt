package tn.esprit.module.model

import com.google.gson.annotations.SerializedName

data class AuthStoreResponse(
    @SerializedName("base_url") val baseUrl: String,
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: AuthStoreData?     // 🔥 Add the missing `data` wrapper
)

data class AuthStoreData(
    @SerializedName("store") val store: Store,
    @SerializedName("informations") val informations: InformationsStore,
    @SerializedName("token") val token: TokenStore,
    @SerializedName("subscriptions") val subscriptions: List<Subscription>
)

data class TokenStore(
    @SerializedName("access_token") val accessToken: String,
    @SerializedName("token_expire_in") val tokenExpireIn: String
)

data class Subscription(
    @SerializedName("abonnement_id") val abonnementId: Int,
    @SerializedName("abonnement_nom") val abonnementNom: String,
    @SerializedName("abonnement_desc") val abonnementDesc: String,
    @SerializedName("abonnement_duree") val abonnementDuree: Int,
    @SerializedName("tarifs") val tarifs: List<Tarif>
)

data class Tarif(
    @SerializedName("abonnement_tarifs_id") val abonnementTarifsId: Int,
    @SerializedName("abonnement_tarifs_duration") val abonnementTarifsDuration: Int,
    @SerializedName("abonnement_tarifs_prix") val abonnementTarifsPrix: Double,
    @SerializedName("abonnement_tarifs_cout") val abonnementTarifsCout: Double,
    @SerializedName("abonnement_tarifs_discount") val abonnementTarifsDiscount: Double,
    @SerializedName("abonnement_tarifs_old_price") val abonnementTarifsOldPrice: Double,
    @SerializedName("payment") val payment: Payment
)

data class Payment(
    @SerializedName("country_payments_methods_id") val countryPaymentsMethodsId: Int,
    @SerializedName("country_payments_methods_name") val countryPaymentsMethodsName: String,
    @SerializedName("country_payments_methods_desc") val countryPaymentsMethodsDesc: String,
    @SerializedName("country_payments_methods_module") val countryPaymentsMethodsModule: String,
    @SerializedName("country_payments_methods_type") val countryPaymentsMethodsType: Int,
    @SerializedName("country_payments_methods_icon") val countryPaymentsMethodsIcon: String,
    @SerializedName("country_payments_methods_gratuite") val countryPaymentsMethodsGratuite: Int
)

data class Store(
    @SerializedName("store_id") val storeId: Int,
    @SerializedName("store_slug") val storeSlug: String,
    @SerializedName("store_name") val storeName: String,
    @SerializedName("store_logo") val storeLogo: String,
    @SerializedName("store_mobile_logo") val storeMobileLogo: String,
    @SerializedName("store_images") val storeImages: List<String>,
    @SerializedName("store_desc") val storeDesc: String,
    @SerializedName("store_mail") val storeMail: String,
    @SerializedName("store_active") val storeActive: Int,
    @SerializedName("store_featured") val storeFeatured: Int,
    @SerializedName("store_mobile") val storeMobile: Int,
    @SerializedName("store_website") val storeWebsite: String,
    @SerializedName("store_facebook") val storeFacebook: String,
    @SerializedName("store_instagram") val storeInstagram: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("updated_at") val updatedAt: String
)

data class InformationsStore(
    @SerializedName("id_facebook") val idFacebook: String,
    @SerializedName("num_whatsapp") val numWhatsapp: String,
    @SerializedName("num_tel") val numTel: String
)
