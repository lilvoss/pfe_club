package tn.esprit.module.model

data class ConfirmSubscribeResponse(
    val base_url: String,
    val status: Boolean,
    val message: String
)
