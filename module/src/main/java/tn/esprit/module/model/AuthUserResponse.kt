package tn.esprit.module.model

data class AuthUserResponse(
    val base_url: String,
    val status: Boolean,
    val message: String,
    val data: AuthUserData?
)

data class AuthUserData(
    val store: Store?,
    val token: Token?,
    val action: String?
)



data class Token(
    val access_token: String,
    val token_expire_in: String
)
