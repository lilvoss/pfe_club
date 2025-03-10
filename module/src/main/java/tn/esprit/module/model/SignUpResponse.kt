package tn.esprit.module.model

data class SignUpResponse(
    val base_url: String,
    val status: Boolean,
    val message: String,
    val data: SignUpData
)

data class SignUpData(
    val client: Client,
    val token: Token
)


