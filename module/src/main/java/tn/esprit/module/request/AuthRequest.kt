package tn.esprit.module.request



data class AuthRequest(
    val store_auth_email: String,
    val store_auth_password: String
)

