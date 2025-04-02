package tn.esprit.module.model

data class SubscribeResponse(
    val base_url: String,
    val status: Boolean,
    val message: String
)
