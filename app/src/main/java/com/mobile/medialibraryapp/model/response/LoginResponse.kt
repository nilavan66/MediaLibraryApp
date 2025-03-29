package com.mobile.medialibraryapp.model.response

data class LoginResponse(
    val uid: String?,
    val email: String?,
    val isSuccess: Boolean,
    val errorMessage: String? = null
)
