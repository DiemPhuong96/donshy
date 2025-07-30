package com.example.donshy.data.model

data class AuthResponse(
    val userId: String,
    val email: String,
    val isEmailVerified: Boolean
)