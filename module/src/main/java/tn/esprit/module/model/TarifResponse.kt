package tn.esprit.module.model

import com.google.gson.annotations.SerializedName

data class TarifResponse(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<Abonnement>
)

data class Abonnement(
    @SerializedName("abonnement_id") val abonnementId: Int,
    @SerializedName("abonnement_nom") val abonnementNom: String,
    @SerializedName("tarifs") val tarifs: List<Tarif>
)


